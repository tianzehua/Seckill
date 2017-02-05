package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by jh on 2017/1/28.
 * 为什么要加载两个呢，因为我们测试的要依赖与DAO。
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private  final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private  SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);

    }

    @Test
    public void getById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long seckillId=1000;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        logger.info("exposer={}",exposer);
    }
    //exposer=Exposer{exposed=false, md5='null', seckillId=1000, now=1486083905220, start=1451577600000, end=1451664000000}
    //没有给我们返回id为1000的商品秒杀地址，
    // 是因为我们当前的时间并不在秒杀时间开启之内，
    // 所以该商品还没有开启。
    // 需要修改数据库中该商品秒杀活动的时间在我们测试时的当前时间之内，
    // 然后再进行该方法的测试.
    @Test
    public void executeSeckill() throws Exception {
        long seckillId=1000;
        long userPhone=13476191876L;
        String md5="bf204e2683e7452aa7db1a50b5713bae";

        SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId,userPhone,md5);
        logger.info("result={}",seckillExecution);

    }
    @Test
    public void executeSeckillProcedure() throws Exception {
        long seckillId = 1001;
        long phone = 13610001000L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId,phone,md5);
            logger.info(execution.getStateInfo());
        }


    }

}