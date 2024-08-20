package com.proyecto.web.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.web.modelos.usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<usuario, Long> {
}
