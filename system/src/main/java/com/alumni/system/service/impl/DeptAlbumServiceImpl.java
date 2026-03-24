package com.alumni.system.service.impl;

import com.alumni.common.utils.SecurityUtils;
import com.alumni.system.domain.DeptAlbum;
import com.alumni.system.entity.ro.CheckRO;
import com.alumni.system.entity.ro.dept.DeptAlbumPageRO;
import com.alumni.system.mapper.DeptAlbumMapper;
import com.alumni.system.service.IDeptAdminService;
import com.alumni.system.service.IDeptAlbumService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 班级相册表 服务实现类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Service
public class DeptAlbumServiceImpl extends ServiceImpl<DeptAlbumMapper, DeptAlbum> implements IDeptAlbumService {
    @Resource
    private IDeptAdminService deptAdminService;

    @Override
    public Page<DeptAlbum> pageByCondition(DeptAlbumPageRO ro) {
        return lambdaQuery()
                .eq(ro.getDeptId() != null, DeptAlbum::getDeptId, ro.getDeptId())
                .eq(Objects.nonNull(ro.getStatus()), DeptAlbum::getStatus, ro.getStatus())
                .eq(DeptAlbum::getDelFlag, "0")
                .orderByDesc(DeptAlbum::getCreateTime)
                .page(new Page<>(ro.getPageNum(), ro.getPageSize()));
    }

    @Override
    public List<DeptAlbum> listByCondition(DeptAlbumPageRO ro) {
        return lambdaQuery()
                .eq(ro.getDeptId() != null, DeptAlbum::getDeptId, ro.getDeptId())
                .eq(Objects.nonNull(ro.getStatus()), DeptAlbum::getStatus, ro.getStatus())
                .eq(DeptAlbum::getDelFlag, "0")
                .list();
    }

    @Override
    public void check(CheckRO ro) {
        lambdaUpdate()
                .eq(DeptAlbum::getId, ro.getBusinessId())
                .set(DeptAlbum::getStatus, ro.getStatus())
                .set(DeptAlbum::getCheckUserId, SecurityUtils.getUserId())
                .set(DeptAlbum::getRemark, ro.getRemark())
                .update()
        ;
    }

    @Override
    public void applyAlbum(DeptAlbum deptAlbum) {
        deptAlbum.setStatus(0);
        save(deptAlbum);
    }

    @Override
    public void deleteAlbum(Long id) {
        DeptAlbum deptAlbum = getById(id);
        Assert.notNull(deptAlbum, "相册不存在");
        Long userId = SecurityUtils.getUserId();
        if (!Objects.equals(userId, deptAlbum.getApplyUserId())) {
            deptAdminService.validCheckUser(userId, deptAlbum.getDeptId());
        }

        removeById(id);
    }

}
