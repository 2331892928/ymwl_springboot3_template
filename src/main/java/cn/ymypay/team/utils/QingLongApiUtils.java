package cn.ymypay.team.utils;

import cn.ymypay.team.dto.utils.QlCreateCronDTO;
import cn.ymypay.team.dto.utils.QlDetailsDTO;
import cn.ymypay.team.dto.utils.QlUpdateCronDTO;
import cn.ymypay.team.exception.WebClientException;
import cn.ymypay.team.exception.YmwlException;
import cn.ymypay.team.vo.utils.QlCronsDetailVO;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author amen
 */
@Service
public class QingLongApiUtils {
    @Value("${qinglong.host}")
    private String host;
    @Value("${qinglong.client_id}")
    private String client_id;
    @Value("${qinglong.client_secret}")
    private String client_secret;
    HttpClient httpClient;
    WebClient webClient;
    public QingLongApiUtils() {
        httpClient = HttpClient.create().proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
                .host("localhost")
                .port(9999));
    }
    private void setBaseUrl() {
        webClient = WebClient.create(host);
//        webClient = WebClient.builder().baseUrl(host).clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }
    public String getToken() {
        setBaseUrl();
        String block = webClient.get().uri(uriBuilder -> uriBuilder.path("/open/auth/token")
                .queryParam("client_id", client_id)
                .queryParam("client_secret", client_secret)
                .build()
        ).retrieve().bodyToMono(String.class).block();
        JSONObject jsonObject = JSONObject.parseObject(block);
        if (jsonObject == null) {
            return null;
        }
        if (jsonObject.getInteger("code") != 200) {
            return null;
        }
        String token = jsonObject.getJSONObject("data").getString("token");
        String value = jsonObject.getJSONObject("data").getString("token_type");
        webClient = webClient.mutate()
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader(HttpHeaders.AUTHORIZATION, value + " " + token)
//                .filter((request, next) -> {
//                    // 获取完整请求 URL（包含协议、域名、端口、路径、参数）
//                    String fullUrl = request.url().toString();
//                    System.out.println("【请求URL】：" + fullUrl);
//                    System.out.println("【请求方法】：" + request.method());
//                    System.out.println("【请求参数】：" + request.url().getQuery()); // 仅打印 query 参数
//                    return next.exchange(request);
//                })
                .build();
        return token;
    }

    public Long create(String command, String schedule, String name, String labels) {
        setBaseUrl();
        getToken();
        QlCreateCronDTO qlCreateCronDTO = new QlCreateCronDTO();
        qlCreateCronDTO.setCommand(command);
        qlCreateCronDTO.setSchedule(schedule);
        qlCreateCronDTO.setName(name);
        ArrayList<String> strings = new ArrayList<>();
        strings.add(labels);
        qlCreateCronDTO.setLabels(strings);
        String block = webClient.post().uri("/open/crons").bodyValue(qlCreateCronDTO)
                .retrieve().bodyToMono(String.class).block();
        JSONObject jsonObject = JSONObject.parseObject(block);
        if (jsonObject == null) {
            return null;
        }
        if (jsonObject.getInteger("code") != 200) {
            return null;
        }
        return jsonObject.getJSONObject("data").getLong("id");
    }
    public Long edit(String command, String schedule, String name, String labels, Long id) {
        setBaseUrl();
        getToken();
        QlUpdateCronDTO qlUpdateCronDTO = new QlUpdateCronDTO();
        qlUpdateCronDTO.setCommand(command);
        qlUpdateCronDTO.setSchedule(schedule);
        qlUpdateCronDTO.setName(name);
        qlUpdateCronDTO.setId(id);
        ArrayList<String> strings = new ArrayList<>();
        strings.add(labels);
        qlUpdateCronDTO.setLabels(strings);
        String block = webClient.put().uri("/open/crons").bodyValue(qlUpdateCronDTO)
                .retrieve()
//
//                .onStatus(
//                        status -> status.isError(),
//                        clientResponse -> clientResponse.bodyToMono(String.class)
//                                .defaultIfEmpty("No body")
//                                .map(body -> new RuntimeException(
//                                        String.format("Server Error: %s, Body: %s",
//                                                clientResponse.statusCode(), body))
//                                )
//                )
                .bodyToMono(String.class)
                .block();
        JSONObject jsonObject = JSONObject.parseObject(block);
        if (jsonObject == null) {
            return null;
        }
        if (jsonObject.getInteger("code") != 200) {
            return null;
        }
        return jsonObject.getJSONObject("data").getLong("id");
    }
    public QlCronsDetailVO getDetail(Long id) {
        setBaseUrl();
        getToken();
        if (id == null) {
            return null;
        }
        String block = webClient.get().uri("/open/crons/" + id.toString()).retrieve().bodyToMono(String.class).block();
        try {
            return JSONObject.parseObject(block, QlCronsDetailVO.class);
        }catch (Exception e) {
            throw new WebClientException("获取定时任务详情失败");
        }
    }
    public List<QlCronsDetailVO.TaskData> getDetails(List<Long> ids) {
        setBaseUrl();
        getToken();
        Long[] idsArray = ids.toArray(new Long[0]);
        QlDetailsDTO.Filters filters = new QlDetailsDTO.Filters();
        filters.setOperation("In");
        filters.setProperty("id");
        filters.setValue(idsArray);
        ArrayList<QlDetailsDTO.Filters> filtersArrayList = new ArrayList<>();
        filtersArrayList.add(filters);
        QlDetailsDTO qlDetailsDTO = new QlDetailsDTO();
        qlDetailsDTO.setFilters(filtersArrayList);
        qlDetailsDTO.setFilterRelation("and");
        String block = webClient.get().uri(URI.create(host + "/open/crons?queryString=%s".formatted(URLEncoder.encode(JSONObject.toJSONString(qlDetailsDTO), StandardCharsets.UTF_8)))
//                .queryParam("queryString", URLEncoder.encode(JSONObject.toJSONString(qlDetailsDTO), StandardCharsets.UTF_8))
//                .build()
        ).retrieve().bodyToMono(String.class).block();
        try {
            JSONObject jsonObject = JSONObject.parseObject(block);
            if (jsonObject == null) {
                throw new YmwlException("获取定时任务详情失败");
            }
            if (jsonObject.getInteger("code") != 200) {
                throw new YmwlException("获取定时任务详情失败");
            }
            JSONObject data = jsonObject.getJSONObject("data");
            return JSONArray.parseArray(data.getJSONArray("data").toJSONString(), QlCronsDetailVO.TaskData.class);
        }catch (Exception e) {
            throw new YmwlException("获取定时任务详情失败");
        }
    }

    /**
     * 查找通过 getDetails 的单个青龙数据
     * @param details
     * @param id 青龙 id
     * @return
     */
    public QlCronsDetailVO.TaskData findDetail(List<QlCronsDetailVO.TaskData> details, Long id) {
        for (QlCronsDetailVO.TaskData detail : details) {
            if (Objects.equals(detail.getId(), id)) {
                return detail;
            }
        }
        return null;
    }
    public boolean run(Long id) {
        setBaseUrl();
        getToken();
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(id);
        String block = webClient.put().uri("/open/crons/run").bodyValue(ids)
                .retrieve().bodyToMono(String.class).block();
        JSONObject jsonObject = JSONObject.parseObject(block);
        if (jsonObject == null) {
            return false;
        }
        return jsonObject.getInteger("code") == 200;
    }
    public boolean stop(Long id) {
        setBaseUrl();
        getToken();
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(id);
        String block = webClient.put().uri("/open/crons/stop").bodyValue(ids)
                .retrieve().bodyToMono(String.class).block();
        JSONObject jsonObject = JSONObject.parseObject(block);
        if (jsonObject == null) {
            return false;
        }
        return jsonObject.getInteger("code") == 200;
    }
    public boolean disable(Long id) {
        setBaseUrl();
        getToken();
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(id);
        String block = webClient.put().uri("/open/crons/disable").bodyValue(ids)
                .retrieve().bodyToMono(String.class).block();
        JSONObject jsonObject = JSONObject.parseObject(block);
        if (jsonObject == null) {
            return false;
        }
        return jsonObject.getInteger("code") == 200;
    }
    public boolean enable(Long id) {
        setBaseUrl();
        getToken();
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(id);
        String block = webClient.put().uri("/open/crons/enable").bodyValue(ids)
                .retrieve().bodyToMono(String.class).block();
        JSONObject jsonObject = JSONObject.parseObject(block);
        if (jsonObject == null) {
            return false;
        }
        return jsonObject.getInteger("code") == 200;
    }
    public boolean remove(Long id) {
        setBaseUrl();
        getToken();
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(id);
        String block = webClient.method(HttpMethod.DELETE).uri("/open/crons").bodyValue(ids)
                .retrieve().bodyToMono(String.class).block();
        JSONObject jsonObject = JSONObject.parseObject(block);
        if (jsonObject == null) {
            return false;
        }
        return jsonObject.getInteger("code") == 200;
    }
    public String getLog(Long id) {
        setBaseUrl();
        getToken();
        String block = webClient.get().uri("/open/crons/" + id.toString() + "/log").retrieve().bodyToMono(String.class).block();
        JSONObject jsonObject = JSONObject.parseObject(block);
        if (jsonObject == null) {
            return "";
        }
        if (jsonObject.getInteger("code") != 200) {
            return "";
        }
        return jsonObject.getString("data");
    }
}