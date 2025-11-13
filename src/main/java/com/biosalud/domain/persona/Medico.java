package com.biosalud.domain.persona;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "medico")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMedico;

    @Column(unique = true, nullable = false)
    private String cmp; // Colegio Médico del Perú

    private String nombres;
    private String apellidos;
    private String especialidad;
    private String telefono;
}