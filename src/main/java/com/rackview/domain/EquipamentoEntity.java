package com.rackview.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "equipamentos")
public class EquipamentoEntity extends AbstractEquipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public EquipamentoEntity() {
        super("", CorEquipamento.AZUL);
    }

    public EquipamentoEntity(String nome) {
        super(nome, CorEquipamento.AZUL);
    }

    public String getNome() {
        return getName();
    }

    public void setNome(String nome) {
        setName(nome);
    }

    @Override
    public String toString() {
        return "EquipamentoEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
