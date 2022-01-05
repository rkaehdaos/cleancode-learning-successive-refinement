package me.rkaehdaos.cleancodelearningsuccessiverefinement;

public class ArgsException extends Exception {
    private char errorArgumentId = '\0';
    private String errorParameter = "TILT";
    private ErrorCode errorCode = ErrorCode.OK;

    // 생성자
    public ArgsException() { }
    public ArgsException(String message) {
        super(message);
    }

    public enum ErrorCode {
        OK,
        MISSING_STRING, MISSING_INTEGER,
        INVALID_INTEGER, INVALID_ARGUMENT_NAME, MISSING_DOUBLE, INVALID_DOUBLE, UNEXPECTED_ARGUMENT
    }


    public String errorMessage() {
        return "";
    }

    //getter
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public char getErrorArgumentId() {
        return errorArgumentId;
    }
}
