package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jh on 2017/1/26.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    //注入Service依赖。
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    //加入混淆的盐值。随便敲，越复杂越好。用于混淆MD5.
    private  final  String slat = "sfdhkajsklvnilvjUOIhi32##**&^%$%$%&*$##@iwqf9382r89042HKAHKiljlkj";



    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }
    //当秒杀开始后暴露接口，否则返回系统时间。
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if(seckill == null){
            return  new Exposer(false,seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统当前时间
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            //当当前时间没有到秒杀时间或者秒杀已经结束的时候，显示秒杀失败
            return  new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //转化特定字符串的编码过程，最大的特点是不可逆。
        String md5 = getMD5(seckillId);
        //表示当当前时间在秒杀时间段的时候，显示秒杀成功。同时使用MD5加密。输出秒杀接口地址。
        return new Exposer(true,md5,seckillId);
    }


    private  String getMD5(long seckillId){

        //如果只对id做MD5算法时，用户可以通过固定的算法跑出相应的值得，所以加入盐值。
        String base = seckillId + "/" + slat;
        //生成MD5的方法就是调取了一个工具类中的方法来实现。
        String md5  = DigestUtils.md5DigestAsHex(base.getBytes());
        return  md5;
    }
    //执行秒杀的操作。
    @Transactional
    //表示需要用到事务。
    /**
     * 使用注解控制事务方法的优点:
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作、只读操作不要事务控制
     *
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        //表示传入的值和生成的值不相同，认为参数传递的没有问题。如果匹配不上的话，就会抛出一个系统异常。
        if (md5 == null || !md5.equals(getMD5(seckillId))){
            throw  new SeckillException("seckill data rewrite(秒杀地址或者数据被重写了)");
        }
        //执行秒杀逻辑。减库存，加记录购买行为。
        Date nowTime = new Date();
        try {
            int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
            if (updateCount <= 0){
                //没有更新到记录。表示的就是秒杀结束了。我们不关心结束的原因。
                //是因为没有库存了，还是因为时间结束了。
                throw  new SeckillCloseException("seckill is closed");
            }else{
                //记录购买行为。
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if (insertCount <= 0){
                    //意味着重复秒杀了。
                    throw  new RepeatKillException("seckill repeated(重复秒杀了)");
                }else{
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return  new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            //所有的编译期异常转化为运行期异常。
            throw  new SeckillException("seckill inner error:"+e.getMessage());
        }
    }
}
