package com.bibliotech.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoriaRepository<T> implements Repository<T> {

    protected List<T> almacenamiento = new ArrayList<>();
    @Override
    public void guardar(T entidad) {
        almacenamiento.add(entidad);
    }
    @Override
    public List<T> listarTodos() {
        return new ArrayList<>(almacenamiento);
    }
    @Override
    public Optional<T> buscarPorId(String id) {
        return Optional.empty();
    }
    @Override
    public void borrar (String id) {

    }
}
