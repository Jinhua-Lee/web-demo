package com.example.webdemo.util;

import com.example.webdemo.controller.dto.ReadCountInfoDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author Jinhua-Lee
 */
@Slf4j
public class FileUtil {

    private static final int BUFFER_SIZE = 4096;

    public static ReadCountInfoDTO readFile(String prefix, String filePath) throws IOException {
        InputStream in = new FileInputStream(prefix + filePath);
        int byteCount = 0;
        int loopCount = 0;
        int bytesRead;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = in.read(buffer)) != -1) {
            loopCount++;
            byteCount += bytesRead;
        }
        log.info("read file: {}, loop count: {}, byteCount: {}", filePath, loopCount, byteCount);
        return ReadCountInfoDTO.builder().loopCount(loopCount).readCount(byteCount).build();
    }
}
