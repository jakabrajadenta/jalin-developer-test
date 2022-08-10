package com.jalin.developertest.configuration;

import com.jalin.developertest.dto.HttpHeaderDto;
import com.jalin.developertest.service.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HttpHeaderConfiguration extends OncePerRequestFilter {

    @Autowired
    private HttpService httpService;

    private static final String SIGNATURE_KEY = "signature";
    private static final String SIGNATURE_VALUE = "1A79F67E43C8E64A0231DB8745FDB67902216E071D741DF0F15C95DC983CA8A9";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var httpHeaderDto = HttpHeaderDto.builder().build();
        var signature = request.getHeader(SIGNATURE_KEY);
        if (signature != null) {
            if (signature.equals(SIGNATURE_VALUE)) {
                httpHeaderDto.setSignature(signature);
                httpService.setHttpHeader(httpHeaderDto);
                filterChain.doFilter(request,response);
            } else {
                this.signatureIsNotValid(response);
            }
        } else {
            this.signatureIsNotValid(response);
        }
    }

    private void signatureIsNotValid(ServletResponse servletResponse) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String error = "Non-Existent or invalid signature";
        response.reset();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        servletResponse.setContentLength(error.length());
        servletResponse.getWriter().write(error);
    }
}
