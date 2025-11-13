package com.biosalud.repository;

import com.biosalud.domain.persona.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring nos da gratis save(), findAll(), findById(), etc.
public interface MedicoRepository extends JpaRepository<Medico, Integer> {

}