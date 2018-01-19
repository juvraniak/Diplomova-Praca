package sk.stuba.parser;

import terminal.common.scopes.Scope;

public interface Parser {

    void parse(String command, Scope scope);
}
