package com.jalin.developertest.configuration.mapper;

import com.jalin.developertest.dto.CustomerDto;
import com.jalin.developertest.dto.MockCustomerDto;
import com.jalin.developertest.service.CustomerService;
import com.jalin.developertest.service.HttpService;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class MockCustomerToCustomerResponseDtoMapper  implements OrikaMapperFactoryConfigurer {

    @Autowired
    private HttpService httpService;

    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(MockCustomerDto.class, CustomerDto.class)
                .byDefault().customize(
                        new CustomMapper<MockCustomerDto,CustomerDto>() {
                            @Override
                            public void mapAtoB(MockCustomerDto mockCustomerDto, CustomerDto customerDto, MappingContext context) {
                                customerDto.setResponse(String.valueOf(HttpStatus.OK.value()));
                                customerDto.setSignature(httpService.getHttpHeader().getSignature());
                                switch (mockCustomerDto.getTipe()){
                                    case CustomerService.TIPE_1:
                                        customerDto.setTelepon(null);
                                        break;
                                    case CustomerService.TIPE_2:
                                        customerDto.setAlamat(null);
                                        break;
                                }
                            }
                        }
                )
                .register();
    }
}
