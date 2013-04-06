package org.sinmetal.sample.controller;

import org.slim3.tester.ControllerTestCase;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PutTaskQueueControllerTest extends ControllerTestCase {

	@Test
	public void TaskQueueAddTest() throws Exception {
		tester.start("/putTaskQueue");
		PutTaskQueueController controller = tester.getController();
		assertThat(controller, is(notNullValue()));
		assertThat(tester.isRedirect(), is(false));
		assertThat(tester.getDestinationPath(), is(nullValue()));
		
		assertThat(tester.tasks.size(), is(1));
	}

	@Test
	public void TaskQueueRunTest() throws Exception {
		tester.start("/putTaskQueue");
		PutTaskQueueController controller = tester.getController();
		assertThat(controller, is(notNullValue()));
		assertThat(tester.isRedirect(), is(false));
		assertThat(tester.getDestinationPath(), is(nullValue()));

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey("example", "hoge");
		Entity entity = ds.get(key);
		assertThat(entity, is(notNullValue()));
	}
}
