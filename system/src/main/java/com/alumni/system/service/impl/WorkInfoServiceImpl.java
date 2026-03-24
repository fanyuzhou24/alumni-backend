package com.alumni.system.service.impl;

import com.alumni.system.domain.WorkInfo;
import com.alumni.system.entity.ro.WorkInfoPageRO;
import com.alumni.system.mapper.WorkInfoMapper;
import com.alumni.system.service.IWorkInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 职业发展表 服务实现类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-22
 */
@Service
public class WorkInfoServiceImpl extends ServiceImpl<WorkInfoMapper, WorkInfo> implements IWorkInfoService {

    @Override
    public Page<WorkInfo> pageByCondition(WorkInfoPageRO ro) {
        return lambdaQuery()
                .eq(ro.getUserId() != null, WorkInfo::getUserId, ro.getUserId())
                .eq(WorkInfo::getDelFlag, "0")
                .page(new Page<>(ro.getPageNum(), ro.getPageSize()));
    }
}
