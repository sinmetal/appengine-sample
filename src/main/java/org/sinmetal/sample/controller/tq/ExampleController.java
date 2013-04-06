package org.sinmetal.sample.controller.tq;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class ExampleController extends Controller {

	@Override
	public Navigation run() throws Exception {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity entity = new Entity("example", "hoge");
		ds.put(entity);
		return null;
	}

	public static void executeQuery() {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Queue queue = QueueFactory.getDefaultQueue();
		try {
			Transaction txn = ds.beginTransaction();

			// ...

			queue.add(TaskOptions.Builder.withUrl("/tq/example"));

			// ...
			txn.commit();
		} catch (DatastoreFailureException e) {
			throw e;
		}
	}
}
