package com.liwei.ruiyi.listener;

import com.liwei.ruiyi.service.*;
import com.liwei.ruiyi.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class ScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    LingxingService lingxingService;
    @Autowired
    MarketplaceService marketplaceService;
    @Autowired
    SellerService sellerService;
    @Autowired
    public OrderService orderService;
    @Autowired
    ProfitDayService profitDayService;

    // 创建固定大小的线程池，例如 5 个线程
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void refreshToken() {
        //每10分钟运行一次的任务
        executorService.submit(() -> {
            logger.info("每10分钟执行一次的任务:{}", "检查当前与领星接口之间的token数据信息");
            lingxingService.getOrRefreshToken();
        });
    }

    // 每周一 2:00:00 执行
    @Scheduled(cron = "0 0 2 * * MON")
    public void weeklyFixedTime() {
        executorService.submit(() -> {
            logger.info("每周一02:00执行的任务:{}", "查询得到亚马逊所有市场列表数据");
            marketplaceService.refreshMarketplace();
            logger.info("每周一02:00执行的任务:{}", "查询得到亚马逊对应国家的地区列表数据");
            marketplaceService.checkAllMarketplaceWorldState();

            logger.info("每周一02:00执行的任务:{}", "查询得到企业已授权到领星ERP的全部亚马逊店铺信息");
            sellerService.saveOrUpdate();
        });
    }

    @Scheduled(fixedRate = 1000 * 60 * 119)
    public void refreshAdData() {
        //每119分钟执行一次，和每10分钟一次的尽量错开
        //获取今天和昨天的广告数据
        executorService.submit(() -> {
            logger.info("每2小时执行一次的任务:{}", "检查当天和前一天的所有广告数据");
            String startDate = "";
            String endDate = "";
            endDate = DateUtils.getSystemDate();
            startDate = DateUtils.addDay(endDate, -1);
            profitDayService.getProfitReport(startDate, endDate);
        });
    }

    @Scheduled(cron = "0 50 23 * * *", zone = "Asia/Shanghai") // 指定北京时间
    public void queryCurrency() {
        executorService.submit(() -> {
            logger.info("每天23:50执行的任务:{}", "查询当天汇率");

        });

    }

    @Scheduled(cron = "0 0-59 2 * * *")
    public void executeTask() {
        executorService.submit(() -> {
            logger.info("每天2:00-3:00之间任意时间段执行的任务:{}", "同步7天的店铺销售数据");

            String startDate = "";
            String endDate = "";
            endDate = DateUtils.getSystemDate();
            startDate = DateUtils.addDay(endDate, -6);
            startDate = startDate + " 00:00:00";
            endDate = endDate + " 23:59:59";
            orderService.queryOrdersByDate(startDate, endDate);
            orderService.queryOrderDetailsByDate(startDate, endDate);


        });

    }

}
