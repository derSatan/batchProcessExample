package de.derSatan.batchProcess.dispatcher.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.derSatan.batchProcess.dispatcher.model.Batch;
import de.derSatan.batchProcess.dispatcher.model.BatchStatus;
import de.derSatan.batchProcess.dispatcher.model.SingleRequest;

@Service
public class XmlReader {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${file.inFolder}")
	private String inFolder;

	public JSONObject getFileAsJson(String fileName) throws JSONException, IOException {
		String filePath = inFolder + fileName;

		// First, we read the file at whole
		// TODO: memory optimization, since it is all loaded into memory
		logger.debug("Reading file >" + filePath + "<");
		String xml = new String(Files.readAllBytes(Paths.get(filePath)));

		// Then we create the json and return it
		return XML.toJSONObject(xml);
	}

	public Batch getBatchFromJson(JSONObject json) throws JSONException {
		Batch batch = new Batch();

		JSONObject masseDetail = json.getJSONObject("CDMasseRequestErzeugenEingabe").getJSONObject("MasseDetail");
		batch.consumer = masseDetail.getString("Konsument");
		batch.countRequests = masseDetail.getLong("AnzahlSaetze");
		batch.description = masseDetail.getString("Beschreibung");
		batch.batchStatus = BatchStatus.READY;

		return batch;
	}

	public List<SingleRequest> getSingleRequestsFromJson(JSONObject json, String batchId) throws JSONException {
		List<SingleRequest> allRequests = new ArrayList<SingleRequest>();
		JSONObject request = json.getJSONObject("CDMasseRequestErzeugenEingabe");
		// TODO: If there is only one request, we only receive an object, if it is more than one, we receive an array
		int anzahlSaetze = request.getJSONObject("MasseDetail").getInt("AnzahlSaetze");
		if (anzahlSaetze == 1) {
			JSONObject singleSatz = request.getJSONObject("MasseRequest");
			Iterator<?> keys = singleSatz.keys();
			SingleRequest sr = new SingleRequest();
	    	sr.batchId = batchId;
			sr.status = "NEW";
			
			// Look for all elements inside of MasseRequest, this should be SatzId and CDSingleRequestErzeugenEingabe
			while(keys.hasNext()) { 
				String key = (String) keys.next();
			    Object element = singleSatz.get(key);
			    if (key.equals("SatzId")) {
			    	sr.satzId = new Long(element.toString());
			    } else if (key.equals("CDSingleRequestErzeugenEingabe")) {
			    	sr.json = new JSONObject(element.toString());
			    }
			}

			allRequests.add(sr);

		} else {
			JSONArray masseRequestArray = request.getJSONArray("MasseRequest");
			for (int i = 0; i < masseRequestArray.length(); i++) {
				JSONObject jsonObject = masseRequestArray.getJSONObject(i);
				Iterator<?> keys = jsonObject.keys();
				SingleRequest sr = new SingleRequest();
		    	sr.batchId = batchId;
				sr.status = "NEW";
				
				// Look for all elements inside of MasseRequest, this should be SatzId and CDSingleRequestErzeugenEingabe
				while(keys.hasNext()) { 
					String key = (String) keys.next();
				    Object element = jsonObject.get(key);
				    if (key.equals("SatzId")) {
				    	sr.satzId = new Long(element.toString());
				    } else if (key.equals("CDSingleRequestErzeugenEingabe")) {
				    	sr.json = new JSONObject(element.toString());
				    }
				}

				allRequests.add(sr);
			}	
		}
		
		return allRequests;
	}
}
