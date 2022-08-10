package com.jalin.developertest.controller;

import com.jalin.developertest.dto.CustomerDto;
import com.jalin.developertest.dto.CustomerRequestDto;
import com.jalin.developertest.service.CustomerService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customer")
    public ResponseEntity<CustomerDto> getCustomerDataByType(@RequestBody @Validated CustomerRequestDto customerRequestDto) throws NotFoundException {
        log.info("Customer Request Data : {}", customerRequestDto);
        return ResponseEntity.ok(customerService.getCustomerDataByType(customerRequestDto));
    }
}
