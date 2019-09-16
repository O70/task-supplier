package org.thraex.supplier.filter;

import io.netty.channel.ChannelOption;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.thraex.supplier.constant.Labels;
import org.thraex.supplier.constant.Status;
import org.thraex.supplier.util.ResponseData;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    private MultiValueMap<String, String> params = null;
    private WebClient webClient;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Initializes the introspect filter.");

        params = new LinkedMultiValueMap<>(3);
        params.add("client_id", "cid");
        params.add("client_secret", "cse");

        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8082/introspect")
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().tcpConfiguration(client -> client
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                                .option(ChannelOption.SO_TIMEOUT, 5000))))
                .build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException { request.setCharacterEncoding(ENCODING);
        HttpServletRequest req = (HttpServletRequest) request;

        String token = Optional.ofNullable(req.getParameter(Labels.token.name()))
                .orElse(req.getHeader(Labels.token.name()));
        log.info("#doFilter Global introspect filter: [path: {}, token: {}].", req.getServletPath(), token);

        try {
            this.introspect(Optional.ofNullable(token)
                    .orElseThrow(() -> new NullPointerException(String.format("%s: %s is null.",
                            Status.ERROR_PARAMS.phrase, Labels.token))));

            chain.doFilter(request, response);
        } catch (NullPointerException | IllegalStateException e) {
            String message = e.getMessage();
            log.error(message);

            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(ENCODING);
            ResponseData.output(response).apply(ResponseData.fail(message)).run();
        }
    }

    private void introspect(String token) {
        try {
            params.set(Labels.token.name(), token);
            Map<String, Object> block = this.webClient.post()
                    .syncBody(params)
                    .retrieve()
                    .onStatus(HttpStatus::isError, cr -> Mono.error(new IllegalStateException(
                            String.format("%s %s", Status.INTROSPECT_FAILURE.phrase, cr.statusCode().toString()))))
                    .bodyToMono(Map.class)
                    .block();

            Boolean active = Optional.ofNullable(
                    Optional.ofNullable(block)
                            .map(it -> it.get(Labels.active.name()))
                            .orElseThrow(() -> new IllegalStateException(Status.INTROSPECT_FAILURE.phrase)))
                    .map(it -> (Boolean) it)
                    .orElseThrow(() -> new IllegalStateException(Status.INTROSPECT_ILLEGAL.phrase));

            if (!active) {
                throw new IllegalStateException(Status.INTROSPECT_ILLEGAL.phrase);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

}
