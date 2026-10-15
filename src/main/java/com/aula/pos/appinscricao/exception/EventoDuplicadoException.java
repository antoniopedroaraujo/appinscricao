package com.aula.pos.appinscricao.exception;

import org.springframework.http.HttpStatus;

public class EventoDuplicadoException extends ApiException {

    public EventoDuplicadoException() {
        super(
                HttpStatus.CONFLICT,
                "Já existe um evento com o mesmo nome, data/hora e cidade"
        );
    }
}