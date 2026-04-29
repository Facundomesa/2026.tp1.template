package com.bibliotech.service;

import com.bibliotech.model.Libro;
import com.bibliotech.model.Prestamo;
import com.bibliotech.model.Recurso;
import com.bibliotech.model.Socio;
import com.bibliotech.repository.PrestamoRepository;
import java.time.LocalDate;
import java.util.UUID;

public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final SocioService socioService;
    private final LibroService libroService;

    public PrestamoService(PrestamoRepository prestamoRepository, SocioService socioService, LibroService libroService) {
        this.prestamoRepository = prestamoRepository;
        this.socioService = socioService;
        this.libroService = libroService;
    }

    public void registrarPrestamo(String dniSocio, Recurso recurso) {

        socioService.validarSocioPrestamo(dniSocio);

        if (recurso instanceof Libro) {
            libroService.validarDisponibilidad(recurso.isbn());
        }

        Socio socio = socioService.buscarPorDni(dniSocio);

        String id = UUID.randomUUID().toString().substring(0, 8);
        LocalDate fechaSalida = LocalDate.now();
        LocalDate fechaDevolucionPrevista = fechaSalida.plusDays(7);

        Prestamo nuevoPrestamo = new Prestamo(
                id,
                socio,
                recurso,
                fechaSalida,
                fechaDevolucionPrevista,
                null
        );

        prestamoRepository.guardar(nuevoPrestamo);

        System.out.println("Éxito Préstamo registrado.");
        System.out.println("El socio" + socio.nombre() + " se llevó el recurso" + recurso.titulo() + ".");
        System.out.println("Debe devolverlo antes del: " + fechaDevolucionPrevista);
    }

    public void registrarDevolucion(String idPrestamo) {
        Prestamo original = prestamoRepository.buscarPorId(idPrestamo)
                .orElseThrow(() -> new IllegalArgumentException("No existe el préstamo ID: " + idPrestamo));

        if (original.fechaDevolucionReal() != null) {
            throw new IllegalStateException("Este préstamo ya fue devuelto.");
        }

        LocalDate hoy = LocalDate.now();
        Prestamo actualizado = new Prestamo(
                original.id(),
                original.socio(),
                original.recurso(),
                original.fechaSalida(),
                original.fechaDevolucionPrevista(),
                hoy
        );

        prestamoRepository.borrar(idPrestamo);
        prestamoRepository.guardar(actualizado);

        System.out.println("Devolución registrada correctamente.");

        if (hoy.isAfter(original.fechaDevolucionPrevista())) {
            System.out.println("Devolución fuera de término, Aplicar penalidad.");
        }
    }
}

