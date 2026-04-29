# Documentación de Diseño - Proyecto Bibliotech

## Decisiones Técnicas
- **Records**: Se utilizaron para las entidades (`Libro`, `Socio`, etc.) para asegurar la **inmutabilidad** (datos que no se pueden mutar nunca más) de los datos y reducir el **código boilerplate** (código repetitivo como getters y setters).
- **Interfaces**: La interfaz `Recurso` permite que el sistema sea extensible (**OCP**), facilitando la adición de nuevos tipos de materiales (como Revistas o Películas) en el futuro sin modificar el core.
- **Enums**: Se encapsuló la lógica de límites de préstamos en `TipoSocio` para centralizar las reglas de negocio y evitar "números mágicos" en el código.
- **Patrón Repository**: Implementación de una arquitectura que desacopla la lógica de negocio del mecanismo de almacenamiento físico.
- **Interfaces Genéricas**: Se utilizaron tipos genéricos para estandarizar el comportamiento de todos los repositorios del sistema (`Repository<T>`).
- **Manejo de Optional**: Integración de `Optional<T>` en las búsquedas para gestionar de forma robusta la ausencia de datos y evitar el clásico `NullPointerException`.
- **Lógica de Servicios**:
    - **SocioService**: Valida estados de habilitación y realiza el cálculo dinámico de préstamos activos para controlar límites.
    - **LibroService**: Administra el catálogo y valida disponibilidad física cruzando datos en tiempo real con préstamos.
    - **PrestamoService**: Orquestador central. Gestiona la creación de transacciones, genera IDs únicos (**UUID**) y controla fechas de devolución.
- **Cálculo Dinámico de Estados**: Se optó por **no guardar contadores manuales** en los modelos. El estado se calcula en tiempo real desde el historial de transacciones, garantizando la integridad de la información y evitando inconsistencias.

---

## Análisis Profundo de Decisiones

### **Uso de Records**
* **¿Por qué?**: Los Records son contenedores de datos inmutables por defecto. Al ser un sistema bibliotecario, necesitamos que la información de un libro o un socio no cambie aleatoriamente durante la ejecución de una transacción.
* **¿Para qué?**: Para eliminar el código redundante y garantizar que la entidad que sale del repositorio llegue intacta al servicio, evitando errores de estado inconsistente.

### **Interfaz `Recurso` y Polimorfismo**
* **¿Por qué?**: Aplicamos el principio de Inversión de Dependencias. El sistema de préstamos no debe "saber" si está prestando un objeto físico o digital, solo debe saber que es algo "prestable".
* **¿Para qué?**: Para que el sistema sea **extensible**. Hoy prestamos `Libro` y `Ebook`, pero mañana podemos agregar nuevos tipos simplemente implementando la interfaz, sin tocar el `PrestamoService`.

### **Inyección de Dependencias (DI)**
* **¿Por qué?**: Los servicios reciben sus repositorios a través del constructor (`private final`). Esto desacopla la creación del objeto de su uso.
* **¿Para qué?**: Para facilitar el **mantenimiento**. Si el día de mañana cambiamos de una lista en memoria a una base de datos SQL, solo cambiamos la instanciación en el `Main` y los servicios siguen funcionando igual.

### **Cálculo Dinámico de Estados (No redundancia)**
* **¿Por qué?**: Decidimos no guardar un contador de `librosPrestados` dentro del Record `Socio`. En su lugar, el servicio cuenta los registros activos en el `PrestamoRepository`.
* **¿Para qué?**: Para garantizar la **Integridad de los Datos**. Esto evita anomalías: si un préstamo se borra o se devuelve, el contador siempre será exacto porque se calcula desde la fuente de la verdad (las transacciones).

---

## Principios SOLID Aplicados
- **S (Single Responsibility)**: Cada clase tiene una única razón para existir. Los Repositorios se encargan exclusivamente del almacenamiento de datos y los Servicios gestionan únicamente la lógica y reglas de negocio.
- **O (Open/Closed)**: Gracias a la interfaz `Recurso`, el sistema está abierto a la extensión (se pueden agregar nuevos tipos como `Revista` o `Audiolibro`) pero cerrado a la modificación de su lógica core en `PrestamoService`.
- **L (Liskov Substitution)**: Las clases concretas que implementan la interfaz `Recurso` (como `Libro` o `Ebook`) pueden ser sustituidas y utilizadas indistintamente por el sistema de préstamos sin alterar la correctitud del programa.
- **I (Interface Segregation)**: Se diseñaron interfaces pequeñas y enfocadas (como `Repository<T>`, `SocioRepository`, `LibroRepository`) en lugar de una interfaz monolítica gigante. Esto garantiza que las clases de memoria no se vean obligadas a implementar métodos que no necesitan.
- **D (Dependency Inversion)**: Los módulos de alto nivel (Servicios) no dependen de módulos de bajo nivel (Implementaciones en memoria), sino de abstracciones (Interfaces). Además, las dependencias se inyectan a través de los constructores, facilitando el testing y la escalabilidad.s dependen de la abstracción `Recurso` y no de implementaciones concretas.