package liteshell.executors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javafx.util.Pair;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultInput;
import liteshell.exceptions.UnknownCommandException;
import liteshell.plugins.ShellPlugin;
import liteshell.scopes.Scope;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xvraniak@stuba.sk
 */

public class ExecutorImpl implements Executor {
    /**
     * use in not piped command
     * @param plugin plugin carrying executive command
     * @param command command string that will be taken to execute method
     * @param scope scope where command will be executed
     */
    @Override
    public void execute(Optional<ShellPlugin> plugin, String command, Scope scope) {

        if (command.startsWith("./")) {
            //handle script - maybe i will handle shell scripts by this
        } else {
            String requestedCommand = command.split(" ")[0];
            if (plugin.isPresent()) {
                CommandOutput output = plugin.get().getCommand().execute(DefaultInput.of(Stream.of(command)), Optional.of(scope));
                if (output.getCommandOutput().isPresent()) {
                    output.getCommandOutput().get().forEach(System.out::println);
                }
            } else {
                throw new UnknownCommandException(requestedCommand);
            }
        }
    }

    /**
     *
     * @param scope scope where list of commands should be executed
     * @param list list of pairs ShellPlugin and command that should be run on ShellPlugin command
     */
    @Override
    public void execute(Scope scope, List<Pair<Optional<ShellPlugin>, String>> list) {
        validatePluginList(list);
        List<ExecutorListener> listeners = new ArrayList<>();
        FinishingExecutor fe = new FinishingExecutor();
        fe.setShouldPrint(list.get(list.size()-1).getKey().get().shouldPrint());
        listeners.add(fe);
        ExecutorListener temp = fe;
        for(int i = list.size()-1; i > -1; i--){
            Pair<Optional<ShellPlugin>, String> item  = list.get(i);
            PipeExecutor executor = PipeExecutor.of(Stream.of(item.getValue()), item.getKey().get(), scope, temp, listeners);
            temp = executor;
            listeners.add(temp);
        }
        temp.execute();
    }

    private void validatePluginList(List<Pair<Optional<ShellPlugin>,String>> list) {
        list.forEach(pair -> {
            if(!pair.getKey().isPresent() || pair.getValue().length() < 1){
                throw new RuntimeException();
            }
        });
    }
}

interface ExecutorListener{
    void finishedExecution(CommandOutput output);
    void onError(CommandOutput output);
    void execute();
}

class PipeExecutor implements ExecutorListener{
    private ShellPlugin plugin;
    private Stream<String> command;
    private boolean shouldPrint;
    private Scope scope;
    private ExecutorListener listener;
    private List<ExecutorListener> listeners;

    private PipeExecutor(Stream<String> command, ShellPlugin plugin, Scope scope,ExecutorListener listener,  List<ExecutorListener> listeners){
        this.command = command;
        this.plugin = plugin;
        this.scope = scope;
        this.listener = listener;
        this.listeners = listeners;
    }

    public static PipeExecutor of(Stream<String> command, ShellPlugin plugin, Scope scope, ExecutorListener listener, List<ExecutorListener> listeners){
        return new PipeExecutor(command, plugin, scope, listener, listeners);
    }


    @Override
    public void finishedExecution(CommandOutput output) {
        command = Stream.concat(command, output.getCommandOutput().get());
        execute();
    }

    @Override
    public void onError(CommandOutput output) {

    }

    @Override
    public void execute(){
        CommandOutput out = plugin.getCommand().execute(DefaultInput.of(command), Optional.of(scope));
        if(out.getReturnCode().get() == 0){
            listener.finishedExecution(out);
        }
        System.out.println("tu");
    }
}

//class MiddleExecutor{
//    private ShellPlugin plugin;
//    private Stream<String> command;
//    private boolean shouldPrint;
//
//    private MiddleExecutor(Stream<String> command, ShellPlugin plugin){
//        this.command = command;
//        this.plugin = plugin;
//    }
//
//    public static MiddleExecutor of(Stream<String> command, ShellPlugin plugin){
//        return new MiddleExecutor(command, plugin);
//    }
//}
@Getter
@Setter
class FinishingExecutor implements ExecutorListener{
    private boolean shouldPrint;
    private Stream<String> output;

    public void printToConsole(){
        if(shouldPrint){
            output.forEach(System.out::println);
        }
    }

    @Override
    public void finishedExecution(CommandOutput output) {
        if(shouldPrint){
            output.getCommandOutput().get().forEach(System.out::println);
        }
    }

    @Override
    public void onError(CommandOutput output) {
        output = (CommandOutput) output.getCommandErrorOutput().get();
    }

    @Override
    public void execute() {

    }
}

interface Step<I, O> {

    O execute(I value);

    default <R> Step<I, R> pipe(Step<O, R> source) {
        return value -> source.execute(execute(value));
    }

    static <I, O> Step<I, O> of(Step<I, O> source) {
        return source;
    }
}
