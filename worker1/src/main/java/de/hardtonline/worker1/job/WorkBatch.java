package de.hardtonline.worker1.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkBatch implements Job {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		logger.debug("Hello, this is the WorkBatch! The current time is >" + timeStamp + "<");
	}
}
