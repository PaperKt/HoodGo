package com.hoodgo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.hoodgo.constant.MessageConstant;
import com.hoodgo.exception.OrderBusinessException;
import com.hoodgo.properties.TencentMapProperties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TencentMapUtil {

    public static final String geocodeUrl = "https://apis.map.qq.com/ws/geocoder/v1/";
    public static final String distanceUrl ="https://apis.map.qq.com/ws/distance/v1/matrix";
    @Autowired
    TencentMapProperties tencentMapProperties;


    /**
     * 根据文本地址调用腾讯地理编码获取经纬度
     */
    public String geocode(String address) {
        Map map = new HashMap();
        map.put("address",address);
        map.put("output","json");
        map.put("key",tencentMapProperties.getKey());
        String responseString = HttpClientUtil.doGet(geocodeUrl, map);
        JSONObject jsonObject = JSON.parseObject(responseString);
        if(!jsonObject.getString("status").equals("0")){
            log.info("地址解析请求状态, {}",jsonObject.getString("status"));
            throw new OrderBusinessException(MessageConstant.PARSE_ADDRESS_FAILED);
        }
        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
        return location.getString("lat") + "," + location.getString("lng");
    }

    /**
     * 根据经纬度判断距离
     */
    public Integer[] distance(String srcLatLng, String targetLatLng) {
        Map map = new HashMap();
        map.put("from",srcLatLng);
        map.put("to",targetLatLng);
        map.put("key",tencentMapProperties.getKey());
        map.put("mode","bicycling");
        String responseString = HttpClientUtil.doGet(distanceUrl, map);
        JSONObject jsonObject = JSON.parseObject(responseString);
        if(!jsonObject.getString("status").equals("0")){
            log.info("距离计算请求状态, {}",jsonObject.getString("status"));
            throw new OrderBusinessException(MessageConstant.PARSE_ADDRESS_FAILED);
        }
        JSONObject disJson = jsonObject.getJSONObject("result")
                .getJSONArray("rows")
                .getJSONObject(0)
                .getJSONArray("elements")
                .getJSONObject(0);

        Integer distance = disJson.getInteger("distance");
        Integer durationSeconds = disJson.getInteger("duration");
        Integer[] res = new Integer[2];
        res[0] = distance;
        res[1] = durationSeconds;
        return res;
    }

    /**
     * 对外封装：判断文本地址是否在配送范围
     */
    public boolean inRangeByAddress(String address) {
        String srcLatLng = geocode(tencentMapProperties.getShopAddress());
        String targetLatLng = geocode(address);
        Integer[] res = distance(srcLatLng, targetLatLng);
        log.info("配送距离: {} 米", res[0]);
        log.info("预计配送时间: {}h{}m{}s", res[1] / 3600, (res[1] % 3600) / 60, res[1] % 60);
        return res[0] <= tencentMapProperties.getDeliveryRadiusMeter();
    }

}

