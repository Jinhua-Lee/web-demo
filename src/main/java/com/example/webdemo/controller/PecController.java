package com.example.webdemo.controller;

import com.cet.electric.matterhorn.devicedataservice.api.TestPecInitRestApi;
import com.cet.electric.matterhorn.devicedataservice.common.PecInitResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jinhua-Lee
 */
@RestController
@RequestMapping(value = "/pec-service")
public class PecController {

    private TestPecInitRestApi testPecInitRestApi;

    @GetMapping(value = "init")
    public PecInitResult init() {
        return testPecInitRestApi.init();
    }

    @Autowired
    public void setTestPecInitRestApi(TestPecInitRestApi testPecInitRestApi) {
        this.testPecInitRestApi = testPecInitRestApi;
    }
}
