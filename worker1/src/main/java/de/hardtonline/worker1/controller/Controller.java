package de.hardtonline.worker1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hardtonline.worker1.repository.BatchRepository;
import de.hardtonline.worker1.repository.SingleRequestRepository;

// TODO: Scheduler für loops einbauen (start & pause & stop über REST realisieren)
// TODO: KOnfigurierbaren ThreadPool aufbauen, der die DB nach Batches im Status "XY" abfragt.
// TODO: "Harter" Stop einbauen
@RestController
public class Controller {
    private static final String template = "Hello, this is a simple REST service looking in a mongo db for things to do";
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private SingleRequestRepository srr;
    
    @Autowired
	private BatchRepository br;
    
    private boolean isRunning = false;
    
    /*
     * Example: http://localhost:10051/worker1/info
     */
    @RequestMapping("/info")
    public String info() {
    	logger.debug("Started info");
        return String.format(template);
    }
    
    /*
     * Example: http://localhost:10051/worker1/startProcess?runOnce=true
     */
    @RequestMapping("/startProcess")
    public String startProcess(@RequestParam(name="runOnce", required=false)boolean onlyOnce) {
    	logger.debug("Started Processing the database");
    	String result = "";
    	
    	if (onlyOnce) {
    		logger.debug("... and we run only once!");
    		result = "Started only once!";
    	} else {
    		isRunning = true;
    		result = "Start successful!";
    	}
    	
    	
    	
        return result;
    }
    
    /*
     * Example: http://localhost:10051/worker1/stopProcess
     */
    @RequestMapping("/stopProcess")
    public String stopProcess() {
    	logger.debug("Stopping processing the database");
    	
    	if (!isRunning) {
    		logger.debug("Worker is already stopped!");
    	}
    	
    	String result = "Stop successful!";
    	isRunning = false;
    	
        return result;
    }
    
    /*
     * Example: http://localhost:10051/worker1/status
     */
    @RequestMapping("/status")
    public String clearMongoDb() {
    	logger.debug("Started Status information");
    	
    	long countBatch = br.count();
    	long countRequests = srr.count();
    	
    	StringBuilder response = new StringBuilder();
    	response.append("We currently have >" + countBatch + "< Batches and >" + countRequests + "< SingleRequests in the db. ");
    	if (isRunning) {
    		response.append("And the worker is running!");
    	} else {
    		response.append("And the worker is not running!");
    	}
    	
        return response.toString();
    }
}
