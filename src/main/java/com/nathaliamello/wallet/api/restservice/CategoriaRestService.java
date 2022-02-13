package com.nathaliamello.wallet.api.restservice;

import com.nathaliamello.wallet.api.resource.CategoriaResource;
import com.nathaliamello.wallet.api.event.RecursoCriadoEvent;
import com.nathaliamello.wallet.api.model.Categoria;
import com.nathaliamello.wallet.api.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
public class CategoriaRestService {

    private final CategoriaService categoriaService;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public CategoriaRestService(CategoriaService categoriaService, ApplicationEventPublisher publisher) {
        this.categoriaService = categoriaService;
        this.publisher = publisher;
    }

    @CrossOrigin(maxAge = 10, origins = {"http://localhost:4200"})
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and hasAuthority('SCOPE_read')" )
    public List<Categoria> listarCategorias() {
        return categoriaService.listarCategorias();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and hasAuthority('SCOPE_write')")
    public ResponseEntity<Categoria> criarCategoria(@Valid @RequestBody CategoriaResource categoriaDto, HttpServletResponse response) {
        try {
            Categoria categoriaSalva = categoriaService.criarCategoria(categoriaDto);
            publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
        } catch (ValidationException ex) {
            throw new ValidationException();
        }
    }

    @GetMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and hasAuthority('SCOPE_read')")
    public ResponseEntity<Optional<Categoria>> buscarPeloCodigo(@PathVariable Long codigo) {
        Optional<Categoria> categoriaOptional = categoriaService.buscarPeloCodigo(codigo);
        return categoriaOptional.isPresent() ? ResponseEntity.ok(categoriaOptional) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.OK)
    public void removerCategoria(@PathVariable Long codigo) {
        categoriaService.removerCategoria(codigo);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Categoria> atualizarCategoria(@PathVariable Long codigo, @Valid @RequestBody CategoriaResource categoriaResource, HttpServletResponse response) {
        Categoria categoriaAtualizada = categoriaService.atualizarCategoria(categoriaResource, codigo);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaAtualizada.getCodigo()));
        return ResponseEntity.status(HttpStatus.OK).body(categoriaAtualizada);
    }
}
