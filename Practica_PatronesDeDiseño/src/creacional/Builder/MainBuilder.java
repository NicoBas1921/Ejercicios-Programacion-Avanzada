package creacional.Builder;
/**
 *
 * @author Nicolas
 */
public class MainBuilder {
  public static void main(String[] args) {
        // Creamos una PC paso a paso usando el Builder
        Computadora pcGamer = new Computadora.ComputadoraBuilder()
                .setProcesador("Intel Core i9")
                .setMemoriaRAM(32)
                .setTarjetaGrafica(true)
                .construir();
                
        pcGamer.mostrarEspecificaciones();
    }  
}
