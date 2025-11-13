package com.biosalud.domain.persona;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPaciente;

    @Column(unique = true, nullable = false)
    private String dni;

    private String nombres;
    private String apellidos;
    private Date fechaNacimiento;
    private String email;
    private String telefono;
}