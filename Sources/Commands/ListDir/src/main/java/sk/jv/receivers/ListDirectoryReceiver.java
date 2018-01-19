package sk.jv.receivers;

import java.io.File;

import terminal.common.receivers.Executable;

public class ListDirectoryReceiver implements Executable {

    @Override
    public void executeCommand(String[] args, terminal.common.scopes.Scope scope) {
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
