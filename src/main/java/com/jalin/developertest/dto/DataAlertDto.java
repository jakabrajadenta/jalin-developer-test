package com.jalin.developertest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataAlertDto {

    private String bankCode;
    private String envirotment;
    private String port;
    private String bankName;
    private String status;
}
