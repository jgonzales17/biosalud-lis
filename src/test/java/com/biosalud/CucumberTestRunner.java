package com.biosalud;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Esta es la clase "Runner" (Corredor) al estilo JUnit 4.
 * Es la que tu profe usa en NetBeans.
 */
@RunWith(Cucumber.class) // 1. Le dice a JUnit "Usa Cucumber para correr esto"
@CucumberOptions(
        features = "src/test/resources/features",  // 2. Dónde están tus .feature
        glue = "com.biosalud"                       // 3. Dónde buscar tus "Steps" y tu "Configuracion"
)

public class CucumberTestRunner {
    // La clase va vacía. Las anotaciones hacen todo.
}