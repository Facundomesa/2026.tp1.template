# Documentación de Diseño

## Decisiones Técnicas
- **Records**: Se utilizaron para las entidades (Libro, Socio, etc.) para asegurar la inmutabilidad(datos que no se pueden mutar nunca mas) de los datos y reducir el código boilerplate(codigo repetitivo).
- **Interfaces**: La interfaz `Recurso` permite que el sistema sea extensible (OCP), facilitando la adición de nuevos tipos de materiales en el futuro.
- **Enums**: Se encapsuló la lógica de límites de préstamos en `TipoSocio` para centralizar las reglas de negocio.

## Principios SOLID Aplicados
- **S (Single Responsibility)**: Cada clase tiene una única razón para existir.
- **D (Dependency Inversion)**: Las transacciones dependen de la abstracción `Recurso` y no de implementaciones concretas como `Libro`.