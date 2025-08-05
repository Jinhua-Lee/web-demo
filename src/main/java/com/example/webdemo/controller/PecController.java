package com.example.webdemo.controller;

import com.cet.electric.commons.ApiResult;
import com.cet.electric.matterhorn.devicedataservice.v2.api.DataServiceV2RestApi;
import com.cet.electric.matterhorn.devicedataservice.v2.api.TestRestApi;
import com.cet.electric.matterhorn.devicedataservice.v2.common.PecInitResult;
import com.cet.electric.matterhorn.devicedataservice.v2.common.entity.datalog.DatalogGroupData;
import com.cet.electric.matterhorn.devicedataservice.v2.common.entity.datalog.DatalogValue;
import com.cet.electric.matterhorn.devicedataservice.v2.common.entity.datalog.MeterDataLogQueryParam;
import com.example.webdemo.feign.FusionFacadeFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author Jinhua-Lee
 */
@Slf4j
@RestController
@RequestMapping(value = "/pec-core")
public class PecController {

    private TestRestApi testRestApi;

    private DataServiceV2RestApi dataServiceV2RestApi;

    private FusionFacadeFeignClient fusionFacadeFeignClient;

    @GetMapping(value = "init")
    public PecInitResult init() {
        return testRestApi.init();
    }

    @GetMapping(value = "nextIds")
    public List<Long> nextNodeIds(
            @RequestParam(value = "nodeType") Long nodeType,
            @RequestParam(value = "count") Integer count,
            @RequestParam(value = "tenantId", required = false) Integer tenantId
    ) {
        return testRestApi.getNextNodeIds(nodeType, count, tenantId).getData();
    }

    @PostMapping(value = "datalog/precise")
    public ApiResult<List<DatalogGroupData>> getDatalogGroupData(
            @RequestBody MeterDataLogQueryParam param,
            @RequestParam (value = "fill", required = false) Boolean fill,
            @RequestParam (value = "p35", required = false) Boolean p35) {
        ApiResult<List<DatalogGroupData>> pecServiceResult = dataServiceV2RestApi
                .queryMultiMeterDataLogGroupDataByStartTimeAndInterval(param, fill, p35);
        ApiResult<List<DatalogGroupData>> facadeResult = fusionFacadeFeignClient
                .queryMultiMeterDataLogGroupDataByStartTimeAndInterval(param, fill, p35);
        if (pecServiceResult.isSuccess() && facadeResult.isSuccess()) {
            // 比较Double值结果是否一致
            // 1. 先找到deviceId, logicalId, dataTypeId, dataId相等的第一条
            for (DatalogGroupData pecServiceData : pecServiceResult.getData()) {
                for (DatalogGroupData facadeData : facadeResult.getData()) {
                    if (Objects.equals(pecServiceData, facadeData)) {
                        // 2. 找到第一条时间相等的记录并比较是否相等
                        findFirstTimeEqualValue(pecServiceData, facadeData);
                        break;
                    }
                }
            }
        }
        return pecServiceResult;
    }

    private void findFirstTimeEqualValue(DatalogGroupData pecServiceData, DatalogGroupData facadeData) {
        for (DatalogValue pecServiceValue : pecServiceData.getDataList()) {
            for (DatalogValue facadeValue : facadeData.getDataList()) {
                // 3. 找到时间相等的第一条值并比较是否相等
                if (Objects.equals(pecServiceValue.getTime(), facadeValue.getTime())) {
                    log.info("pecServiceValue: {}", pecServiceValue);
                    log.info("facadeValue: {}", facadeValue);
                    log.info("value equals: {}", Objects.equals(pecServiceValue.getValue(), facadeValue.getValue()));
                    break;
                }
            }
        }
    }


    @Autowired
    public void setTestRestApi(TestRestApi testRestApi) {
        this.testRestApi = testRestApi;
    }

    @Autowired
    public void setDataServiceV2RestApi(DataServiceV2RestApi dataServiceV2RestApi) {
        this.dataServiceV2RestApi = dataServiceV2RestApi;
    }

    @Autowired
    public void setFusionFacadeFeignClients(FusionFacadeFeignClient fusionFacadeFeignClient) {
        this.fusionFacadeFeignClient = fusionFacadeFeignClient;
    }
}
