package backend.java.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import backend.java.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class RestErrorHandler {

	@ExceptionHandler(value = EntityNotFoundException.class)
	public ResponseEntity<Void> handleNoSuchElementException(Exception e) {
		//log.error(e.getMessage());
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
