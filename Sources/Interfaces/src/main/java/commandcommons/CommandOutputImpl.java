package commandcommons;

public class CommandOutputImpl implements CommandOutput {
    private String output;

    public CommandOutputImpl(String output) {
        this.output = output;
    }

    @Override
    public String getCommandOutput() {
        return output;
    }
}
