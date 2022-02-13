package com.nathaliamello.wallet.api.repository;

import com.nathaliamello.wallet.api.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
