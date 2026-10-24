package com.rackview.domain;

public class OOBM extends AbstractEquipamento {

    public OOBM(String name) {
        super(validateName(name), CorEquipamento.CINZA);
    }

    private static String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome do OOBM é obrigatório");
        }

        String normalized = name.trim().replaceAll("\\s+", "");
        if (!normalized.startsWith("OOBM") && !normalized.endsWith("OOBM")) {
            throw new IllegalArgumentException("OOBM deve começar ou terminar com OOBM");
        }

        return normalized;
    }
}
