package cn.edu.scu.virjarjcd.exception;

public class ValidationError extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7031839017845544375L;

	public ValidationError(String message) {
        super(message);
    }
}
