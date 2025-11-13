package com.biosalud.controller;

import com.biosalud.controller.dto.FacturaRequest;
import com.biosalud.domain.facturacion.Factura;
import com.biosalud.service.FacturacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facturacion") // La URL base
public class FacturacionController {

    @Autowired
    private FacturacionService facturacionService;

    /**
     * Endpoint para HU05: Crear una nueva factura
     */
    @PostMapping("/crear")
    public ResponseEntity<?> crearFactura(@RequestBody FacturaRequest request) {
        try {
            // Usamos la "caja" (Request) para pasar los datos al "cerebro" (Service)
            Factura nuevaFactura = facturacionService.crearFactura(request.getFactura(), request.getDetalles());
            return ResponseEntity.ok(nuevaFactura);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}