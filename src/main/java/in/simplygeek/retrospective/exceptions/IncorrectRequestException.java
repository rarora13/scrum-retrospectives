package in.simplygeek.retrospective.exceptions;

public class IncorrectRequestException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8852975368404211949L;

	public IncorrectRequestException(String error) {
		super(error);
	}
}