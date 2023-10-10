package com.jack.schedules;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created By: jacky<workinglang@163.com>
 * Created At: 2023/9/21 17:42
 * <p></p>
 */
@Component
public class PayScheduled {

	private static Logger LOGGER = LoggerFactory.getLogger(PayScheduled.class);
	@Scheduled(cron = "0/1 * * * * ?")
	public void pay(){
		LOGGER.info("Pay Start.......");
	}
}
