package com.bibliotech.model;

public enum TipoSocio {
    ESTUDIANTE(3),
    DOCENTE(5);

    private final int limitePrestamos;

    TipoSocio(int limite) {
        this.limitePrestamos = limite;
    }
    public int getLimitePrestamos() {
        return limitePrestamos;
    }
}
