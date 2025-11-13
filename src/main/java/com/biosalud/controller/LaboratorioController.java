package com.biosalud.controller;

import com.biosalud.domain.laboratorio.OrdenLaboratorio;
import com.biosalud.domain.laboratorio.ResultadoLaboratorio;
import com.biosalud.service.LaboratorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratorio") // La URL base será: http://localhost:8080/api/laboratorio
public class LaboratorioController {

    @Autowired
    private LaboratorioService laboratorioService; // ¡Conectamos el cerebro!

    // --- Endpoint para RF-02: Crear Orden ---
    @PostMapping("/orden/crear")
    public ResponseEntity<?> crearOrden(@RequestBody OrdenLaboratorio orden) {
        try {
            OrdenLaboratorio nuevaOrden = laboratorioService.crearOrden(orden);
            return ResponseEntity.ok(nuevaOrden);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- Endpoint para RF-05: Registrar Resultado ---
    @PostMapping("/resultado/registrar")
    public ResponseEntity<?> registrarResultado(@RequestBody ResultadoLaboratorio resultado) {
        try {
            ResultadoLaboratorio nuevoResultado = laboratorioService.registrarResultado(resultado);
            return ResponseEntity.ok(nuevoResultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- Endpoint para RF-06: Validar Resultado ---
    @PostMapping("/resultado/validar/{id}")
    public ResponseEntity<?> validarResultado(@PathVariable Integer id) {
        try {
            ResultadoLaboratorio resultadoValidado = laboratorioService.validarResultado(id);
            return ResponseEntity.ok(resultadoValidado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- Endpoint para HU03: Actualizar Resultado (con validación) ---
    @PutMapping("/resultado/actualizar/{id}")
    public ResponseEntity<?> actualizarResultado(@PathVariable Integer id, @RequestBody String nuevosValores) {
        try {
            ResultadoLaboratorio resultadoActualizado = laboratorioService.actualizarResultado(id, nuevosValores);
            return ResponseEntity.ok(resultadoActualizado);
        } catch (Exception e) {
            // Aquí es donde devolvemos el error "No se puede editar un resultado que ya está VALIDADO"
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ... (Debajo de tu método actualizarResultado() ...)

    // --- Endpoint para LISTAR Órdenes (GET) ---
    @GetMapping("/orden/listar")
    public List<OrdenLaboratorio> listarOrdenes() {
        return laboratorioService.listarTodasLasOrdenes();
    }

    // --- Endpoint para LISTAR Resultados (GET) ---
    @GetMapping("/resultado/listar")
    public List<ResultadoLaboratorio> listarResultados() {
        return laboratorioService.listarTodosLosResultados();
    }
}