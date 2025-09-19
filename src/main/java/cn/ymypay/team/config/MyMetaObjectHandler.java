package cn.ymypay.team.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import cn.ymypay.team.utils.UserUtils;
import cn.ymypay.team.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author lvjinfa
 * @date 2025/6/1 下午17:09
 * @description: mybatis-plus自动填充
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充...");
        UserInfoVo userInfo = UserUtils.getUserInfo();
        if (userInfo != null) {
            this.strictInsertFill(metaObject, "createdBy", String.class, userInfo.getUuid());
        }
        this.strictInsertFill(metaObject, "uuid", String.class, UUID.randomUUID().toString().replace("-", ""));
        this.strictInsertFill(metaObject, "createdTime", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充...");
        UserInfoVo userInfo = UserUtils.getUserInfo();
        if (userInfo != null) {
            this.strictInsertFill(metaObject, "updatedBy", String.class, userInfo.getUuid());
        }
        this.strictInsertFill(metaObject, "createdTime", Date.class, new Date());
    }
}
