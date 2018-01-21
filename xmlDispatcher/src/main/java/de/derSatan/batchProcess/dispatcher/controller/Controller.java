package de.derSatan.batchProcess.dispatcher.controller;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.derSatan.batchProcess.dispatcher.model.Batch;
import de.derSatan.batchProcess.dispatcher.model.SingleRequest;
import de.derSatan.batchProcess.dispatcher.repository.BatchRepository;
import de.derSatan.batchProcess.dispatcher.repository.SingleRequestRepository;
import de.derSatan.batchProcess.dispatcher.service.XmlReader;

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
    
    private ObjectMapper mapper = new ObjectMapper();
    
    /*
     * Example: http://localhost:10050/dispatcher/info
     */
    @RequestMapping(method = RequestMethod.GET, 
    		value = "/info",
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public String info() {
    	logger.debug("Started info");
    	
        ArrayNode arrayNode = mapper.createArrayNode();
        ObjectNode response = mapper.createObjectNode();
        response.put("information", String.format(template));
        arrayNode.add(response);
        
        return arrayNode.toString();
    }
    
    /*
     * Example: http://localhost:10050/dispatcher/readXml?fileName=large.xml
     */
    @RequestMapping(method = RequestMethod.POST, 
    		value = "/readXml",
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public String readXml(@RequestParam(name="fileName")String fileName) {
    	logger.debug("Started readXml");
    	ArrayNode arrayNode = mapper.createArrayNode();
        ObjectNode response = mapper.createObjectNode();
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
	        response.put("result", "The new added batch with ID >" + batchId + "< looks like this:\n" + json.toString(4));
	        arrayNode.add(response);
			
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			response.put("result", "WRONG! - Look at log!");
	        arrayNode.add(response);
		}
    	
        return arrayNode.toString();
    }
    
    /*
     * Example: http://localhost:10050/dispatcher/clearMongoDb
     */
    @RequestMapping(method = RequestMethod.DELETE, 
    		value = "/clearMongoDb",
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public String clearMongoDb() {
    	logger.debug("Started clearMongoDb");
    	ArrayNode arrayNode = mapper.createArrayNode();
        ObjectNode response = mapper.createObjectNode();
        
    	br.deleteAll();
    	srr.deleteAll();
    	
    	response.put("result", "Mongo DB cleared - everything is gone ...");
        arrayNode.add(response);
        
        return arrayNode.toString();
    }
}
