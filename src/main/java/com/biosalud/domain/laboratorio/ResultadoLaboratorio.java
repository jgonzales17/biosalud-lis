package com.biosalud.domain.laboratorio;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "resultado_laboratorio")
public class ResultadoLaboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idResultado;

    private Date fechaResultado;
    private String descripcion;
    private String valores; // Tu diagrama dice "valores" (plural), lo respetamos
    private String conclusiones;
    private Boolean validado; // Para el RF-06: "Validación de resultados"

    // --- RELACIÓN ---
    // Relación con OrdenLaboratorio: Muchos resultados pueden ser de UNA Orden
    @ManyToOne
    @JoinColumn(name = "idOrden") // Así se llama la Foreign Key en tu diagrama
    private OrdenLaboratorio ordenLaboratorio;
}