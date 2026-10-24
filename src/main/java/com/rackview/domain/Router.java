package com.rackview.domain;

public class Router extends AbstractEquipamento {

    public Router(String name) {
        super(name, CorEquipamento.VERDE);
        validateName(name, "GT");
    }

    private static void validateName(String name, String prefix) {
        if (name == null || !name.startsWith(prefix)) {
            throw new IllegalArgumentException("Router deve começar por GT");
        }
    }
}
