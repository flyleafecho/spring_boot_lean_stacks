package org.example.ws.service;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import org.example.ws.model.Greeting;
import org.example.ws.repository.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GreetingServiceBean implements GreetingService {

	@Autowired
	private GreetingRepository greetingRepository;
	
	@Autowired
	private CounterService counterService;

	
	@Override
	public Collection<Greeting> findAll() {
		counterService.increment("method.invoked.greetingServiceBean.findAll");
		Collection<Greeting> greetings = greetingRepository.findAll();
		return greetings;
	}

	@Override
	@Cacheable(value = "greetings", key = "#id")
	public Greeting findOne(Long id) {
		counterService.increment("method.invoked.greetingServiceBean.findOne");
		Greeting greeting = greetingRepository.findOne(id);		
		return greeting;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "greetings", key ="#result.id")
	public Greeting create(Greeting greeting) {
		counterService.increment("method.invoked.greetingServiceBean.create");
		if (greeting.getId() != null) {
			// Cannot create existing greeting
			throw new EntityExistsException("Cannot create new Greeting with supplied id. The attribute id must be null");
		}
		Greeting savedGreeting = greetingRepository.save(greeting);		
		return savedGreeting;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "greetings", key ="#greeting.id")
	public Greeting update(Greeting greeting) {
		counterService.increment("method.invoked.greetingServiceBean.update");
		Greeting greetingPersisted = findOne(greeting.getId());
		if (greetingPersisted == null) {
			//Cannot update Greeting that has not persisted
			throw new NoResultException("Cannot update a Greeting object without any id. The attribute id must not be null");
		}
		Greeting updatedGreeting = greetingRepository.save(greeting);
		return updatedGreeting;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CacheEvict(value = "greetings", key = "#id")
	public void delete(Long id) {
		counterService.increment("method.invoked.greetingServiceBean.delete");
		greetingRepository.delete(id);
	}
	
	@Override
	@CacheEvict(value = "greetings", allEntries = true)
	public void evictCache() {
		// TODO Auto-generated method stub
		
	}

}
