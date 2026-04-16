package comportamiento.strategy;

/**
 *
 * @author Nicolas
 */
// 1. La Interfaz Estrategia
interface EstrategiaEnvio {
    double calcularCosto(double peso);
}

// 2. Estrategias Concretas
class EnvioNormal implements EstrategiaEnvio {
    @Override
    public double calcularCosto(double peso) {
        return peso * 50.0; // $50 por Kilo
    }
}

class EnvioExpress implements EstrategiaEnvio {
    @Override
    public double calcularCosto(double peso) {
        return peso * 100.0 + 200.0; // $100 por Kilo + recargo de urgencia
    }
}

// 3. El Contexto (El "Carrito de Compras" que usa la estrategia)
class CalculadoraDeEnvios {
    private EstrategiaEnvio estrategiaActual;

    // Método para cambiar la estrategia dinámicamente
    public void setEstrategia(EstrategiaEnvio nuevaEstrategia) {
        this.estrategiaActual = nuevaEstrategia;
    }

    public void mostrarPresupuesto(double peso) {
        double costoFinal = estrategiaActual.calcularCosto(peso);
        System.out.println("El costo del envio para " + peso + " kg es: $" + costoFinal);
    }
}

// Archivo: MainStrategy.java
public class MainStrategy {
    public static void main(String[] args) {
        CalculadoraDeEnvios calculadora = new CalculadoraDeEnvios();
        
        System.out.println("--- Seleccionando Envio Normal ---");
        calculadora.setEstrategia(new EnvioNormal());
        calculadora.mostrarPresupuesto(5.0); // 5 kilos
        
        System.out.println("--- El cliente cambio a Envio Express ---");
        calculadora.setEstrategia(new EnvioExpress());
        calculadora.mostrarPresupuesto(5.0);
    }
}
