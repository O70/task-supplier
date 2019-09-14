package org.thraex.supplier.task.dto;

import lombok.Getter;
import org.thraex.supplier.task.constant.Types;

import java.util.List;

/**
 * @author 鬼王
 * @date 2019/09/12 18:37
 */
@Getter
public class Parameters {

    private String userCode;
    private String beginTime;
    private String endTime;
    private List<String> ids;

    /**
     * 用于{@link Types#TODO}
     */
    public Parameters(String userCode, String beginTime, String endTime) {
        this.userCode = userCode;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    /**
     * 用于{@link Types#DONE}
     */
    public Parameters(String userCode, List<String> ids) {
        this.userCode = userCode;
        this.ids = ids;
    }

}
