package org.example.ws.web.api;

import java.util.Map;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.example.ws.web.DefaultExceptionAttributes;
import org.example.ws.web.ExceptionAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(NoResultException.class)
	public ResponseEntity<Map<String, Object>> handleNoResultException(NoResultException nre,
			HttpServletRequest httpRequest) {
		logger.error("> handleNoResultException");
		logger.error("- NoResultException: ", nre);

		ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();
		Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(nre, httpRequest,
				HttpStatus.NOT_FOUND);
		
		logger.error("< handleNoResultException");
		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleException(Exception e, HttpServletRequest httpRequest) {
		logger.error("> handleException");
		logger.error("- Exception: ", e);

		ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();
		Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(e, httpRequest,
				HttpStatus.INTERNAL_SERVER_ERROR);

		logger.error("< handleException");
		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
