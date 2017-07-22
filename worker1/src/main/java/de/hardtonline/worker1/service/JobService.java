package de.hardtonline.worker1.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hardtonline.worker1.model.Batch;
import de.hardtonline.worker1.model.BatchStatus;
import de.hardtonline.worker1.model.SingleRequest;
import de.hardtonline.worker1.repository.BatchRepository;
import de.hardtonline.worker1.repository.SingleRequestRepository;

@Service
public class JobService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
	private BatchRepository br;
    
    @Autowired
    private SingleRequestRepository srr;
	
	public List<Batch> getBatchList() {
		logger.debug("Search for Batches");
		return br.findByBatchStatus(BatchStatus.READY);
	}

	public void saveBatch(Batch batch) {
		br.save(batch);
	}

	public List<SingleRequest> getSingleRequestList(String id) {
		logger.debug("Search for SingleRequest");
		return srr.findByBatchId(id);
	}
}
