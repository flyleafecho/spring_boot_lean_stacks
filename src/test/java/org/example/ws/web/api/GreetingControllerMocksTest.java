package org.example.ws.web.api;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.example.ws.AbstractControllerTest;
import org.example.ws.model.Greeting;
import org.example.ws.service.EmailService;
import org.example.ws.service.GreetingService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static junit.framework.Assert.*;

@Transactional
public class GreetingControllerMocksTest extends AbstractControllerTest{

	@Mock
	private EmailService emailService;
	
	@Mock
	private GreetingService greetingService;
	
	@InjectMocks
	private GreetingController controller;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);		
		setUp(controller); 
	}
	
	@Test
	public void testGetGreetings() throws Exception {
		Collection<Greeting> list = getEntityListStubData();
		when(greetingService.findAll()).thenReturn(list);
		
		String uri = "/api/greetings";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();

		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		//verify if methos findAll of greetingService was called once
		verify(greetingService, times(1)).findAll();

		assertEquals("failure - expected HTTP status 200", 200, status);
		assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
	}

	@Test
	public void testGetGreeting() throws Exception {
		Long id = new Long(1L);
		Greeting entity = getEntityStubData();
		
		when(greetingService.findOne(id)).thenReturn(entity);
		String uri = "/api/greetings/{id}";

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON))
				.andReturn();

		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(greetingService, times(1)).findOne(id);

		assertEquals("failure - expected HTTP status 200", 200, status);
		assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
	}

	@Test
	public void testGetGreetingNotFound() throws Exception {
		Long id = Long.MAX_VALUE;
		
		when(greetingService.findOne(id)).thenReturn(null);
		String uri = "/api/greetings/{id}";

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(greetingService, times(1)).findOne(id);

		assertEquals("failure - expected HTTP status 404", 404, status);
		assertTrue("failure - expected HTTP response body to be empty", content.trim().length() == 0);
	}

	@Test
	public void testCreateGreeting() throws Exception {
		Greeting entity = getEntityStubData();
		
		when(greetingService.create(any(Greeting.class))).thenReturn(entity);
		
		String uri = "/api/greetings";
		String inputJson = super.mapToJson(entity);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(greetingService, times(1)).create(any(Greeting.class));
		
		assertEquals("failure - expected HTTP status 201", 201, status);
		assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
		
		Greeting createdGreeting = super.mapFromJson(content, Greeting.class);
		
		assertNotNull("failure - expected greeting not null", createdGreeting);
		assertNotNull("failure - expected greeting.id not null", createdGreeting.getId());
		assertEquals("failure - expected greeting.text match", "hello", createdGreeting.getText());
	}
	
	@Test
	public void testUpdateGreeting() throws Exception {
		Long id = new Long(1L);
		Greeting greeting = getEntityStubData();
		String updatedText = greeting.getText() + " test";		
		greeting.setText(updatedText);
		
		when(greetingService.update(any(Greeting.class))).thenReturn(greeting);
		
		String uri = "/api/greetings/{id}";
		String inputJson = super.mapToJson(greeting);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(greetingService, times(1)).update(any(Greeting.class));
		
		assertEquals("failure - expected HTTP status 200", 200, status);
		assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
		
		Greeting updatedGreeting = super.mapFromJson(content, Greeting.class);
		
		assertNotNull("failure - expected greeting not null", updatedGreeting);
		assertEquals("failure - expected greeting.id not changed", greeting.getId(), updatedGreeting.getId());
		assertEquals("failure - expected greeting.text match", updatedText, updatedGreeting.getText());
	}
	
	@Test
	public void testDeleteGreeting() throws Exception {
		Long id = new Long(1);
		
		String uri = "/api/greetings/{id}";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, id).contentType(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		verify(greetingService, times(1)).delete(id);
		
		assertEquals("failure - expected HTTP status 204", 204, status);
		assertTrue("failure - expected HTTP response body to be empty", content.trim().length() == 0);
		
		Greeting deletedGreeting = greetingService.findOne(id);
		assertNull("failure - expected greeting to be null", deletedGreeting);
	}
	
	private Collection<Greeting> getEntityListStubData() {
        Collection<Greeting> list = new ArrayList<Greeting>();
        list.add(getEntityStubData());
        return list;
    }

    private Greeting getEntityStubData() {
        Greeting entity = new Greeting();
        entity.setId(1L);
        entity.setText("hello");
        return entity;
    }
	
}
