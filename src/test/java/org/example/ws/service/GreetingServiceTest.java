package org.example.ws.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import org.example.ws.AbstractTest;
import org.example.ws.model.Greeting;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class GreetingServiceTest extends AbstractTest {

	@Autowired
	private GreetingService service;

	@Before
	public void setUp() {
		service.evictCache();
	}

	@After
	public void tearDown() {
		// clean up after each test method
	}

	@Test
	public void testFindAll() {
		Collection<Greeting> list = service.findAll();

		assertNotNull("failure - expected not null", list);
		assertEquals("failure - expected size", 2, list.size());
	}

	@Test
	public void testFindOne() {
		Long id = new Long(1L);
		Greeting entity = service.findOne(id);

		assertNotNull("faiulure - expected not null", entity);
		assertEquals("failure - extected id attribute match", id, entity.getId());
	}

	@Test
	public void testFindOneNotFound() {
		long id = Long.MAX_VALUE;
		Greeting greeting = service.findOne(id);

		assertNull("failure - expected null", greeting);
	}

	@Test
	public void testCreate() {
		Greeting entity = new Greeting();
		entity.setText("test");
		Greeting createdEntity = service.create(entity);

		assertNotNull("failure - expected not null", createdEntity);
		assertNotNull("failure - expected id attribute not nutll", createdEntity.getId());
		assertEquals("failure - expected text attribute match", "test", createdEntity.getText());

		Collection<Greeting> list = service.findAll();
		assertEquals("failure - expected size", 3, list.size());
	}

	@Test
	public void testCreateWithId() {
		Exception e = null;

		Greeting entity = new Greeting();
		entity.setId(Long.MAX_VALUE);
		entity.setText("test");

		try {
			service.create(entity);
		} catch (EntityExistsException eee) {
			e = eee;
		}

		assertNotNull("failure - expected exception", e);
		assertTrue("failure - expected EntityExistsException", e instanceof EntityExistsException);
	}

	@Test
	public void testUpdate() {
		Long id = new Long(1);
		Greeting entity = service.findOne(id);

		assertNotNull("failure - expected updated entity not null", entity);

		String updatedText = entity.getText() + " test";
		entity.setText(updatedText);
		Greeting updatedEntity = service.update(entity);

		assertNotNull("failure - expected updated entity not null", updatedEntity);
		assertEquals("failure - expected updated entity id attribute unchanged", id, updatedEntity.getId());
		assertEquals("failure - expected updated entity text attribute match", updatedText, updatedEntity.getText());
	}

	@Test
	public void testUpdateNotFound() {
		Exception e = null;

		Greeting greeting = new Greeting();
		greeting.setId(Long.MAX_VALUE);
		greeting.setText("test");

		try {
			service.update(greeting);
		} catch (NoResultException eee) {
			e = eee;
		}

		assertNotNull("failure - expected exception", e);
		assertTrue("failure - expected NoResultException", e instanceof NoResultException);
	}

	@Test
	public void testDelete() {
		Long id = new Long(1L);
		Greeting entity = service.findOne(id);

		assertNotNull("failure - expected size", entity);

		service.delete(id);
		Collection<Greeting> list = service.findAll();

		assertEquals("failure - expected size", 1, list.size());

		Greeting deletedEntity = service.findOne(id);
		assertNull("failure - expected entity to be deleted", deletedEntity);
	}
}
