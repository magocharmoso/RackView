package com.rackview.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Bastidor {
    private final String nome;
    private final String nickname;

    private final List<Equipamento> equipamentos;

    public Bastidor(String nome, List<Equipamento> equipamentos) {
        this(nome, null, equipamentos);
    }

    public Bastidor(String nome, String nickname, List<Equipamento> equipamentos) {
        this.nome = nome;
        this.nickname = nickname;
        this.equipamentos = new ArrayList<>(equipamentos == null ? List.of() : equipamentos);
    }

    public String getNome() {
        return nome;
    }

    public String getNickname() {
        return nickname;
    }

    public List<Equipamento> getEquipamentos() {
        return Collections.unmodifiableList(equipamentos);
    }

    public Optional<Equipamento> buscarEquipamentoPorNome(String nomeEquipamento) {
        if (nomeEquipamento == null || nomeEquipamento.isBlank()) {
            return Optional.empty();
        }

        String nomeNormalizado = nomeEquipamento.trim();
        return equipamentos.stream()
                .filter(equipamento -> equipamento.getName().equalsIgnoreCase(nomeNormalizado))
                .findFirst();
    }

    public boolean contemEquipamentoComNome(String nomeEquipamento) {
        return buscarEquipamentoPorNome(nomeEquipamento).isPresent();
    }

    public void adicionarEquipamento(Equipamento equipamento) {
        if (equipamento == null) {
            throw new IllegalArgumentException("Equipamento não pode ser nulo");
        }

        if (buscarEquipamentoPorNome(equipamento.getName()).isPresent()) {
            throw new IllegalArgumentException("Adição impossível, este equipamento já existe no bastidor " + nome);
        }

        equipamentos.add(equipamento);
    }

    public void removerEquipamento(Equipamento equipamento) {
        equipamentos.remove(equipamento);
    }
}