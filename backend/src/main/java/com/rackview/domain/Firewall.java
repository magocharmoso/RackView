package com.rackview.domain;

public class Firewall extends AbstractEquipamento {

    public Firewall(String name) {
        super(name, CorEquipamento.VERMELHO);
        validateName(name, "FW");
    }

    private static void validateName(String name, String prefix) {
        if (name == null || !name.startsWith(prefix)) {
            throw new IllegalArgumentException("Firewall deve começar por FW");
        }
    }
}
