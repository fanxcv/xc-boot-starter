package com.fan.xc.boot.starter.interfaces;

/**
 * @author fan
 */
public interface ParamCheck {
    /**
     * 参数校验方法
     * @param value 待校验的参数值
     * @param args  校验类入参
     * @return 是否通过校验
     */
    boolean check(Object value, String... args);

    /**
     * 校验出错的返回值
     * @param name  参数名
     * @param value 参数值
     * @param args  校验类入参
     * @return 错误提示内容
     */
    default String message(String name, Object value, String... args) {
        return "参数$" + name + "错误";
    }

    /**
     * 参数映射方法,不建议这里使用,容易使数据混乱
     * @param value 入参值
     * @return 出参值
     */
    default Object map(Object value) {
        return value;
    }
}
