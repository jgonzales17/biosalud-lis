package com.biosalud.domain.facturacion;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "detalle_factura")
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle;

    private String descripcionServicio;
    private Integer cantidad;
    private Double precioUnitario;

    // --- RELACIÓN ---
    // Relación con Factura: Muchos detalles pertenecen a UNA Factura
    @ManyToOne
    @JoinColumn(name = "idFactura")
    private Factura factura;
}