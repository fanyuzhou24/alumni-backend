package com.alumni.system.service;

import com.alumni.system.domain.DeptJoinApply;
import com.alumni.system.entity.ro.CheckRO;
import com.alumni.system.entity.ro.dept.DeptJoinApplyPageRO;
import com.alumni.system.entity.ro.dept.DeptJoinRO;
import com.alumni.system.entity.vo.DeptJoinApplyVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 加入班级申请表 服务类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
public interface IDeptJoinApplyService extends IService<DeptJoinApply> {

    IPage<DeptJoinApplyVO> pageByCondition(DeptJoinApplyPageRO ro);

    List<DeptJoinApply> listByCondition(DeptJoinApplyPageRO ro);

    void check(CheckRO ro);

    void applyJoin(DeptJoinRO ro);
}
