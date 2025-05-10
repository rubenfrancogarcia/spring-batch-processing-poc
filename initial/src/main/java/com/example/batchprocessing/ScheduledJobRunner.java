package com.example.batchprocessing;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJobRunner {

    private static final Logger log = LoggerFactory.getLogger(ScheduledJobRunner.class);
    final JobLauncher jobLauncher;
    final JobRegistry jobRegistry;
    //private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    public ScheduledJobRunner(JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    public void runSpecificJob(String jobName) throws Exception {
        Job job = jobRegistry.getJob(jobName);
        //spring requires unique job parameters for each execution.
        jobLauncher.run(job, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
    }


    @Scheduled(fixedRate = 10000) // Runs every 10 seconds
    public void runJob() {
        try {
            log.info("\n\n---------------- running job");
            runSpecificJob("importUserJob");
            log.info("\n\n---------------- running job end");

        } catch (Exception e) {
            log.error("error", e);
        }
    }


}