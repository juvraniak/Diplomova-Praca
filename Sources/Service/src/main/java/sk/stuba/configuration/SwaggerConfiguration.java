package sk.stuba.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by juraj on 28/12/2017.
 */

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("sk.stuba.controller"))
            .paths(PathSelectors.regex("/libraries/.*"))
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Dependency resolver service.")
            .description("Web service to get dependencies for running program in Java Shell.")
            .contact(new Contact("Juraj Vraniak", "https://is.stuba.sk/auth/lide/clovek.pl?id=64685", "xvraniak@stuba.sk"))
            .version("1.0.0-beta")
            .build();
    }
}
