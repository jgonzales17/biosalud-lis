package com.biosalud.domain.laboratorio;

import com.biosalud.domain.persona.Medico;
import com.biosalud.domain.persona.Paciente;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "orden_laboratorio")
public class OrdenLaboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOrden;

    private Date fechaOrden;
    private String tipoExamen;
    private String observaciones;
    private Boolean entregado; // Vemos esto en tu diagrama

    // --- RELACIONES (LO MÁS IMPORTANTE) ---

    // Relación con Paciente: Muchas órdenes pueden ser de UN Paciente
    @ManyToOne
    @JoinColumn(name = "idPaciente") // Así se llama la Foreign Key en tu diagrama
    private Paciente paciente;

    // Relación con Medico: Muchas órdenes pueden ser de UN Médico
    @ManyToOne
    @JoinColumn(name = "idMedico") // Así se llama la Foreign Key en tu diagrama
    private Medico medico;
}