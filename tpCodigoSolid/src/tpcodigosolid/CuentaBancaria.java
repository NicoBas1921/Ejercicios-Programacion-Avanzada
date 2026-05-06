package tpcodigosolid;

/**
 * PRINCIPIO APLICADO: OCP y SRP.
 * Es una clase abstracta base. No se modifica, se extiende.
 */
public abstract class CuentaBancaria {
    protected Usuario titular;
    protected double saldo;
    protected String moneda; // ARS o USD

    public CuentaBancaria(Usuario titular, double saldoInicial, String moneda) {
        this.titular = titular;
        this.saldo = saldoInicial;
        this.moneda = moneda;
    }

    public void depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
            System.out.println("[EXITO] Deposito realizado. Nuevo saldo: " + saldo + " " + moneda);
        } else {
            System.out.println("[ERROR] El monto debe ser mayor a cero.");
        }
    }

    // Obligamos a las clases hijas a implementar sus reglas de retiro
    public abstract boolean retirar(double monto);

    public double getSaldo() { return saldo; }
    public String getMoneda() { return moneda; }
    public Usuario getTitular() { return titular; }
}