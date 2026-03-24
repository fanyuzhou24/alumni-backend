package com.alumni.system.service;

import com.alumni.system.domain.DeptCreateApply;
import com.alumni.system.entity.ro.CheckRO;
import com.alumni.system.entity.ro.dept.DeptCreateApplyPageRO;
import com.alumni.system.entity.ro.dept.DeptCreateRO;
import com.alumni.system.entity.vo.DeptCreateApplyVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 创建班级申请表 服务类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
public interface IDeptCreateApplyService extends IService<DeptCreateApply> {

    IPage<DeptCreateApplyVO> pageByCondition(DeptCreateApplyPageRO ro);

    List<DeptCreateApply> listByCondition(DeptCreateApplyPageRO ro);

    void check(CheckRO ro);

    void applyCreate(DeptCreateRO ro);
}
