package de.hardtonline.worker1.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class Batch {

	@Id
	public String id;
	
	@Indexed
	public String consumer;
	
	public String description;
	public long countRequests;
	
	public Batch() {}
	
	
	public Batch(String consumer, String description, long countRequests) {
		this.consumer = consumer;
		this.description = description;
		this.countRequests = countRequests;
	}

	@Override
	public String toString() {
		return "Batch [id=" + id + ", consumer=" + consumer + ", description=" + description + ", countRequests="
				+ countRequests + "]";
	}
}
