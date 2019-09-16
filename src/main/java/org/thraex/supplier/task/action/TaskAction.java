package org.thraex.supplier.task.action;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.thraex.supplier.constant.Labels;
import org.thraex.supplier.constant.Status;
import org.thraex.supplier.constant.Types;
import org.thraex.supplier.task.dto.Parameters;
import org.thraex.supplier.task.service.QueryService;
import org.thraex.supplier.task.service.impl.QueryServiceImpl;
import org.thraex.supplier.util.ResponseData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author 鬼王
 * @date 2019/09/12 18:42
 */
@Log4j2
@WebServlet({ "/task", "/taskListService" })
public class TaskAction extends HttpServlet {

    private final static String ENCODING = "utf-8";
    private final static String CONTENT_TYPE = "application/json;charset=utf-8";

    private QueryService queryService;

    private Function<Labels, Supplier> nullFunction;
    private Function<String, String> codeFunction;

    @Override
    public void init() throws ServletException {
        log.info("Initializes the member variable.");
        queryService = new QueryServiceImpl();

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
            start(request, response);
        } catch (NullPointerException | IllegalArgumentException | IllegalStateException e) {
            String message = e.getMessage();
            log.error(message);
            ResponseData.output(response).apply(ResponseData.fail(message)).run();
        }

        log.info("#doPost execution time: {}.", (System.currentTimeMillis() - start) / 1000F);
    }

    private Function<Labels, String> getParameter(HttpServletRequest request) {
        return l -> Optional.ofNullable(request.getParameter(l.name()))
                .orElseThrow(nullFunction.apply(l));
    }

    private Runnable start(HttpServletRequest request, HttpServletResponse response) {
        String typeStr = getParameter(request).apply(Labels.type);
        log.info("#doPost Global parameters: [type: {}].", typeStr);

        boolean isTodo = Types.TODO.equals(Optional.ofNullable(Types.get(typeStr))
                .orElseThrow(() -> new IllegalArgumentException(Status.ERROR_TYPE.phrase)));

        Function<Parameters, Parameters> starter = Function.identity();

        return starter
                .andThen(query(isTodo))
                .andThen(ResponseData.output(response))
                .apply(parameters(request, isTodo).get());
    }

    private Supplier<Parameters> parameters(HttpServletRequest request, boolean isTodo) {
        return isTodo ? () -> {
            String loginId = getParameter(request).apply(Labels.loginId),
                    beginTime = getParameter(request).apply(Labels.beginTime),
                    endTime = request.getParameter(Labels.endTime.name());
            log.info("{} parameters: [loginId: {}, beginTime: {}, endTime: {}]",
                    Types.TODO, loginId, beginTime, endTime);

            return new Parameters(codeFunction.apply(loginId), beginTime, endTime);
        } : () -> {
            Map<String, Object> map;
            try (InputStreamReader reader = new InputStreamReader(request.getInputStream())) {
                map = new Gson().fromJson(reader, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IllegalArgumentException(
                        String.format("Failure %s parameters got via InputStreamReader", Types.DONE));
            }
            return Optional.ofNullable(map).map(it -> {
                String loginId = Optional.ofNullable(it.get(Labels.loginId.name()))
                        .map(l -> l.toString()).orElseThrow(nullFunction.apply(Labels.loginId));
                List<String> ids = Optional.ofNullable(it.get(Labels.ids.name()))
                        .map(l -> (List<String>) l).orElseThrow(nullFunction.apply(Labels.ids));
                log.info("{} parameters: [loginId: {}, ids: {}]", Types.DONE, loginId, ids);

                return new Parameters(codeFunction.apply(loginId), ids);
            }).orElseGet(() -> {
                // InputStreamReader获取无果，通过request获取
                String loginId = getParameter(request).apply(Labels.loginId),
                        ids = getParameter(request).apply(Labels.ids);
                log.info("{} parameters: [loginId: {}, ids: {}]", Types.DONE, loginId, ids);

                return new Parameters(codeFunction.apply(loginId), Arrays.asList(ids.split(",")));
            });
        };
    }

    private Function<Parameters, ResponseData> query(boolean isTodo) {
        return p -> ResponseData.success(isTodo ?
                Optional.ofNullable(this.queryService.getUid(p.getUserCode()))
                        .map(it -> this.queryService.getTodoList(it, p.getBeginTime(), p.getEndTime()))
                        .orElseThrow(() -> new NullPointerException(Status.ERROR_NOT_USER.phrase)) :
                Optional.ofNullable(this.queryService.getDoneList(p.getUserCode(), p.getIds()))
                        .get());
    }

}
