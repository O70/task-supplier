package org.thraex.supplier.task.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.thraex.supplier.task.constant.Labels;
import org.thraex.supplier.task.constant.Status;
import org.thraex.supplier.util.ResponseData;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author 鬼王
 * @date 2019/09/12 18:42
 */
@Log4j2
@WebServlet({ "/taskListService", "/task" })
public class TaskAction extends HttpServlet {

    private final static String ENCODING = "utf-8";
    private final static String CONTENT_TYPE = "application/json;charset=utf-8";

    private String idpUrl = null;
    private Map<String, String> introspectParams = null;

    private Function<Labels, Supplier> nullFunction;
    private Function<String, String> codeFunction;

    @Override
    public void init() throws ServletException {
        log.info("Initializes the member variable.");

        this.nullFunction = n -> () -> new NullPointerException(
                String.format("%s: %s is null.", Status.ERROR_PARAMS.phrase, n));
        this.codeFunction = it -> {
            final String delimiter = "\\\\";
            final int index = it.lastIndexOf(delimiter);
            return index > -1 ? it.substring(index + delimiter.length()) : it;
        };
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        request.setCharacterEncoding(ENCODING);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(ENCODING);

        try {
            start(request, response).run();
        } catch (NullPointerException | IllegalArgumentException | IllegalStateException e) {
            String message = e.getMessage();
            log.error(message);
            output(response).apply(ResponseData.fail(message)).run();
        }

        log.info("#doPost execution time: {}.", (System.currentTimeMillis() - start) / 1000F);
    }

    private Runnable start(HttpServletRequest request, HttpServletResponse response) {
        return () -> System.out.println("Starting...");
    }

    private Function<ResponseData, Runnable> output(HttpServletResponse resp) {
        return data -> () -> {
            try (ServletOutputStream os = resp.getOutputStream()) {
                new ObjectMapper().writeValue(os, data);
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

}
