package com.alumni.framework.web.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.alumni.common.utils.http.HttpUtils;
import com.alumni.framework.config.properties.TencentMapProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TencentMapService {

    private final TencentMapProperties mapProperties;

    /**
     * 逆地理解析，根据经纬度获取地址
     */
    public Map<String, Object> getAddressByLocation(String location) {
        String url = mapProperties.getGeocoderUrl();
        String key = mapProperties.getKey();

        String param = "location=" + location + "&key=" + key;

        String result = HttpUtils.sendGet(url, param);

        return JSON.parseObject(result, new TypeReference<Map<String, Object>>() {});
    }
}
