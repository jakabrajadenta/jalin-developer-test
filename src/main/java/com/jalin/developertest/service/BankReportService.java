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
        mapConnectedBank.put("MDR","brajamobilelegends@gmail.com");
        mapConnectedBank.put("BTN","brajamobilelegends@gmail.com");
        mapConnectedBank.put("BNI","brajamobilelegends@gmail.com");
        mapConnectedBank.put("MTP","brajamobilelegends@gmail.com");
        mapConnectedBank.put("BCA","brajamobilelegends@gmail.com");
        return mapConnectedBank;
    }
}
