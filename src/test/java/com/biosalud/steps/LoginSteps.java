package com.biosalud.steps;

import com.biosalud.repository.UsuarioRepository; // ¡Importante!
import com.biosalud.domain.persona.Usuario; // ¡Importante!

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSteps {

    @Autowired
    private UsuarioRepository usuarioRepo; // Para crear el admin de prueba

    private WebDriver driver;
    private WebDriverWait wait;

    @Before // ANTES de cada escenario
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Limpiamos la BD de usuarios
        usuarioRepo.deleteAll();

        // ¡Creamos el usuario 'admin' para que la prueba de login pueda funcionar!
        Usuario admin = new Usuario();
        admin.setNombreUsuario("admin");
        admin.setContrasena("admin"); // La contraseña simple que pusimos
        admin.setRol("ADMIN");
        usuarioRepo.save(admin);
    }

    @Given("el usuario esta en la pagina de login")
    public void elUsuarioEstaEnLaPaginaDeLogin() {
        driver.get("http://localhost:8080/login.html");
    }

    @When("ingresa usuario {string} y contrasena {string}")
    public void ingresaUsuarioYContrasena(String usuario, String contrasena) {
        // Espera a que los campos sean visibles
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-user")));

        // Llena el formulario
        driver.findElement(By.id("login-user")).sendKeys(usuario);
        driver.findElement(By.id("login-pass")).sendKeys(contrasena);

        // Clic en el botón
        driver.findElement(By.xpath("//button[text()='Ingresar']")).click();
    }

    @Then("el sistema lo redirige al {string}")
    public void elSistemaLoRedirigeAl(String pagina) {
        // Espera a que la URL cambie y contenga "index.html"
        wait.until(ExpectedConditions.urlContains("/index.html"));

        // Como verificación extra, busca el título del Dashboard
        WebElement titulo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[text()='Bienvenido al Sistema de Gestión de Laboratorio']")));
        assertTrue(titulo.isDisplayed());
    }

    @Then("el sistema muestra el mensaje de error {string}")
    public void elSistemaMuestraElMensajeDeError(String mensajeEsperado) {
        // Espera a que aparezca el Toast de error
        WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".toast.error")));

        // Verifica que el texto sea el correcto
        // (El JS de login() puede devolver "Credenciales inválidas (Usuario no encontrado)" o "(Contraseña incorrecta)")
        assertTrue(toast.getText().contains(mensajeEsperado),
                "Se esperaba que el Toast contenga '" + mensajeEsperado + "', pero decía '" + toast.getText() + "'");
    }

    @After // DESPUÉS de cada escenario
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Cierra el navegador
        }
    }
}