package com.bibliotech.model;

public record Libro(
        String isbn,
        String titulo,
        String autor,
        int anio,
        CategoriaLibro categoria,
        int cantidadPaginas) implements Recurso { }
