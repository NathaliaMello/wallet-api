package com.nathaliamello.wallet.api.service;

import com.nathaliamello.wallet.api.model.Categoria;
import com.nathaliamello.wallet.api.resource.CategoriaResource;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    List<Categoria> listarCategorias();
    Categoria criarCategoria(CategoriaResource categoriaDto);
    Optional<Categoria> buscarPeloCodigo(Long codigo);
    void removerCategoria(Long codigo);
    Categoria atualizarCategoria(CategoriaResource categoriaResource, Long codigo);
}
