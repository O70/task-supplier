package org.thraex.supplier.filter;

import lombok.extern.log4j.Log4j2;
import org.thraex.supplier.task.constant.Labels;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Map;

/**
 * @author 鬼王
 * @date 2019/09/12 22:48
 */
@Log4j2
@WebFilter
public class IntrospectFilter implements Filter {

    private String IDP_URL = null;
    private Map<String, String> INTROSPECT_PARAMS = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Initializes the introspect filter.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getParameter(Labels.token.name());
        log.info("#doFilter Global introspect token: [token: {}].", token);

        chain.doFilter(request, response);
    }

}
