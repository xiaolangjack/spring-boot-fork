package com.jack.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created By: jacky<workinglang@163.com>
 * Created At: 2023/9/21 18:01
 * <p></p>
 */
@Configuration
public class ScheduleConfiguration implements SchedulingConfigurer {
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(3);
		ConcurrentTaskScheduler concurrentTaskScheduler = new ConcurrentTaskScheduler(scheduledThreadPoolExecutor);
		taskRegistrar.setTaskScheduler(concurrentTaskScheduler);
	}
}
