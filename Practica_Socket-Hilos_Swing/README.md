# Servidor Socket - Tercera Parte: Cliente con Interfaz Gráfica (Swing)

## 📋 Descripción General

Este proyecto implementa un **servidor socket multicliente** en Java con dos tipos de clientes: uno de consola y uno con interfaz gráfica Swing. Permite:

- ✅ Atender múltiples clientes simultáneamente usando hilos
- ✅ Comunicación directa entre clientes (mensajes privados y globales)
- ✅ Asignación automática de nombres únicos a cada cliente
- ✅ Sistema completo de logging de todas las operaciones
- ✅ Soporte para conexiones remotas vía ZeroTier
- ✅ Resolución de expresiones matemáticas
- ✅ Análisis de texto (palabras, vocales, consonantes)
- ✅ Listado de provincias de Argentina
- ✅ **Interfaz gráfica con ventanas de chat independientes por destinatario**

## 🏗️ Arquitectura

### Servidor (Servidor.java)
**Características principales:**
- **Gestor de clientes concurrente**: Usa `ConcurrentHashMap` para almacenar clientes conectados
- **Escucha en todas las interfaces**: Configurado con `InetAddress.getByName("0.0.0.0")` para aceptar conexiones locales y remotas
- **Asignación automática de nombres**: Genera nombres únicos a partir del nombre enviado por el cliente (ej: Nicolas, Nicolas1, Nicolas2)
- **Registro de nombre personalizado**: El primer mensaje del cliente debe ser `NOMBRE nombreUsuario`
- **Comunicación inter-clientes**: Envía mensajes entre clientes o a todos simultáneamente con `*NOMBRE` y `*ALL`
- **Sistema de logging**: Registra todas las operaciones en consola y archivo (`servidor.log`)
- **Manejo robusto de errores**: Valida mensajes, clientes inexistentes y formatos incorrectos

### Cliente de Consola (Cliente.java)
**Características principales:**
- **Interfaz interactiva por terminal**: Prompt claro con el nombre del usuario
- **Registro de nombre**: Solicita nombre antes de conectar y lo envía al servidor con `NOMBRE`
- **Hilo lector asincrónico**: Recibe mensajes de otros clientes sin bloquear la entrada
- **Validación de entrada**: Verifica formato de comandos antes de enviar
- **Soporte para conexiones remotas**: Facilita conexión vía ZeroTier

### Cliente Gráfico (ClienteGUI.java) — *Novedad en esta parte*
**Características principales:**
- **Interfaz Swing con múltiples paneles**: Ventana principal con área de salida, panel de botones y barra de estado
- **Botones para cada comando**: `Ver Fecha`, `Lista Clientes`, `Provincias`, `Resolver`, `Contar`, sin necesidad de escribir texto
- **Ventanas de chat independientes**: Cada conversación (privada o global) abre su propia ventana `JFrame`
- **Chat privado**: Botón "Chat Privado" solicita el nombre del destinatario y abre una ventana dedicada
- **Chat global**: Botón "Chat Global (*ALL)" abre una ventana de broadcast a todos los conectados
- **Indicador de estado de conexión**: Muestra "Conectado" o "Desconectado" en tiempo real con color verde/rojo
- **Procesamiento diferenciado de mensajes**: Separa respuestas de comandos, mensajes privados y mensajes globales para mostrarlos en el lugar correcto
- **Hilo lector con SwingUtilities**: Los mensajes entrantes se procesan en el Event Dispatch Thread para evitar problemas de concurrencia en la UI

## 📡 Comandos Disponibles

### Comandos del Servidor (accesibles desde ambos clientes)

| Comando | Descripción | Ejemplo |
|---------|-------------|---------|
| `MENU` | Muestra la lista de comandos disponibles | `MENU` |
| `FECHA` | Muestra la fecha y hora actual | `FECHA` |
| `LISTA` | Lista los clientes conectados con su IP | `LISTA` |
| `RESOLVER "expresión"` | Resuelve expresiones matemáticas (+, -, *, /, paréntesis) | `RESOLVER "45*23/54+234"` |
| `CONTAR "texto"` | Cuenta palabras, vocales y consonantes | `CONTAR "Hola mundo"` |
| `PROVINCIAS` | Muestra las 24 provincias de Argentina | `PROVINCIAS` |
| `SALIR` | Desconecta al cliente del servidor | `SALIR` |

