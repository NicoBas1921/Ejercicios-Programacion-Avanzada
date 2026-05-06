package tpcodigosolid;

// PRINCIPIO APLICADO: OCP (Abierto/Cerrado)
public class SMSNotificador implements Notificador {
    @Override
    public void enviarComprobante(String msg) {
        System.out.println("[SMS ENVIADO] -> " + msg);
    }
}
