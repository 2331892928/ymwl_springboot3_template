package cn.ymypay.team.utils.YLogin.domain;

import lombok.Data;

/**
 * @author lvjinfa
 * @date 2025/5/29 上午3:16
 * @description: 二维码数据
 */
@Data
public class QrcodeDomain {
    private Integer code;
    private String msg;
    private String url;
}
