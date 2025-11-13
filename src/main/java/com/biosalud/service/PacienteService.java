package com.biosalud.service;

import com.biosalud.domain.persona.Paciente;
import com.biosalud.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Le dice a Spring que esta es una clase de Lógica de Negocio
public class PacienteService {

    @Autowired // Inyecta el repositorio para poder usarlo
    private PacienteRepository pacienteRepo;

    /**
     * Lógica para RF-01 y HU-02: Registrar un paciente nuevo
     */
    public Paciente registrarPaciente(Paciente paciente) throws Exception {

        // Criterio de Aceptación: "El sistema debe validar que el DNI no esté duplicado"
        if(pacienteRepo.findByDni(paciente.getDni()).isPresent()) {
            // Caso de Prueba: "esperado: mensaje 'Paciente ya registrado'"
            throw new Exception("ERROR: El DNI " + paciente.getDni() + " ya existe.");
        }

        // Criterio de Aceptación: "Todos los campos obligatorios deben completarse"
        if(paciente.getNombres() == null || paciente.getNombres().isEmpty() ||
                paciente.getApellidos() == null || paciente.getApellidos().isEmpty()) {
            throw new Exception("ERROR: Nombres y Apellidos son obligatorios.");
        }

        // Si pasa todas las validaciones, lo guarda.
        return pacienteRepo.save(paciente);
    }
    public List<Paciente> listarTodos() {
        return pacienteRepo.findAll(); // 'findAll()' nos lo regala JpaRepository
    }
}