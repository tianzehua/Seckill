package org.seckill.dto;

/**
 * Created by jh on 2017/2/3.
 */
//将所有的ajax请求返回类型，全部封装成json数据
public class SeckillResult<T> {

    private boolean success;//判断是否成功
    private T data;        //泛型数据
    private String error;   //错误信息

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
