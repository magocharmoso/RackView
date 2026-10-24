package com.rackview.dto;

public class EquipamentoResponseDTO {

    private String nome;

    public EquipamentoResponseDTO() {
    }

    public EquipamentoResponseDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
