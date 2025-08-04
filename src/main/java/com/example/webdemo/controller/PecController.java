package com.example.webdemo.controller;

import com.cet.electric.commons.ApiResult;
import com.cet.electric.matterhorn.devicedataservice.v2.api.DataServiceV2RestApi;
import com.cet.electric.matterhorn.devicedataservice.v2.api.TestRestApi;
import com.cet.electric.matterhorn.devicedataservice.v2.common.PecInitResult;
import com.cet.electric.matterhorn.devicedataservice.v2.common.entity.datalog.DatalogGroupData;
import com.cet.electric.matterhorn.devicedataservice.v2.common.entity.datalog.MeterDataLogQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jinhua-Lee
 */
@RestController
@RequestMapping(value = "/pec-core")
public class PecController {

    private TestRestApi testRestApi;

    private DataServiceV2RestApi dataServiceV2RestApi;

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
        return dataServiceV2RestApi.queryMultiMeterDataLogGroupDataByStartTimeAndInterval(param, fill, p35);
    }


    @Autowired
    public void setTestRestApi(TestRestApi testRestApi) {
        this.testRestApi = testRestApi;
    }

    @Autowired
    public void setDataServiceV2RestApi(DataServiceV2RestApi dataServiceV2RestApi) {
        this.dataServiceV2RestApi = dataServiceV2RestApi;
    }
}
