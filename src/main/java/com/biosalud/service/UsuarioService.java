package com.biosalud.service;

import com.biosalud.domain.persona.Usuario;
import com.biosalud.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    /**
     * Lógica para registrar un nuevo usuario (Técnico, Médico, etc.)
     */
    public Usuario registrarUsuario(Usuario usuario) throws Exception {
        if (usuario.getNombreUsuario() == null || usuario.getContrasena() == null) {
            throw new Exception("El nombre de usuario y la contraseña son obligatorios.");
        }
        if (usuarioRepo.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
            throw new Exception("El nombre de usuario '" + usuario.getNombreUsuario() + "' ya existe.");
        }
        // Aquí deberíamos encriptar la contraseña, pero para el T3 lo dejamos simple
        return usuarioRepo.save(usuario);
    }

    /**
     * Lógica para el Login (¡Esto es para tu T3!)
     * Compara el usuario y contraseña con la BD.
     */
    public Usuario login(String nombreUsuario, String contrasena) throws Exception {
        // 1. Busca al usuario por su nombre
        Optional<Usuario> usuarioOpt = usuarioRepo.findByNombreUsuario(nombreUsuario);

        if (usuarioOpt.isEmpty()) {
            // Caso de prueba: Usuario no existe
            throw new Exception("Credenciales inválidas (Usuario no encontrado)");
        }

        Usuario usuario = usuarioOpt.get();

        // 2. Compara la contraseña
        if (!usuario.getContrasena().equals(contrasena)) {
            // Caso de prueba: Contraseña incorrecta
            throw new Exception("Credenciales inválidas (Contraseña incorrecta)");
        }

        // 3. ¡Éxito! Devuelve el usuario
        return usuario;
    }
}