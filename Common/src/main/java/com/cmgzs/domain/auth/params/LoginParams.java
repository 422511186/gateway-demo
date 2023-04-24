package com.cmgzs.domain.auth.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户登录params
 *
 * @author huangzhenyu
 * @date 2022/10/5
 */
@Data
public class LoginParams {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")

    private String userName;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?![a-zA-Z]+$)(?![0-9]+$)(?![\\W_]+$)[\\w\\W]{8,16}$", message = "密码必须包含字母、数字、特殊字符，且长度大于等于8位，小于等于16位")
    private String passWord;

    public interface Insert {

    }

    public interface Update {

    }
}
