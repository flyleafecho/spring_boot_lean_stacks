package org.example.ws.service;

import java.math.BigInteger;
import java.util.Collection;

import org.example.ws.model.Greeting;

public interface GreetingService {

	Collection<Greeting> findAll();
	
	Greeting findOne(BigInteger id);
	
	Greeting create(Greeting greeting);
	
	Greeting update(Greeting greeting);
	
	void delete(BigInteger id);
}
