package org.seckill.exception;

/**
 * 秒杀相关的所有业务异常
 * Created by jh on 2017/1/26.
 */
public class SeckillException extends  RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
