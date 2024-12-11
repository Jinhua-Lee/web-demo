package com.example.webdemo.controller.dto;

import lombok.*;

/**
 * @author Jinhua-Lee
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReadCountInfoDTO {

    private Integer loopCount;
    private Integer readCount;
}
