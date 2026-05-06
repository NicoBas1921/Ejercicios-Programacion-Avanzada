package tpcodigosolid;

// PRINCIPIO APLICADO: OCP (Abierto/Cerrado)

public class EmailNotificador implements Notificador {
    @Override
    public void enviarComprobante(String msg) {
        System.out.println("[EMAIL ENVIADO] -> " + msg);
    }
}
