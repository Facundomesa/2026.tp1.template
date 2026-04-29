package com.bibliotech.service;
import com.bibliotech.model.Socio;
import com.bibliotech.model.Prestamo;
import com.bibliotech.repository.SocioRepository;
import com.bibliotech.repository.PrestamoRepository;
import java.util.Optional;

public class SocioService {
    private final SocioRepository SocioRepository;
    private final PrestamoRepository prestamoRepository;
    public SocioService(SocioRepository socioRepository, PrestamoRepository prestamoRepository ) {
        this.SocioRepository = socioRepository;
        this.prestamoRepository = prestamoRepository;
    }
    public Socio buscarPorDni(String dni){
        Optional<Socio> resultado = SocioRepository.buscarPorId(dni);

        if (resultado.isEmpty()){
            throw new IllegalArgumentException("Error: No existe ningun socio registrado con ese dni" + dni);
        }
        return resultado.get();
    }
    public void validarSocioPrestamo(String dni){
        Socio socio = buscarPorDni(dni);
        int limitePermitido = 3;
        long cantidadLibroActuales = prestamoRepository.listarTodos().stream()
                .filter(prestamo -> prestamo.socio().dni().equals(dni))
                .filter(prestamo -> prestamo.fechaDevolucionReal() == null)
                .count();

        if (cantidadLibroActuales >= limitePermitido){
            throw new IllegalStateException("Limite pasado: El Socio" + socio.nombre() + "ya tiene" + cantidadLibroActuales + "libros sin devolver");
        }
        System.out.println("Validación exitosa: El socio " + socio.nombre() + " puede solicitar el préstamo.");
    }
}
