package com.cmgzs.domain.auth.params;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author huangzhenyu
 * @date 2023/4/14
 */
@Data
public class RegisterParams {

    @NotBlank(message = "用户邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?![a-zA-Z]+$)(?![0-9]+$)(?![\\W_]+$)[\\w\\W]{8,16}$",
            message = "密码必须包含字母、数字、特殊字符，且长度大于等于8位，小于等于16位")
    private String passWord;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
}
