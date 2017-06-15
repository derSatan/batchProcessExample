package de.hardtonline.xml2jsonReader.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.hardtonline.xml2jsonReader.model.Batch;
import de.hardtonline.xml2jsonReader.model.SingleRequest;

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

		return batch;
	}

	public List<SingleRequest> getSingleRequestsFromJson(JSONObject json, String batchId) throws JSONException {
		List<SingleRequest> allRequests = new ArrayList<SingleRequest>();

		JSONArray jsonArray = json.getJSONObject("CDMasseRequestErzeugenEingabe").getJSONArray("MasseRequest");

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			SingleRequest sr = new SingleRequest();
			sr.batchId = batchId;
			sr.satzId = obj.getLong("SatzId");
			sr.json = obj.getJSONObject("CDSingleRequestErzeugenEingabe");
			sr.status = "NEW";
			
			allRequests.add(sr);
		}

		return allRequests;
	}
}
