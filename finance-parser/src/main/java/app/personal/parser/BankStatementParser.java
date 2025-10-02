package app.personal.parser;

import java.io.File;

public interface BankStatementParser {
    ParseResult parse(File file) throws ParseException;
}
