package com.jalin.developertest.service;

import com.jalin.developertest.dto.CustomerDto;
import com.jalin.developertest.dto.CustomerRequestDto;
import com.jalin.developertest.dto.MockCustomerDto;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class CustomerService {

    @Autowired
    private MapperFacade mapperFacade;

    public static final String TIPE_1 = "1";
    public static final String TIPE_2 = "2";
    private static final Integer ONE_DATA = 1;
    private static final Integer FIRST_DATA_INDEX = 0;

    private List<MockCustomerDto> getListDataCustomer(){
        var listCustomer = new ArrayList<MockCustomerDto>();
        listCustomer.add(MockCustomerDto.builder().userId(1L).nama("ricky").telepon("0822xxx").alamat("jakarta").build());
        listCustomer.add(MockCustomerDto.builder().userId(2L).nama("braja").telepon("0856xxx").alamat("bandung").build());
        return listCustomer;
    }

    public CustomerDto getCustomerDataByType(CustomerRequestDto customerRequestDto) throws NotFoundException {
        if (!(customerRequestDto.getTipe().equals(TIPE_1) || customerRequestDto.getTipe().equals(TIPE_2))) {
            throw new NotFoundException("Tipe yang dimasukan salah!");
        }
        var mockCustomerTemporaryList = new ArrayList<MockCustomerDto>();
        var listCustomer = this.getListDataCustomer();
        listCustomer.forEach(mockCustomerDto -> {
            if (mockCustomerDto.getNama().equals(customerRequestDto.getNama())){
                mockCustomerTemporaryList.add(mockCustomerDto);
            }
        });

        if (mockCustomerTemporaryList.size() != ONE_DATA) {
           throw new NoSuchElementException("Nama tidak ditemukan atau lebih dari satu!");
        }
        var mockCustomer = mockCustomerTemporaryList.get(FIRST_DATA_INDEX);
        mockCustomer.setTipe(customerRequestDto.getTipe());
        log.info("Customer Full Data : {}", mockCustomer);
        return mapperFacade.map(mockCustomer,CustomerDto.class);
    }
}
