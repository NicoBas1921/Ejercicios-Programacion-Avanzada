package creacional.Builder;
/**
 *
 * @author Nicolas
 */
public class Computadora {
    private String procesador;
    private int memoriaRAM;
    private boolean tieneTarjetaGrafica;
    
    // El constructor es privado, solo el Builder puede crear la Computadora
    private Computadora(ComputadoraBuilder builder) {
        this.procesador = builder.procesador;
        this.memoriaRAM = builder.memoriaRAM;
        this.tieneTarjetaGrafica = builder.tieneTarjetaGrafica;
    }

    public void mostrarEspecificaciones() {
        System.out.println("PC -> Procesador: " + procesador + ", RAM: " + memoriaRAM + "GB, GPU: " + tieneTarjetaGrafica);
    }

    // --- CLASE BUILDER INTERNA ---
    public static class ComputadoraBuilder {
        private String procesador;
        private int memoriaRAM;
        private boolean tieneTarjetaGrafica;

        public ComputadoraBuilder setProcesador(String procesador) {
            this.procesador = procesador;
            return this;
        }

        public ComputadoraBuilder setMemoriaRAM(int memoriaRAM) {
            this.memoriaRAM = memoriaRAM;
            return this;
        }

        public ComputadoraBuilder setTarjetaGrafica(boolean tiene) {
            this.tieneTarjetaGrafica = tiene;
            return this;
        }

        public Computadora construir() {
            return new Computadora(this);
        }
    }
}
