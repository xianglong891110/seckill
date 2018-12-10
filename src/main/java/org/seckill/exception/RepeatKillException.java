package org.seckill.exception;

/**
 * @author whitedot
 * @date 2018/11/25
 * @description 重复秒杀异常（运行期异常）
 **/
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatKillException(String message) {

        super(message);
    }
}
