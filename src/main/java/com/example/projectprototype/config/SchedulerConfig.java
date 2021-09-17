package com.example.projectprototype.config;

import com.example.projectprototype.scheduler.job.RoomFailManageJob;
import com.example.projectprototype.scheduler.job.RoomSuccessManageJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import javax.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:/quartz.properties")
public class SchedulerConfig {

    private final Scheduler scheduler;

    @Autowired
    public SchedulerConfig(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostConstruct
    public void schedulingJobs() {
        JobDetail successManageDetail = newJob(RoomSuccessManageJob.class)
                .withIdentity("SuccessManage","RoomSchedule")
                .build();

        JobDetail failManageDetail = newJob(RoomFailManageJob.class)
                .withIdentity("FailManage","RoomSchedule")
                .build();

        Trigger successManageTrigger = newTrigger()
                .withIdentity("SuccessManage", "RoomSchedule")
                .withSchedule(cronSchedule("0 28/30 * * * ?"))// 매 시간 28분, 58분에 스케줄 시작
                .forJob(successManageDetail)
                .build();

        Trigger failManageTrigger = newTrigger()
                .withIdentity("FailManage","RoomSchedule")
                .withSchedule(cronSchedule("0 0 1 * * ?"))
                .forJob("FailManage","RoomSchedule")
                .build();
        try {
            scheduler.scheduleJob(successManageDetail, successManageTrigger);
            scheduler.scheduleJob(failManageDetail, failManageTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            //error 로깅
        }

    }
}
