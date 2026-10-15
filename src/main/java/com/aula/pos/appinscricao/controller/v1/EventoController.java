package com.aula.pos.appinscricao.controller.v1;

import com.aula.pos.appinscricao.dto.ErrorResponse;
import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import com.aula.pos.appinscricao.exception.ApiException;
import com.aula.pos.appinscricao.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    // Listar eventos com paginação e filtros
    @GetMapping
    public Page<EventoResponse> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @PageableDefault(
                    size = 10,
                    sort = "dataHoraEvento",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {

        return eventoService.findAll(nome, cidade, estado, pageable);
    }

    // Buscar evento pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(eventoService.findById(id));
        } catch (ApiException e) {
            return error(e);
        }
    }

    // Cadastrar evento
    @PostMapping
    public ResponseEntity<?> save(
            @Valid @RequestBody EventoRequest request) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(eventoService.save(request));
        } catch (ApiException e) {
            return error(e);
        }
    }

    // Atualizar evento
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @Valid @RequestBody EventoRequest request) {
        try {
            return ResponseEntity.ok(eventoService.update(id, request));
        } catch (ApiException e) {
            return error(e);
        }
    }

    // Excluir evento
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        try {
            eventoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ApiException e) {
            return error(e);
        }
    }

    // Montar resposta padronizada de erro
    private ResponseEntity<ErrorResponse> error(ApiException e) {
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                e.getStatus().value(),
                e.getMessage()
        );

        return ResponseEntity.status(e.getStatus()).body(erro);
    }
}