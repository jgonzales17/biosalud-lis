# Esto viene de tu PC 4 - HU02
Feature: Registro de Pacientes en BioSalud LIS

  Scenario: (Prueba Negativa) Registrar un paciente con un DNI duplicado
    Given que ya existe un paciente con DNI "45646545"
    When el usuario navega a la pagina de "Gestión de Pacientes"
    And intenta registrar un paciente nuevo con DNI "45646545" y nombre "Paciente Duplicado Test"
    Then el sistema debe mostrar el mensaje de error "El DNI 45646545 ya existe."