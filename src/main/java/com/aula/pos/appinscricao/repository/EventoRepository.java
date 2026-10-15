package com.aula.pos.appinscricao.repository;

import com.aula.pos.appinscricao.model.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EventoRepository
        extends JpaRepository<Evento, UUID> {

    Page<Evento> findByNomeContainingIgnoreCase(
            String nome, Pageable pageable);

    Page<Evento> findByCidadeIgnoreCase(
            String cidade, Pageable pageable);

    Page<Evento> findByEstadoIgnoreCase(
            String estado, Pageable pageable);

    boolean existsByNomeIgnoreCaseAndDataHoraEventoAndCidadeIgnoreCase(
            String nome,
            LocalDateTime dataHoraEvento,
            String cidade);

    boolean existsByNomeIgnoreCaseAndDataHoraEventoAndCidadeIgnoreCaseAndIdNot(
            String nome,
            LocalDateTime dataHoraEvento,
            String cidade,
            UUID id);
}