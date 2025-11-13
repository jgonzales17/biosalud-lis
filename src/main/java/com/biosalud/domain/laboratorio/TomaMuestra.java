package com.biosalud.domain.laboratorio;

import com.biosalud.domain.persona.TecnicoLaboratorio;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "toma_muestra")
public class TomaMuestra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idToma;

    private Date fechaHora;
    private String tipoMuestra;

    // --- RELACIONES ---

    // Relación con OrdenLaboratorio: UNA TomaMuestra es para UNA Orden
    // (Tu diagrama dice 1 a 1 aquí, así que usamos @OneToOne)
    @OneToOne
    @JoinColumn(name = "idOrden")
    private OrdenLaboratorio ordenLaboratorio;

    // Relación con TecnicoLaboratorio: Muchas tomas pueden ser hechas por UN Tecnico
    @ManyToOne
    @JoinColumn(name = "idTecnico")
    private TecnicoLaboratorio tecnicoLaboratorio;
}