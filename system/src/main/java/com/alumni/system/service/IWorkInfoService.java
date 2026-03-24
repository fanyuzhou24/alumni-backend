package com.alumni.system.service;

import com.alumni.system.domain.WorkInfo;
import com.alumni.system.entity.ro.WorkInfoPageRO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 职业发展表 服务类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-22
 */
public interface IWorkInfoService extends IService<WorkInfo> {

    Page<WorkInfo> pageByCondition(WorkInfoPageRO ro);
}
