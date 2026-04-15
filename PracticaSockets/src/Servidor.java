import java.io.*;
import java.net.*;

public class Servidor {

    private static final int PUERTO = 6789;

    private static final String[] PROVINCIAS = {
        "Buenos Aires", "Catamarca", "Chaco", "Chubut", "Cordoba",
        "Corrientes", "Entre Rios", "Formosa", "Jujuy", "La Pampa",
        "La Rioja", "Mendoza", "Misiones", "Neuquen", "Rio Negro",
        "Salta", "San Juan", "San Luis", "Santa Cruz", "Santa Fe",
        "Santiago del Estero", "Tierra del Fuego", "Tucuman",
        "Ciudad Autonoma de Buenos Aires"
    };

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PUERTO);
        System.out.println("============================================");
        System.out.println("  SERVIDOR INICIADO EN EL PUERTO " + PUERTO);
        System.out.println("============================================");
        System.out.println("Esperando conexiones de clientes...\n");

        // Acepta clientes en bucle (un hilo por cliente)
        while (true) {
            Socket socketCliente = serverSocket.accept();
            System.out.println("[CONEXION] Nuevo cliente conectado desde: "
                    + socketCliente.getInetAddress().getHostAddress());
            Thread hilo = new Thread(() -> manejarCliente(socketCliente));
            hilo.start();
        }
    }

    // -------------------------------------------------------------------------
    // Manejo del cliente en su propio hilo
    // -------------------------------------------------------------------------
    private static void manejarCliente(Socket socket) {
        String ip = socket.getInetAddress().getHostAddress();
        try (
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "UTF-8"));
            PrintWriter salida = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true)
        ) {
            // Mensaje de bienvenida
            salida.println("============================================");
            salida.println("  Bienvenido al Servidor Socket!");
            salida.println("  Comandos disponibles:");
            salida.println("    RESOLVER \"expresion\"");
            salida.println("    CONTAR \"texto\"");
            salida.println("    PROVINCIAS");
            salida.println("    SALIR");
            salida.println("============================================");

            String mensajeRecibido;
            while ((mensajeRecibido = entrada.readLine()) != null) {
                // Log en consola del servidor
                System.out.println("[LOG " + ip + "] >>> " + mensajeRecibido);

                String respuesta = procesarComando(mensajeRecibido.trim());
                salida.println(respuesta);

                // Si el cliente envia SALIR se corta la comunicacion
                if (mensajeRecibido.trim().equalsIgnoreCase("SALIR")) {
                    System.out.println("[CONEXION] Cliente " + ip + " se desconecto.");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Cliente " + ip + ": " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Procesamiento de comandos
    // -------------------------------------------------------------------------
    private static String procesarComando(String mensaje) {
        String upper = mensaje.toUpperCase();

        if (upper.equals("SALIR")) {
            return "Hasta luego! Conexion cerrada correctamente.";
        }

        if (upper.startsWith("RESOLVER ")) {
            String argumento = mensaje.substring(9).trim();
            String expresion = extraerContenidoComillas(argumento);
            return resolverExpresion(expresion);
        }

        if (upper.startsWith("CONTAR ")) {
            String argumento = mensaje.substring(7).trim();
            String texto = extraerContenidoComillas(argumento);
            return contarTexto(texto);
        }

        if (upper.equals("PROVINCIAS")) {
            return listarProvincias();
        }

        return "Comando no reconocido. Comandos validos: RESOLVER, CONTAR, PROVINCIAS, SALIR";
    }

    /** Si el string esta entre comillas las elimina, sino devuelve el string tal cual */
    private static String extraerContenidoComillas(String s) {
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    // -------------------------------------------------------------------------
    // COMANDO: RESOLVER - evaluador de expresiones matematicas
    // -------------------------------------------------------------------------
    private static String resolverExpresion(String expr) {
        if (expr.isEmpty()) {
            return "Error: debe proporcionar una expresion. Ej: RESOLVER \"45*23/54+234\"";
        }
        try {
            double resultado = evaluar(expr);
            // Si el resultado es entero lo muestra sin decimales
            if (resultado == Math.floor(resultado) && !Double.isInfinite(resultado)) {
                return "Resultado de [" + expr + "] = " + (long) resultado;
            }
            return "Resultado de [" + expr + "] = " + resultado;
        } catch (Exception e) {
            return "Error al resolver la expresion: " + e.getMessage();
        }
    }

    /**
     * Evaluador de expresiones matematicas sin librerias externas.
     * Soporta: +  -  *  /  parentesis  numeros decimales
     * Implementacion: analizador descendente recursivo0
     */
    private static double evaluar(final String expr) {
        return new Object() {
            int pos = -1;
            int ch;

            void siguienteChar() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }

            boolean consumir(int charBuscado) {
                while (ch == ' ') siguienteChar();
                if (ch == charBuscado) {
                    siguienteChar();
                    return true;
                }
                return false;
            }

            double parsear() {
                siguienteChar();
                double x = parseExpresion();
                if (pos < expr.length()) {
                    throw new RuntimeException("Caracter inesperado: '" + (char) ch + "'");
                }
                return x;
            }

            // Suma y resta (menor precedencia)
            double parseExpresion() {
                double x = parseTerm();
                for (;;) {
                    if      (consumir('+')) x += parseTerm();
                    else if (consumir('-')) x -= parseTerm();
                    else return x;
                }
            }

            // Multiplicacion y division (mayor precedencia)
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (consumir('*')) x *= parseFactor();
                    else if (consumir('/')) x /= parseFactor();
                    else return x;
                }
            }

            // Numeros, parentesis y signo unario
            double parseFactor() {
                if (consumir('+')) return parseFactor();
                if (consumir('-')) return -parseFactor();

                double x;
                int inicio = this.pos;

                if (consumir('(')) {
                    x = parseExpresion();
                    consumir(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') siguienteChar();
                    x = Double.parseDouble(expr.substring(inicio, this.pos));
                } else {
                    throw new RuntimeException(
                        "Caracter no valido: '" + (char) ch + "' en posicion " + pos);
                }
                return x;
            }
        }.parsear();
    }

    // -------------------------------------------------------------------------
    // COMANDO: CONTAR - palabras, vocales y consonantes
    // -------------------------------------------------------------------------
    private static String contarTexto(String texto) {
        if (texto.isEmpty()) {
            return "Error: debe proporcionar un texto. Ej: CONTAR \"Hola mundo\"";
        }

        // Contar palabras (separadas por espacios en blanco)
        String[] palabras = texto.trim().split("\\s+");
        int cantPalabras = palabras.length;

        // Contar vocales y consonantes
        final String VOCALES = "aeiouAEIOU";
        int vocales = 0;
        int consonantes = 0;

        for (char c : texto.toCharArray()) {
            if (Character.isLetter(c)) {
                if (VOCALES.indexOf(c) >= 0) {
                    vocales++;
                } else {
                    consonantes++;
                }
            }
        }

        return "Texto: \"" + texto + "\"\n"
             + "  -> Palabras:     " + cantPalabras + "\n"
             + "  -> Vocales:      " + vocales + "\n"
             + "  -> Consonantes:  " + consonantes;
    }

    // -------------------------------------------------------------------------
    // COMANDO: PROVINCIAS - lista las 24 provincias de Argentina
    // -------------------------------------------------------------------------
    private static String listarProvincias() {
        StringBuilder sb = new StringBuilder();
        sb.append("Provincias de Argentina (").append(PROVINCIAS.length).append(" en total):\n");
        for (int i = 0; i < PROVINCIAS.length; i++) {
            sb.append(String.format("  %2d. %s%n", i + 1, PROVINCIAS[i]));
        }
        return sb.toString().trim();
    }
}
