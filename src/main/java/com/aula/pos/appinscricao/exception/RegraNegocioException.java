package com.aula.pos.appinscricao.exception;

import org.springframework.http.HttpStatus;

public class RegraNegocioException extends ApiException {

    public RegraNegocioException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}