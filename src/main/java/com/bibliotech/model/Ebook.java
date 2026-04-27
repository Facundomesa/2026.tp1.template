package com.bibliotech.model;

public record Ebook(
        String isbn,
        String titulo,
        String autor,
        int anio,
        CategoriaLibro categoria,
        String formato,
        double tamañoArchivoMB) implements Recurso { }
