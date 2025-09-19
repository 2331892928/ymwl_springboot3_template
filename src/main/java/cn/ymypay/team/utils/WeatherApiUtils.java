package cn.ymypay.team.utils;

import com.alibaba.fastjson2.JSONObject;
import cn.ymypay.team.exception.WebClientException;
import cn.ymypay.team.domain.WeatherApiNow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.time.Duration;

/**
 * @author amen
 */
@Service
public class WeatherApiUtils {
    @Value("${weatherapi.key}")
    private String key;
    HttpClient httpClient;
    WebClient webClient;
    public WeatherApiUtils() {
        httpClient = HttpClient.create().proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
                .host("192.168.3.17")
                .port(9999));
        webClient = WebClient.create("http://api.weatherapi.com/v1");
//        webClient = WebClient.builder().baseUrl("http://api.weatherapi.com/v1").clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }


    public WeatherApiNow getWeatherBylatlng(Double lat,Double lng) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("key", key);
        formData.add("q", String.format("%s,%s", lat,lng));
        formData.add("lang", "zh");

        String block = webClient.post().uri("/current.json")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve().bodyToMono(String.class).block(Duration.ofSeconds(3));
        try {
            WeatherApiNow dataDeocde = JSONObject.parseObject(block, WeatherApiNow.class);
            return dataDeocde;
        }catch (Exception e) {
            throw new WebClientException("天气获取失败 请手动填写 或重新进入");
        }
    }
}