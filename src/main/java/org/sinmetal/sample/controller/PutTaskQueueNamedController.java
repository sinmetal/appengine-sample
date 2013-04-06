package org.sinmetal.sample.controller;

import org.sinmetal.sample.controller.tq.ExampleController;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class PutTaskQueueNamedController extends Controller {

    @Override
    public Navigation run() throws Exception {
    	ExampleController.executeQueryNamed();
        return null;
    }
}
