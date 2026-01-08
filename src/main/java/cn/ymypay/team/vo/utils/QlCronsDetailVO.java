package cn.ymypay.team.vo.utils;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author 湮灭网络工作室——amen
 * @date 2025/12/14 下午5:38
 * @description: 任务信息实体类
 */
@Data
public class QlCronsDetailVO {
    /** 响应状态码 */
    private Integer code;

    /** 响应数据体 */
    private TaskData data;

    /**
     * 任务数据内部类（小驼峰命名 + FastJSON字段映射）
     */
    @Data
    public static class TaskData {
        /** 任务ID */
        private Long id;

        /** 任务名称 */
        private String name;

        /** 执行命令 */
        private String command;

        /** 定时表达式（cron） */
        private String schedule;

        /** 时间戳字符串 */
        private String timestamp;

        /** 是否保存 */
        private Boolean saved;

        /** 状态值 */
        private Integer status;

        /** 是否系统任务 */
        @JSONField(name = "isSystem")
        private Integer isSystem;

        /** 进程ID */
        private Integer pid;

        /** 是否禁用 */
        @JSONField(name = "isDisabled")
        private Integer isDisabled;

        /** 是否置顶 */
        @JSONField(name = "isPinned")
        private Integer isPinned;

        /** 日志路径 */
        @JSONField(name = "log_path")
        private String logPath;

        /** 标签列表 */
        private List<String> labels;

        /** 最后运行时间（时间戳） */
        @JSONField(name = "last_running_time")
        private Long lastRunningTime;

        /** 最后执行时间（时间戳） */
        @JSONField(name = "last_execution_time")
        private Long lastExecutionTime;

        /** 子任务ID */
        @JSONField(name = "sub_id")
        private Integer subId;

        /** 额外定时配置 */
        @JSONField(name = "extra_schedules")
        private String extraSchedules;

        /** 前置任务 */
        @JSONField(name = "task_before")
        private String taskBefore;

        /** 后置任务 */
        @JSONField(name = "task_after")
        private String taskAfter;

        /** 创建时间（UTC） */
        @JSONField(name = "createdAt")
        private String createdAt;

        /** 更新时间（UTC） */
        @JSONField(name = "updatedAt")
        private String updatedAt;
    }
}
