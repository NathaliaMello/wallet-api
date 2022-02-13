package com.nathaliamello.wallet.api.service.impl;

import com.nathaliamello.wallet.api.model.Categoria;
import com.nathaliamello.wallet.api.model.Lancamento;
import com.nathaliamello.wallet.api.model.Pessoa;
import com.nathaliamello.wallet.api.model.TipoLancamento;
import com.nathaliamello.wallet.api.repository.LancamentoRepository;
import com.nathaliamello.wallet.api.repository.filter.LancamentoFilter;
import com.nathaliamello.wallet.api.repository.projection.ResumoLancamento;
import com.nathaliamello.wallet.api.resource.LancamentoResource;
import com.nathaliamello.wallet.api.service.LancamentoService;
import com.nathaliamello.wallet.api.service.PessoaService;
import com.nathaliamello.wallet.api.service.exception.PessoaInexistenteOuInativaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    private LancamentoRepository lancamentoRepository;

    private PessoaService pessoaService;

    @Autowired
    public LancamentoServiceImpl(LancamentoRepository lancamentoRepository, PessoaService pessoaService) {
        this.lancamentoRepository = lancamentoRepository;
        this.pessoaService = pessoaService;
    }

    @Override
    public List<Lancamento> listarLancamentos() {
        return lancamentoRepository.findAll();
    }

    @Override
    public Optional<Lancamento> buscarLancamentoPeloCodigo(Long codigo) {
        return lancamentoRepository.findById(codigo);
    }

    @Override
    public Lancamento criarLancamento(LancamentoResource lancamentoResource) {
        Lancamento lancamento = bindLancamentoResourceParaLancamento(lancamentoResource);
        Pessoa pessoa = pessoaService.buscarPessoaPeloCodigo(lancamentoResource.getPessoa().getCodigo()).orElseThrow(PessoaInexistenteOuInativaException::new);
        if(pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }
        return salvarLancamento(lancamento);
    }

    @Override
    public Page<Lancamento> filtrarLancamentos(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoRepository.filtrar(lancamentoFilter, pageable);
    }

    @Override
    public void deletarLancamento(Long codigo) {
        lancamentoRepository.deleteById(codigo);
    }

    @Override
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoRepository.resumir(lancamentoFilter, pageable);
    }

    private Lancamento salvarLancamento(Lancamento lancamento) {
        return lancamentoRepository.save(lancamento);
    }

    private Lancamento bindLancamentoResourceParaLancamento(LancamentoResource lancamentoResource) {
        Lancamento lancamento = new Lancamento();
        Pessoa pessoa = new Pessoa();
        Categoria categoria = new Categoria();
        lancamento.setCodigo(lancamentoResource.getCodigo() != null ? lancamentoResource.getCodigo() : null);
        lancamento.setDescricao(lancamentoResource.getDescricao() != null ? lancamentoResource.getDescricao() : null);
        lancamento.setDataPagamento(lancamentoResource.getDataPagamento() != null ? lancamentoResource.getDataPagamento() : null);
        lancamento.setDataVencimento(lancamentoResource.getDataVencimento() != null ? lancamentoResource.getDataVencimento() : null);
        lancamento.setValor(lancamentoResource.getValor() != null ? lancamentoResource.getValor() : null);
        lancamento.setObservacao(lancamentoResource.getObservacao() != null ? lancamentoResource.getObservacao() : null);
        lancamento.setTipo(bindLancamentoResourceParaTipo(lancamentoResource));
        lancamento.setPessoa(pessoa);
        lancamento.getPessoa().setCodigo(lancamentoResource.getPessoa().getCodigo());
        lancamento.setCategoria(categoria);
        lancamento.getCategoria().setCodigo(lancamentoResource.getCategoria().getCodigo());

        return lancamento;
    }

    private TipoLancamento bindLancamentoResourceParaTipo(LancamentoResource lancamentoResource) {
        return lancamentoResource.getTipo() == TipoLancamento.RECEITA ? TipoLancamento.RECEITA : TipoLancamento.DESPESA;
    }
}
