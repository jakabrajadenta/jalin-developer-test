package com.jalin.developertest.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDto {

    @NotNull
    private String nama;
    @NotNull
    private String tipe;
}
