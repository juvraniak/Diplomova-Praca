package sk.stuba.commands;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sk.stuba.recievers.DownloadLibraryReceiver;
import terminal.common.command.Command;
import terminal.common.receivers.Executable;
import terminal.common.scopes.Scope;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class DownloadLibraryCommand implements Command {

    Executable receiver = new DownloadLibraryReceiver();

    @Override
    public void execute(String s, Scope scope) {
        Pattern p = Pattern.compile("\\b(pkg) \\b(download) .+[.](jar)");
        Matcher m = p.matcher(s);
        if (m.matches()) {
            receiver.executeCommand(parseComand(s), scope);
        }
    }

    @Override
    public String[] parseComand(String s) {
        return s.split(" ");
    }
}
