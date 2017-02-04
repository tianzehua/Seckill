package org.seckill.exception;

/** 秒杀关闭异常，当秒杀结束时用户还要进行秒杀就会出现这个异常
 * Created by jh on 2017/1/26.
 */
public class SeckillCloseException extends RuntimeException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
