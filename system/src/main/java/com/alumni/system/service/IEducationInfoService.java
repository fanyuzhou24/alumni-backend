package com.alumni.system.service;

import com.alumni.system.domain.EducationInfo;
import com.alumni.system.entity.ro.EducationInfoPageRO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 教育经历表 服务类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-22
 */
public interface IEducationInfoService extends IService<EducationInfo> {

    Page<EducationInfo> pageByCondition(EducationInfoPageRO ro);

}
