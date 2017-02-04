package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * Created by jh on 2017/1/24.
 */
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId
     * @param killTime 对应在数据库中就是createtime
     * @return 如果影响行数>1，表示更新库存的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀的商品信息
     * @param seckillId
     * @return
     *
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param off
     * @param limit
     * @return
     */
    //java没有保存形参的记录，当多于一个参数进行传递时，
    // Java传的是：arg0,arg1,所以多个参数无法绑定。所以mybatis使用@param绑定参数。
    List<Seckill> queryAll(@Param("offset") int off,@Param("limit") int limit);
}
