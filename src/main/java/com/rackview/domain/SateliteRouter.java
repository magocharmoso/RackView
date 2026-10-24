package com.rackview.domain;

import java.util.Objects;

public class SateliteRouter extends AbstractEquipamento {

    private final Router pai;

    public SateliteRouter(Router pai) {
        super(generateName(pai), CorEquipamento.AMARELO);
        this.pai = Objects.requireNonNull(pai, "O router pai não pode ser nulo");
        validateName(this.name, "Sat");
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

    private static void validateName(String name, String prefix) {
        if (name == null || !name.startsWith(prefix)) {
            throw new IllegalArgumentException("Satelite deve começar por Sat");
        }
    }
}
