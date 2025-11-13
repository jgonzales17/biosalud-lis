package com.biosalud.controller;

import com.biosalud.domain.persona.Usuario;
import com.biosalud.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map; // Importante para el login simple

@RestController
@RequestMapping("/api/auth") // Usamos /api/auth para autenticación
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para REGISTRAR un nuevo usuario
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para LOGIN (¡Este es el que usarás en JMeter y Selenium!)
     * Usamos un Map para recibir "nombreUsuario" y "contrasena"
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        try {
            String nombreUsuario = credenciales.get("nombreUsuario");
            String contrasena = credenciales.get("contrasena");

            Usuario usuarioLogueado = usuarioService.login(nombreUsuario, contrasena);

            // Si el login es exitoso, devolvemos los datos del usuario
            return ResponseEntity.ok(usuarioLogueado);

        } catch (Exception e) {
            // Si el login falla (user o pass mal), devolvemos "No Autorizado"
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}