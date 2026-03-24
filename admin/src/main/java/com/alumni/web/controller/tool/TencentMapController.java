package com.alumni.web.controller.tool;

import com.alumni.common.core.controller.BaseController;
import com.alumni.common.core.domain.AjaxResult;
import com.alumni.framework.web.service.TencentMapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 腾讯地图
 * 
 * @author lifeng
 */
@RestController
@RequestMapping("/tencent/map")
@Api(value = "腾讯地图", tags = "腾讯地图")
public class TencentMapController extends BaseController {
    @Resource
    private TencentMapService tencentMapService;

    /**
     * 根据经纬度查询地址
     */
    @GetMapping("/address")
    @ApiOperation(value = "根据经纬度查询地址", notes = "根据经纬度查询地址")
    public AjaxResult getAddress(@RequestParam("location") String location) {
        Map<String, Object> result = tencentMapService.getAddressByLocation(location);
        return AjaxResult.success(result);
    }
}
