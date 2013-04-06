package org.sinmetal.sample.controller.tq;

import org.slim3.tester.ControllerTestCase;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ExampleControllerTest extends ControllerTestCase {

	@Test
	public void addTaskTest() throws Exception {
		// TODO task queue run test
		ExampleController.executeQuery();
	}

	@Test
	public void run() throws Exception {
		tester.start("/tq/example");
		ExampleController controller = tester.getController();
		assertThat(controller, is(notNullValue()));
		assertThat(tester.isRedirect(), is(false));
		assertThat(tester.getDestinationPath(), is(nullValue()));

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity entity = ds.get(KeyFactory.createKey("example", "hoge"));
		assertThat(entity, is(notNullValue()));
	}
}
