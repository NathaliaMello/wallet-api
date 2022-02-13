package com.nathaliamello.wallet.api.repository;

import com.nathaliamello.wallet.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Usuariorepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}