### Comandos de Mensajería

| Comando | Descripción | Ejemplo |
|---------|-------------|---------|
| `*NOMBRE "mensaje"` | Envía un mensaje a un cliente específico | `*Nicolas1 "Hola, ¿cómo estás?"` |
| `*ALL "mensaje"` | Envía un mensaje a todos los clientes conectados | `*ALL "¿Alguien conectado?"` |

> En el `ClienteGUI`, estos comandos se envían automáticamente al escribir en la ventana de chat correspondiente.

## 🚀 Cómo Usar

### Compilación

```bash
javac Servidor.java
javac Cliente.java
javac ClienteGUI.java
```

### Ejecución Local

**Terminal 1 — Iniciar el servidor:**
```bash
java Servidor
```

**Terminal 2 — Cliente de consola:**
```bash
java Cliente
```

**Terminal 3 — Cliente gráfico:**
```bash
java ClienteGUI
```

### Conexión Remota vía ZeroTier

#### Paso 1: Instalar y configurar ZeroTier

1. Descarga ZeroTier desde: https://www.zerotier.com/download/
2. Instálalo en todas las máquinas que necesitás conectar
3. En cada máquina, unite a la misma red ZeroTier con su Network ID

#### Paso 2: Obtener las direcciones IP virtuales

```bash
# En Windows
ipconfig /all | findstr "ZeroTier"

# En Linux/Mac
ip addr | grep zt
```

#### Paso 3: Configuración del servidor

El servidor ya está configurado para escuchar en todas las interfaces:

```java
ServerSocket serverSocket = new ServerSocket(PUERTO, 50, InetAddress.getByName("0.0.0.0"));
```

No es necesario modificarlo.

#### Paso 4: Configurar la IP en los clientes

En `Cliente.java` y `ClienteGUI.java`, modificar:

```java
private static final String HOST = "192.168.x.xxx"; // IP virtual ZeroTier del servidor
```

## 📝 Ejemplo de Ejecución

### Servidor

```
============================================
  SERVIDOR INICIADO EN EL PUERTO 6789
============================================
Esperando conexiones de clientes...
Hora inicio: 16/04/2026 14:30:45

[CONEXION] Nuevo cliente desde IP: 127.0.0.1
[CLIENTE] Nicolas (127.0.0.1) - CONECTADO
[LOG Nicolas] >>> FECHA
[CONEXION] Nuevo cliente desde IP: 127.0.0.1
[CLIENTE] Nicolas1 (127.0.0.1) - CONECTADO
[LOG Nicolas1] >>> *Nicolas "Hola"
[MENSAJE] Nicolas1 -> Nicolas: Hola
```

### Cliente de Consola

```
============================================
  CLIENTE SOCKET
============================================
Ingrese su nombre de usuario: Nicolas
Bienvenido, Nicolas!
Conectando a 192.168.194.119:6789...

Conexion establecida con el servidor!
Registrado como: Nicolas

[Nicolas] > FECHA
[SERVIDOR] Fecha y Hora: 16/04/2026 14:30:50

[Nicolas] > *ALL "Hola a todos"
[SERVIDOR] Mensaje enviado a 1 cliente(s).

[MENSAJE] 📨 [Nicolas1 -> Nicolas] Hola
```

### Cliente Gráfico

Al iniciar `ClienteGUI`, se abre una ventana de diálogo para ingresar el nombre. Luego:

- El **área central** muestra respuestas de comandos como `FECHA`, `LISTA`, `PROVINCIAS`
- Al hacer clic en **"Resolver"** o **"Contar"**, se abre un `JOptionPane` para ingresar la expresión
- Al hacer clic en **"Chat Privado"**, se pide el nombre del destinatario y se abre una ventana de chat
- Al hacer clic en **"Chat Global"**, se abre una ventana para mensajes `*ALL`
- Los mensajes entrantes abren la ventana de chat correspondiente automáticamente si estaba cerrada
- El indicador superior muestra `● Conectado a 192.168.x.x` en verde cuando hay conexión activa

## 🔧 Detección y Manejo de Errores

