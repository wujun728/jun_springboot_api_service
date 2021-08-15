package cn.edu.scu.virjarjcd.exception;

public class CodegenException extends RuntimeException
{
  private static final long serialVersionUID = -7762077902992060385L;

  public CodegenException()
  {
  }

  public CodegenException(String message)
  {
    super(message);
  }

  public CodegenException(Throwable cause) {
    super(cause);
  }

  public CodegenException(String message, Throwable cause) {
    super(message, cause);
  }

  public CodegenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
