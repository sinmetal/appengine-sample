package org.sinmetal.sample.controller;

import org.sinmetal.sample.controller.tq.ExampleController;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class PutTaskQueueController extends Controller {

    @Override
    public Navigation run() throws Exception {
    	ExampleController.executeQuery();
        return null;
    }
}
