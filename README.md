# TAREA 1 PRINCIPIOS DE SISTEMAS OPERATIVOS
# MINI PC
## DAVID CALVO GARCÍA 2024122451

### Estado del proyecto: Excelente
### Enlace del video: https://youtu.be/8e_jOAPWHno

---

## Descripción del Proyecto

<img width="1917" height="1017" alt="image" src="https://github.com/user-attachments/assets/4f38a702-a9c8-42fb-8996-dfa0765ea034" />


Es un simulador de una mini computadora, hecho en Java con interfaz gráfica en Swing (NetBeans). La idea es tomar un archivo de código ensamblador (`.asm`), reconocer sus instrucciones, cargarlas en una memoria simulada, y ejecutarlas paso a paso (o de una sola vez) siguiendo el ciclo fetch-decode-execute que se explica en el libro de Stallings (*Operating Systems: Internals and Design Principles*).

El programa simula:
- Una CPU con registros de propósito general (AX, BX, CX, DX) y registros especiales (PC, IR, AC)
- Una memoria RAM dividida en dos segmentos: kernel y usuario
- Un BCP (Bloque de Control de Proceso) que vive dentro del segmento de kernel de la memoria, siguiendo el modelo real de un sistema operativo

## Requisitos

- Java 11 o superior
- NetBeans 

## Cómo correrlo

1. Abrir el proyecto en NetBeans
2. Correr la clase `T1SOMiniPC.java` (tiene el `main()`)
3. Desde la interfaz, dar clic en "Cargar archivo .asm" y seleccionar un archivo con instrucciones válidas
4. Usar "Paso a paso" para ejecutar instrucción por instrucción, o "Ejecutar todo" para correr el programa completo de un jalón

## Formato del archivo .asm

Cada línea representa una instrucción, con esta estructura:

```
OPERADOR REGISTRO[, VALOR]
```

Operaciones soportadas:

| Operador | Formato | Qué hace |
|---|---|---|
| MOV | `MOV registro, valor` | Pone el valor directo en el registro |
| LOAD | `LOAD registro` | Carga el valor del registro al acumulador (AC) |
| ADD | `ADD registro` | Suma el valor del registro al AC |
| SUB | `SUB registro` | Resta el valor del registro al AC |
| STORE | `STORE registro` | Guarda el valor del AC de vuelta en el registro |

Registros válidos: `AX`, `BX`, `CX`, `DX`

Los valores numéricos van de -127 a 127, porque el formato interno de número usa 1 bit de signo y 7 bits de magnitud.

Ejemplo de archivo válido:

```
MOV AX, 5
MOV BX, 3
LOAD AX
ADD BX
SUB AX
STORE AX
MOV BX, -8
```

Si alguna línea no cumple con el formato esperado (operador desconocido, registro inválido, cantidad de argumentos incorrecta, valor fuera de rango, etc.), el programa la rechaza y le avisa al usuario con el número de línea y el motivo, deteniendo también la carga de las demás instrucciones válidas.

## Estructura del proyecto

El proyecto sigue una arquitectura MVC (Modelo - Vista - Controlador):

```
com.minipc.t1sominipc
├── model/
│   ├── Instruccion.java       -> representa una instrucción ya parseada (operador, registro, valor)
│   ├── ConvertidorASM.java    -> lee el archivo .asm y lo convierte en objetos Instruccion, validando la sintaxis
│   ├── Memoria.java           -> simula la RAM, dividida en segmento de kernel y de usuario
│   ├── BCP.java               -> Bloque de Control de Proceso, guarda su información directamente en el segmento de kernel de la memoria
│   └── CPU.java                -> ejecuta las instrucciones, maneja los registros y el ciclo de ejecución
├── view/
│   └── MiniPCFrame.java       -> ventana principal en Swing (tablas, botones, panel de registros)
└── controller/
    └── ControllerMiniPC.java  -> conecta los botones de la vista con la lógica del modelo
```

### Sobre la memoria y el BCP

La memoria se divide en dos segmentos configurables: kernel (por defecto, posiciones 0 a 63) y usuario (de 64 en adelante). El BCP no es un objeto Java suelto con sus propios atributos,sus campos (PID, Estado, PC, AC, AX, BX, CX, DX, Base, Contador de instrucciones) se guardan directamente como valores dentro del segmento de kernel de la memoria, tal como funciona un sistema operativo real. Esto se puede ver en tiempo real en la tabla de "Memoria Principal" de la interfaz, que se actualiza en cada paso de ejecución.

El programa cargado (en el segmento de usuario) se guarda tanto en formato binario (para mostrarlo en las tablas) como en su forma de objeto `Instruccion`, que es la que realmente usa la CPU para ejecutar, la CPU nunca decodifica binario para decidir qué hacer, eso solo se calcula para efectos visuales.

## Funcionalidades de la interfaz

- **Cargar archivo .asm**: abre un selector de archivos, valida las líneas y las muestra en la tabla "Programa cargado". El proceso pasa primero por el estado "Nuevo" y, tras un breve momento simulando la preparación de memoria, pasa a "Listo" ya con el programa escrito en RAM.
- **Paso a paso**: ejecuta una instrucción a la vez, actualizando registros, memoria y el estado del proceso.
- **Ejecutar todo**: corre el programa completo de una sola vez.
- **Configurar memoria**: abre una ventana donde se puede ajustar el tamaño total de la RAM y el tamaño del segmento de kernel. Se bloquea mientras hay un proceso activo en memoria.
- **Limpiar / Reset**: reinicia todo el simulador a su estado inicial.





