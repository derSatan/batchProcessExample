package de.hardtonline.xml2jsonReader.model;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class SingleRequest {

	@Id
	public String id;
	
	@Indexed
	public String batchId;
	
	@Indexed
	public String satzId;
	
	public String status;
	
	public JSONObject json;

	public SingleRequest() {}
	
	public SingleRequest(String satzId, JSONObject json) {
		this.satzId = satzId;
		this.json = json;
	}
}
