package cn.ymypay.team.dto.utils;

import lombok.Data;

import java.util.List;

/**
 * @author 湮灭网络工作室——amen
 * @date 2025/12/15 上午11:14
 * @description: 青龙获取任务列表的queryString
 */
@Data
public class QlDetailsDTO {
    private List<Filters> filters;
    // 暂时未知 我看是没用上
    private Object sorts = null;
    private String filterRelation;
    @Data
    public static class Filters {
        private String property;
        private String operation;
        private Object value;
    }
}
