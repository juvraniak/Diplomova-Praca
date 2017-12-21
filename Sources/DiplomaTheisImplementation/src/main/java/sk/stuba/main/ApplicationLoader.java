package sk.stuba.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import sk.stuba.parser.CommandParser;
import sk.stuba.registration.Registrator;

public class ApplicationLoader {

    private static Registrator registrator;
    private static CommandParser commandParser;

    public static void main(String[] args) throws IOException {
        String[] commands = {"ls /Users/jvraniak@sk.ibm.com/Desktop/test2/",
            "cp /Users/jvraniak@sk.ibm.com/Desktop/test/test.txt /Users/jvraniak@sk.ibm.com/Desktop/test2/test.txt",
            "ls /Users/jvraniak@sk.ibm.com/Desktop/test2/",
            "ls /Users/jvraniak@sk.ibm.com/Desktop/test/",
            "mv /Users/jvraniak@sk.ibm.com/Desktop/test2/test.txt /Users/jvraniak@sk.ibm.com/Desktop/test/test1.txt",
            "ls /Users/jvraniak@sk.ibm.com/Desktop/test/"};
        initialize();
        for(String str : commands){

            commandParser.parse(str);
        }

//        while (true) {
//            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//            String s = br.readLine();
////            String input = System.console().readLine();
//            commandParser.parse(s);
//            break;
//        }
    }

    private static void initialize(){
        registrator = new Registrator();
        commandParser = new CommandParser();
    }

    public static Registrator getRegistrator(){
        return registrator;
    }

}
