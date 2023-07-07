package com.cmgzs.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * 记录进入网关的请求
 *
 * @author huangzhenyu
 * @date 2022/9/9
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "Filter.LogFilter", name = "enabled", havingValue = "true")
public class LogFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求参数
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> params = exchange.getRequest().getQueryParams();

        log.info("URI ==> {}, address ==> {}, 请求的参数为 ==> {}", request.getURI(), JSONObject.toJSON(request.getRemoteAddress()), JSONObject.toJSON(params));

        return chain.filter(exchange);
    }
}
