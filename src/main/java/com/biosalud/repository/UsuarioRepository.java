package com.biosalud.repository;

import com.biosalud.domain.persona.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // ¡Asegúrate de que este import esté!

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // ¡AÑADE ESTA LÍNEA!
    // Spring mágicamente creará el SQL para buscar por este campo
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

}