package estructural.Adapter;

/**
 *
 * @author Nicolas
 */

// 1. La interfaz que el sistema moderno espera usar
interface EnchufeEuropeo {
    void conectar220V();
}

// 2. Un sistema o clase vieja que funciona distinto
class EnchufeAmericanoViejo {
    public void conectar110V() {
        System.out.println("Conectado a 110V (Sistema Americano).");
    }
}

// 3. EL ADAPTADOR
class AdaptadorAmericanoAEuropeo implements EnchufeEuropeo {
    private EnchufeAmericanoViejo enchufeViejo;

    public AdaptadorAmericanoAEuropeo(EnchufeAmericanoViejo enchufeViejo) {
        this.enchufeViejo = enchufeViejo;
    }

    @Override
    public void conectar220V() {
        System.out.println("Adaptador en uso... Convirtiendo 220V a 110V...");
        enchufeViejo.conectar110V(); // Llama al método viejo internamente
    }
}

public class MainAdapter {
    public static void main(String[] args) {
        EnchufeAmericanoViejo miAparatoViejo = new EnchufeAmericanoViejo();
        
        // Usamos el adaptador para que cumpla con la nueva interfaz
        EnchufeEuropeo aparatoAdaptado = new AdaptadorAmericanoAEuropeo(miAparatoViejo);
        
        // El sistema moderno usa el método de 220V sin problemas
        aparatoAdaptado.conectar220V();
    }
}