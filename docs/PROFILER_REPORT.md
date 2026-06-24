# Profiler Report - Serify

## 1. Objetivo

Evaluar el comportamiento de Serify durante los flujos principales, detectando
consumo excesivo de CPU, memoria, red y energía. El análisis se centra en Inicio,
detalle de serie, persistencia Room, Firestore y chat de recomendaciones.

## 2. Configuración recomendada

| Elemento | Configuración |
|---|---|
| Variante | `debug` |
| Herramienta | Android Studio Profiler |
| Dispositivo | Emulador Pixel API 35 o dispositivo físico |
| Duración | 3 a 5 minutos por escenario |
| Estado inicial | Aplicación cerrada y proceso detenido |
| Red | Wi-Fi estable, una ejecución online y otra sin conexión |

## 3. Escenarios de medición

### Escenario A - Inicio

1. Iniciar sesión.
2. Abrir Inicio desde proceso detenido.
3. Esperar a que carguen estrenos y tendencias.
4. Desplazarse hasta el final.

Medir tiempo de arranque, CPU, memoria máxima, solicitudes de red y estabilidad de
frames.

### Escenario B - Detalle y Room

1. Abrir una serie.
2. Expandir una temporada.
3. Regresar a Inicio.
4. Confirmar que aparece en `Vistas recientemente`.
5. Repetir con cinco series.

Verificar que Room actualice una fila existente en lugar de duplicarla y que la
lista se mantenga limitada.

### Escenario C - Chat IA

1. Abrir el recomendador.
2. Enviar tres preguntas consecutivas.
3. Regresar y abrir nuevamente el chat.
4. Confirmar que se recuperen las últimas recomendaciones desde Room.

Medir tráfico enviado, latencia, memoria y ausencia de solicitudes duplicadas.

### Escenario D - Modo sin conexión

1. Cargar previamente recientes y recomendaciones.
2. Desactivar Wi-Fi y datos móviles.
3. Abrir Inicio y el chat.

Room debe seguir mostrando la información persistida. Las funciones de red deben
fallar de forma controlada sin cerrar la aplicación.

## 4. Análisis técnico

### CPU

- `HomeScreenViewModel` realiza varias búsquedas de TVMaze durante la carga inicial.
- `SeriesDetailScreenViewModel` solicita detalle, elenco, temporadas y episodios.
- La traducción de sinopsis y episodios agrega trabajo asíncrono.
- Room utiliza consultas acotadas y `Flow`, por lo que no debería producir sondeo continuo.

Riesgo principal: picos durante la carga inicial y al traducir episodios completos.

### Memoria

- Coil administra la carga y caché de imágenes.
- Las listas de Inicio y detalle mantienen objetos de series, elenco y episodios.
- Room limita recientes a 10 elementos visibles.
- El chat recupera 6 recomendaciones y conserva un historial acotado para la API.

Riesgo principal: imágenes grandes y listas de episodios extensas.

### Red

- Firestore almacena las series guardadas por UID.
- TVMaze provee catálogo, agenda y detalle.
- Groq procesa las consultas del chat.
- ML Kit puede requerir descarga inicial del modelo de traducción.

Riesgo principal: múltiples llamadas iniciales y envío de contexto al chat. El
request de IA limita el contexto a 8 series, 8 mensajes y 250 caracteres de
sinopsis por serie.

### Persistencia

- `recently_viewed` usa clave compuesta `userId + serieId`.
- `ai_recommendations` guarda pregunta, respuesta, usuario y fecha.
- Los datos locales quedan separados por UID.
- Las recomendaciones se recortan a las 20 más recientes.

## 5. Criterios de aceptación

| Métrica | Criterio |
|---|---|
| Cierres o ANR | 0 |
| Crecimiento de memoria | Debe estabilizarse al repetir el flujo |
| Series recientes duplicadas | 0 |
| Datos cruzados entre usuarios | 0 |
| Requests duplicados por una acción | 0 |
| Acceso a datos locales sin red | Disponible |
| Trabajo pesado en hilo principal | No sostenido |

## 6. Registro de mediciones

Completar esta tabla después de ejecutar Android Studio Profiler:

| Escenario | CPU pico | Memoria pico | Red enviada | Red recibida | Observaciones |
|---|---:|---:|---:|---:|---|
| Inicio | | | | | |
| Detalle y Room | | | | | |
| Chat IA | | | | | |
| Sin conexión | | | | | |

No se incluyen valores simulados. Las cifras deben obtenerse en el equipo y
dispositivo utilizados para la entrega, ya que cambian según hardware, emulador,
versión de Android y estado de la red.

## 7. Evidencias a adjuntar

- Captura de CPU durante la carga de Inicio.
- Captura de memoria después de abrir cinco detalles.
- Captura de Network Inspector durante una consulta IA.
- Captura de Database Inspector mostrando ambas tablas Room.
- Captura de los tests ejecutados correctamente.

## 8. Conclusión

La implementación reduce el volumen de datos mantenidos y enviados mediante
límites explícitos. Room permite consultar recientes y recomendaciones sin red,
mientras Firestore conserva los datos sincronizados. Los puntos que requieren
especial seguimiento son la carga múltiple de Inicio, la traducción de episodios y
el tamaño de las imágenes.
