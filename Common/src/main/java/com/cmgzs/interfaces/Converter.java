package com.cmgzs.interfaces;

import com.cmgzs.domain.archive.Content;
import com.cmgzs.domain.mq.ConversionMessage;
import com.cmgzs.domain.mq.ResultBody;

/**
 * @author huangzhenyu
 * @date 2023/3/15
 */
public interface Converter {

    /**
     * 处理转换请求
     *
     * @param msg 消息
     */
    void convert(ConversionMessage msg);

    ResultBody convert(Content content);
}
