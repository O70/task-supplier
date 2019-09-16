package org.thraex.supplier.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.thraex.supplier.constant.Keys;
import org.thraex.supplier.constant.Labels;
import org.thraex.supplier.util.ResponseData;

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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Initializes the introspect filter.");
        this.IDP_URL = "http://localhost:8082";
        this.INTROSPECT_PARAMS = new HashMap<>(3);
        this.INTROSPECT_PARAMS.put(Keys.client_id.name(), "cid");
        this.INTROSPECT_PARAMS.put(Keys.client_secret.name(), "cse");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException { request.setCharacterEncoding(ENCODING);
        HttpServletRequest req = (HttpServletRequest) request;

        String token = Optional.ofNullable(req.getParameter(Labels.token.name()))
                .orElse(req.getHeader(Labels.token.name()));
        log.info("#doFilter Global introspect filter: [path: {}, token: {}].", req.getServletPath(), token);

        if (this.introspect(token)) {
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
        return "Guiwang".equals(token);
    }

}
