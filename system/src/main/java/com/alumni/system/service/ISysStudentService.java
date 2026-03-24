package com.alumni.system.service;

import com.alumni.system.domain.SysStudent;
import com.alumni.system.domain.excel.SysStudentExcel;
import com.alumni.system.entity.ro.StudentPageRO;
import com.alumni.system.entity.vo.SysStudentVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 学生信息表 服务类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-19
 */
public interface ISysStudentService extends IService<SysStudent> {

    IPage<SysStudentVO> pageByCondition(StudentPageRO ro);

    List<SysStudent> listByCondition(StudentPageRO ro);

    /**
     * 班级学生列表（含用户）
     * @param ro
     * @return
     */
    List<SysStudentVO> listByConditionV2(StudentPageRO ro);


    SysStudent getOne(Long studentId);

    void insert(SysStudent sysStudent);

    void update(SysStudent sysStudent);

    void remove(Long studentId);

    void batchUpdate(List<SysStudent> studentList);

    void bindStudents(List<Long> studentIds, Long userId);

    String importStudent(List<SysStudentExcel> userList);
}
