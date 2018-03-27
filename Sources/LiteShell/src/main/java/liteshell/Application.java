package liteshell;

import static java.util.Arrays.asList;

import java.util.List;

/**
 * @author xvraniak@stuba.sk
 */

public class Application {

    private String colot;

    public Application() {
        this.colot = "white";
    }

    public int howMany(boolean b, boolean... b2) {
        return b2.length;
    }

    public static int count = 0;

    public static void walk(int start, Integer... nums) {
        System.out.println(nums.length);
    }

    public static void main(String[] args) {
        walk(1, new Integer[]{null});
        Application a = new Application();
        System.out.println(a.colot);
        System.out.println(a.count);
        a.howMany(true, new boolean[2]);
        a.change(new StringBuilder());
        a = null;
        System.out.println(a.count);
        List<String> l = asList("1", "2");
        StringBuilder p = new StringBuilder("a");
        change(p);
        System.out.println(p);
//        DateTimeFormatter f = DateTimeFormatter.ofPattern("MMMM dd yy");
//        LocalDate date = LocalDate.parse("January 02 15", f);
//        LocalTime time = LocalTime.parse("11:22");
//        System.out.println(date); // 2015-01-02 System.out.println(time);
//        System.out.println(time);

//        StringBuilder sb = new StringBuilder("");
////        if (s == sb) {
////            ;
////        }
//        int i = 0;
//        double b = 0;
//        if (i == b) {
//            ;
//        }
//        list.add(8, "dsad");
//        System.out.println(time.plus(period));
//        PluginFactory pluginFactory = PluginFactory.init();
//        Executor executor = new ExecutorImpl();

//        Scope application = new ApplicationScope(pluginFactory, executor);

//        try {
//            ((ApplicationScope) application).run();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        String s = "function void aaa3a(void a1, int a, list c){\n"
//            + "command;\ncommand\n"
//            + "}"
//            + "\n"
//            + "function int funct2(void a1, int a, list c){\n"
//            + "line 1\n"
//            + "line 2;\n"
//            + "}\n"
//            + "\n";
//        String types = "(void|int|double|list|map)";
//        String charsAndNumbers = "[a-zA-Z1-9]";
//        String line = "((\t*.*[;]\n){0,1})*";
//        String[] lines = s.split("\n");
//        Pattern p = Pattern.compile(
//            "function " + types + " \\D.{0,20}[(](" + types + " \\D" + charsAndNumbers + "{0,20})*(,\\s*" + types + " \\D" + charsAndNumbers
//                + "{0,20})*[)]\\s*[{]\\s*\n" + line + "[}]\n*");
//        match(s, p);
////        for (String str : lines) {
////            match(str, p);
////        }
////        s = "command | command par1 | command par1 par2 | command par1 par2 par 3;";
////        p = Pattern.compile("^.+\\s*([|]\\s*.+)+[;]$");
////        match(s, p);
//        s = "command;\ncommand;\n";
//        p = Pattern.compile(line);
////        match(s, p);
//    }
//
//    public static void match(String str, Pattern p) {
//        Matcher m = p.matcher(str);
//        System.out.print(str + " - ");
//        if (m.matches()) {
//            System.out.println("ok");
//        } else {
//            System.out.println("not ok");
//        }
    }

    private static void change(StringBuilder p) {
        p.append("dsa");
    }


}
