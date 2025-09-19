package cn.ymypay.team.config;

import lombok.Data;

import java.util.List;
@Data
public class XssFilterProperties {
    /**
     * 是否启用XSS过滤。
     */
    private boolean enabled = true;
    /**
     * 需要排除的URL模式，这些URL不会进行XSS过滤。
     */
    private List<String> excludeUrlList;
}
