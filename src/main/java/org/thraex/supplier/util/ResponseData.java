package org.thraex.supplier.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.function.Function;

/**
 * @author 鬼王
 * @date 2019/09/12 21:31
 */
@Data
public class ResponseData {

    private Object data;

    private boolean success;

    public ResponseData() {}

    public ResponseData(Object data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public static ResponseData success(Object data) {
        return new ResponseData(data, true);
    }

    public static ResponseData fail(Object data) {
        return new ResponseData(data, false);
    }

    public static Function<ResponseData, Runnable> output(ServletResponse response) {
        return d -> () -> {
            try (ServletOutputStream os = response.getOutputStream()) {
                new ObjectMapper().writeValue(os, d);
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

}
