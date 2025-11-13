# Esta es la prueba que pide el T3 y T2
Feature: Login de Usuario en BioSalud LIS

  Scenario: Login exitoso
    Given el usuario esta en la pagina de login
    When ingresa usuario "admin" y contrasena "admin"
    Then el sistema lo redirige al "Dashboard"

  Scenario: Login fallido (contrasena incorrecta)
    Given el usuario esta en la pagina de login
    When ingresa usuario "admin" y contrasena "0000"
    Then el sistema muestra el mensaje de error "Credenciales inválidas"