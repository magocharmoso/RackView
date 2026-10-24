package com.rackview.domain;

import java.util.Locale;

public final class EquipamentoFactory {

    private EquipamentoFactory() {
    }

    public static Equipamento criar(String nome, Router pai) {
        String nomeSanitizado = sanitizar(nome);

        if (pai != null && nomeSanitizado.startsWith("Sat")) {
            return new SateliteRouter(pai, nomeSanitizado);
        }

        if (nomeSanitizado.startsWith("GT")) {
            return new Router(nomeSanitizado);
        }

        if (nomeSanitizado.startsWith("SW")) {
            return new Switch(nomeSanitizado);
        }

        if (nomeSanitizado.startsWith("FW")) {
            return new Firewall(nomeSanitizado);
        }

        if (nomeSanitizado.startsWith("SRV")) {
            return new Servidor(nomeSanitizado);
        }

        return new Servidor(nomeSanitizado);
    }

    public static String sanitizar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do equipamento é obrigatório");
        }

        String semEspacos = nome.trim().replaceAll("\\s+", "");

        if (semEspacos.isEmpty()) {
            throw new IllegalArgumentException("O nome do equipamento não pode ficar vazio");
        }

        if (!semEspacos.startsWith("SRV")) {
            return semEspacos.toUpperCase(Locale.ROOT);
        }

        return semEspacos;
    }
}
