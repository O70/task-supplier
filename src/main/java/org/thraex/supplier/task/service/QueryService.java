package org.thraex.supplier.task.service;

import java.util.List;

/**
 * @author 鬼王
 * @date 2019/09/12 22:04
 */
public interface QueryService {

    /**
     * Get the user ID.
     *
     * @param account User login account
     * @return
     */
    String getUid(String account);

    /**
     * Get to-do list;
     *
     * @param uid User ID
     * @param begin Required, Format: yyyy-MM-dd HH:mm:ss.
     * @param end Optional, Format: yyyy-MM-dd HH:mm:ss.
     * @return
     */
    List getTodoList(String uid, String begin, String end);

    /**
     * Get the done list based on the to-do ID
     *
     * @param account User login account
     * @param todoIds List of to-do’s ID
     * @return
     */
    List<String> getDoneList(String account, List<String> todoIds);

}
