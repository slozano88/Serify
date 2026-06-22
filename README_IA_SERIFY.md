# Serify: Groq + Room

Esta versión utiliza Groq como chat online y Room para persistencia local.

El proyecto usa KSP 2.3.6, compatible con AGP 9 y Kotlin integrado.

## Configuración de Groq

1. Creá una API key en Groq Console.
2. En la raíz del proyecto, agregá estas propiedades a `local.properties`:

```properties
GROQ_API_KEY=gsk_TU_API_KEY
GROQ_MODEL=llama-3.1-8b-instant
```

Podés usar `local.properties.example` como referencia.

3. En Android Studio ejecutá:

```text
File > Sync Project with Gradle Files
Build > Clean Project
Build > Rebuild Project
```

## Funcionamiento

- El chat consulta Groq online y conserva el contexto reciente de la conversación.
- Firestore sigue almacenando las series guardadas por cada cuenta.
- Room guarda las series vistas recientemente y las últimas recomendaciones de IA.
- Los datos locales están separados por el UID de Firebase.
- Inicio muestra las series vistas recientemente.
- El chat recupera las últimas seis recomendaciones guardadas al volver a abrirlo.

## Nota de seguridad

La API key se agrega a `BuildConfig`, por lo que puede extraerse del APK. Para una
aplicación publicada, la llamada a Groq debe pasar por un backend propio. Esta
integración directa es adecuada para desarrollo, demostraciones y entregas.
