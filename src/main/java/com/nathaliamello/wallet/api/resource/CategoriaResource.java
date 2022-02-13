package com.nathaliamello.wallet.api.resource;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategoriaResource {

    @NotNull
    @Size(min = 3, max = 20)
    private String nome;

    public String getNome() {
        return nome;
    }
}
