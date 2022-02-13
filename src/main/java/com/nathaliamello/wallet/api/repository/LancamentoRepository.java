package com.nathaliamello.wallet.api.repository;

import com.nathaliamello.wallet.api.model.Lancamento;
import com.nathaliamello.wallet.api.repository.lancamento.LancamentoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {
}
