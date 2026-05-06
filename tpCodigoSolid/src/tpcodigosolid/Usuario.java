/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpcodigosolid;

/**
 *
 * @author Nicolas
 */

/**
 * PRINCIPIO APLICADO: SRP (Responsabilidad Unica).
 * Maneja unicamente los datos y la seguridad del cliente.
 */
public class Usuario {
    private String nombre;
    private String password;

    public Usuario(String nombre, String password) {
        this.nombre = nombre;
        this.password = password;
    }

    public String getNombre() { 
        return nombre; 
    }
    
    // Nueva funcionalidad: Validar credenciales
    public boolean validarLogin(String user, String pass) {
        return this.nombre.equals(user) && this.password.equals(pass);
    }
    
    public void blanquearPassword(String nueva) {
        this.password = nueva;
        System.out.println("[EXITO] Password actualizada para: " + nombre);
    }
}