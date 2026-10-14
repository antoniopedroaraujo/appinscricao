package com.aula.pos.appinscricao.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EventoRequest(

        @NotBlank
        @Size(max = 150)
        String nome,

        @NotNull
        @Future
        LocalDateTime dataHoraEvento,

        @NotBlank
        @Size(max = 200)
        String local,

        @NotBlank
        @Size(max = 100)
        String cidade,

        @NotBlank
        @Size(min = 2, max = 2)
        String estado,

        @NotNull
        @Min(1)
        Integer qtde

) {}
