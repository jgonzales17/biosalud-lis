package com.biosalud.steps;

import com.biosalud.domain.persona.Paciente;
import com.biosalud.repository.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
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

public class PacienteSteps {

    @Autowired
    private PacienteRepository pacienteRepo;
    @Autowired
    private OrdenLaboratorioRepository ordenRepo;
    @Autowired
    private ResultadoLaboratorioRepository resultadoRepo;

    // --- ¡AÑADE TODOS ESTOS QUE FALTAN! ---
    @Autowired
    private DetalleFacturaRepository detalleRepo;
    @Autowired
    private FacturaRepository facturaRepo;
    @Autowired
    private TomaMuestraRepository tomaMuestraRepo;
    @Autowired
    private MedicoRepository medicoRepo;
    @Autowired
    private TecnicoLaboratorioRepository tecnicoRepo;
    @Autowired
    private UsuarioRepository usuarioRepo;
    // ----------------------------------------

    private WebDriver driver;
    private WebDriverWait wait;

    @Before // ANTES de cada prueba
    public void setUp() {
        // Abre un navegador Chrome
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // ¡LA LIMPIEZA MAESTRA! (Hijos de hijos, Hijos, y al final Padres)

        // 1. Nietos (los que dependen de otros)
        detalleRepo.deleteAll();
        resultadoRepo.deleteAll();
        tomaMuestraRepo.deleteAll();

        // 2. Hijos (los que dependen de personas pero tienen nietos)
        facturaRepo.deleteAll();
        ordenRepo.deleteAll();

        // 3. Padres (las personas)
        pacienteRepo.deleteAll();
        medicoRepo.deleteAll();
        tecnicoRepo.deleteAll();
        usuarioRepo.deleteAll();
    }

    @Given("que ya existe un paciente con DNI {string}")
    public void queYaExisteUnPacienteConDNI(String dni) {
        // Preparamos la BD con el paciente duplicado
        Paciente p = new Paciente();
        p.setDni(dni);
        p.setNombres("Usuario");
        p.setApellidos("De Prueba (Test)");
        p.setEmail("test@correo.com");
        pacienteRepo.save(p);
    }

    @When("el usuario navega a la pagina de {string}")
    public void elUsuarioNavegaALaPaginaDe(String nombrePagina) {
        // 1. Abre tu página web
        driver.get("http://localhost:8080/");
        // 2. Busca el link por su texto y le da clic
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(nombrePagina))).click();
    }

    @And("intenta registrar un paciente nuevo con DNI {string} y nombre {string}")
    public void intentaRegistrarUnPacienteNuevoConDNIYNombre(String dni, String nombre) {
        // Espera a que los campos del formulario de paciente sean visibles
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pac-dni")));

        // Llenamos el formulario
        driver.findElement(By.id("pac-dni")).sendKeys(dni);
        driver.findElement(By.id("pac-nombres")).sendKeys(nombre);
        driver.findElement(By.id("pac-apellidos")).sendKeys("De Prueba");
        driver.findElement(By.id("pac-email")).sendKeys("prueba@correo.com");
        driver.findElement(By.id("pac-telefono")).sendKeys("999888777");

        // Damos clic al botón
        driver.findElement(By.xpath("//button[text()='Registrar Paciente']")).click();
    }

    @Then("el sistema debe mostrar el mensaje de error {string}")
    public void elSistemaDebeMostrarElMensajeDeError(String mensajeEsperado) {
        // ¡NUEVA LÓGICA! Espera a que aparezca un "Toast" de error
        // Buscamos un div que tenga la clase "toast" Y la clase "error"
        WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".toast.error")));

        // Obtenemos el texto real del Toast
        String mensajeReal = toast.getText();

        // Verificamos que el mensaje real "CONTENGA" el mensaje esperado.
        // Esto es más flexible que una comparación exacta.
        assertTrue(mensajeReal.contains(mensajeEsperado),
                "El Toast SÍ apareció, pero el texto era incorrecto. " +
                        "Esperábamos que contenga: '" + mensajeEsperado + "', pero fue: '" + mensajeReal + "'");
    }

    @After // DESPUÉS de cada prueba
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Cierra el navegador
        }
    }
}