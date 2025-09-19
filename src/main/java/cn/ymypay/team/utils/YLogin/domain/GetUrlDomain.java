package cn.ymypay.team.utils.YLogin.domain;

import lombok.Data;

/**
 * @author lvjinfa
 * @date 2025/5/26 上午10:02
 * @description: 获取登录跳转地址的vo
 */
@Data
public class GetUrlDomain {
    private Integer code;
    private String msg;
    private String type;
    private String url;
    private String qrcode;
}
