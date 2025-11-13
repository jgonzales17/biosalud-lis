package com.biosalud.repository;

import com.biosalud.domain.laboratorio.ResultadoLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultadoLaboratorioRepository extends JpaRepository<ResultadoLaboratorio, Integer> {
    // Spring nos da gratis save(), findAll(), findById(), etc.
}