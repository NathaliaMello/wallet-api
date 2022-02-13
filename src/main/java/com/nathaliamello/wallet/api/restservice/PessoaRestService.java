package com.nathaliamello.wallet.api.restservice;

import com.nathaliamello.wallet.api.event.RecursoCriadoEvent;
import com.nathaliamello.wallet.api.model.Pessoa;
import com.nathaliamello.wallet.api.resource.PessoaResource;
import com.nathaliamello.wallet.api.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pessoas")
public class PessoaRestService {

    private final PessoaService pessoaService;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public PessoaRestService(PessoaService pessoaService, ApplicationEventPublisher publisher) {
        this.pessoaService = pessoaService;
        this.publisher = publisher;
    }

    @GetMapping
    public List<Pessoa> listarPessoas() {
        return pessoaService.listarPessoas();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and hasAuthority('SCOPE_write')")
    public ResponseEntity<Pessoa> criarPessoa(@Valid @RequestBody PessoaResource pessoaResource, HttpServletResponse response) {
        Pessoa pessoaSalva = pessoaService.criarPessoa(pessoaResource);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and hasAuthority('SCOPE_read')")
    public ResponseEntity<Pessoa> buscarPessoaPeloCodigo(@PathVariable Long codigo) {
        Optional<Pessoa> pessoaOptional = pessoaService.buscarPessoaPeloCodigo(codigo);
        return pessoaOptional.isPresent() ? ResponseEntity.ok(pessoaOptional.get()) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and hasAuthority('SCOPE_write')")
    public void remover(@PathVariable Long codigo) {
        pessoaService.removerPessoaPeloCodigo(codigo);
    }

    @PutMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and hasAuthority('SCOPE_write')")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody PessoaResource pessoaResource, HttpServletResponse response) {
        Pessoa pessoaAtualizada = pessoaService.atualizar(pessoaResource, codigo);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaAtualizada.getCodigo()));
        return ResponseEntity.status(HttpStatus.OK).body(pessoaAtualizada);
    }

    @PutMapping("/{codigo}/ativo")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and hasAuthority('SCOPE_write')")
    public ResponseEntity<Pessoa> atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo, HttpServletResponse response) {
        Pessoa pessoaAtualizada = pessoaService.atualizarPropriedadeAtivo(codigo, ativo);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaAtualizada.getCodigo()));
        return ResponseEntity.status(HttpStatus.OK).body(pessoaAtualizada);
    }
}
