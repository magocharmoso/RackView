package com.rackview.dto;

import jakarta.validation.constraints.NotBlank;

public class EquipamentoRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    public EquipamentoRequestDTO() {
    }

    public EquipamentoRequestDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
