package com.jalin.developertest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private BankReportService bankReportService;

    private static final String OFFLINE_STATUS = "offline";

    public void sendMailBaseDataAlert() throws FileNotFoundException {
        var mapConnectedBank = bankReportService.getMapConnectedBankByBankCode();
        var listDataAlert = bankReportService.getListDataAlert();

        var subject = "REPORT STATUS OFFLINE";
        var greetingMessage = "Selamat Siang Rekan Bank %s,\n\n";
        var introductionMessage = "Mohon bantuan untuk Sign on pada envi berikut:\n\n";
        var listDataMessage = " -Envi %s Port %s terpantau %s \n";
        var closingMessage = "\nTerima Kasih";
        mapConnectedBank.entrySet().forEach(bankCodeAndEmail -> {
            var bodyMessage = new ArrayList<String>();
            bodyMessage.add(String.format(greetingMessage,bankCodeAndEmail.getKey()));
            bodyMessage.add(introductionMessage);
            listDataAlert.forEach(dataAlertDto -> {
                if (dataAlertDto.getBankCode().equals(bankCodeAndEmail.getKey()) && dataAlertDto.getStatus().equalsIgnoreCase(OFFLINE_STATUS)){
                    var offline = StringUtils.capitalize(dataAlertDto.getStatus());
                    bodyMessage.add(String.format(listDataMessage,dataAlertDto.getEnvirotment(),dataAlertDto.getPort(),offline));
                }
            });
            bodyMessage.add(closingMessage);
            this.sendSimpleMessage(bankCodeAndEmail.getValue(),subject,String.join("",bodyMessage));
        });

    }

    public void sendSimpleMessage(String destinasion, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("braja@mail.com");
        message.setTo(destinasion);
        message.setSubject(subject);
        message.setText(text);
        log.info("Message : {}",message);
        log.info("Body Message : {}",text);
        emailSender.send(message);
    }

}
