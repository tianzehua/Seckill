package org.seckill.dao.Cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by jh on 2017/2/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//还需要告诉junit spring的配置文件。
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

    private long id = 1001;
    @Autowired
    private  RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;
    @Test
    public void testSeckill() throws Exception {
     //全局测试 get and put
        Seckill seckill = redisDao.getSeckill(id);
        //从redis中获得seckill对象，一开始肯定是空的。
        if(seckill == null){
            seckill = seckillDao.queryById(id);
            if (seckill != null){
              String result = redisDao.putSeckill(seckill);
                System.out.println(result);
               seckill = redisDao.getSeckill(id);
                System.out.println(seckill);
            }
        }
    }

}