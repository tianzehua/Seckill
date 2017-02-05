package org.seckill.dao.Cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by jh on 2017/2/4.
 */
public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //JedisPool有些类似数据连接时的连接池。
    private JedisPool jedisPool;

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public  RedisDao(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }

    //通过缓存拿到当前的需要的值
    public Seckill getSeckill(long seckillId){
        //redis操作的逻辑，不应该放在service层中，因为他是属于数据访问的逻辑而不是业务逻辑。

        try {
            //jedis相当于数据库连接。
            Jedis jedis = jedisPool.getResource();

            try {
               String key = "Seckill:"+seckillId;
               //我们是一个对象，既然是一个对象，那不管是redis还是jedis也好，并没有实现内部序列化操作.
                // 首先我们get一个二进制的数组----->byte[] ——————>需要反序列化拿到我们的对象---->Object(Seckill)
                //所以我们采用自定义的序列化方式。
                // 通过这个第三方的工具protostuff将我们的对象转换成二进制的数组保存在redis中。
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes != null){
                    //空对象
                    Seckill seckill = schema.newMessage();
                    ProtobufIOUtil.mergeFrom(bytes,seckill,schema);
                    //Seckill被反序列化。
                    return  seckill;
                }

            } finally {
                //关闭要写在finally里面
                jedis.close();
            }
            ;
            return  null;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return  null;
    }

    //当发现缓存中没有这个seckill时，去数据库中put一个值
    public String putSeckill(Seckill seckill){
        //set 就是put
        //set Object(Seckill) --->序列化————————>byte[]


        try {
            Jedis jedis = jedisPool.getResource();

            try {
                String key = "seckill:"+seckill.getSeckillId();
                //调用ProtobufIOUtil的toByteArray方法，传入对象和类的模式，pojo告诉对象的模式。然后就是一个缓存器。
                byte[] bytes = ProtobufIOUtil.toByteArray(seckill,schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout = 60 * 60;
                //result 正确返回OK
                String result = jedis.setex(key.getBytes(),timeout,bytes);
                return  result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return  null;
    }
}
