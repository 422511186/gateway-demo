package com.cmgzs.domain.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huangzhenyu
 * @date 2023/4/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultBody {
    // 前缀消息
    private String prefix_data;
    // 消息体
    private String data;
    // 后缀消息
    private String post_data;
}
