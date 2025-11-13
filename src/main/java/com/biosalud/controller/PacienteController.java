package com.biosalud.controller;

import com.biosalud.domain.persona.Paciente;
import com.biosalud.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // ¡Esto lo convierte en una API! Le dice a Spring que esta clase manejará tráfico web.
@RequestMapping("/api/pacientes") // La URL base será: http://localhost:8080/api/pacientes
public class PacienteController {

    @Autowired // Inyecta el "Cerebro" (el Service que hicimos antes)
    private PacienteService pacienteService;

    /**
     * Este es tu primer ENDPOINT (la "página" que registra)
     * Se accede con POST a: http://localhost:8080/api/pacientes/registrar
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Paciente paciente) {
        // @RequestBody significa que recibirá los datos del paciente (en formato JSON) desde el frontend

        try {
            // 1. Intenta llamar al servicio para registrarlo
            Paciente pacienteGuardado = pacienteService.registrarPaciente(paciente);

            // 2. Si todo sale bien (no hay DNI duplicado, etc.),
            //    devuelve un 200 OK con el paciente que se guardó (ya con su ID)
            return ResponseEntity.ok(pacienteGuardado);

        } catch (Exception e) {
            // 3. Si el servicio lanzó un error (DNI duplicado, campos vacíos)
            //    Devuelve un 400 Bad Request con el mensaje de error (Ej: "ERROR: El DNI ya existe.")
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public List<Paciente> listarPacientes() {
        return pacienteService.listarTodos(); // ¡UPS! Esto también falta
    }

}