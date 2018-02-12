package parsers;

public class DoubleParser implements CommandParser{
    @Override
    public String[] parse(String input) {
        return input.split(" ");
    }

}
