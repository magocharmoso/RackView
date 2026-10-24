package com.rackview.domain;

public class Switch extends AbstractEquipamento {

    public Switch(String name) {
        super(name, CorEquipamento.AZUL);
        validateName(name, "SW");
    }

    private static void validateName(String name, String prefix) {
        if (name == null || !name.startsWith(prefix)) {
            throw new IllegalArgumentException("Switch deve começar por SW");
        }
    }
}
