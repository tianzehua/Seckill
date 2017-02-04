package org.seckill.exception;

/**
 * 重复提交异常，是一个运行期异常，不需要我们手动try catch
 * Mysql只支持运行期异常的回滚操作
 * Created by jh on 2017/1/26.
 */
public class RepeatKillException extends  RuntimeException{
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }

}
