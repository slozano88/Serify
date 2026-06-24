# Testing de Serify

## Alcance

La suite valida lógica de negocio sin depender de la red, Firebase ni claves de API.
También incluye pruebas instrumentadas para comprobar el comportamiento real de Room.

## Tests unitarios locales

Ubicación:

```text
app/src/test/java/com/serify/
```

Casos incluidos:

- `SpanishTextMapperTest`: traducción de géneros y estados de TVMaze.
- `SavedScreenStateTest`: búsqueda por nombre y género.
- `LocalHistoryMapperTest`: conversión entre `Serie` y `RecentlyViewedEntity`.
- `AiRequestFactoryTest`: construcción del request, límite de historial, limpieza de HTML y reducción del contexto.

Total: 15 tests unitarios locales.

Ejecución:

```text
./gradlew testDebugUnitTest
```

En Windows:

```text
gradlew.bat testDebugUnitTest
```

## Tests instrumentados de Room

Ubicación:

```text
app/src/androidTest/java/com/serify/data/local/SerifyDaoTest.kt
```

Casos incluidos:

- Orden de series recientes por fecha.
- Separación de datos por UID de Firebase.
- Reemplazo de una serie ya vista sin duplicarla.
- Orden y recorte del historial de recomendaciones.

Total: 3 tests instrumentados.

Se ejecutan con un emulador o dispositivo conectado:

```text
./gradlew connectedDebugAndroidTest
```

En Windows:

```text
gradlew.bat connectedDebugAndroidTest
```

## Ejecución desde Android Studio

1. Abrir la carpeta raíz del proyecto.
2. Ejecutar `File > Sync Project with Gradle Files`.
3. Para tests locales, hacer clic derecho sobre `app/src/test` y seleccionar `Run Tests`.
4. Para Room, iniciar un emulador, hacer clic derecho sobre `SerifyDaoTest` y seleccionar `Run`.

## Resultado esperado

Todos los tests deben finalizar en verde. Los tests locales no necesitan conexión a
internet. Los tests instrumentados utilizan una base Room en memoria y no modifican
los datos normales de la aplicación.
