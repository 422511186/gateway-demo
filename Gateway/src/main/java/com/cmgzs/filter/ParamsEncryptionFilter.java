package com.cmgzs.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmgzs.constant.RequestConstants;
import com.cmgzs.domain.MyCachedBodyOutputMessage;
import com.cmgzs.utils.AccessRequestCheck;
import com.cmgzs.utils.RSAUtils;
import io.netty.buffer.ByteBufAllocator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.cmgzs.utils.AccessRequestCheck.checkSign;


/**
 * 参数加密传输
 *
 * @author huangzhenyu
 * @date 2022/9/23
 */
@Slf4j
@Component
@RefreshScope
public class ParamsEncryptionFilter implements GlobalFilter, Ordered {

    /**
     * RSA 密钥写死
     */
    public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIlBefGEz0P24yvTsLz7e3hmITcA7hVLAf97A4xLqIkrE0XTtrHA95FgWcKenO6gI4YyyNU0zuR8wwCY0YalozBYdJjts+9ylLn6AQg2mWZICFfzvA8b/SePEl6dHwt3BD48DcGHBdCCIojSz1hqMRsAXTQTnYg2Knx+SLvDgAExAgMBAAECgYA95DiIn8qWaw2lBZ/8l6nlcKgplVHGaDxOZ7oB2Vv1/maCZiVLoigAdeID0GITeEKMkPneqiFhBqEn88EHZklfI8ATTRZknXvv7MVN/DFIsHHtNY3JfMOWDHtyf7/T1cpBytHo4o/9pvQQ+g4JFAbZt+5GWiN71Jar/eJ6o6ALwQJBAPg85l97PzbR721bJZrW6Xwzvxy2xFwsGYrNyAK/o5hLd9c8C9kvSojKbB5FsgaZp/7r1gP7Y927Hw6PSOmugzcCQQCNjC8vMb5WXjAR8FxJ/X15aIHEWkOW0MDQatoBeSel6ThrlCNGgHLs2uKCGD7VqaAzaMn1xJo6DytceMkl0aLXAkBtd02PpWXG4uTWMG9wzHzBzH/mRaJpkvjggMZGkAOwUPdT7qK672PK1pi+8LUEvBWdEJqbvuvXB4E2hnD8u3wZAkATFGxfzjK77aJJKL8n8hVxwhaL4ybtM2JqNZ0BSdWAVbmXNraykCntp2uU4bPGlUDU7TEcAc5QOS89HcLvaBytAkEA1/VVqz4ZfOKIVdgSHdBE+o+aFL8rTB+bfwpAdoXd5P1BGMz6ijxGJLEIOA6EdWpAOpye36GSjZ4n1FrLZSVSeQ==";

    /**
     * 解密过滤器必须在所有过滤器之前，否后后续过滤器获取参数会报错
     * 如果有的其他的过滤器添加请调整过滤器顺序
     */
    @Override
    public int getOrder() {
        return -1;
    }

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获取时间戳
        Long dateTimestamp = AccessRequestCheck.getDateTimestamp(exchange.getRequest().getHeaders());
        // 获取RequestId
        String requestId = AccessRequestCheck.getRequestId(exchange.getRequest().getHeaders());

        // 获取签名
        String sign = AccessRequestCheck.getSign(exchange.getRequest().getHeaders());

        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        HttpMethod method = serverHttpRequest.getMethod();

        String encryptionType = "RSA";

        URI uri = serverHttpRequest.getURI();
        if (encryptionType != null) {
            if (method == HttpMethod.POST || method == HttpMethod.PUT) {
                // 获取请求体,修改请求体
                Map<String, Object> paramMap = new HashMap<>();
                ServerRequest serverRequest
                        = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

                Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap((String body) -> {
                    String encrypt = RSAUtils.decrypt(body, PRIVATE_KEY);
                    JSONObject jsonObject = JSON.parseObject(encrypt);
                    if (jsonObject != null) {
                        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                            paramMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                    checkSign(sign, dateTimestamp, requestId, paramMap);
                    return Mono.just(encrypt);
                });

                //创建BodyInserter修改请求体
                BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
                HttpHeaders headers = new HttpHeaders();
                headers.putAll(exchange.getRequest().getHeaders());
                headers.remove(HttpHeaders.CONTENT_LENGTH);

                MyCachedBodyOutputMessage outputMessage = new MyCachedBodyOutputMessage(exchange, headers);
                outputMessage.initial(paramMap, requestId, sign, dateTimestamp);

                return bodyInserter.insert(outputMessage, new BodyInserterContext())
                        .then(Mono.defer(() -> {
                            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(serverHttpRequest) {
                                @Override
                                public Flux<DataBuffer> getBody() {
                                    Flux<DataBuffer> body = outputMessage.getBody();
                                    if (body.equals(Flux.empty())) {
                                        //验证签名
                                        checkSign(outputMessage.getSign(), outputMessage.getDateTimestamp(), outputMessage.getRequestId(), outputMessage.getParamMap());
                                    }
                                    return outputMessage.getBody();
                                }

                                @Override
                                public HttpHeaders getHeaders() {
                                    return headers;
                                }
                            };
                            return chain.filter(exchange.mutate().request(decorator).build());
                        }));

            } else if (method == HttpMethod.GET || method == HttpMethod.DELETE) {
                try {
                    MultiValueMap<String, String> requestQueryParams = serverHttpRequest.getQueryParams();
                    String params = requestQueryParams.get(RequestConstants.PARAMS).get(0);

                    Map<String, Object> paramMap = new HashMap<>();
                    String encrypt = RSAUtils.decrypt(params, PRIVATE_KEY);
                    JSONObject jsonObject = JSON.parseObject(encrypt);
                    if (jsonObject != null) {
                        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                            paramMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                    checkSign(sign, dateTimestamp, requestId, paramMap);

                    // 封装URL
                    URI plaintUrl = new URI(uri.getScheme(), uri.getAuthority(),
                            uri.getPath(), encrypt, uri.getFragment());
                    //封装request，传给下一级
                    ServerHttpRequest request = serverHttpRequest.mutate().uri(plaintUrl).build();
                    return chain.filter(exchange.mutate().request(request).build());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return chain.filter(exchange);

    }



}