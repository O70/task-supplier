package org.thraex.supplier.servlet.task.service.impl;

import org.thraex.supplier.servlet.task.service.QueryService;

import java.util.Arrays;
import java.util.List;

/**
 * @author 鬼王
 * @date 2019/09/12 22:30
 */
public class QueryServiceImpl implements QueryService {

    @Override
    public String getUid(String account) {
        return null;
    }

    @Override
    public List getTodoList(String uid, String begin, String end) {
        return null;
    }

    @Override
    public List<String> getDoneList(String account, List<String> todoIds) {
        return Arrays.asList();
    }

}
