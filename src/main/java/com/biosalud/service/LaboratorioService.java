package com.biosalud.service;

import com.biosalud.domain.laboratorio.OrdenLaboratorio;
import com.biosalud.domain.laboratorio.ResultadoLaboratorio;
import com.biosalud.repository.OrdenLaboratorioRepository;
import com.biosalud.repository.ResultadoLaboratorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service // Marca esto como una clase de Lógica de Negocio
public class LaboratorioService {

    @Autowired
    private OrdenLaboratorioRepository ordenRepo;

    @Autowired
    private ResultadoLaboratorioRepository resultadoRepo;

    // --- Lógica para RF-02: Registrar Órdenes ---
    public OrdenLaboratorio crearOrden(OrdenLaboratorio orden) throws Exception {
        // Validaciones básicas (un Service SIEMPRE valida)
        if (orden.getPaciente() == null || orden.getMedico() == null) {
            throw new Exception("Una orden debe tener un Paciente y un Médico asignado.");
        }
        orden.setFechaOrden(new Date()); // Pone la fecha de hoy
        orden.setEntregado(false);
        return ordenRepo.save(orden);
    }

    // --- Lógica para RF-05: Registrar Resultados ---
    public ResultadoLaboratorio registrarResultado(ResultadoLaboratorio resultado) throws Exception {
        if (resultado.getOrdenLaboratorio() == null) {
            throw new Exception("El resultado debe estar asociado a una Orden.");
        }

        // Criterio de Aceptación (HU03): Resultados nuevos siempre están "pendientes" (sin validar)
        resultado.setValidado(false);
        resultado.setFechaResultado(new Date());
        return resultadoRepo.save(resultado);
    }

    // --- Lógica para RF-06 y HU03: Validar Resultados ---
    public ResultadoLaboratorio validarResultado(Integer idResultado) throws Exception {
        // 1. Buscamos el resultado en la BD
        Optional<ResultadoLaboratorio> resultadoOpt = resultadoRepo.findById(idResultado);

        if (resultadoOpt.isEmpty()) {
            throw new Exception("No se encontró el resultado con ID: " + idResultado);
        }

        ResultadoLaboratorio resultado = resultadoOpt.get();

        // 2. Aplicamos la lógica de negocio
        resultado.setValidado(true); // ¡Aquí ocurre la validación!

        // 3. Guardamos el cambio en la BD
        return resultadoRepo.save(resultado);
    }

    // --- Lógica para HU03: No editar resultados validados ---
    public ResultadoLaboratorio actualizarResultado(Integer idResultado, String nuevosValores) throws Exception {
        Optional<ResultadoLaboratorio> resultadoOpt = resultadoRepo.findById(idResultado);

        if (resultadoOpt.isEmpty()) {
            throw new Exception("No se encontró el resultado con ID: " + idResultado);
        }

        ResultadoLaboratorio resultado = resultadoOpt.get();

        // ¡¡ESTA ES LA LÓGICA CLAVE DE TU HU03!!
        if (resultado.getValidado() == true) {
            // Caso de Prueba Negativo (PC 4): "esperado: mensaje 'No se puede editar...'"
            throw new Exception("ERROR (HU03): No se puede editar un resultado que ya está VALIDADO.");
        }

        // Si no está validado, sí dejamos editar
        resultado.setValores(nuevosValores);
        return resultadoRepo.save(resultado);
    }

    // --- Lógica para LISTAR Órdenes (para el dropdown) ---
    public List<OrdenLaboratorio> listarTodasLasOrdenes() {
        return ordenRepo.findAll();
    }

    // --- Lógica para LISTAR Resultados (para la tabla) ---
    public List<ResultadoLaboratorio> listarTodosLosResultados() {
        return resultadoRepo.findAll();
    }
}