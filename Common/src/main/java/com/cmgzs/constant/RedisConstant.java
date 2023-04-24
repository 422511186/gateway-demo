package com.cmgzs.constant;

/**
 * Redis的key
 *
 * @author huangzhenyu
 * @date 2022/9/25
 */
public class RedisConstant {

    /**
     * 文档锁， 保证一段时间内只有当前用户可以打开此文档
     */
    public final static String archive_lock = "archiveId::lock::";

    /**
     * email 验证码
     */
    public final static String EMAIL_PREFIX = "email::verify::";

    /**
     * 切片计数
     */
    public static final String CONVERSION = "latex::conversion::count::";

    /**
     * 转换结果集
     */
    public static final String CONVERSION_FINISH_MAP = "latex::conversion::finish::map::";

    public static final String CONVERSION_IMAGES_MAP = "latex::conversion::images::map::";

    /**
     * 提取的文档顺序集
     */
    public static final String CONVERSION_ORDER = "latex::conversion::order::";

}
