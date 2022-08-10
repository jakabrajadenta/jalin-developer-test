package com.jalin.developertest.controller;

import com.jalin.developertest.dto.DataAlertDto;
import com.jalin.developertest.service.BankReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@RestController
public class BankController {

    @Autowired
    private BankReportService bankReportService;

    @GetMapping("/bank/data-alert")
    public ResponseEntity<List<DataAlertDto>> getCustomerDataByType() throws FileNotFoundException {
        return ResponseEntity.ok(bankReportService.getListDataAlert());
    }
}
