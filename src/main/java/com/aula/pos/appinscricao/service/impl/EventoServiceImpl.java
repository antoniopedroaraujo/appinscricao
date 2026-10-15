package com.aula.pos.appinscricao.service.impl;

import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import com.aula.pos.appinscricao.exception.EventoDuplicadoException;
import com.aula.pos.appinscricao.exception.RegraNegocioException;
import com.aula.pos.appinscricao.exception.ResourceNotFoundException;
import com.aula.pos.appinscricao.mapper.EventoMapper;
import com.aula.pos.appinscricao.model.Evento;
import com.aula.pos.appinscricao.repository.EventoRepository;
import com.aula.pos.appinscricao.service.EventoService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;
    private final EventoMapper mapper;

    @Override
    public Page<EventoResponse> findAll(
            String nome,
            String cidade,
            String estado,
            Pageable pageable) {

        Page<Evento> eventos;

        if (nome != null && !nome.isBlank()) {
            eventos = eventoRepository
                    .findByNomeContainingIgnoreCase(nome.trim(), pageable);

        } else if (cidade != null && !cidade.isBlank()) {
            eventos = eventoRepository
                    .findByCidadeIgnoreCase(cidade.trim(), pageable);

        } else if (estado != null && !estado.isBlank()) {
            eventos = eventoRepository
                    .findByEstadoIgnoreCase(
                            estado.trim().toUpperCase(Locale.ROOT),
                            pageable);

        } else {
            eventos = eventoRepository.findAll(pageable);
        }

        return eventos.map(mapper::toResponse);
    }

    @Override
    public EventoResponse findById(UUID id) {
        Evento evento = findByIdOrThrow(id);
        return mapper.toResponse(evento);
    }

    @Override
    @Transactional
    public EventoResponse save(EventoRequest request) {

        boolean duplicado = eventoRepository
                .existsByNomeIgnoreCaseAndDataHoraEventoAndCidadeIgnoreCase(
                        request.nome().trim(),
                        request.dataHoraEvento(),
                        request.cidade().trim());

        if (duplicado) {
            throw new EventoDuplicadoException();
        }

        Evento evento = mapper.toEntity(request);

        evento.setNome(request.nome().trim());
        evento.setCidade(request.cidade().trim());
        evento.setEstado(
                request.estado().trim().toUpperCase(Locale.ROOT));

        Evento eventoSalvo = eventoRepository.save(evento);

        return mapper.toResponse(eventoSalvo);
    }

    @Override
    @Transactional
    public EventoResponse update(UUID id, EventoRequest request) {

        Evento evento = findByIdOrThrow(id);

        boolean duplicado = eventoRepository
                .existsByNomeIgnoreCaseAndDataHoraEventoAndCidadeIgnoreCaseAndIdNot(
                        request.nome().trim(),
                        request.dataHoraEvento(),
                        request.cidade().trim(),
                        id);

        if (duplicado) {
            throw new EventoDuplicadoException();
        }

        mapper.updateEntity(request, evento);

        evento.setNome(request.nome().trim());
        evento.setCidade(request.cidade().trim());
        evento.setEstado(
                request.estado().trim().toUpperCase(Locale.ROOT));

        Evento eventoAtualizado = eventoRepository.save(evento);

        return mapper.toResponse(eventoAtualizado);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {

        Evento evento = findByIdOrThrow(id);

        if (evento.getDataHoraEvento().isBefore(LocalDateTime.now())) {
            throw new RegraNegocioException(
                    "Não é possível excluir um evento já realizado");
        }

        eventoRepository.delete(evento);
    }

    private Evento findByIdOrThrow(UUID id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}