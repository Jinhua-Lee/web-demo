package com.example.webdemo.controller;

import com.cet.electric.matterhorn.devicedataservice.api.TestRestApi;
import com.cet.electric.matterhorn.devicedataservice.common.PecInitResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Jinhua-Lee
 */
@RestController
@RequestMapping(value = "/pec-core")
public class PecController {

    private TestRestApi testRestApi;

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

    @Autowired
    public void setTestRestApi(TestRestApi testRestApi) {
        this.testRestApi = testRestApi;
    }
}
