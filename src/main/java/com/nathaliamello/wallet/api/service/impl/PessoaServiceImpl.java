package com.nathaliamello.wallet.api.service.impl;

import com.nathaliamello.wallet.api.model.Endereco;
import com.nathaliamello.wallet.api.model.Pessoa;
import com.nathaliamello.wallet.api.repository.PessoaRepository;
import com.nathaliamello.wallet.api.resource.PessoaResource;
import com.nathaliamello.wallet.api.service.PessoaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaServiceImpl implements PessoaService {

    private PessoaRepository pessoaRepository;

    @Autowired
    public PessoaServiceImpl(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    public Pessoa criarPessoa (PessoaResource pessoaResource) {
        Pessoa pessoa = bindPessoaResourceParaPessoa(pessoaResource);
        return salvarPessoa(pessoa);
    }

    @Override
    public List<Pessoa> listarPessoas() {
        return pessoaRepository.findAll();
    }

    @Override
    public Optional<Pessoa> buscarPessoaPeloCodigo(Long codigo) {
        return pessoaRepository.findById(codigo);
    }

    @Override
    public void removerPessoaPeloCodigo(Long codigo) {
        pessoaRepository.deleteById(codigo);
    }

    @Override
    public Pessoa atualizar(PessoaResource pessoaResource, Long codigo) {
        Pessoa pessoa = bindPessoaResourceParaPessoa(pessoaResource);
        Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));
        BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
        return salvarPessoa(pessoaSalva);
    }

    @Override
    public Pessoa atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
        Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));
        pessoaSalva.setAtivo(ativo);
        return salvarPessoa(pessoaSalva);
    }

    private Pessoa bindPessoaResourceParaPessoa(PessoaResource pessoaResource) {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(
                pessoaResource.getCodigo() != null ? pessoaResource.getCodigo() : null);
        pessoa.setNome(
                pessoaResource.getNome() != null ? pessoaResource.getNome() : null);
        pessoa.setEmail(
                pessoaResource.getEmail() != null ? pessoaResource.getEmail() : null);
        pessoa.setAtivo(
                pessoaResource.getAtivo() != null ? pessoaResource.getAtivo() : null);
        pessoa.setEndereco(bindPessoaResourceParaEndereco(pessoaResource));
        return pessoa;
    }

    private Endereco bindPessoaResourceParaEndereco(PessoaResource pessoaResource) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(
                pessoaResource.getLogradouro() != null ? pessoaResource.getLogradouro() : null );
        endereco.setNumero(
                pessoaResource.getNumero() != null ? pessoaResource.getNumero() : null);
        endereco.setComplemento(
                pessoaResource.getComplemento() != null ? pessoaResource.getComplemento() : null);
        endereco.setBairro(
                pessoaResource.getBairro() != null ? pessoaResource.getBairro() : null);
        endereco.setCep(
                pessoaResource.getCep() != null ? pessoaResource.getCep() : null);
        endereco.setCidade(
                pessoaResource.getCidade() != null ? pessoaResource.getCidade() : null);
        endereco.setEstado(
                pessoaResource.getEstado() != null ? pessoaResource.getEstado() : null);

        return endereco;
    }

    private Pessoa salvarPessoa(Pessoa pessoa) throws ValidationException {
        return pessoaRepository.save(pessoa);
    }

}
