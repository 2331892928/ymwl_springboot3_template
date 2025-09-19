package cn.ymypay.team.utils.YPay;

import com.alibaba.fastjson2.JSONObject;
import cn.ymypay.team.exception.YmwlException;
import cn.ymypay.team.utils.DateUtils;
import cn.ymypay.team.utils.YPay.domain.V1mapiDomain;
import cn.ymypay.team.utils.YmwlUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lvjinfa
 * @date 2025/6/1 下午7:49
 * @description: 易支付类
 */
@Service
public class YPay {
    @Value("${y.pay.url}")
    private String url;
    @Value("${y.pay.PID}")
    private String PID;
    @Value("${y.pay.V1KEY}")
    private String V1KEY;
    private final HttpClient httpClient;
    private WebClient webClient;

    public YPay() {
        httpClient = HttpClient.create().proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
                .host("localhost")
                .port(9090));
    }
    private void setBaseUrl() {
        webClient = WebClient.create(url);
//        webClient = WebClient.builder().baseUrl(url).clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }
    public V1mapiDomain getQrcode(String outTradeNo, String type, String notifyUrl, String name, String money, String clientip) {
        setBaseUrl();
        //参数存入 map
        LinkedMultiValueMap<String,String> sign2 = new LinkedMultiValueMap<>();
        sign2.add("pid",PID);
        sign2.add("type",type);
        sign2.add("out_trade_no",outTradeNo);
        sign2.add("notify_url",notifyUrl);
        sign2.add("name",name);
        sign2.add("money",money);
        sign2.add("clientip",clientip);
        Map<String,String> sign = YmwlUtils.ConvertMap(sign2);
        StringBuilder signStr = getSign(sign);
        sign2.add("sign_type","MD5");
        sign2.add("sign", String.valueOf(signStr));
//        System.out.println(String.valueOf(signStr));
        String v1mapiDomainString = webClient.post().uri("mapi.php")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(sign2)
                .retrieve().bodyToMono(String.class).block();
        V1mapiDomain v1mapiDomain = JSONObject.parseObject(v1mapiDomainString, V1mapiDomain.class);
        if (v1mapiDomain == null) {
            throw new YmwlException("获取支付二维码失败");
        }
        if (v1mapiDomain.getCode() == 1) {
            return v1mapiDomain;
        }
        throw new YmwlException(v1mapiDomain.getMsg());
    }
    public StringBuilder getSign(Map<String, String> sign) {
        sign = sortByKey(sign);

        StringBuilder signStr = new StringBuilder();
        //遍历map 转成字符串
        for(Map.Entry<String,String> m :sign.entrySet()){
            signStr.append(m.getKey()).append("=").append(m.getValue()).append("&");
        }
        //去掉最后一个 &
        signStr = new StringBuilder(signStr.substring(0, signStr.length() - 1));
        //最后拼接上KEY
        signStr.append(V1KEY);

        //转为MD5
        signStr = new StringBuilder(DigestUtils.md5DigestAsHex(signStr.toString().getBytes()));
        return signStr;
    }

    /**
     * 查询订单是否已被支付
     * @param outTradeNo
     * @return
     */
    public boolean isPay(String outTradeNo) {
        setBaseUrl();
        String getPayOrderString = webClient.get().uri(uriBuilder ->
                uriBuilder.path("api.php")
                        .queryParam("act", "order")
                        .queryParam("pid", PID)
                        .queryParam("key", V1KEY)
                        .queryParam("out_trade_no", outTradeNo)
                        .build()
        ).retrieve().bodyToMono(String.class).block();
        JSONObject getPayOrder = JSONObject.parseObject(getPayOrderString);
        if (getPayOrder == null) {
            return false;
        }
        Integer code = getPayOrder.getInteger("code");
        if (code != 1) {
            return false;
        }
        String status = getPayOrder.getString("status");
        return !Objects.equals(status, "0");
    }
    public boolean checkSign(Map<String, String> sign, String signStr) {
        StringBuilder signStr2 = getSign(sign);
        return Objects.equals(signStr, signStr2.toString());
    }
    /**
     * 生成唯一的微信支付订单号
     *
     * @return 返回生成的订单号
     */
    public String generateOutTradeNo() {
        // 1. 获取当前时间戳
        long timestamp = DateUtils.nowMillis().getTime();

        // 2. 将时间戳转换为字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestampStr = sdf.format(new Date(timestamp));

        // 3. 将字符串进行格式化处理，添加随机数和校验位
        Random random = new Random();
        int randomNum = random.nextInt(900) + 100; // 生成一个三位随机数

        String outTradeNo = timestampStr + randomNum;

        // 计算校验位
        int checkCode = generateCheckCode(outTradeNo);
        outTradeNo += checkCode;

        // 4. 返回生成的订单号
        return outTradeNo;
    }
    /**
     * 生成订单号的校验位
     *
     * @param outTradeNo 待生成校验位的订单号
     * @return 返回校验位
     */
    private static int generateCheckCode(String outTradeNo) {
        char[] chars = outTradeNo.toCharArray();
        int sum = 0;
        for (char c : chars) {
            sum += Integer.parseInt(String.valueOf(c));
        }

        return sum % 10;
    }
    public static <K extends Comparable<? super K>, V > Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();

        map.entrySet().stream()
                .sorted(Map.Entry.<K, V>comparingByKey()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

}
