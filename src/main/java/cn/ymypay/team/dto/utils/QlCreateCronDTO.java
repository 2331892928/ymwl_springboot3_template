package cn.ymypay.team.dto.utils;

import lombok.Data;

import java.util.List;

/**
 * @author 湮灭网络工作室——amen
 * @date 2025/12/14 下午5:11
 * @description:
 */
@Data
public class QlCreateCronDTO {
    private String command;
    private String schedule;
    private String name;
    private List<String> labels;
}
