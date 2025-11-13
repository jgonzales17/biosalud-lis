package com.biosalud.repository;

import com.biosalud.domain.laboratorio.OrdenLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenLaboratorioRepository extends JpaRepository<OrdenLaboratorio, Integer> {
    // Spring nos da gratis save(), findAll(), findById(), etc.
}