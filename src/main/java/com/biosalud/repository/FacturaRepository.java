package com.biosalud.repository;

import com.biosalud.domain.facturacion.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Integer> {
}