package org.thraex.supplier.common.constant;

/**
 * @author 鬼王
 * @date 2019/09/12 18:35
 */
public enum Status {

    ERROR_TYPE("无匹配接口类型"),
    ERROR_PARAMS("参数错误"),
    ERROR_NOT_USER("用户未找到"),
    INTROSPECT_FAILURE("请求合法性验证失败"),
    INTROSPECT_ILLEGAL("请求合法性验证未通过");

    public final String phrase;

    Status(String phrase) {
        this.phrase = phrase;
    }

}
