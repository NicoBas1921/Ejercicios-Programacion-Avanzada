import java.util.Random;
 
public class EjercicioNumeros {
 
    public static void main(String[] args) {
 
        
        int CANTIDAD = 500;
        int MIN = 10;
        int MAX = 1000;
 
        int[] numeros = new int[CANTIDAD];
        Random random = new Random();
 
        
        for (int i = 0; i < CANTIDAD; i++) {
            numeros[i] = random.nextInt(MAX - MIN + 1) + MIN;
            // random.nextInt(991) genera un número entre 0 y 990, luego le sumamos 10
            // para obtener el rango [10, 1000]
        }
 
        // Suma
        long sumaTotal = 0;
        for (int numero : numeros) {
            sumaTotal += numero;
        }
 
        // Promedio
        double promedio = (double) sumaTotal / CANTIDAD;

        System.out.println("========================================");
        System.out.println("       RESULTADOS DEL EJERCICIO         ");
        System.out.println("========================================");
        System.out.println("Cantidad de números generados: " + CANTIDAD);
        System.out.println("Rango: entre " + MIN + " y " + MAX);
        System.out.println("----------------------------------------");
        System.out.println("Suma total:  " + sumaTotal);
        System.out.printf("Promedio:    %.2f%n", promedio);
        System.out.println("========================================");
    }
}