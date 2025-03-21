package com.liwei.ruiyi.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.liwei.ruiyi.service.LingxingService;


@Service
public class ScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);


    LingxingService lingxingService;

    @Scheduled(fixedRate = 1000 * 10 * 100) // 每10分钟执行一次任务检测
    public void runTask() {
        //每10分钟运行一次的任务
        logger.info("每10分钟执行一次的任务:{}", "检查当前与领星接口之间的token数据信息");
        lingxingService.getOrRefreshToken();
    }

    @Scheduled(cron = "0 0-59 2 * * *")
    public void executeTask() {
        logger.info("每天2:00-3:00之间任意时间段执行的任务:{}", "检查当前与领星接口之间的token数据信息");
    }

}
