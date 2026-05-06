package tpcodigosolid;


/**
 * Cuenta en Pesos: Permite un descubierto (saldo negativo) de hasta 50.000.
 */

public class CuentaPesos extends CuentaBancaria {
    private double limiteDescubierto = 50000;

    public CuentaPesos(Usuario t, double s) { 
        super(t, s, "ARS"); 
    }

    @Override
    public boolean retirar(double monto) {
        if (monto > 0 && (saldo + limiteDescubierto) >= monto) {
            saldo -= monto;
            System.out.println("[EXITO] Retiro de " + monto + " ARS completado.");
            return true;
        }
        System.out.println("[ERROR] Fondos insuficientes en la cuenta en Pesos.");
        return false;
    }
}
