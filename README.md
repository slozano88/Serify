# Serify

Serify es una aplicación Android para descubrir series, consultar información detallada y organizar una lista personal. También incluye un asistente de inteligencia artificial que recomienda contenido según la consulta del usuario y, cuando resulta útil, toma como contexto sus series guardadas.

El proyecto fue desarrollado en Kotlin con Jetpack Compose y utiliza una arquitectura MVVM con repositorios para separar la interfaz, la lógica y las fuentes de datos.

## Funcionalidades

- Inicio de sesión con Google mediante Firebase Authentication.
- Catálogo de series obtenido desde TVMaze.
- Búsqueda de series por nombre.
- Exploración y filtrado por género.
- Detalle con sinopsis, calificación, estado, elenco, temporadas y episodios.
- Traducción de sinopsis y episodios al español.
- Lista personal de series guardadas en Firestore.
- Historial local de series vistas recientemente.
- Chat online de recomendaciones con Groq.
- Recuperación local de las últimas recomendaciones de la IA.
- Separación de datos locales y remotos por usuario.
- Manejo de estados de carga, errores y ausencia de conexión.

## Tecnologías

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- ViewModel, StateFlow y Coroutines
- Retrofit y Gson
- Room
- Firebase Authentication
- Cloud Firestore
- Google Credential Manager
- Groq API
- TVMaze API
- ML Kit Translation
- Coil
- JUnit y AndroidX Test

## Arquitectura

La aplicación sigue una estructura MVVM:

```text
Pantallas Compose
        |
        v
ViewModels y estados
        |
        v
Repositorios
        |
        +-- TVMaze API
        +-- Groq API
        +-- Firebase
        +-- Room
```

Las pantallas se encargan de mostrar el estado y recibir las acciones del usuario. Los ViewModel coordinan cada flujo y exponen su estado mediante `StateFlow`. Los repositorios concentran el acceso a servicios externos y al almacenamiento local.

Entre los repositorios principales se encuentran:

- `SeriesRepository`: catálogo, búsqueda, elenco, temporadas y episodios.
- `FirebaseSeriesRepository`: almacenamiento de series guardadas.
- `LocalHistoryRepository`: recientes y recomendaciones persistidas con Room.
- `AiRepository`: comunicación con Groq.
- `EnglishToSpanishTranslationRepository`: traducción online y respaldo con ML Kit.

## Fuentes de datos

### TVMaze

Se utiliza para obtener el catálogo y la información de las series:

- búsquedas;
- programación del día;
- detalles;
- elenco;
- temporadas;
- episodios.

### Firebase

Firebase Authentication administra la sesión de Google. Firestore guarda la lista personal de cada usuario con una estructura similar a:

```text
users/{userId}/savedSeries/{serieId}
```

El UID de Firebase permite mantener los datos separados por cuenta.

### Room

La base local `serify_local.db` contiene dos tablas:

- `recently_viewed`: guarda las últimas series abiertas.
- `ai_recommendations`: guarda preguntas y respuestas exitosas del chat.

Las series recientes usan `userId` y `serieId` como clave compuesta. Por eso, abrir nuevamente una serie actualiza su fecha en lugar de crear un duplicado.

La aplicación muestra hasta 10 series recientes. En el chat recupera las últimas 6 recomendaciones y conserva un máximo de 20 por usuario.

Room permite consultar estos datos locales sin conexión. El catálogo de TVMaze, las series de Firestore y las nuevas respuestas de la IA siguen necesitando internet.

## Chat con IA

El asistente utiliza la API de Groq y el modelo configurado en `local.properties`. Su objetivo es responder consultas relacionadas con series, episodios, actores, géneros y recomendaciones.

Para personalizar la respuesta se puede enviar:

- la pregunta actual;
- hasta 8 mensajes anteriores;
- hasta 8 series guardadas;
- una versión reducida de cada sinopsis.

Estos límites evitan enviar un contexto innecesariamente grande. Cuando Groq devuelve una respuesta válida, la pregunta y la respuesta se almacenan en Room.

### Firebase

El archivo `google-services.json` debe estar dentro del módulo `app`:

```text
app/google-services.json
```

En Firebase deben estar habilitados:

- Authentication con Google;
- Cloud Firestore.

### Groq

Agregar estas propiedades en el archivo `local.properties` de la raíz:

```properties
GROQ_API_KEY=tu_api_key
GROQ_MODEL=llama-3.1-8b-instant
```

No se debe publicar `local.properties` ni subir claves privadas al repositorio.

Después de configurar el proyecto:

1. Sincronizar Gradle.
2. Compilar la aplicación.
3. Ejecutarla en un emulador o dispositivo.

## Navegación principal

```text
Splash
  |
  +-- Login
  |
  +-- Inicio
       +-- Explorar
       +-- Géneros
       +-- Detalle de serie
       +-- Guardadas
       +-- Chat IA
       +-- Perfil
```

La pantalla inicial revisa si existe una sesión activa. Si el usuario ya está autenticado, navega directamente a Inicio; de lo contrario, muestra el acceso con Google.

## Pruebas

El proyecto incluye pruebas unitarias para:

- filtrado de series guardadas;
- conversión entre modelos de dominio y entidades de Room;
- traducción de géneros y estados;
- construcción y reducción del contexto enviado a la IA.

También incluye pruebas instrumentadas para comprobar:

- orden y separación de recientes por usuario;
- actualización de una serie sin duplicados;
- límite y recorte de recomendaciones.

En Windows:

```powershell
gradlew.bat testDebugUnitTest
gradlew.bat connectedDebugAndroidTest
```

En Linux o macOS:

```bash
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
```

Las pruebas instrumentadas requieren un emulador o dispositivo conectado.

## Análisis de rendimiento

El proyecto incluye un reporte preparado para Android Studio Profiler. Los escenarios evaluados abarcan:

- carga inicial;
- apertura del detalle de una serie;
- escritura y lectura en Room;
- consultas al chat;
- comportamiento sin conexión.

El análisis considera CPU, memoria, tráfico de red, duplicación de solicitudes y estabilidad de la aplicación.

## Estructura principal

```text
app/src/main/java/com/serify/
├── components/        Pantallas, estados y ViewModels
├── data/
│   ├── api/           Interfaces de Retrofit
│   ├── local/         Base de datos, entidades y DAO de Room
│   ├── model/         Modelos de datos
│   ├── repository/    Acceso a APIs, Firebase y Room
│   └── util/          Conversión y adaptación de textos
├── di/                 Módulos de inyección de dependencias con Hilt
├── domain/            Contratos de repositorios
└── ui/theme/          Tema visual de Compose
```

## Estado del proyecto

Serify integra el catálogo online, autenticación, almacenamiento remoto, persistencia local y recomendaciones con IA en un único flujo. El objetivo de esta versión es ofrecer una base funcional y organizada sobre la que se puedan agregar nuevas características, como seguimiento de episodios, calificaciones personales o sincronización adicional del historial.

# Inyección de dependencias

Serify utiliza Hilt para administrar las dependencias de la aplicación.

- `NetworkModule` crea los servicios de TVMaze, Groq y Google Translate.
- `DatabaseModule` crea la base Room y su DAO.
- `FirebaseModule` provee Firebase Auth y Firestore.
- `TranslationModule` administra el traductor de ML Kit.
- `RepositoryModule` vincula `ISeriesRepository` con `SeriesRepository`.
- Los repositorios reciben sus dependencias por constructor.
- Los ViewModels usan `@HiltViewModel` y reciben sus repositorios por constructor.
