package com.example.webdemo.controller;

import com.example.webdemo.controller.dto.ReadCountInfoDTO;
import com.example.webdemo.util.FileUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jinhua-Lee
 */
@RestController
@RequestMapping(value = "/io-test")
public class IoTestController {

    private static final String FILE_PREFIX_PATH = "/cgi/graph/json/";

    @GetMapping(value = "read")
    public ReadCountInfoDTO readFile(@RequestParam(value = "filePath") String filePath) throws Exception {
        return FileUtil.readFile(FILE_PREFIX_PATH, filePath);
    }
}
