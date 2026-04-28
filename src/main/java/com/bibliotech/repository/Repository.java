package com.bibliotech.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    void guardar(T entidad);
    Optional<T> buscarPorId(String id);
    List<T> listarTodos();
    void borrar (String id);
}
