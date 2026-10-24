package com.rackview.domain;

public class ODF extends AbstractEquipamento {

    public ODF(String name) {
        super(validateName(name), CorEquipamento.CINZA);
    }

    private static String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome do ODF é obrigatório");
        }

        String normalized = name.trim().replaceAll("\\s+", "");
        if (!normalized.toUpperCase().startsWith("ODF")) {
            throw new IllegalArgumentException("ODF deve começar por ODF");
        }

        return normalized.toUpperCase();
    }
}
