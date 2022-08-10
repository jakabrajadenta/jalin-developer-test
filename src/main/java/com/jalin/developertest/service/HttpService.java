package com.jalin.developertest.service;

import com.jalin.developertest.dto.HttpHeaderDto;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
public class HttpService {

    private HttpHeaderDto httpHeaderDto;

    public void setHttpHeader(HttpHeaderDto httpHeaderDtoRequest) {
        if (httpHeaderDto == null) {
            this.httpHeaderDto = httpHeaderDtoRequest;
        }
    }

    public HttpHeaderDto getHttpHeader() {
        return this.httpHeaderDto;
    }
}
