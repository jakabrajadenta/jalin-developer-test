package com.jalin.developertest.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultErrorAttributes {

    private int status;
    private String message;
    private String error;
    private String path;
    private String timestamp;
}
