package com.bibliotech.service;
import com.bibliotech.model.Libro;
import com.bibliotech.repository.LibroRepository;
import com.bibliotech.repository.PrestamoRepository;

import java.util.Optional;

public class LibroService {
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public LibroService(LibroRepository libroRepository, PrestamoRepository prestamoRepository) {
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public Libro buscarPorIsbn(String isbn) {
        Optional<Libro> resultado = libroRepository.buscarPorId(isbn);

        if (resultado.isEmpty()) {
            throw new IllegalArgumentException("Error: No se encontró ningún libro en el catálogo con el ISBN " + isbn);
        }
        return resultado.get();
    }

    public void validarDisponibilidad(String isbn) {
        Libro libro = buscarPorIsbn(isbn);
        boolean estaPrestado = prestamoRepository.listarTodos().stream()
                .anyMatch(prestamo -> prestamo.recurso().isbn().equals(isbn) && prestamo.fechaDevolucionReal() == null);

        if (estaPrestado) {
            throw new IllegalStateException("El libro '" + libro.titulo() + "' ya se encuentra prestado actualmente.");
        }

        System.out.println("El libro '" + libro.titulo() + "' está disponible para ser prestado.");
    }
}
