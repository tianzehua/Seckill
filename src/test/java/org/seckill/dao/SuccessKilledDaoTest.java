package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by jh on 2017/1/25.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Resource
    private  SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        Long id =1000L;
        Long phone =12345678912L;
        int insertCount = successKilledDao.insertSuccessKilled(id,phone);
        System.out.println("insertCount："+insertCount);


    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        Long id =1000L;
        Long phone =12345678912L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id,phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
        /**
         * SuccessKilled{seckillId=1000, userPhone=12345678912, state=0, createTime=Wed Jan 25 17:02:58 CST 2017}
         Seckill{seckillId=1000, name='1000元秒杀iphone6', number=0, startTime=Fri Jan 01 00:00:00 CST 2016, endTime=Sat Jan 02 00:00:00 CST 2016, createTime=Sat Jan 07 21:33:23 CST 2017}
         */

    }

}