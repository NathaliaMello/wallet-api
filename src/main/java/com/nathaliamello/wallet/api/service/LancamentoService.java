package com.nathaliamello.wallet.api.service;

import com.nathaliamello.wallet.api.model.Lancamento;
import com.nathaliamello.wallet.api.repository.filter.LancamentoFilter;
import com.nathaliamello.wallet.api.repository.projection.ResumoLancamento;
import com.nathaliamello.wallet.api.resource.LancamentoResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    List<Lancamento> listarLancamentos();
    Optional<Lancamento> buscarLancamentoPeloCodigo(Long codigo);
    Lancamento criarLancamento(LancamentoResource lancamentoResource);
    Page<Lancamento> filtrarLancamentos(LancamentoFilter lancamentoFilter, Pageable pageable);
    void deletarLancamento(Long codigo);
    Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
}
