package com.jalin.developertest.controller;

import com.jalin.developertest.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/email/offline-alert")
    public String sendMailToBankByDataAlert() throws FileNotFoundException {
        emailService.sendMailBaseDataAlert();
        return "OK";
    }
}
