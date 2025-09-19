package cn.ymypay.team.utils.YLogin.domain;

import lombok.Data;

/**
 * @author lvjinfa
 * @date 2025/5/26 上午10:04
 * @description: 获取用户信息的vo
 */
@Data
public class GetInfoDomain {
    private Integer code;
    private String msg;
    private String type;
    private String social_uid;
    private String access_token;
    private String nickname;
    private String faceimg;
    private String location;
    private String gender;
    private String ip;
}
