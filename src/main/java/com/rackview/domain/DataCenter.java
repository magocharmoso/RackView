package com.rackview.domain;

import java.util.List;
import java.util.Objects;

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

    public Bastidor encontrarBastidorComEquipamento(String nomeEquipamento) {
        Objects.requireNonNull(nomeEquipamento, "O nome do equipamento não pode ser nulo");

        return bastidores.stream()
                .filter(bastidor -> bastidor.contemEquipamentoComNome(nomeEquipamento))
                .findFirst()
                .orElse(null);
    }

    public void adicionarEquipamentoNoBastidor(String nomeBastidor, Equipamento equipamento) {
        Bastidor bastidor = bastidores.stream()
                .filter(b -> b.getNome().equalsIgnoreCase(nomeBastidor))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Bastidor não encontrado: " + nomeBastidor));

        if (bastidor.contemEquipamentoComNome(equipamento.getName())) {
            throw new IllegalArgumentException("Adição impossivel, este equipamento já existe no bastidor " + bastidor.getNome());
        }

        bastidor.adicionarEquipamento(equipamento);
    }
}
