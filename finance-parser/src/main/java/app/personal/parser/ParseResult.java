package app.personal.parser;

public class ParseResult {
    private final boolean success;
    private final Object result;
    private final String errorMessage;

    private ParseResult(boolean success, Object result, String errorMessage) {
        this.success = success;
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public static ParseResult success(Object result) {
        return new ParseResult(true, result, null);
    }

    public static ParseResult failure(String errorMessage) {
        return new ParseResult(false, null, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
