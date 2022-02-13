package com.nathaliamello.wallet.api.restservice;

import com.nathaliamello.wallet.api.event.RecursoCriadoEvent;
import com.nathaliamello.wallet.api.exceptionhandler.WalletExceptionHandler;
import com.nathaliamello.wallet.api.model.Lancamento;
import com.nathaliamello.wallet.api.repository.filter.LancamentoFilter;
import com.nathaliamello.wallet.api.repository.projection.ResumoLancamento;
import com.nathaliamello.wallet.api.resource.LancamentoResource;
import com.nathaliamello.wallet.api.service.LancamentoService;
import com.nathaliamello.wallet.api.service.exception.PessoaInexistenteOuInativaException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoRestService {

    private final LancamentoService lancamentoService;
    private final ApplicationEventPublisher publisher;
    private final MessageSource messageSource;

    @Autowired
    public LancamentoRestService(LancamentoService lancamentoService, ApplicationEventPublisher publisher, MessageSource messageSource) {
        this.lancamentoService = lancamentoService;
        this.publisher = publisher;
        this.messageSource = messageSource;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
    public Page<Lancamento> pesquisarLancamentos(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoService.filtrarLancamentos(lancamentoFilter, pageable);
    }

    @GetMapping(params = "resumo")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoService.resumir(lancamentoFilter, pageable);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
    public ResponseEntity<Lancamento> buscarLancamentoPeloCodigo(@PathVariable Long codigo) {
        Optional<Lancamento> lancamentoOptional = lancamentoService.buscarLancamentoPeloCodigo(codigo);
        return lancamentoOptional.isPresent() ? ResponseEntity.ok(lancamentoOptional.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and hasAuthority('SCOPE_write')")
    public ResponseEntity<Lancamento> criarLancamento(@Valid @RequestBody LancamentoResource lancamentoResource, HttpServletResponse response) {
        Lancamento lancamentoSalvo = lancamentoService.criarLancamento(lancamentoResource);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and hasAuthority('SCOPE_write')")
    public void deletarLancamento(@PathVariable Long codigo) {
        lancamentoService.deletarLancamento(codigo);
    }

    @ExceptionHandler({PessoaInexistenteOuInativaException.class})
    private ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
        String mensagemUsuario = messageSource.getMessage("pessoa.inexisten.ou.inativa", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
        List<WalletExceptionHandler.Erro> erros = Arrays.asList(new WalletExceptionHandler.Erro(mensagemUsuario, mensagemDesenvolvedor));
        return ResponseEntity.badRequest().body(erros);
    }
}