| Error | Acción |
|-------|--------|
| Cliente inexistente | Notifica al remitente que el cliente no existe |
| Formato de mensaje inválido | Retorna error de sintaxis |
| Expresión matemática inválida | Retorna mensaje de error de cálculo |
| Cliente enviando mensaje a sí mismo | Rechaza la acción |
| Servidor desconectado | El cliente (consola o GUI) se cierra o indica desconexión |
| Error al conectar (GUI) | Muestra mensaje de error en el área de salida |

## 📊 Sistema de Logging

El servidor mantiene el archivo `servidor.log` con marca de tiempo en cada entrada:

```
[16/04/2026 14:30:45] ============================================
[16/04/2026 14:30:45]   SERVIDOR INICIADO EN EL PUERTO 6789
[16/04/2026 14:30:46] [CONEXION] Nuevo cliente desde IP: 127.0.0.1
[16/04/2026 14:30:46] [CLIENTE] Nicolas (127.0.0.1) - CONECTADO
[16/04/2026 14:30:51] [LOG Nicolas] >>> FECHA
[16/04/2026 14:30:56] [LOG Nicolas] >>> *ALL "Hola a todos"
[16/04/2026 14:30:56] [MENSAJE] Nicolas envió a TODOS: Hola a todos
[16/04/2026 14:31:02] [CLIENTE] Nicolas (127.0.0.1) - DESCONECTADO
```

## 🖼️ Componentes Swing Utilizados (ClienteGUI)

| Componente | Uso |
|------------|-----|
| `JFrame` | Ventana principal y ventanas de chat independientes |
| `JTextArea` | Área de salida de comandos y área de mensajes en cada chat |
| `JTextField` | Campo de entrada de mensajes en ventanas de chat |
| `JButton` | Botones de acción para cada comando disponible |
| `JScrollPane` | Desplazamiento en áreas de texto |
| `JPanel` | Organización de layout (estado, botones) |
| `JLabel` | Indicador de estado de conexión |
| `JOptionPane` | Diálogos para ingreso de nombre, expresiones y destinatario |
| `GridLayout` | Distribución vertical del panel de botones |
| `BorderLayout` | Layout general de la ventana principal |
| `SwingUtilities.invokeLater` | Procesamiento seguro de mensajes en el EDT |

## 🌐 Características de ZeroTier

- ✅ No requiere configuración de puertos en el router
- ✅ Encriptación end-to-end
- ✅ Funciona detrás de firewalls y NAT
- ✅ Las máquinas remotas se ven como si estuvieran en la misma LAN
- ✅ Gratuito para uso personal

Para crear una red: ingresá a https://my.zerotier.com/, creá una cuenta, hacé clic en "Create a Network" y compartí el Network ID con los demás.

## 🔐 Seguridad

- Los mensajes entre clientes viajan siempre a través del servidor
- ZeroTier proporciona encriptación en tránsito
- El archivo `servidor.log` contiene el registro completo de operaciones
- No hay sistema de autenticación (proyecto educativo)

## 📚 Tecnologías Utilizadas

- **Java 8+**: Lenguaje de programación
- **Sockets TCP**: Comunicación cliente-servidor
- **Hilos (Threads)**: Manejo concurrente de clientes y lectura asincrónica
- **ConcurrentHashMap**: Almacenamiento thread-safe de clientes conectados
- **BufferedReader / PrintWriter**: E/S en streams con charset configurable
- **Swing (javax.swing)**: Interfaz gráfica del `ClienteGUI`
- **ZeroTier**: Conectividad remota (opcional)

## 🗂️ Archivos del Proyecto

| Archivo | Descripción |
|---------|-------------|
| `Servidor.java` | Servidor multicliente con hilos y logging |
| `Cliente.java` | Cliente de consola interactivo |
| `ClienteGUI.java` | Cliente con interfaz gráfica Swing |
| `servidor.log` | Log generado automáticamente al iniciar el servidor |
| `README.md` | Este archivo |

## 📄 Licencia

Proyecto educativo — libre de usar y modificar.

## 📞 Referencias

- ZeroTier: https://www.zerotier.com/
- Java Sockets: https://docs.oracle.com/javase/tutorial/networking/sockets/
- Java Swing: https://docs.oracle.com/javase/tutorial/uiswing/
