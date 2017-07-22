package de.hardtonline.worker1.service;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.hardtonline.worker1.job.WorkBatch;

@Service
public class WorkerService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Scheduler scheduler;
	private JobKey jobKey;
	
	public WorkerService() {
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			logger.error("Scheduler not started!");
			e.printStackTrace();
		}
		
	}
	
	public void startWorker() throws SchedulerException {
		JobDetail job = JobBuilder.newJob(WorkBatch.class)
				.withIdentity("workBatch", "group1").build();
		
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("triggerWorkBatch", "group1")
				.withSchedule(
				    SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(5).repeatForever())
				.build();
		
		jobKey = job.getKey();
		scheduler.scheduleJob(job, trigger);
		logger.debug("Job " + jobKey.toString() + " started!");
	}
	
	public void stopWorker() throws SchedulerException {
		scheduler.deleteJob(jobKey);
		logger.debug("Job " + jobKey.toString() + " deleted!");
	}

	public void startWorkerOnce() throws SchedulerException {
		JobDetail job = JobBuilder.newJob(WorkBatch.class)
				.withIdentity("workBatch", "group1").build();
		
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("triggerWorkBatch", "group1")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
				.build();
		
		jobKey = job.getKey();
		scheduler.scheduleJob(job, trigger);
		logger.debug("Job " + jobKey.toString() + " started once!");
	}
	
	public boolean isRunning() throws SchedulerException {
		return scheduler.checkExists(jobKey);
	}
}
