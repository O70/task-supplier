package org.thraex.supplier.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.thraex.supplier.constant.Labels;
import org.thraex.supplier.util.ResponseData;
import reactor.core.publisher.Mono;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author 鬼王
 * @date 2019/09/12 22:48
 */
@Log4j2
@WebFilter("*")
public class IntrospectFilter implements Filter {

    private final static String ENCODING = "utf-8";
    private final static String CONTENT_TYPE = "application/json;charset=utf-8";

    private String IDP_URL = null;
    private Map<String, String> INTROSPECT_PARAMS = null;

    private WebClient webClient;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Initializes the introspect filter.");

        //this.IDP_URL = "http://localhost:8082";

        Map<String, String> params = new HashMap<>(2);
        params.put("client_id", "cid");
        params.put("client_secret", "cse");

        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8082/introspect")
                .defaultUriVariables(params)
                .build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException { request.setCharacterEncoding(ENCODING);
        HttpServletRequest req = (HttpServletRequest) request;

        String token = Optional.ofNullable(req.getParameter(Labels.token.name()))
                .orElse(req.getHeader(Labels.token.name()));
        log.info("#doFilter Global introspect filter: [path: {}, token: {}].", req.getServletPath(), token);

        if (!StringUtils.isEmpty(token) && this.introspect(token)) {
            chain.doFilter(request, response);
        }  else {
            request.setCharacterEncoding(ENCODING);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);

            ServletOutputStream os = response.getOutputStream();
            new ObjectMapper().writeValue(os, ResponseData.fail("Filter..."));
            os.flush();
            os.close();
        }
    }

    private boolean introspect(String token) {
        Mono<Map> mono = this.webClient.post()
                .syncBody(BodyInserters.fromFormData(Labels.token.name(), token))
                .retrieve()
                .bodyToMono(Map.class);

        Map block = mono.block();

        return "Guiwang".equals(token);
    }

}
