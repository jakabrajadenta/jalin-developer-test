package com.jalin.developertest.service;

import com.jalin.developertest.dto.DataAlertDto;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service
public class BankReportService {

    private static final String USER_DIRECTORY = System.getProperty("user.dir");
    private static final String FILE_PATH = "/input/";
    private static final String FILE_NAME = "Data Alert.txt";

//  pattern "MDR;MP;8101;Bank Mandiri Online         ;offline\n" delimiter ; dan \n
//          "bankCode;envirotment;port;bankName;status\n"
    public List<DataAlertDto> getListDataAlert() throws FileNotFoundException {
        var input = new Scanner(new File(USER_DIRECTORY.concat(FILE_PATH.concat(FILE_NAME))));
        input.useDelimiter(";|\n");
        var dataAlertList = new ArrayList<DataAlertDto>();
        while (input.hasNext()){
            var dataAlert = DataAlertDto.builder()
                    .bankCode(input.next())
                    .envirotment(input.next())
                    .port(input.next())
                    .bankName(input.next().trim())
                    .status(input.next())
                    .build();
            dataAlertList.add(dataAlert);
        }
        return dataAlertList;
    }

    public Map<String,String> getMapConnectedBankByBankCode(){
        var mapConnectedBank = new HashMap<String,String>();
        mapConnectedBank.put("MDR","mandiri@mail.com");
        mapConnectedBank.put("BTN","btn@mail.com");
        mapConnectedBank.put("BNI","bni@mail.com");
        mapConnectedBank.put("MTP","mtp@mail.com");
        mapConnectedBank.put("BCA","bca@mail.com");
        return mapConnectedBank;
    }
}
