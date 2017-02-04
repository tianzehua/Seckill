package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by jh on 2017/1/25.
 *
 * 首先我们需要配置spring和junit的整合，是为了junit启动时加载springIOC容器。
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//还需要告诉junit spring的配置文件。
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖，我们需要注入这个依赖，才能使用seckill测试。

    //Resource是spring提供的注入的注解。
    // 它会在spring的容器中查找SeckillDao的实现类。然后注入到我们的单元测试中来。

    @Resource
    private  SeckillDao seckillDao;


    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        //System.out.println();在IntellJ中是输入sout
        System.out.println(seckill.getName());
        System.out.println(seckill);
        /**
         * 1000元秒杀iphone6
         Seckill{seckillId=1000, name='1000元秒杀iphone6', number=100,
         startTime=Fri Jan 01 00:00:00 CST 2016, endTime=Sat Jan 02 00:00:00 CST 2016,
         createTime=Sat Jan 07 21:33:23 CST 2017}
         *
         *
         */
    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(0,100);
        for (Seckill seckill:seckills) {
            System.out.println(seckill);
        }

        /**
         *Seckill{seckillId=1000, name='1000元秒杀iphone6', number=100, startTime=Fri Jan 01 00:00:00 CST 2016, endTime=Sat Jan 02 00:00:00 CST 2016, createTime=Sat Jan 07 21:33:23 CST 2017}
         Seckill{seckillId=1001, name='800元秒杀ipad', number=200, startTime=Fri Jan 01 00:00:00 CST 2016, endTime=Sat Jan 02 00:00:00 CST 2016, createTime=Sat Jan 07 21:33:23 CST 2017}
         Seckill{seckillId=1002, name='6600元秒杀mac book pro', number=300, startTime=Fri Jan 01 00:00:00 CST 2016, endTime=Sat Jan 02 00:00:00 CST 2016, createTime=Sat Jan 07 21:33:23 CST 2017}
         Seckill{seckillId=1003, name='7000元秒杀iMac', number=400, startTime=Fri Jan 01 00:00:00 CST 2016, endTime=Sat Jan 02 00:00:00 CST 2016, createTime=Sat Jan 07 21:33:23 CST 2017}
         *
         */

    }

    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L,killTime);
        System.out.println("updateCount:"+updateCount);
        /**
         * updateCount:0
         */

    }



}