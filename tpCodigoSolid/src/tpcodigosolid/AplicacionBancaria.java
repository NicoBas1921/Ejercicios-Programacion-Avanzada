package tpcodigosolid;

import java.util.Scanner;

public class AplicacionBancaria {
    public static void main(String[] args) {
        Scanner sn = new Scanner(System.in);
        
        // Inicializamos los objetos
        Usuario clienteActivo = new Usuario("Nicolas", "2004");
        CuentaBancaria cuentaPesos = new CuentaPesos(clienteActivo, 100000); 
        CuentaBancaria cuentaDolares = new CuentaDolares(clienteActivo, 1000); 

        System.out.println("--- BIENVENIDO AL SISTEMA BANCARIO ---");
        
        boolean programaEnEjecucion = true; // Bucle maestro para mantener la app viva
        
        while (programaEnEjecucion) {
            
            // 1. SISTEMA DE LOGIN
            boolean autenticado = false;
            while (!autenticado) {
                System.out.print("Ingrese usuario: ");
                String user = sn.nextLine();
                System.out.print("Ingrese clave: ");
                String pass = sn.nextLine();
                
                if (clienteActivo.validarLogin(user, pass)) {
                    autenticado = true;
                    System.out.println("\n[EXITO] Login correcto. Bienvenido " + clienteActivo.getNombre());
                } else {
                    System.out.println("[ERROR] Credenciales incorrectas. Intente nuevamente.\n");
                }
            }

            // 2. MENU PRINCIPAL (Sesion iniciada)
            boolean sesionActiva = true;
            while (sesionActiva) {
                System.out.println("\n--- MENU PRINCIPAL ---");
                System.out.println("1. Operar con cuenta en Pesos (ARS)");
                System.out.println("2. Operar con cuenta en Dolares (USD)");
                System.out.println("3. Blanquear Contrasena");
                System.out.println("4. Salir del Banco");
                System.out.print("Seleccione una opcion: ");
                String opcPrincipal = sn.nextLine();

                CuentaBancaria cuentaSeleccionada = null;

                if (opcPrincipal.equals("1")) {
                    cuentaSeleccionada = cuentaPesos;
                } else if (opcPrincipal.equals("2")) {
                    cuentaSeleccionada = cuentaDolares;
                } else if (opcPrincipal.equals("3")) {
                    System.out.print("Ingrese su nueva contrasena: ");
                    String nuevaPass = sn.nextLine();
                    clienteActivo.blanquearPassword(nuevaPass);
                    
                    // ACCION DE DESLOGUEO
                    System.out.println("[INFO] Por seguridad, su sesion se ha cerrado. Vuelva a ingresar.");
                    sesionActiva = false; // Esto rompe el menu y vuelve al login
                    continue; 
                    
                } else if (opcPrincipal.equals("4")) {
                    sesionActiva = false;
                    programaEnEjecucion = false; // Rompe TODOS los bucles y apaga el programa
                    System.out.println("Gracias por usar el sistema.");
                    continue;
                } else {
                    System.out.println("[ERROR] Opcion no valida.");
                    continue;
                }

                // 3. SUBMENU DE LA CUENTA SELECCIONADA
                if (cuentaSeleccionada != null) {
                    boolean volver = false;
                    while (!volver) {
                        System.out.println("\n--- OPERANDO EN " + cuentaSeleccionada.getMoneda() + " ---");
                        System.out.println("1. Ver Saldo");
                        System.out.println("2. Depositar");
                        System.out.println("3. Retirar");
                        System.out.println("4. Volver al menu principal");
                        System.out.print("Opcion: ");
                        String opcCuenta = sn.nextLine();

                        if (opcCuenta.equals("1")) {
                            System.out.println("Saldo actual: " + cuentaSeleccionada.getSaldo() + " " + cuentaSeleccionada.getMoneda());
                        } else if (opcCuenta.equals("2")) {
                            System.out.print("Monto a depositar: ");
                            double monto = Double.parseDouble(sn.nextLine());
                            cuentaSeleccionada.depositar(monto);
                        } else if (opcCuenta.equals("3")) {
                            System.out.print("Monto a retirar: ");
                            double monto = Double.parseDouble(sn.nextLine());
                            
                            if (cuentaSeleccionada.retirar(monto)) {
                                System.out.println("Desea comprobante? (1: Email, 2: SMS, 3: Ninguno)");
                                String opcNotif = sn.nextLine();
                                
                                Notificador n = null;
                                if (opcNotif.equals("1")) n = new EmailNotificador();
                                else if (opcNotif.equals("2")) n = new SMSNotificador();
                                
                                if (n != null) {
                                    n.enviarComprobante("Se retiraron " + monto + " " + cuentaSeleccionada.getMoneda());
                                }
                            }
                        } else if (opcCuenta.equals("4")) {
                            volver = true;
                        } else {
                            System.out.println("[ERROR] Opcion no valida.");
                        }
                    }
                }
            }
        }
        sn.close();
    }
}