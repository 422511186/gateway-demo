package com.cmgzs.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.util.Md5Utils;
import com.cmgzs.constant.RequestConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author huangzhenyu
 * @date 2022/9/22
 */
@Slf4j
@Component
public class AccessRequestCheck {

    public static final String ERROR_MESSAGE = "拒绝服务";
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * @param sign          签名
     * @param dateTimestamp 请求时间
     * @param requestId     请求id
     * @param paramMap      请求参数
     */
    public void checkSign(String sign, Long dateTimestamp, String requestId, Map<String, Object> paramMap) {
        String str = JSON.toJSONString(paramMap) + requestId + dateTimestamp;
        String tempSign = Md5Utils.getMD5(str.getBytes());
        if (!tempSign.equals(sign)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }

    /**
     * @param headers 请求头
     * @return
     */
    public String getSign(HttpHeaders headers) {
        List<String> list = headers.get(RequestConstants.SIGN);
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        return list.get(0);
    }

    /**
     * @param headers 请求头
     * @return
     */
    public Long getDateTimestamp(HttpHeaders headers) {
        List<String> list = headers.get(RequestConstants.TIMESTAMP);
        if (CollectionUtils.isEmpty(list)) {
            log.info("非法请求");
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        long timestamp = Long.parseLong(list.get(0));
        long currentTimeMillis = System.currentTimeMillis();
        //有效时长为3分钟
        if (currentTimeMillis - timestamp > 1000 * 60 * 3) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        return timestamp;
    }

    /**
     * @param headers 请求头
     * @return
     */
    public String getRequestId(HttpHeaders headers) {
        List<String> list = headers.get(RequestConstants.REQUESTID);
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        String requestId = list.get(0);
        //如果requestId存在redis中直接返回
        String temp = redisTemplate.opsForValue().get(requestId);
        if (temp != null) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        redisTemplate.opsForValue().set(requestId, requestId, 5, TimeUnit.MINUTES);
        return requestId;
    }

}
