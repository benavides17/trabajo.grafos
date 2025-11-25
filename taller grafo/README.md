# Carnival Routes (Java)

## Requisitos
- Tener instalado Java (JDK 8+ recomendado).
- Comprobar instalación: `java -version` y `javac -version`.
- Sistema operativo: Windows (comandos mostrados para PowerShell).

## Archivos
- `CarnivalRoutes.java`: Código fuente del programa.

## Compilar
- Ejecuta: `javac CarnivalRoutes.java`
- Esto generará `CarnivalRoutes.class` en el mismo directorio.

## Ejecutar (CLI + Gráfica)
- Ejecuta: `java CarnivalRoutes`
- El programa mostrará la lista de ubicaciones y solicitará:
  - `Número de ubicación de salida`
  - `Número de ubicación de destino`
- Al ingresar los números, verás:
  - La ruta más corta y su distancia en consola.
  - Una ventana con el grafo: nodos en verde, pesos en las aristas y el camino más corto resaltado en rojo.

## Ejemplo
- Entrada: `1` y `8`
- Resultado:
  - Ruta: `Carnival Plaza -> Children Park`
  - Distancia: `9` unidades
  - Ventana: `Main Carnival Routes - Pasto, Colombia` con el camino resaltado.

## Uso y notas
- Los lugares son:
  1. Carnival Plaza
  2. Nariño Square
  3. Panamerican Avenue
  4. Carnival Street
  5. Cultural Museum
  6. Hummingbird Monument
  7. Sun Square
  8. Children Park
- Cierra la ventana para finalizar la ejecución.
- El layout del grafo es circular; los pesos se muestran cerca del centro de cada arista.

## Solución de problemas
- `javac` o `java` no se reconocen:
  - Instala JDK y agrega `JAVA_HOME` y `bin` al `PATH`.
- No se abre la ventana:
  - Asegúrate de ejecutar en un entorno con GUI (Windows). Si hay otro proceso del programa abierto, ciérralo y vuelve a ejecutar.
- Error por selección inválida:
  - Ingresa números entre `1` y `8` correspondientes a las ubicaciones.

## Comandos rápidos
- Compilar: `javac CarnivalRoutes.java`
- Ejecutar: `java CarnivalRoutes`
