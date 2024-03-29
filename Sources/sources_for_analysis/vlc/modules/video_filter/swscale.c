/*****************************************************************************
 * swscale.c: scaling and chroma conversion using libswscale
 *****************************************************************************
 * Copyright (C) 1999-2008 VLC authors and VideoLAN
 * $Id: 91562ceca707b77dc95d84807d133e01bf63b3ef $
 *
 * Authors: Laurent Aimar <fenrir@via.ecp.fr>
 *          Gildas Bazin <gbazin@videolan.org>
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

/*****************************************************************************
 * Preamble
 *****************************************************************************/
#ifdef HAVE_CONFIG_H
# include "config.h"
#endif
#include <assert.h>

#include <vlc_common.h>
#include <vlc_plugin.h>
#include <vlc_filter.h>
#include <vlc_cpu.h>

#include <libswscale/swscale.h>

#ifdef __APPLE__
# include <TargetConditionals.h>
#endif

#include "../codec/avcodec/chroma.h" // Chroma Avutil <-> VLC conversion

/* Gruikkkkkkkkkk!!!!! */
#undef AVPALETTE_SIZE
#define AVPALETTE_SIZE (256 * sizeof(uint32_t))

/*****************************************************************************
 * Module descriptor
 *****************************************************************************/
static int  OpenScaler( vlc_object_t * );
static void CloseScaler( vlc_object_t * );

#define SCALEMODE_TEXT N_("Scaling mode")
#define SCALEMODE_LONGTEXT N_("Scaling mode to use.")

