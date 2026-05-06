package tpcodigosolid;

/**
 * Cuenta en Dolares: NO permite descubierto. Estricta con el saldo.
 */

public class CuentaDolares extends CuentaBancaria {
    
    public CuentaDolares(Usuario t, double s) { 
        super(t, s, "USD"); 
    }

    @Override
    public boolean retirar(double monto) {
        if (monto > 0 && saldo >= monto) {
            saldo -= monto;
            System.out.println("[EXITO] Retiro de " + monto + " USD completado.");
            return true;
        }
        System.out.println("[ERROR] Saldo insuficiente en la cuenta en Dolares.");
        return false;
    }
}
