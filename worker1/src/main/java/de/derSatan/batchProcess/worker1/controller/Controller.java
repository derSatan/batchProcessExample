package de.derSatan.batchProcess.worker1.controller;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.derSatan.batchProcess.worker1.repository.BatchRepository;
import de.derSatan.batchProcess.worker1.repository.SingleRequestRepository;
import de.derSatan.batchProcess.worker1.service.WorkerService;

@RestController
public class Controller {
    private static final String template = "Hello, this is a simple REST service looking in a mongo db for things to do";
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private WorkerService ws;
    
    @Autowired
    private SingleRequestRepository srr;
    
    @Autowired
	private BatchRepository br;
    
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
		try {
			if (onlyOnce) {
				ws.startWorkerOnce();
				logger.debug("... and we run only once!");
				result = "Started only once!";
			} else {
				ws.startWorker(30); // TODO: Move interval to config
				result = "Start successful!";
			}
		} catch (SchedulerException e) {
			result = "START ERROR!";
			e.printStackTrace();
		}
    	
        return result;
    }
    
    /*
     * Example: http://localhost:10051/worker1/stopProcess
     */
    @RequestMapping("/stopProcess")
    public String stopProcess() {
    	logger.debug("Stopping processing the database");
    	String result = "";
    	try {
    		if (ws.isRunning()) {
    			ws.stopWorker();
    		} else {
    			logger.debug("Worker is already stopped!");
    		}
    	
    		result = "Stop successful!";
		} catch (SchedulerException e) {
			result = "START ERROR!";
			e.printStackTrace();
		} finally {
			result = "Stop successful!";
		}
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
    	try {
    		response.append("We currently have >" + countBatch + "< Batches and >" + countRequests + "< SingleRequests in the db. ");
    	
    		if (ws.isRunning()) {
    			response.append("And the worker is running!");
    		} else {
    			response.append("And the worker is not running!");
    		}
		} catch (SchedulerException e) {
			response.append("And we have a problem with the worker!");
			e.printStackTrace();
		}
        return response.toString();
    }
}
