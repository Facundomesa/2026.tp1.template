package com.bibliotech;
import com.bibliotech.model.*;
import com.bibliotech.repository.*;
import com.bibliotech.service.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        SocioRepository socioRepo = new SocioRepositoryMemoria();
        LibroRepository libroRepo = new LibroRepositoryMemoria();
        PrestamoRepository prestamoRepo = new PrestamosRepositoryMemoria();

        SocioService socioService = new SocioService(socioRepo, prestamoRepo);
        LibroService libroService = new LibroService(libroRepo, prestamoRepo);
        PrestamoService prestamoService = new PrestamoService(prestamoRepo, socioService, libroService);

        Socio socio1 = new Socio("40123456", "Angel", "Gutierrez", "angelgutierrez@mail.com", TipoSocio.ESTUDIANTE);
        socioRepo.guardar(socio1);

        Libro libro1 = new Libro("978-1", "Clean Code", "Robert Martin", 2008, CategoriaLibro.TECNOLOGIA, 464);
        libroRepo.guardar(libro1);

        Libro libro2 = new Libro("978-2", "The Pragmatic Programmer", "Andrew Hunt", 1999, CategoriaLibro.TECNOLOGIA, 352);
        libroRepo.guardar(libro2);

        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        System.out.println("SISTEMA BIBLIOTECH");

        while (!salir) {
            System.out.println("\n1. Registrar Préstamo\n2. Registrar Devolución\n3. Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            try {
                switch (opcion) {
                    case "1":
                        System.out.print("Ingrese DNI de socio: ");
                        String dni = scanner.nextLine();
                        System.out.print("Ingrese ISBN del libro: ");
                        String isbn = scanner.nextLine();

                        Libro libroAPrestar = libroService.buscarPorIsbn(isbn);
                        prestamoService.registrarPrestamo(dni, libroAPrestar);
                        break;

                    case "2":
                        System.out.print("Ingrese el ID del préstamo a devolver: ");
                        String idPrestamo = scanner.nextLine();
                        prestamoService.registrarDevolucion(idPrestamo);
                        break;

                    case "3":
                        salir = true;
                        System.out.println("Cerrando sistema...");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }
    }
}
