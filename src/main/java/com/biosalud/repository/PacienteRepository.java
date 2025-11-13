package com.biosalud.repository;

import com.biosalud.domain.persona.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Le decimos: "Maneja la clase 'Paciente', cuyo ID es de tipo 'Integer'"
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    /**
     * ¡ESTA ES LA LÍNEA QUE ARREGLA EL ERROR!
     * Al escribir esto, Spring crea el método "findByDni" por nosotros.
     */
    Optional<Paciente> findByDni(String dni);
}