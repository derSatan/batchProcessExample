package de.hardtonline.worker1.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.hardtonline.worker1.model.Batch;
import de.hardtonline.worker1.model.BatchStatus;
import de.hardtonline.worker1.model.SingleRequest;
import de.hardtonline.worker1.repository.BatchRepository;
import de.hardtonline.worker1.service.JobService;

@Component
public class WorkBatch implements Job {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private JobService js;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		logger.debug("Hello, this is the WorkBatch! The current time is >" + timeStamp + "<");
		
		List<Batch> batches = js.getBatchList();
		
		for (Batch batch : batches) {
			logger.debug("Start working on batch >" + batch.id  +"<");
			
			// Save the status
			batch.batchStatus = BatchStatus.RUNNING_STEP1;
			js.saveBatch(batch);
			logger.debug(">" + batch.id  +"< - Status is RUNNING_STEP1");
			
			// Get all SingleRequests
			List<SingleRequest> singleRequests = js.getSingleRequestList(batch.id);
			for (SingleRequest sr : singleRequests) {
				logger.debug(">" + batch.id  +"< - >" + sr.id + "< - Satz to work on ...");	
			}
			
			// Save the status
			batch.batchStatus = BatchStatus.DONE_STEP1;
			js.saveBatch(batch);
			logger.debug(">" + batch.id  +"< - Status is DONE_STEP1");
		}
	}
}