static const int pi_mode_values[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
const char *const ppsz_mode_descriptions[] =
{ N_("Fast bilinear"), N_("Bilinear"), N_("Bicubic (good quality)"),
  N_("Experimental"), N_("Nearest neighbour (bad quality)"),
  N_("Area"), N_("Luma bicubic / chroma bilinear"), N_("Gauss"),
  N_("SincR"), N_("Lanczos"), N_("Bicubic spline") };

vlc_module_begin ()
    set_description( N_("Video scaling filter") )
    set_shortname( N_("Swscale" ) )
    set_capability( "video filter2", 150 )
    set_category( CAT_VIDEO )
    set_subcategory( SUBCAT_VIDEO_VFILTER )
    set_callbacks( OpenScaler, CloseScaler )
    add_integer( "swscale-mode", 2, SCALEMODE_TEXT, SCALEMODE_LONGTEXT, true )
        change_integer_list( pi_mode_values, ppsz_mode_descriptions )
vlc_module_end ()

/* Version checking */
#if LIBSWSCALE_VERSION_INT >= ((0<<16)+(5<<8)+0)
/****************************************************************************
 * Local prototypes
 ****************************************************************************/

/**
 * Internal swscale filter structure.
 */
struct filter_sys_t
{
    SwsFilter *p_src_filter;
    SwsFilter *p_dst_filter;
    int i_cpu_mask, i_sws_flags;

    video_format_t fmt_in;
    video_format_t fmt_out;

    struct SwsContext *ctx;
    struct SwsContext *ctxA;
    picture_t *p_src_a;
    picture_t *p_dst_a;
    int i_extend_factor;
    picture_t *p_src_e;
    picture_t *p_dst_e;
    bool b_add_a;
    bool b_copy;
    bool b_swap_uvi;
    bool b_swap_uvo;
};

static picture_t *Filter( filter_t *, picture_t * );
static int  Init( filter_t * );
static void Clean( filter_t * );

typedef struct
{
    int  i_fmti;
    int  i_fmto;
    bool b_has_a;
    bool b_add_a;
    int  i_sws_flags;
    bool b_copy;
    bool b_swap_uvi;
    bool b_swap_uvo;
} ScalerConfiguration;

static int GetParameters( ScalerConfiguration *,
                          const video_format_t *p_fmti,
                          const video_format_t *p_fmto,
                          int i_sws_flags_default );

static int GetSwsCpuMask(void);

/* SwScaler point resize quality seems really bad, let our scale module do it
 * (change it to true to try) */
#define ALLOW_YUVP (false)
/* SwScaler does not like too small picture */
#define MINIMUM_WIDTH (32)

/* XXX is it always 3 even for BIG_ENDIAN (blend.c seems to think so) ? */
#define OFFSET_A (3)

/*****************************************************************************
 * OpenScaler: probe the filter and return score
 *****************************************************************************/
static int OpenScaler( vlc_object_t *p_this )
{
    filter_t *p_filter = (filter_t*)p_this;
    filter_sys_t *p_sys;

    int i_sws_mode;

    if( GetParameters( NULL,
                       &p_filter->fmt_in.video,
                       &p_filter->fmt_out.video, 0 ) )
        return VLC_EGENERIC;

    /* */
    p_filter->pf_video_filter = Filter;
    /* Allocate the memory needed to store the decoder's structure */
    if( ( p_filter->p_sys = p_sys = malloc(sizeof(filter_sys_t)) ) == NULL )
        return VLC_ENOMEM;

    /* Set CPU capabilities */
    p_sys->i_cpu_mask = GetSwsCpuMask();

    /* */
    i_sws_mode = var_CreateGetInteger( p_filter, "swscale-mode" );
    switch( i_sws_mode )
    {
    case 0:  p_sys->i_sws_flags = SWS_FAST_BILINEAR; break;
    case 1:  p_sys->i_sws_flags = SWS_BILINEAR; break;
    case 2:  p_sys->i_sws_flags = SWS_BICUBIC; break;
    case 3:  p_sys->i_sws_flags = SWS_X; break;
    case 4:  p_sys->i_sws_flags = SWS_POINT; break;
    case 5:  p_sys->i_sws_flags = SWS_AREA; break;
    case 6:  p_sys->i_sws_flags = SWS_BICUBLIN; break;
    case 7:  p_sys->i_sws_flags = SWS_GAUSS; break;
    case 8:  p_sys->i_sws_flags = SWS_SINC; break;
    case 9:  p_sys->i_sws_flags = SWS_LANCZOS; break;
    case 10: p_sys->i_sws_flags = SWS_SPLINE; break;
    default: p_sys->i_sws_flags = SWS_BICUBIC; i_sws_mode = 2; break;
    }

    p_sys->p_src_filter = NULL;
    p_sys->p_dst_filter = NULL;

    /* Misc init */
    p_sys->ctx = NULL;
    p_sys->ctxA = NULL;
    p_sys->p_src_a = NULL;
    p_sys->p_dst_a = NULL;
    p_sys->p_src_e = NULL;
    p_sys->p_dst_e = NULL;
    memset( &p_sys->fmt_in,  0, sizeof(p_sys->fmt_in) );
    memset( &p_sys->fmt_out, 0, sizeof(p_sys->fmt_out) );

    if( Init( p_filter ) )
    {
        if( p_sys->p_src_filter )
            sws_freeFilter( p_sys->p_src_filter );
        free( p_sys );
        return VLC_EGENERIC;
    }

    msg_Dbg( p_filter, "%ix%i chroma: %4.4s -> %ix%i chroma: %4.4s with scaling using %s",
             p_filter->fmt_in.video.i_width, p_filter->fmt_in.video.i_height,
             (char *)&p_filter->fmt_in.video.i_chroma,
             p_filter->fmt_out.video.i_width, p_filter->fmt_out.video.i_height,
             (char *)&p_filter->fmt_out.video.i_chroma,
             ppsz_mode_descriptions[i_sws_mode] );

    return VLC_SUCCESS;
}

/*****************************************************************************
 * CloseFilter: clean up the filter
 *****************************************************************************/
static void CloseScaler( vlc_object_t *p_this )
{
    filter_t *p_filter = (filter_t*)p_this;
    filter_sys_t *p_sys = p_filter->p_sys;

    Clean( p_filter );
    if( p_sys->p_src_filter )
        sws_freeFilter( p_sys->p_src_filter );
    free( p_sys );
}

/*****************************************************************************
 * Helpers
 *****************************************************************************/
static int GetSwsCpuMask(void)
{
    int i_sws_cpu = 0;

#if defined(__i386__) || defined(__x86_64__)
    if( vlc_CPU_MMX() )
        i_sws_cpu |= SWS_CPU_CAPS_MMX;
#if (LIBSWSCALE_VERSION_INT >= ((0<<16)+(5<<8)+0))
    if( vlc_CPU_MMXEXT() )
        i_sws_cpu |= SWS_CPU_CAPS_MMX2;
#endif
    if( vlc_CPU_3dNOW() )
        i_sws_cpu |= SWS_CPU_CAPS_3DNOW;
#elif defined(__ppc__) || defined(__ppc64__) || defined(__powerpc__)
    if( vlc_CPU_ALTIVEC() )
        i_sws_cpu |= SWS_CPU_CAPS_ALTIVEC;
#endif

    return i_sws_cpu;
}
static bool IsFmtSimilar( const video_format_t *p_fmt1, const video_format_t *p_fmt2 )
{
    return p_fmt1->i_chroma == p_fmt2->i_chroma &&
           p_fmt1->i_width  == p_fmt2->i_width &&
           p_fmt1->i_height == p_fmt2->i_height;
}

static void FixParameters( int *pi_fmt, bool *pb_has_a, bool *pb_swap_uv, vlc_fourcc_t fmt )
{
    switch( fmt )
    {
    case VLC_CODEC_YUVA:
        *pi_fmt = PIX_FMT_YUV444P;
        *pb_has_a = true;
        break;
    case VLC_CODEC_RGBA:
        *pi_fmt = PIX_FMT_BGR32;
        *pb_has_a = true;
        break;
    case VLC_CODEC_YV12:
        *pi_fmt = PIX_FMT_YUV420P;
        *pb_swap_uv = true;
        break;
    case VLC_CODEC_YV9:
        *pi_fmt = PIX_FMT_YUV410P;
        *pb_swap_uv = true;
        break;
    default:
        break;
    }
}

static int GetParameters( ScalerConfiguration *p_cfg,
                          const video_format_t *p_fmti,
                          const video_format_t *p_fmto,
                          int i_sws_flags_default )
{
    int i_fmti = -1;
    int i_fmto = -1;

    bool b_has_ai = false;
    bool b_has_ao = false;
    int i_sws_flags = i_sws_flags_default;
    bool b_swap_uvi = false;
    bool b_swap_uvo = false;

    GetFfmpegChroma( &i_fmti, p_fmti );
    GetFfmpegChroma( &i_fmto, p_fmto );

    if( p_fmti->i_chroma == p_fmto->i_chroma )
    {
        if( p_fmti->i_chroma == VLC_CODEC_YUVP && ALLOW_YUVP )
        {
            i_fmti = i_fmto = PIX_FMT_GRAY8;
            i_sws_flags = SWS_POINT;
        }
    }

    FixParameters( &i_fmti, &b_has_ai, &b_swap_uvi, p_fmti->i_chroma );
    FixParameters( &i_fmto, &b_has_ao, &b_swap_uvo, p_fmto->i_chroma );

#if !defined (__ANDROID__) && !defined(TARGET_OS_IPHONE)
    /* FIXME TODO removed when ffmpeg is fixed
     * Without SWS_ACCURATE_RND the quality is really bad for some conversions */
    switch( i_fmto )
    {
    case PIX_FMT_ARGB:
    case PIX_FMT_RGBA:
    case PIX_FMT_ABGR:
        i_sws_flags |= SWS_ACCURATE_RND;
        break;
    }
#endif

    if( p_cfg )
    {
        p_cfg->i_fmti = i_fmti;
        p_cfg->i_fmto = i_fmto;
        p_cfg->b_has_a = b_has_ai && b_has_ao;
        p_cfg->b_add_a = (!b_has_ai) && b_has_ao;
        p_cfg->b_copy = i_fmti == i_fmto &&
                        p_fmti->i_width == p_fmto->i_width &&
                        p_fmti->i_height == p_fmto->i_height;
        p_cfg->b_swap_uvi = b_swap_uvi;
        p_cfg->b_swap_uvo = b_swap_uvo;
        p_cfg->i_sws_flags = i_sws_flags;
    }

    if( i_fmti < 0 || i_fmto < 0 )
        return VLC_EGENERIC;

    return VLC_SUCCESS;
}

static int Init( filter_t *p_filter )
{
    filter_sys_t *p_sys = p_filter->p_sys;
    const video_format_t *p_fmti = &p_filter->fmt_in.video;
    video_format_t       *p_fmto = &p_filter->fmt_out.video;

    if( IsFmtSimilar( p_fmti, &p_sys->fmt_in ) &&
        IsFmtSimilar( p_fmto, &p_sys->fmt_out ) &&
        p_sys->ctx )
    {
        return VLC_SUCCESS;
    }
    Clean( p_filter );

    /* Init with new parameters */
    ScalerConfiguration cfg;
    if( GetParameters( &cfg, p_fmti, p_fmto, p_sys->i_sws_flags ) )
    {
        msg_Err( p_filter, "format not supported" );
        return VLC_EGENERIC;
    }
    if( p_fmti->i_width <= 0 || p_fmto->i_width <= 0 )
    {
        msg_Err( p_filter, "0 width not supported" );
        return VLC_EGENERIC;
    }

    /* swscale does not like too small width */
    p_sys->i_extend_factor = 1;
    while( __MIN( p_fmti->i_width, p_fmto->i_width ) * p_sys->i_extend_factor < MINIMUM_WIDTH)
        p_sys->i_extend_factor++;

    const unsigned i_fmti_width = p_fmti->i_width * p_sys->i_extend_factor;
    const unsigned i_fmto_width = p_fmto->i_width * p_sys->i_extend_factor;
    for( int n = 0; n < (cfg.b_has_a ? 2 : 1); n++ )
    {
        const int i_fmti = n == 0 ? cfg.i_fmti : PIX_FMT_GRAY8;
        const int i_fmto = n == 0 ? cfg.i_fmto : PIX_FMT_GRAY8;
        struct SwsContext *ctx;

        ctx = sws_getContext( i_fmti_width, p_fmti->i_height, i_fmti,
                              i_fmto_width, p_fmto->i_height, i_fmto,
                              cfg.i_sws_flags | p_sys->i_cpu_mask,
                              p_sys->p_src_filter, p_sys->p_dst_filter, 0 );
        if( n == 0 )
            p_sys->ctx = ctx;
        else
            p_sys->ctxA = ctx;
    }
    if( p_sys->ctxA )
    {
        p_sys->p_src_a = picture_New( VLC_CODEC_GREY, i_fmti_width, p_fmti->i_height, 0, 1 );
        p_sys->p_dst_a = picture_New( VLC_CODEC_GREY, i_fmto_width, p_fmto->i_height, 0, 1 );
    }
    if( p_sys->i_extend_factor != 1 )
    {
        p_sys->p_src_e = picture_New( p_fmti->i_chroma, i_fmti_width, p_fmti->i_height, 0, 1 );
        p_sys->p_dst_e = picture_New( p_fmto->i_chroma, i_fmto_width, p_fmto->i_height, 0, 1 );

        if( p_sys->p_src_e )
            memset( p_sys->p_src_e->p[0].p_pixels, 0, p_sys->p_src_e->p[0].i_pitch * p_sys->p_src_e->p[0].i_lines );
        if( p_sys->p_dst_e )
            memset( p_sys->p_dst_e->p[0].p_pixels, 0, p_sys->p_dst_e->p[0].i_pitch * p_sys->p_dst_e->p[0].i_lines );
    }

    if( !p_sys->ctx ||
        ( cfg.b_has_a && ( !p_sys->ctxA || !p_sys->p_src_a || !p_sys->p_dst_a ) ) ||
        ( p_sys->i_extend_factor != 1 && ( !p_sys->p_src_e || !p_sys->p_dst_e ) ) )
    {
        msg_Err( p_filter, "could not init SwScaler and/or allocate memory" );
        Clean( p_filter );
        return VLC_EGENERIC;
    }

    p_sys->b_add_a = cfg.b_add_a;
    p_sys->b_copy = cfg.b_copy;
    p_sys->fmt_in  = *p_fmti;
    p_sys->fmt_out = *p_fmto;
    p_sys->b_swap_uvi = cfg.b_swap_uvi;
    p_sys->b_swap_uvo = cfg.b_swap_uvo;

    video_format_ScaleCropAr( p_fmto, p_fmti );
#if 0
    msg_Dbg( p_filter, "%ix%i chroma: %4.4s -> %ix%i chroma: %4.4s extend by %d",
             p_fmti->i_width, p_fmti->i_height, (char *)&p_fmti->i_chroma,
             p_fmto->i_width, p_fmto->i_height, (char *)&p_fmto->i_chroma,
             p_sys->i_extend_factor );
#endif
    return VLC_SUCCESS;
}
static void Clean( filter_t *p_filter )
{
    filter_sys_t *p_sys = p_filter->p_sys;

    if( p_sys->p_src_e )
        picture_Release( p_sys->p_src_e );
    if( p_sys->p_dst_e )
        picture_Release( p_sys->p_dst_e );

    if( p_sys->p_src_a )
        picture_Release( p_sys->p_src_a );
    if( p_sys->p_dst_a )
        picture_Release( p_sys->p_dst_a );

    if( p_sys->ctxA )
        sws_freeContext( p_sys->ctxA );

    if( p_sys->ctx )
        sws_freeContext( p_sys->ctx );

    /* We have to set it to null has we call be called again :( */
    p_sys->ctx = NULL;
    p_sys->ctxA = NULL;
    p_sys->p_src_a = NULL;
    p_sys->p_dst_a = NULL;
    p_sys->p_src_e = NULL;
    p_sys->p_dst_e = NULL;
}

static void GetPixels( uint8_t *pp_pixel[4], int pi_pitch[4],
                       const picture_t *p_picture,
                       int i_plane_start, int i_plane_count,
                       bool b_swap_uv )
{
    assert( !b_swap_uv || i_plane_count >= 3 );
    int n;
    for( n = 0; n < __MIN(i_plane_count, p_picture->i_planes-i_plane_start ); n++ )
    {
        const int nd = ( b_swap_uv && n >= 1 && n <= 2 ) ? (3 - n) : n;
        pp_pixel[nd] = p_picture->p[i_plane_start+n].p_pixels;
        pi_pitch[nd] = p_picture->p[i_plane_start+n].i_pitch;
    }
    for( ; n < 4; n++ )
    {
        pp_pixel[n] = NULL;
        pi_pitch[n] = 0;
    }
}

static void ExtractA( picture_t *p_dst, const picture_t *p_src, unsigned i_width, unsigned i_height )
{
    plane_t *d = &p_dst->p[0];
    const plane_t *s = &p_src->p[0];

    for( unsigned y = 0; y < i_height; y++ )
        for( unsigned x = 0; x < i_width; x++ )
            d->p_pixels[y*d->i_pitch+x] = s->p_pixels[y*s->i_pitch+4*x+OFFSET_A];
}
static void InjectA( picture_t *p_dst, const picture_t *p_src, unsigned i_width, unsigned i_height )
{
    plane_t *d = &p_dst->p[0];
    const plane_t *s = &p_src->p[0];

    for( unsigned y = 0; y < i_height; y++ )
        for( unsigned x = 0; x < i_width; x++ )
            d->p_pixels[y*d->i_pitch+4*x+OFFSET_A] = s->p_pixels[y*s->i_pitch+x];
}
static void FillA( plane_t *d, int i_offset )
{
    for( int y = 0; y < d->i_visible_lines; y++ )
        for( int x = 0; x < d->i_visible_pitch; x += d->i_pixel_pitch )
            d->p_pixels[y*d->i_pitch+x+i_offset] = 0xff;
}

static void CopyPad( picture_t *p_dst, const picture_t *p_src )
{
    picture_Copy( p_dst, p_src );
    for( int n = 0; n < p_dst->i_planes; n++ )
    {
        const plane_t *s = &p_src->p[n];
        plane_t *d = &p_dst->p[n];

        for( int y = 0; y < s->i_lines; y++ )
        {
            for( int x = s->i_visible_pitch; x < d->i_visible_pitch; x += s->i_pixel_pitch )
                memcpy( &d->p_pixels[y*d->i_pitch + x], &d->p_pixels[y*d->i_pitch + s->i_visible_pitch - s->i_pixel_pitch], s->i_pixel_pitch );
        }
    }
}

static void SwapUV( picture_t *p_dst, const picture_t *p_src )
{
    picture_t tmp = *p_src;
    tmp.p[1] = p_src->p[2];
    tmp.p[2] = p_src->p[1];

    picture_CopyPixels( p_dst, &tmp );
}
static void Convert( filter_t *p_filter, struct SwsContext *ctx,
                     picture_t *p_dst, picture_t *p_src, int i_height, int i_plane_start, int i_plane_count,
                     bool b_swap_uvi, bool b_swap_uvo )
{
    uint8_t palette[AVPALETTE_SIZE];

    uint8_t *src[4]; int src_stride[4];
    uint8_t *dst[4]; int dst_stride[4];

    GetPixels( src, src_stride, p_src, i_plane_start, i_plane_count, b_swap_uvi );
    if( p_filter->fmt_in.video.i_chroma == VLC_CODEC_RGBP )
    {
        memset( palette, 0, sizeof(palette) );
        if( p_filter->fmt_in.video.p_palette )
            memcpy( palette, p_filter->fmt_in.video.p_palette->palette,
                    __MIN( sizeof(video_palette_t), AVPALETTE_SIZE ) );
        src[1] = palette;
        src_stride[1] = 4;
    }

    GetPixels( dst, dst_stride, p_dst, i_plane_start, i_plane_count, b_swap_uvo );

#if LIBSWSCALE_VERSION_INT  >= ((0<<16)+(5<<8)+0)
    sws_scale( ctx, src, src_stride, 0, i_height,
               dst, dst_stride );
#else
    sws_scale_ordered( ctx, src, src_stride, 0, i_height,
                       dst, dst_stride );
#endif
}

/****************************************************************************
 * Filter: the whole thing
 ****************************************************************************
 * This function is called just after the thread is launched.
 ****************************************************************************/
static picture_t *Filter( filter_t *p_filter, picture_t *p_pic )
{
    filter_sys_t *p_sys = p_filter->p_sys;
    const video_format_t *p_fmti = &p_filter->fmt_in.video;
    const video_format_t *p_fmto = &p_filter->fmt_out.video;
    picture_t *p_pic_dst;

    /* Check if format properties changed */
    if( Init( p_filter ) )
    {
        picture_Release( p_pic );
        return NULL;
    }

    /* Request output picture */
    p_pic_dst = filter_NewPicture( p_filter );
    if( !p_pic_dst )
    {
        picture_Release( p_pic );
        return NULL;
    }

    /* */
    picture_t *p_src = p_pic;
    picture_t *p_dst = p_pic_dst;
    if( p_sys->i_extend_factor != 1 )
    {
        p_src = p_sys->p_src_e;
        p_dst = p_sys->p_dst_e;

        CopyPad( p_src, p_pic );
    }

    if( p_sys->b_copy && p_sys->b_swap_uvi == p_sys->b_swap_uvo )
        picture_CopyPixels( p_dst, p_src );
    else if( p_sys->b_copy )
        SwapUV( p_dst, p_src );
    else
        Convert( p_filter, p_sys->ctx, p_dst, p_src, p_fmti->i_height, 0, 3,
                 p_sys->b_swap_uvi, p_sys->b_swap_uvo );
    if( p_sys->ctxA )
    {
        /* We extract the A plane to rescale it, and then we reinject it. */
        if( p_fmti->i_chroma == VLC_CODEC_RGBA )
            ExtractA( p_sys->p_src_a, p_src, p_fmti->i_width * p_sys->i_extend_factor, p_fmti->i_height );
        else
            plane_CopyPixels( p_sys->p_src_a->p, p_src->p+A_PLANE );

        Convert( p_filter, p_sys->ctxA, p_sys->p_dst_a, p_sys->p_src_a, p_fmti->i_height, 0, 1, false, false );
        if( p_fmto->i_chroma == VLC_CODEC_RGBA )
            InjectA( p_dst, p_sys->p_dst_a, p_fmto->i_width * p_sys->i_extend_factor, p_fmto->i_height );
        else
            plane_CopyPixels( p_dst->p+A_PLANE, p_sys->p_dst_a->p );
    }
    else if( p_sys->b_add_a )
    {
        /* We inject a complete opaque alpha plane */
        if( p_fmto->i_chroma == VLC_CODEC_RGBA )
            FillA( &p_dst->p[0], OFFSET_A );
        else
            FillA( &p_dst->p[A_PLANE], 0 );
    }

    if( p_sys->i_extend_factor != 1 )
    {
        picture_CopyPixels( p_pic_dst, p_dst );
    }

    picture_CopyProperties( p_pic_dst, p_pic );
    picture_Release( p_pic );
    return p_pic_dst;
}

#else /* LIBSWSCALE_VERSION_INT >= ((0<<16)+(5<<8)+0) */

int OpenScaler( vlc_object_t *p_this )
{
    return VLC_EGENERIC;
}

void CloseScaler( vlc_object_t *p_this )
{
}

#endif /* LIBSWSCALE_VERSION_INT >= ((0<<16)+(5<<8)+0) */
