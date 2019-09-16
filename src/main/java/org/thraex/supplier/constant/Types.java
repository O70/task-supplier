package org.thraex.supplier.constant;

import java.util.Arrays;

/**
 * @author 鬼王
 * @date 2019/09/12 18:36
 */
public enum Types {

    TODO, DONE;

    public static Types get(String name) {
        return Arrays.stream(values()).filter(it -> it.name().equalsIgnoreCase(name)).findAny().orElse(null);
    }

}
