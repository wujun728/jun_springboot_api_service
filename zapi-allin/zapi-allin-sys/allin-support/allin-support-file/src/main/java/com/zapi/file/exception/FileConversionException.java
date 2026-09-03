package com.zapi.file.exception;

/**
 * 文件转换异常
 * 用于文件转换过程中的异常处理
 */
public class FileConversionException extends Exception {

    private String errorCode;
    private String taskId;

    public FileConversionException(String message) {
        super(message);
    }

    public FileConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileConversionException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public FileConversionException(String errorCode, String message, String taskId) {
        super(message);
        this.errorCode = errorCode;
        this.taskId = taskId;
    }

    public FileConversionException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * 常见错误代码
     */
    public static class ErrorCodes {
        public static final String UNSUPPORTED_FORMAT = "UNSUPPORTED_FORMAT";
        public static final String FILE_TOO_LARGE = "FILE_TOO_LARGE";
        public static final String CONVERSION_TIMEOUT = "CONVERSION_TIMEOUT";
        public static final String INVALID_FILE = "INVALID_FILE";
        public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
        public static final String DEPENDENCY_MISSING = "DEPENDENCY_MISSING";
    }
}
