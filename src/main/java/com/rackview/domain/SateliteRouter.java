package com.rackview.domain;

import java.util.Objects;

public class SateliteRouter extends AbstractEquipamento {

    private final Router pai;

    public SateliteRouter(Router pai) {
        this(pai, generateName(pai));
    }

    public SateliteRouter(Router pai, String nome) {
        super(validateAndNormalizeName(nome), CorEquipamento.AMARELO);
        this.pai = Objects.requireNonNull(pai, "O router pai não pode ser nulo");
    }

    public Router getPai() {
        return pai;
    }

    private static String generateName(Router pai) {
        String base = pai.getName();
        String clean = base.replace("-", "").replace("_", "").replace(" ", "");
        String suffix = clean.replaceFirst("^GT", "");
        return "Sat1" + suffix;
    }

    private static String validateAndNormalizeName(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O satélite precisa de um nome válido");
        }

        String normalized = nome.trim().replaceAll("\\s+", "");
        if (!normalized.matches("Sat[0-9]+[A-Za-z0-9]+")) {
            throw new IllegalArgumentException("Satélite inválido. Use o formato SatXYZ ou SatXY");
        }

        return normalized.toUpperCase();
    }
}
