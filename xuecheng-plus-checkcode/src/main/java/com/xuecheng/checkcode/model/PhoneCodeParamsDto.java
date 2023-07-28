package com.xuecheng.checkcode.model;

import lombok.Data;

/**
 * @author xingchen
 * @version V1.0
 * @description 找回密码响应
 * @Package com.xuecheng.checkcode.model
 * @date 2023/2/20 19:58
 */
@Data
public class PhoneCodeParamsDto {

    private String cellphone;

    private String email;

    private String checkcodekey;

    private String checkCode;

    private String password;

}
