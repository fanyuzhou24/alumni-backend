package com.alumni.framework.hanlder;

import com.alumni.common.core.domain.model.LoginUser;
import com.alumni.common.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MybatisPlus插入时自动填充和更新时自动填充
 *
 * @author lifeng
 */
@Component
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
    private final String createTime = "createTime";
    private final String createUser = "createBy";
    private final String updateTime = "updateTime";
    private final String updateUser = "updateBy";

    private final String deleted = "deleted";


    /**
     * 插入时自动填充
     *
     * @param metaObject org.apache.ibatis.reflection.MetaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        // 如果插入操作的实体类有createTime字段，且添加@TableField(fill = FieldFill.INSERT)，则为其自动设置为当前时间
        // 需要在数据库链接中加入serverTimezone字段标明数据库服务器所在时区
        // 如serverTimezone=CTT: 标明东八区
        if (metaObject.hasSetter(createTime)) {
            this.fillStrategy(metaObject, createTime, new Date());
        }

        if (metaObject.hasSetter(deleted)) {
            this.fillStrategy(metaObject, deleted, false);
        }

        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (null != loginUser) {
            if (metaObject.hasSetter(createUser)) {
                this.fillStrategy(metaObject, createUser, loginUser.getUserId().toString());
            }
        }


    }

    /**
     * 更新时自动填充
     *
     * @param metaObject org.apache.ibatis.reflection.MetaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");

        // 如果插入操作的实体类有updateTime字段，且添加@TableField(fill = FieldFill.UPDATE)，则为其自动设置为当前时间
        // 需要在数据库链接中加入serverTimezone字段标明数据库服务器所在时区
        // 如serverTimezone=CTT: 标明东八区
        if (metaObject.hasSetter(updateTime)) {
            this.setFieldValByName(updateTime, new Date(), metaObject);
        }

//         如果插入操作的实体类有createUser字段，且添加@TableField(fill = FieldFill.INSERT)，则为其自动设置为当前用户名
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (null != loginUser) {
            if (metaObject.hasSetter(updateUser)) {
                this.setFieldValByName(updateUser, loginUser.getUserId().toString(), metaObject);
            }
        }
    }
}
