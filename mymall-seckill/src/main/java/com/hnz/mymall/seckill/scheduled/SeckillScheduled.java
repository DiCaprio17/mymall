package com.hnz.mymall.seckill.scheduled;

import com.hnz.mymall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 *
 * @createTime: 2020-07-09 19:22
 **/

/**
 * 定时任务
 * 秒杀商品定时上架
 *  每天晚上3点，上架最近三天需要三天秒杀的商品
 *  当天00:00:00 - 23:59:59
 *  明天00:00:00 - 23:59:59
 *  后天00:00:00 - 23:59:59
 */

@Slf4j
@Service
public class SeckillScheduled {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedissonClient redissonClient;

    //秒杀商品上架功能的锁
    private final String upload_lock = "seckill:upload:lock";

    //TODO 保证幂等性问题
    // @Scheduled(cron = "*/5 * * * * ? ")
    @Scheduled(cron = "0 0 1/1 * * ? ")
    public void uploadSeckillSkuLatest3Days() {
        //1、重复上架无需处理
        log.info("上架秒杀的商品...");

        //分布式锁 所的业务已完成，状态已更新，释放锁后其他人就可以获取到最新的状态
        RLock lock = redissonClient.getLock(upload_lock);
        try {
            //加锁，10秒后解锁
            lock.lock(10, TimeUnit.SECONDS);
            seckillService.uploadSeckillSkuLatest3Days();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
