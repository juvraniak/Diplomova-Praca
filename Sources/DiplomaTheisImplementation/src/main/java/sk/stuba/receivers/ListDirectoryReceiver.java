package sk.stuba.receivers;

import java.io.File;

public class ListDirectoryReceiver implements Executable{

    @Override
    public void executeCommand(String[] args) {
        File curDir = new File(args[1]);
        File[] filesList = curDir.listFiles();
        for(File f : filesList){
            if(f.isDirectory())
                System.out.println(f.getName());
            if(f.isFile()){
                System.out.println(f.getName());
            }
        }
    }
}
