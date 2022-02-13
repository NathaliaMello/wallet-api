package com.nathaliamello.wallet.api.service;

import com.nathaliamello.wallet.api.resource.PessoaResource;
import com.nathaliamello.wallet.api.model.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaService {

    Pessoa criarPessoa(PessoaResource pessoaResource);
    List<Pessoa> listarPessoas();
    Optional<Pessoa> buscarPessoaPeloCodigo(Long codigo);
    void removerPessoaPeloCodigo(Long codigo);
    Pessoa atualizar(PessoaResource pessoaResource, Long codigo);
    Pessoa atualizarPropriedadeAtivo(Long codigo, Boolean ativo);
}
