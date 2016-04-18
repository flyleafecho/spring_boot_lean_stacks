package org.example.ws.service;

import java.util.concurrent.Future;

import org.example.ws.model.Greeting;
import org.example.ws.util.AsyncResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceBean implements EmailService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Boolean send(Greeting greeting) {
		logger.info("> send");
		
		Boolean success = Boolean.FALSE;
		
		long pause = 5000;
		try {
			Thread.sleep(pause);
		} catch (Exception e) {
			// TODO: handle exception
		}
		logger.info("< send");
		return success;
	}

	@Async
	@Override
	public void sendAsync(Greeting greeting) {
		logger.info("> sendAsync");
		
		try {
			send(greeting);
		} catch (Exception e) {
			logger.warn("Exception caught sending asynchronous mall.", e);
		}
		logger.info("< sendAsync");
	}

	@Async
	@Override
	public Future<Boolean> sendAsyncWithResult(Greeting greeting) {
		logger.info("> sendAsyncWithResult");
		
		AsyncResponse<Boolean> response = new AsyncResponse<Boolean>();
		
		try {
			Boolean success = send(greeting);
			response.complete(success);
		} catch (Exception e) {
			logger.warn("Exception caught sending asynchronous mall.", e);
			response.completeExceptionally(e);
		}
		logger.info("< sendAsyncWithResult");
		return null;
	}

}
