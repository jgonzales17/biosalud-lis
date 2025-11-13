package com.biosalud.service;

import com.biosalud.domain.facturacion.DetalleFactura;
import com.biosalud.domain.facturacion.Factura;
import com.biosalud.repository.DetalleFacturaRepository;
import com.biosalud.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class FacturacionService {

    @Autowired
    private FacturaRepository facturaRepo;

    @Autowired
    private DetalleFacturaRepository detalleRepo;

    /**
     * Lógica para HU05: Crear una factura y calcular su total.
     */
    @Transactional // Esto asegura que si algo falla, no se guarde nada (todo o nada)
    public Factura crearFactura(Factura factura, List<DetalleFactura> detalles) throws Exception {

        // Criterio de Aceptación (HU05): "La factura solo se puede emitir si está asociada a un paciente" [cite: 946]
        if (factura.getPaciente() == null) {
            throw new Exception("La factura debe estar asociada a un paciente.");
        }
        if (detalles == null || detalles.isEmpty()) {
            throw new Exception("La factura debe tener al menos un detalle de servicio.");
        }

        // Criterio de Aceptación (HU05): "El sistema debe calcular automáticamente el monto total"
        double montoTotalCalculado = 0.0;
        for (DetalleFactura detalle : detalles) {
            // Criterio (HU05): "validar que no existan valores vacíos en el cálculo" [cite: 947]
            if (detalle.getCantidad() == null || detalle.getPrecioUnitario() == null) {
                throw new Exception("Los detalles de la factura tienen valores inválidos.");
            }
            montoTotalCalculado += detalle.getCantidad() * detalle.getPrecioUnitario();
        }

        // 1. Guardamos la Factura "padre"
        factura.setFechaEmision(new Date());
        factura.setMontoTotal(montoTotalCalculado);
        Factura facturaGuardada = facturaRepo.save(factura);

        // 2. Vinculamos los "hijos" (detalles) a la factura guardada
        for (DetalleFactura detalle : detalles) {
            detalle.setFactura(facturaGuardada);
        }

        // 3. Guardamos todos los "hijos"
        detalleRepo.saveAll(detalles);

        return facturaGuardada;
    }
}