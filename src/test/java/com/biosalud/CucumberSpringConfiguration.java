package com.biosalud;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Esta clase le dice a Cucumber que arranque un contexto de Spring Boot.
 * Es necesaria para que @Autowired funcione en tus clases de "Steps" (como PacienteSteps).
 */
@CucumberContextConfiguration
@SpringBootTest(classes = BiosaludApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CucumberSpringConfiguration {
    // La clase le dice a Cucumber cómo arrancar Spring.
    // webEnvironment.DEFINED_PORT se asegura que corra en el 8080 que ya conocemos.
}