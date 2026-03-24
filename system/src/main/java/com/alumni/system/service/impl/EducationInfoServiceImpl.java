package com.alumni.system.service.impl;

import com.alumni.system.domain.EducationInfo;
import com.alumni.system.entity.ro.EducationInfoPageRO;
import com.alumni.system.mapper.EducationInfoMapper;
import com.alumni.system.service.IEducationInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 教育经历表 服务实现类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-22
 */
@Service
public class EducationInfoServiceImpl extends ServiceImpl<EducationInfoMapper, EducationInfo> implements IEducationInfoService {

    @Override
    public Page<EducationInfo> pageByCondition(EducationInfoPageRO ro) {
        return lambdaQuery()
                .eq(ro.getUserId() != null, EducationInfo::getUserId, ro.getUserId())
                .eq(EducationInfo::getDelFlag, "0")
                .orderByDesc(EducationInfo::getCreateTime)
                .page(new Page<>(ro.getPageNum(), ro.getPageSize()));
    }
}
