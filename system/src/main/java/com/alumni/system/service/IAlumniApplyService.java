package com.alumni.system.service;

import com.alumni.system.domain.AlumniApply;
import com.alumni.system.entity.dto.AlumniApplyDTO;
import com.alumni.system.entity.ro.AlumniApplyPageRO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 校友申请表 服务类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-22
 */
public interface IAlumniApplyService extends IService<AlumniApply> {

    Page<AlumniApply> pageByCondition(AlumniApplyPageRO ro);

    List<AlumniApply> listByCondition(AlumniApplyPageRO ro);

    AlumniApply getOne(Long id);

    void insert(AlumniApply alumniApply);

    void update(AlumniApply alumniApply);

    void delete(Long id);

    void check(AlumniApplyDTO alumniApplyDTO);

    Integer getUserStatus();
}
