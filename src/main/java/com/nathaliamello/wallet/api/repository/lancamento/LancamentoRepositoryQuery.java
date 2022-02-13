package com.nathaliamello.wallet.api.repository.lancamento;

import com.nathaliamello.wallet.api.model.Lancamento;
import com.nathaliamello.wallet.api.repository.filter.LancamentoFilter;
import com.nathaliamello.wallet.api.repository.projection.ResumoLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LancamentoRepositoryQuery {

    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
    public Page<ResumoLancamento> resumir (LancamentoFilter lancamentoFilter, Pageable pageable);
}
