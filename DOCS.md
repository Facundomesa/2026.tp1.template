# Documentación de Diseño

## Decisiones Técnicas
- *Records*: Se utilizaron para las entidades (Libro, Socio, etc.) para asegurar la inmutabilidad(datos que no se pueden mutar nunca mas) de los datos y reducir el código boilerplate(codigo repetitivo).
- *Interfaces*: La interfaz `Recurso` permite que el sistema sea extensible (OCP), facilitando la adición de nuevos tipos de materiales en el futuro.
- *Enums*: Se encapsuló la lógica de límites de préstamos en `TipoSocio` para centralizar las reglas de negocio.
- *Patrón Repository*: Implementación de una arquitectura que desacopla la lógica de negocio del mecanismo de almacenamiento.
- *Interfaces Genéricas*: Se utilizaron tipos genéricos para estandarizar el comportamiento de todos los repositorios del sistema.
- *Manejo de Optional*: Integración de `Optional<T>` en las búsquedas para gestionar de forma robusta la ausencia de datos y evitar el `NullPointerException`
- *SocioService*: Valida estados de habilitación y realiza el cálculo dinámico de préstamos activos para controlar límites. |
- *LibroService*: Administra el catálogo y valida disponibilidad física cruzando datos en tiempo real con préstamos. |
- *PrestamoService*: Orquestador central. Gestiona la creación de transacciones, genera IDs únicos (UUID) y controla fechas. |
- *Cálculo Dinámico*: Se optó por no guardar contadores manuales en los modelos. El estado se calcula en tiempo real desde el historial de transacciones, garantizando la integridad de la información.

## Principios SOLID Aplicados
- **S (Single Responsibility)**: Cada clase tiene una única razón para existir.
- **O (Open/Closed)**: Gracias a la interfaz `Recurso`, el sistema está abierto a la extensión (nuevos materiales) pero cerrado a la modificación de su lógica core.
- **D (Dependency Inversion)**: Las transacciones dependen de la abstracción `Recurso` y no de implementaciones concretas como `Libro`.
## Decisiones

## **Uso de Records**
* **¿Por qué?**: Los Records son contenedores de datos inmutables por defecto. Al ser un sistema bibliotecario, necesitamos que la información de un libro o un socio no cambie aleatoriamente durante la ejecución de una transacción.
* **¿Para qué?**: Para eliminar el código "boilerplate" (getters, equals, hashCode) y garantizar que la entidad que sale del repositorio llegue intacta al servicio, evitando errores de estado inconsistente.

### **Interfaz `Recurso` y Polimorfismo**
* **¿Por qué?**: Aplicamos el principio de Inversión de Dependencias. El sistema de préstamos no debe "saber" si está prestando un objeto físico o digital, solo debe saber que es algo "prestable".
* **¿Para qué?**: Para que el sistema sea **extensible**. Hoy prestamos `Libro` y `Ebook`, pero mañana podemos agregar `Revista` o `Película` simplemente implementando la interfaz, sin tener que tocar ni una sola línea de código del `PrestamoService`.

### **Inyección de Dependencias (DI)**
* **¿Por qué?**: Los servicios reciben sus repositorios a través del constructor (`private final`). Esto desacopla la creación del objeto de su uso.
* **¿Para qué?**: Para facilitar el **testeo unitario** y el mantenimiento. Si el día de mañana cambiamos la base de datos de una lista en memoria a SQL Server, solo cambiamos el repositorio y los servicios siguen funcionando igual.

### **Cálculo Dinámico de Estados (No redundancia)**
* **¿Por qué?**: Decidimos no guardar un contador de `librosPrestados` dentro del Record `Socio`. En su lugar, el servicio cuenta los registros activos en el `PrestamoRepository` cada vez que se solicita un préstamo.
* **¿Para qué?**: Para garantizar la **Integridad de los Datos**. En bases de datos, esto evita anomalías de actualización. Si un préstamo se borra o se devuelve, el "contador" siempre será exacto porque se calcula en tiempo real desde la fuente de la verdad (las transacciones).

### **Uso de Enums para Reglas de Negocio**
* **¿Por qué?**: Las categorías de socios (ALUMNO, PROFESOR) tienen reglas distintas.
* **¿Para qué?**: Para centralizar las reglas de límites. Al usar un Enum en combinación con un `switch` o `if`, el compilador nos ayuda a asegurar que todos los tipos de socios sean contemplados en las validaciones.