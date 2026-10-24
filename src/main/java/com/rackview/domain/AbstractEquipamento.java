package com.rackview.domain;

import java.util.Objects;

public abstract class AbstractEquipamento implements Equipamento {

    protected String name;
    protected final CorEquipamento cor;

    protected AbstractEquipamento(String name, CorEquipamento cor) {
        this.name = normalizeName(name);
        this.cor = Objects.requireNonNull(cor, "A cor não pode ser nula");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = normalizeName(name);
    }

    @Override
    public CorEquipamento getCor() {
        return cor;
    }

    @Override
    public String getColour() {
        return cor.name();
    }

    protected String normalizeName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("O nome do equipamento não pode ser vazio");
        }
        return value.trim();
    }
}
