package com.bibliotech.repository;

import com.bibliotech.model.Socio;
import java.util.Optional;

public class SocioRepositoryMemoria extends MemoriaRepository<Socio> implements SocioRepository{

    @Override
    public Optional<Socio> buscarPorId(String id) {
        return almacenamiento.stream()
                .filter(socio -> socio.dni().equals(id))
                .findFirst();
    }
}
