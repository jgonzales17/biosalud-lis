package com.biosalud.controller;

import com.biosalud.domain.persona.Medico;
import com.biosalud.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos") // El link será http://localhost:8080/api/medicos
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepo; // Usamos el repo directo por ahora

    // Endpoint para registrar un médico (POST)
    @PostMapping("/registrar")
    public Medico registrarMedico(@RequestBody Medico medico) {
        // Aquí iría un Service para validar que el CMP no esté duplicado,
        // pero por ahora lo guardamos directo para avanzar.
        return medicoRepo.save(medico);
    }

    // Endpoint para ver todos los médicos (GET)
    @GetMapping("/listar")
    public List<Medico> listarMedicos() {
        return medicoRepo.findAll();
    }
}