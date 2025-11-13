package com.biosalud.service;

import com.biosalud.domain.laboratorio.ResultadoLaboratorio;
import com.biosalud.repository.ResultadoLaboratorioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*; // Importa las aserciones
import static org.mockito.Mockito.*; // Importa los mocks

@ExtendWith(MockitoExtension.class) // Activa Mockito
class LaboratorioServiceTest {

    @Mock // Crea un "simulador" de tu repositorio
    private ResultadoLaboratorioRepository resultadoRepo;

    @InjectMocks // Crea una instancia real de tu Service e inyéctale los "simuladores"
    private LaboratorioService laboratorioService;

    // --- PRUEBA CASO NEGATIVO (HU03) ---
    @Test
    void testActualizarResultado_CUANDO_YA_ESTA_VALIDADO_DEBE_LANZAR_ERROR() {
        // 1. PREPARACIÓN (Arrange)
        // Creamos un resultado falso que SIMULA estar "ya validado"
        ResultadoLaboratorio resultadoValidado = new ResultadoLaboratorio();
        resultadoValidado.setIdResultado(1);
        resultadoValidado.setValidado(true); // ¡Importante!
        resultadoValidado.setValores("Glucosa: 110");

        // Le decimos al simulador (Mock) qué debe responder:
        // "Cuando alguien busque el ID 1, devuelve el resultadoValidado"
        when(resultadoRepo.findById(1)).thenReturn(Optional.of(resultadoValidado));

        // 2. ACCIÓN Y VERIFICACIÓN (Act & Assert)
        // Verificamos que el código lance la Excepción que esperamos
        Exception exception = assertThrows(Exception.class, () -> {
            // Intentamos actualizar el resultado
            laboratorioService.actualizarResultado(1, "Glucosa: 125");
        });

        // Verificamos que el mensaje de error sea el correcto (de tu HU03)
        String mensajeEsperado = "ERROR (HU03): No se puede editar un resultado que ya está VALIDADO.";
        String mensajeReal = exception.getMessage();

        assertEquals(mensajeEsperado, mensajeReal);
    }

    // --- PRUEBA CASO POSITIVO (HU03) ---
    @Test
    void testActualizarResultado_CUANDO_NO_ESTA_VALIDADO_DEBE_GUARDAR() throws Exception {
        // 1. PREPARACIÓN (Arrange)
        // Creamos un resultado falso que SIMULA estar "pendiente"
        ResultadoLaboratorio resultadoPendiente = new ResultadoLaboratorio();
        resultadoPendiente.setIdResultado(2);
        resultadoPendiente.setValidado(false); // ¡Importante!
        resultadoPendiente.setValores("Glucosa: 90");

        // Le decimos al simulador (Mock) qué debe responder:
        when(resultadoRepo.findById(2)).thenReturn(Optional.of(resultadoPendiente));
        // Le decimos al simulador que cuando guarde, devuelva el mismo objeto
        when(resultadoRepo.save(any(ResultadoLaboratorio.class))).then(invocation -> invocation.getArgument(0));

        // 2. ACCIÓN (Act)
        // Intentamos actualizar el resultado
        ResultadoLaboratorio resultadoActualizado = laboratorioService.actualizarResultado(2, "Glucosa: 95");

        // 3. VERIFICACIÓN (Assert)
        // Verificamos que el valor se haya actualizado correctamente
        assertNotNull(resultadoActualizado);
        assertEquals("Glucosa: 95", resultadoActualizado.getValores());
    }
}