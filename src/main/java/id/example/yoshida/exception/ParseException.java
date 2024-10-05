package id.example.yoshida.exception;

public class ParseException extends RuntimeException {

	public ParseException(String message, Throwable e) {
		super(message, e);
	}

}
