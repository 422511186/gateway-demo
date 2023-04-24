package com.cmgzs.domain.mq;

import com.cmgzs.domain.archive.Content;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * 转换请求消息体
 *
 * @author huangzhenyu
 * @date 2023/3/13
 */
@Data
public class ConversionMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    //消息Id
    private String messageId;

    //编译记录Id
    private String recordId;

    //消息体
    private Content requestBody;

    //转换结果
    private ResultBody resultBody;

    /**
     * 图片别名映射
     */
    private Map<String, Object> imagesMapper;

    //是否完成
    private boolean finish;

}
