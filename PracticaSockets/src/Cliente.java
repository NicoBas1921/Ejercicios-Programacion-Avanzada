import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    private static final String HOST   = "localhost";
    private static final int    PUERTO = 6789;

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("  CLIENTE SOCKET");
        System.out.println("  Conectando a " + HOST + ":" + PUERTO + "...");
        System.out.println("============================================");

        try (
            Socket socket = new Socket(HOST, PUERTO);
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "UTF-8"));
            PrintWriter salida = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Conexion establecida con el servidor!\n");

            // ----------------------------------------------------------------
            // Hilo lector: imprime en pantalla todo lo que llega del servidor
            // ----------------------------------------------------------------
            Thread hiloLector = new Thread(() -> {
                try {
                    String linea;
                    while ((linea = entrada.readLine()) != null) {
                        System.out.println("[SERVIDOR] " + linea);
                    }
                } catch (IOException e) {
                    // El servidor cerro la conexion
                    System.out.println("\n[INFO] Conexion con el servidor finalizada.");
                }
            });
            hiloLector.setDaemon(true);
            hiloLector.start();

            // ----------------------------------------------------------------
            // Bucle principal: lee input del usuario y lo envia al servidor
            // ----------------------------------------------------------------
            mostrarAyuda();

            String input;
            do {
                System.out.print("\n[TU] > ");
                input = scanner.nextLine();

                if (input.trim().isEmpty()) {
                    continue;
                }

                // Enviar al servidor
                salida.println(input.trim());

                // Pausa corta para que la respuesta del servidor llegue
                // antes de que aparezca el proximo prompt
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            } while (!input.trim().equalsIgnoreCase("SALIR"));

            System.out.println("\n[INFO] Sesion finalizada. Hasta luego!");

        } catch (ConnectException e) {
            System.out.println("[ERROR] No se pudo conectar al servidor.");
            System.out.println("        Verifique que el Servidor este en ejecucion en el puerto " + PUERTO + ".");
        } catch (IOException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    /** Muestra los comandos disponibles al iniciar el cliente */
    private static void mostrarAyuda() {
        System.out.println("\n--- Comandos que podes enviar al servidor ---");
        System.out.println("  RESOLVER \"expresion\"   ej: RESOLVER \"45*23/54+234\"");
        System.out.println("  CONTAR \"texto\"         ej: CONTAR \"Hola como estas hoy\"");
        System.out.println("  PROVINCIAS             lista las provincias de Argentina");
        System.out.println("  SALIR                  desconectarse del servidor");
        System.out.println("--------------------------------------------");
    }
}
