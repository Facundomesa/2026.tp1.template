package com.bibliotech.model;

public record Socio(
        String dni,
        String nombre,
        String apellido,
        String email,
        TipoSocio tipo) { }
