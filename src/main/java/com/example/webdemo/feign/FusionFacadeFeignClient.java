package com.example.webdemo.feign;

import com.cet.electric.commons.ApiResult;
import com.cet.electric.matterhorn.devicedataservice.v2.common.entity.datalog.DatalogGroupData;
import com.cet.electric.matterhorn.devicedataservice.v2.common.entity.datalog.MeterDataLogQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "fusion-facade", url = "${feign.custom-routing.services.fusion-facade}")
public interface FusionFacadeFeignClient {

    /**
     * <p> 批量获取指定间隔的定时记录
     * 原api path: POST /api/v1/batch/datalog/span/group/precise
     *
     * @param params 查询参数
     * @param fill   是否对缺失数据进行填充，true-填充，false-不进行填充。按照interval进行填充，interval为0则不进行填充
     * @param p35    是否查询p35的库，为true，则查询p35的库。默认查询p40的库
     * @return 精确获取指定间隔的定时记录
     * @date 2019/11/23 15:31
     */
    @PostMapping(value = "/api/v1/batch/datalog/span/group/precise")
    ApiResult<List<DatalogGroupData>> queryMultiMeterDataLogGroupDataByStartTimeAndInterval(
            @RequestBody MeterDataLogQueryParam params,
            @RequestParam(name = "fill", required = false) @Nullable Boolean fill,
            @RequestParam(name = "p35", required = false) @Nullable Boolean p35);
}
