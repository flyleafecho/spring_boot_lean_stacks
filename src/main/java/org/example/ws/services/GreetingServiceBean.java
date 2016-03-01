package org.example.ws.services;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.example.ws.model.Greeting;
import org.springframework.stereotype.Service;

@Service
public class GreetingServiceBean implements GreetingService {
	
	private static BigInteger nextId;
	private static Map<BigInteger, Greeting> greetingMap;

	private static Greeting save(Greeting greeting) {
		if (greetingMap == null) {
			greetingMap = new HashMap<BigInteger, Greeting>();
			nextId = BigInteger.ONE;
		}
		// If Update..
		if (greeting.getId() != null) {
			Greeting oldGreeting = greetingMap.get(greeting.getId());
			if (oldGreeting == null) {
				return null;
			}
			greetingMap.remove(greeting.getId());
			greetingMap.put(greeting.getId(), greeting);
			return greeting;
		}
		// If Create..
		greeting.setId(nextId);
		nextId = nextId.add(BigInteger.ONE);
		greetingMap.put(greeting.getId(), greeting);
		return greeting;
	}

	private static boolean remove(Long id) {
		Greeting deletedGreeting = greetingMap.remove(id);
		if (deletedGreeting == null) {
			return false;
		}
		return true;
	}

	@Override
	public Collection<Greeting> findAll() {
		Collection<Greeting> greetings = greetingMap.values();
		return greetings;
	}

	@Override
	public Greeting findOne(Long id) {
		Greeting greeting = greetingMap.get(id);
		return greeting;
	}

	@Override
	public Greeting create(Greeting greeting) {
		Greeting savedGreeting = save(greeting);
		return savedGreeting;
	}

	@Override
	public Greeting update(Greeting greeting) {
		Greeting updatedGreeting = save(greeting);
		return updatedGreeting;
	}

	@Override
	public void delete(Long id) {
		remove(id);

	}

}
