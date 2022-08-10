package com.jalin.developertest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private String nama;
    private String alamat;
    private String telepon;

    private String tipe;
    private String response;
    private String signature;
}
