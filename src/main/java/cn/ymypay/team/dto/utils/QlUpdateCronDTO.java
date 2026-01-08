package cn.ymypay.team.dto.utils;

import lombok.Data;

import java.util.List;

/**
 * @author 湮灭网络工作室——amen
 * @date 2025/12/14 下午5:11
 * @description:
 */
@Data
public class QlUpdateCronDTO {
    private String command;
    private String extra_schedules = null;
    private Long id;
    private List<String> labels;
    private String name;
    private String schedule;
    private String task_after = null;
    private String task_before = null;

}
