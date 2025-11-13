package com.biosalud.controller.dto;

import com.biosalud.domain.facturacion.DetalleFactura;
import com.biosalud.domain.facturacion.Factura;
import lombok.Data;
import java.util.List;

@Data // Lombok nos crea los getters y setters
public class FacturaRequest {
    // Esta es la "caja" que recibirá los datos del frontend
    private Factura factura;
    private List<DetalleFactura> detalles;
}