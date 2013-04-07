package org.sinmetal.sample.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.TaskQueuePb.TaskQueueAddRequest;
import com.google.appengine.api.taskqueue.TaskQueuePb.TaskQueueBulkAddRequest;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.ApiConfig;
import com.google.apphosting.api.ApiProxy.ApiProxyException;
import com.google.apphosting.api.ApiProxy.Delegate;
import com.google.apphosting.api.ApiProxy.Environment;
import com.google.apphosting.api.ApiProxy.LogRecord;

public class PutTaskQueueNamedControllerTest extends ControllerTestCase {

	LocalServiceTestHelper helper;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		LocalTaskQueueTestConfig taskQueueTestConfig = new LocalTaskQueueTestConfig();
		taskQueueTestConfig.setQueueXmlPath("war/WEB-INF/queue.xml").setDisableAutoTaskExecution(true);

		helper = new LocalServiceTestHelper(taskQueueTestConfig);
		helper.setUp();
		ApiProxy.setDelegate(new TQDelegate());
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	static class TQDelegate implements Delegate<Environment> {

		@SuppressWarnings("unchecked")
		final Delegate<Environment> parent = ApiProxy.getDelegate();

		@Override
		public void flushLogs(Environment environment) {
			parent.flushLogs(environment);

		}

		@Override
		public List<Thread> getRequestThreads(Environment environment) {
			return parent.getRequestThreads(environment);
		}

		@Override
		public void log(Environment environment, LogRecord record) {
			parent.log(environment, record);
		}

		@Override
		public Future<byte[]> makeAsyncCall(Environment environment,
				String packageName, String methodName, byte[] request,
				ApiConfig apiConfig) {
			inspectTaskAddService(packageName, methodName, request);
			return parent.makeAsyncCall(environment, packageName, methodName,
					request, apiConfig);
		}

		@Override
		public byte[] makeSyncCall(Environment environment, String packageName,
				String methodName, byte[] request) throws ApiProxyException {
			inspectTaskAddService(packageName, methodName, request);
			return parent.makeSyncCall(environment, packageName, methodName,
					request);
		}

		void inspectTaskAddService(String packageName, String methodName,
				byte[] request) {
			if (packageName.equals("taskqueue") && methodName.equals("BulkAdd")) {
				TaskQueueBulkAddRequest taskPb = new TaskQueueBulkAddRequest();
				taskPb.mergeFrom(request);
				for (int i = 0; i < taskPb.addRequestSize(); i++) {
					TaskQueueAddRequest addRequest = taskPb.getAddRequest(i);
					System.out.println("add taskqueue url = "
							+ addRequest.getUrl());
				}
			}
		}
	}

	@Test
	public void TaskQueueAddTest() throws Exception {
		tester.start("/putTaskQueueNamed");
		PutTaskQueueNamedController controller = tester.getController();
		assertThat(controller, is(notNullValue()));
		assertThat(tester.isRedirect(), is(false));
		assertThat(tester.getDestinationPath(), is(nullValue()));

		assertThat(tester.tasks.size(), is(1));
	}

	@Test
	public void TaskQueueRunTest() throws Exception {
		tester.start("/putTaskQueueNamed");
		PutTaskQueueNamedController controller = tester.getController();
		assertThat(controller, is(notNullValue()));
		assertThat(tester.isRedirect(), is(false));
		assertThat(tester.getDestinationPath(), is(nullValue()));

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey("example", "hoge");
		Entity entity = ds.get(key);
		assertThat(entity, is(notNullValue()));
	}
}
