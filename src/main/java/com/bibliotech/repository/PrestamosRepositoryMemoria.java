package com.bibliotech.repository;
import com.bibliotech.model.Prestamo;
import java.util.Optional;

public class PrestamosRepositoryMemoria extends MemoriaRepository<Prestamo> implements PrestamoRepository {
    @Override
    public Optional<Prestamo> buscarPorId(String id) {
        return almacenamiento.stream()
                .filter(p -> p.id().equals(id))
                .findFirst();
    }
}
