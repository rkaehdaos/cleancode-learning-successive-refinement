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


    public String errorMessage() throws Exception {
        switch(errorCode){
            case OK:
                throw new Exception("TILT: Should not get here.");
            case UNEXPECTED_ARGUMENT:
                return String.format("인자 -%c 예상치 못함.", errorArgumentId);
            case MISSING_STRING:
                return String.format("문자열 파라미터 -%c를 찾을 수 없음.", errorArgumentId);
            case INVALID_INTEGER:
                return String.format("Argument -%c는 정수를 기대했지만 %s.", errorArgumentId);
            case MISSING_INTEGER:
                return String.format("정수형 파라미터 -%c를 찾을 수 없음.", errorArgumentId);
            case INVALID_DOUBLE:
                return String.format("Argument -%c는 double을 기대했지만 %s.", errorArgumentId);
            case MISSING_DOUBLE:
                return String.format("double 파라미터 -%c를 찾을 수 없음.", errorArgumentId);
        }
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
