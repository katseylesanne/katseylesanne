package ee.ivo.katseyl.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NonExistentIdException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NonExistentIdException(String message) {
		super(message);
	}

}
