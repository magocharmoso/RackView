package com.rackview.domain;

import java.util.List;

public class DataCenter {

    private final String nome;

    private final List<Bastidor> bastidores;

    public DataCenter(String nome, List<Bastidor> bastidores) {
        this.nome = nome;
        this.bastidores = bastidores == null ? List.of() : List.copyOf(bastidores);
    }

    public String getNome() {
        return nome;
    }

    public List<Bastidor> getBastidores() {
        return bastidores;
    }
}
