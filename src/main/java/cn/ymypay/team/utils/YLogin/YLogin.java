package cn.ymypay.team.utils.YLogin;

import cn.ymypay.team.exception.YmwlException;
import cn.ymypay.team.utils.YLogin.domain.GetInfoDomain;
import cn.ymypay.team.utils.YLogin.domain.GetUrlDomain;
import cn.ymypay.team.utils.YLogin.domain.QrcodeDomain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;


/**
 * @author lvjinfa
 * @date 2025/5/26 上午9:46
 * @description: 易聚合登录
 */
@Service
public class YLogin {
    @Value("${y.login.url}")
    private String url;
    @Value("${y.login.APPID}")
    private String APPID;
    @Value("${y.login.APPKEY}")
    private String APPKEY;
    private final HttpClient httpClient;
    private WebClient webClient;
    public YLogin() {
//        webClient = WebClient.create(url);
        httpClient = HttpClient.create().proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
                .host("localhost")
                .port(9090));
    }
    private void setBaseUrl() {
        webClient = WebClient.create(url);
//        webClient = WebClient.builder().baseUrl(url).clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }
    public String getUrl(String type, String redirectUri, Boolean qrcode) {
        setBaseUrl();
        GetUrlDomain getUrlDomain = webClient.get().uri(uriBuilder ->
                uriBuilder.path("connect.php")
                        .queryParam("act", "login")
                        .queryParam("appid", APPID)
                        .queryParam("appkey", APPKEY)
                        .queryParam("type", type)
                        .queryParam("redirect_uri", redirectUri)
                        .build()
        ).retrieve().bodyToMono(GetUrlDomain.class).block();
        if (getUrlDomain == null) {
            throw new YmwlException("获取登录地址/二维码失败");
        }
        if (getUrlDomain.getCode() != 0) {
            throw new YmwlException(getUrlDomain.getMsg());
        }
        if (qrcode) {
            return getUrlDomain.getQrcode();
        }
        return getUrlDomain.getUrl();
    }
    public String getUrl(String type, String redirectUri) {
        return getUrl(type, redirectUri, false);
    }
    public String getQrcode(String type, String redirectUri) {
        return getUrl(type, redirectUri, true);
    }

    public GetInfoDomain getCallbackInfo(String type, String code) {
        setBaseUrl();
        GetInfoDomain getInfoDomain = webClient.get().uri(uriBuilder ->
                uriBuilder.path("connect.php")
                        .queryParam("act", "callback")
                        .queryParam("appid", APPID)
                        .queryParam("appkey", APPKEY)
                        .queryParam("type", type)
                        .queryParam("code", code)
                        .build()
        ).retrieve().bodyToMono(GetInfoDomain.class).block();
        if (getInfoDomain == null) {
            throw new YmwlException("获取信息失败");
        }
        if (getInfoDomain.getCode() != 0) {
            throw new YmwlException(getInfoDomain.getMsg());
        }
        return getInfoDomain;
    }

    public GetInfoDomain getInfo(String type, String socialUid) {
        setBaseUrl();
        GetInfoDomain getInfoDomain = webClient.get().uri(uriBuilder ->
                uriBuilder.path("connect.php")
                        .queryParam("act", "query")
                        .queryParam("appid", APPID)
                        .queryParam("appkey", APPKEY)
                        .queryParam("type", type)
                        .queryParam("socialUid", socialUid)
                        .build()
        ).retrieve().bodyToMono(GetInfoDomain.class).block();
        if (getInfoDomain == null) {
            throw new YmwlException("获取信息失败");
        }
        if (getInfoDomain.getCode() != 0) {
            throw new YmwlException(getInfoDomain.getMsg());
        }
        return getInfoDomain;
    }
    public QrcodeDomain wxCheckLogin(String state) {
        setBaseUrl();
        QrcodeDomain qrcodeDomain = webClient.get().uri(uriBuilder ->
                uriBuilder.path("ajax.php")
                        .queryParam("act", "login")
                        .queryParam("state", state)
                        .build()
        ).header("referer",url + "ajax.php?state"+state).retrieve().bodyToMono(QrcodeDomain.class).block();
        return qrcodeDomain;
    }
}
