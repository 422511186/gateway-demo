package com.cmgzs.domain.base;

import com.cmgzs.constant.HttpStatus;

import java.util.HashMap;


/**
 * 返回数据给前端的封装类
 *
 * @author hzy
 */
public class ApiResult extends HashMap<String, Object> {

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";
    /**
     * 返回内容
     */
    public static final String MSG_TAG = "message";
    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";
    private static final long serialVersionUID = 1L;

    /**
     * 初始化一个新创建的 ApiResult 对象，使其表示一个空消息。
     */
    public ApiResult() {
    }

    /**
     * 初始化一个新创建的 ApiResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public ApiResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 ApiResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public ApiResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (data != null) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static ApiResult success() {
        return ApiResult.success("操作成功");
    }


    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static ApiResult success(Object data) {
        if (data == null) {
            data = new HashMap<>();
        }
        return ApiResult.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static ApiResult success(String msg) {
        return ApiResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static ApiResult success(String msg, Object data) {
        return new ApiResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static ApiResult error() {
        return ApiResult.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static ApiResult error(String msg) {
        return ApiResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static ApiResult error(String msg, Object data) {
        return new ApiResult(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static ApiResult error(int code, String msg) {
        return new ApiResult(code, msg, null);
    }

    public ApiResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
