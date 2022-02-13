package com.nathaliamello.wallet.api.service.impl;

import com.nathaliamello.wallet.api.model.Categoria;
import com.nathaliamello.wallet.api.repository.CategoriaRepository;
import com.nathaliamello.wallet.api.resource.CategoriaResource;
import com.nathaliamello.wallet.api.service.CategoriaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria criarCategoria(CategoriaResource categoriaResource) {
        Categoria categoria = bindCategoriaDtoParaCategoria(categoriaResource);
        return salvarCategoria(categoria);
    }

    @Override
    public Optional<Categoria> buscarPeloCodigo(Long codigo) {
        return categoriaRepository.findById(codigo);
    }

    @Override
    public void removerCategoria(Long codigo) {
        categoriaRepository.deleteById(codigo);
    }

    @Override
    public Categoria atualizarCategoria(CategoriaResource categoriaResource, Long codigo) {
        Categoria categoria = bindCategoriaDtoParaCategoria(categoriaResource);
        Categoria categoriaSalva = buscarPeloCodigo(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));
        BeanUtils.copyProperties(categoria, categoriaSalva, "codigo");
        return salvarCategoria(categoriaSalva);
    }

    private Categoria salvarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    private Categoria bindCategoriaDtoParaCategoria(CategoriaResource categoriaResource) {
        Categoria categoria = new Categoria();
        categoria.setNome(
                categoriaResource.getNome() != null ? categoriaResource.getNome() : null);
        return categoria;
    }
}
