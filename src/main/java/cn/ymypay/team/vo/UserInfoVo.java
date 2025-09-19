package cn.ymypay.team.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author lvjinfa
 * @date 2025/6/1 下午4:44
 * @description: 用户数据视图对象
 */
@Data
public class UserInfoVo {
    private String uuid;
    private String userName;
    private String name;
    private String qqId;
    private String wxId;
    private String email;
    private Integer vip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 指定 JSON 序列化格式和时区
    private Date vipTime;
    private String faceimg;
    private String token;
    private Long loginExpireTime;
}
