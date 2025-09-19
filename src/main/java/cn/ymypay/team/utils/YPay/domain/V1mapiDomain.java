package cn.ymypay.team.utils.YPay.domain;

import lombok.Data;

/**
 * @author lvjinfa
 * @date 2025/6/2 下午6:11
 * @description: v1 api支付接口domain
 */
@Data
public class V1mapiDomain {
    private Integer code;
    private String msg;
    private String trade_no;
    private String payurl;
    private String qrcode;
    private String urlscheme;
}
