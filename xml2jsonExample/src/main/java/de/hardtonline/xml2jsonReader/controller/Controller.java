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
	private BatchRepository batchRepo;
    
    @RequestMapping("/info")
    public String info() {
    	logger.debug("Started info");
        return String.format(template);
    }
    
    @RequestMapping("/readXml")
    public String readXml(@RequestParam(name="fileName")String fileName) {
    	logger.debug("Started readXml");
    	String result = "";
    	try {
    		// Read file into JSON
			JSONObject json = xr.getFileAsJson(fileName);
			
			// Get Batch from JSON and write it into mongo db
			Batch batch = xr.getBatchFromJson(json);
			String batchId = batchRepo.save(batch).id;
			
			// Get List of SingleRequests from JSON and write it into mongo db
			List<SingleRequest> allSingleRequests = xr.getSingleRequestsFromJson(json, batchId);
//			srr.save(allSingleRequests);
			
			// For testing purposes, we give the json back with the ID of the batch
			result = "The new added batch with ID >" + batchId + "< looks like this:\n" + json.toString(4);
			
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			result = "WRONG! - Look at log!";
		}
    	
        return result;
    }
}
