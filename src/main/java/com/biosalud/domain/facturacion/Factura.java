package com.biosalud.domain.facturacion;

import com.biosalud.domain.persona.Paciente;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFactura;

    private Date fechaEmision;
    private Double montoTotal;
    private String metodoPago;

    // --- RELACIÓN ---
    // Relación con Paciente: Muchas facturas pueden ser de UN Paciente
    @ManyToOne
    @JoinColumn(name = "idPaciente")
    private Paciente paciente;
}