package com.biosalud.domain.persona;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tecnico_laboratorio")
public class TecnicoLaboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTecnico;

    private String nombres;
    private String apellidos;
    private String especialidad;
    private String telefono;
}