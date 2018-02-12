package commandcommons;

public class CommandInputImpl implements CommandInput{
    private String input;

    public CommandInputImpl(String input) {
        this.input = input;
    }

    @Override
    public String getCommandInput() {
        return input;
    }
}
