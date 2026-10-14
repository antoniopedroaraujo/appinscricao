package com.aula.pos.appinscricao.mapper;

import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import com.aula.pos.appinscricao.model.Evento;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Component
public class EventoMapper {

    public Evento toEntity(EventoRequest request) {
        return new Evento(
                null,
                request.nome(),
                request.dataHoraEvento(),
                request.local(),
                request.cidade(),
                request.estado().toUpperCase(Locale.ROOT),
                request.qtde()
        );
    }

    public EventoResponse toResponse(Evento evento) {
        long diasParaEvento = ChronoUnit.DAYS.between(
                LocalDate.now(), evento.getDataHoraEvento().toLocalDate()
        );

        return new EventoResponse(
                evento.getId(),
                evento.getNome(),
                evento.getDataHoraEvento(),
                evento.getLocal(),
                evento.getCidade(),
                evento.getEstado(),
                evento.getQtde(),
                diasParaEvento
        );
    }

    public void updateEntity(EventoRequest request, Evento evento) {
        evento.setNome(request.nome());
        evento.setDataHoraEvento(request.dataHoraEvento());
        evento.setLocal(request.local());
        evento.setCidade(request.cidade());
        evento.setEstado(request.estado().toUpperCase(Locale.ROOT));
        evento.setQtde(request.qtde());
    }
}
