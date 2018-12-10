package org.seckill.exception;

/**
 * @author whitedot
 * @date 2018/11/25
 * @description 秒杀关闭异常
 **/
public class SeckillCloseException extends SeckillException{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
