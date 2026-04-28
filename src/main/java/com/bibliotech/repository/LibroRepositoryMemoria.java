package com.bibliotech.repository;
import com.bibliotech.model.Libro;
import java.util.Optional;

public class LibroRepositoryMemoria extends MemoriaRepository<Libro> implements LibroRepository {
    @Override
    public Optional<Libro> buscarPorId(String id) {
        return almacenamiento.stream()
                .filter(l -> l.isbn().equals(id))
                .findFirst();
    }
}
