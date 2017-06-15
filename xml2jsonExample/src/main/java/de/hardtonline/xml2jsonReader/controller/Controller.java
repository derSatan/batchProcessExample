package de.hardtonline.xml2jsonReader.controller;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hardtonline.xml2jsonReader.model.Batch;
import de.hardtonline.xml2jsonReader.model.SingleRequest;
import de.hardtonline.xml2jsonReader.repository.BatchRepository;
import de.hardtonline.xml2jsonReader.repository.SingleRequestRepository;
import de.hardtonline.xml2jsonReader.service.XmlReader;

@RestController
public class Controller {
    private static final String template = "Hello, this is a simple REST service writing a xml file as json into a db";
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private XmlReader xr;
    
    @Autowired
    private SingleRequestRepository srr;
    
    @Autowired
	private BatchRepository br;
    
    @RequestMapping("/info")
    public String info() {
    	logger.debug("Started info");
        return String.format(template);
    }
    
    /*
     * Example: http://localhost:8080/readXml?fileName=large.xml
     */
    @RequestMapping("/readXml")
    public String readXml(@RequestParam(name="fileName")String fileName) {
    	logger.debug("Started readXml");
    	String result = "";
    	try {
    		// Read file into JSON
			JSONObject json = xr.getFileAsJson(fileName);
			logger.debug("XML read into JSON object");
			
			// Get Batch from JSON and write it into mongo db
			logger.debug("Read Batch from JSON object");
			Batch batch = xr.getBatchFromJson(json);
			String batchId = br.save(batch).id;
			logger.debug("Wrote Batch with id >" + batchId + "< into Mongo DB");
			
			// Get List of SingleRequests from JSON and write it into mongo db
			logger.debug("Read all SingleRequests from JSON object");
			List<SingleRequest> allSingleRequests = xr.getSingleRequestsFromJson(json, batchId);
			srr.save(allSingleRequests);
			logger.debug("Wrote >" + allSingleRequests.size() + "< SingleRequests into Mongo DB (expected " + batch.countRequests  +")");
			
			// For testing purposes, we give the json back with the ID of the batch
			result = "The new added batch with ID >" + batchId + "< looks like this:\n" + json.toString(4);
			
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			result = "WRONG! - Look at log!";
		}
    	
        return result;
    }
    
    /*
     * Example: http://localhost:8080/clearMongoDb
     */
    @RequestMapping("/clearMongoDb")
    public String clearMongoDb() {
    	logger.debug("Started clearMongoDb");
    	br.deleteAll();
    	srr.deleteAll();
    	
        return "Mongo DB cleared - everything is gone ...";
    }
}
