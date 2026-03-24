package com.alumni.system.service;

import com.alumni.system.domain.DeptAlbum;
import com.alumni.system.entity.ro.CheckRO;
import com.alumni.system.entity.ro.dept.DeptAlbumPageRO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 班级相册表 服务类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
public interface IDeptAlbumService extends IService<DeptAlbum> {

    Page<DeptAlbum> pageByCondition(DeptAlbumPageRO ro);

    List<DeptAlbum> listByCondition(DeptAlbumPageRO ro);

    void check(CheckRO ro);

    void applyAlbum(DeptAlbum deptAlbum);

    void deleteAlbum(Long id);
}
