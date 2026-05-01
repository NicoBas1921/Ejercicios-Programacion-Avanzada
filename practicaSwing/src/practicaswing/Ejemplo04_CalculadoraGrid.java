package practicaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ejemplo 04 - Calculadora con GridLayout
 *
 * Temas del apunte practicados:
 *  - GridLayout para la grilla de botones (4 filas x 4 columnas)
 *  - BorderLayout para armar la estructura general de la ventana
 *  - Manejo de eventos con ActionListener
 *  - Acceso a componentes desde el listener (atributos de instancia)
 *
 * Mejoras propias:
 *  - Display con historial de operacion visible
 *  - Soporte de decimales y signo negativo
 *  - Boton de borrar ultimo digito (C / Bksp)
 *  - Division por cero controlada con mensaje de error
 *  - Estilos de color diferenciados por tipo de boton
 */
public class Ejemplo04_CalculadoraGrid extends JFrame implements ActionListener {

    // Display principal donde se muestra el numero actual
    private JTextField  display;
    // Label secundario que muestra la operacion en curso
    private JLabel      lblOperacion;

    // Estado interno de la calculadora
    private double  acumulador  = 0;
    private String  operador    = "";
    private boolean esperandoNumero = true; // true = proximo digito empieza numero nuevo

    public Ejemplo04_CalculadoraGrid() {
        super("Calculadora - GridLayout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 420);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panelRaiz = new JPanel(new BorderLayout(0, 8));
        panelRaiz.setBorder(new EmptyBorder(12, 12, 12, 12));

        // --- DISPLAY ---
        lblOperacion = new JLabel(" ", SwingConstants.RIGHT);
        lblOperacion.setFont(new Font("Monospaced", Font.PLAIN, 13));
        lblOperacion.setForeground(new Color(120, 120, 120));

        display = new JTextField("0");
        display.setFont(new Font("Monospaced", Font.BOLD, 30));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(new Color(245, 248, 255));
        display.setBorder(new LineBorder(new Color(160, 190, 220), 2));

        JPanel panelDisplay = new JPanel(new BorderLayout(0, 2));
        panelDisplay.add(lblOperacion, BorderLayout.NORTH);
        panelDisplay.add(display, BorderLayout.CENTER);

        panelRaiz.add(panelDisplay, BorderLayout.NORTH);

        // --- GRILLA DE BOTONES con GridLayout ---
        // 5 filas x 4 columnas
        JPanel grilla = new JPanel(new GridLayout(5, 4, 6, 6));

        // Definimos las etiquetas en orden (de arriba a abajo, izquierda a derecha)
        String[] etiquetas = {
            "C",   "+/-",  "%",   "/",
            "7",   "8",    "9",   "*",
            "4",   "5",    "6",   "-",
            "1",   "2",    "3",   "+",
            "Bksp","0",    ".",   "="
        };

        for (int i = 0; i < etiquetas.length; i++) {
            String lbl = etiquetas[i];
            JButton btn = new JButton(lbl);
            btn.setFont(new Font("SansSerif", Font.BOLD, 18));
            btn.setFocusPainted(false);

            // Colores segun tipo de boton
            if (lbl.equals("=")) {
                btn.setBackground(new Color(50, 120, 200));
                btn.setForeground(Color.WHITE);
            } else if (lbl.equals("/") || lbl.equals("*") ||
                       lbl.equals("-") || lbl.equals("+")) {
                btn.setBackground(new Color(230, 160, 40));
                btn.setForeground(Color.WHITE);
            } else if (lbl.equals("C") || lbl.equals("Bksp") ||
                       lbl.equals("+/-") || lbl.equals("%")) {
                btn.setBackground(new Color(160, 160, 160));
                btn.setForeground(Color.WHITE);
            } else {
                // Digitos
                btn.setBackground(new Color(230, 230, 230));
                btn.setForeground(Color.BLACK);
            }

            btn.addActionListener(this);
            grilla.add(btn);
        }

        panelRaiz.add(grilla, BorderLayout.CENTER);
        setContentPane(panelRaiz);
    }

    // Un solo ActionListener para todos los botones (como indica el apunte)
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        String txt = display.getText();

        if (cmd.equals("C")) {
            // Reinicio total
            display.setText("0");
            lblOperacion.setText(" ");
            acumulador = 0;
            operador   = "";
            esperandoNumero = true;

        } else if (cmd.equals("Bksp")) {
            // Borrar ultimo digito
            if (txt.length() > 1) {
                display.setText(txt.substring(0, txt.length() - 1));
            } else {
                display.setText("0");
                esperandoNumero = true;
            }

        } else if (cmd.equals("+/-")) {
            // Cambiar signo
            if (!txt.equals("0")) {
                if (txt.startsWith("-")) {
                    display.setText(txt.substring(1));
                } else {
                    display.setText("-" + txt);
                }
            }

        } else if (cmd.equals("%")) {
            // Porcentaje
            double val = Double.parseDouble(txt) / 100;
            display.setText(formatear(val));
            esperandoNumero = true;

        } else if (cmd.equals("/") || cmd.equals("*") ||
                   cmd.equals("-") || cmd.equals("+")) {
            // Operador: guardamos acumulador y operador
            acumulador = Double.parseDouble(display.getText());
            operador   = cmd;
            lblOperacion.setText(formatear(acumulador) + " " + cmd);
            esperandoNumero = true;

        } else if (cmd.equals("=")) {
            // Calcular resultado
            double b = Double.parseDouble(display.getText());
            double resultado = calcular(acumulador, b, operador);
            lblOperacion.setText(formatear(acumulador) + " " + operador
                                 + " " + formatear(b) + " =");
            display.setText(formatear(resultado));
            acumulador = resultado;
            operador   = "";
            esperandoNumero = true;

        } else if (cmd.equals(".")) {
            // Punto decimal
            if (esperandoNumero) {
                display.setText("0.");
                esperandoNumero = false;
            } else if (!txt.contains(".")) {
                display.setText(txt + ".");
            }

        } else {
            // Digito numerico
            if (esperandoNumero) {
                display.setText(cmd);
                esperandoNumero = false;
            } else {
                if (txt.equals("0")) {
                    display.setText(cmd);
                } else {
                    display.setText(txt + cmd);
                }
            }
        }
    }

    // Realiza la operacion matematica
    private double calcular(double a, double b, String op) {
        if (op.equals("+")) return a + b;
        if (op.equals("-")) return a - b;
        if (op.equals("*")) return a * b;
        if (op.equals("/")) {
            if (b == 0) {
                JOptionPane.showMessageDialog(this,
                    "No se puede dividir por cero.",
                    "Error de calculo", JOptionPane.ERROR_MESSAGE);
                return 0;
            }
            return a / b;
        }
        return b;
    }

    // Formatea el numero: entero si no tiene decimales, decimal si los tiene
    private String formatear(double val) {
        if (val == Math.floor(val) && !Double.isInfinite(val)) {
            return String.valueOf((long) val);
        }
        return String.valueOf(val);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Ejemplo04_CalculadoraGrid().setVisible(true);
            }
        });
    }
}