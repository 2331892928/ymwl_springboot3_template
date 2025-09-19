package cn.ymypay.team.vo;

import lombok.Data;

/**
 * @author lvjinfa
 * @date 2025/6/2 下午9:38
 * @description: 二维码支付视图对象
 */
@Data
public class QrcodePayInfo {
    private String qrCode;
    private String outTradeNo;
}
