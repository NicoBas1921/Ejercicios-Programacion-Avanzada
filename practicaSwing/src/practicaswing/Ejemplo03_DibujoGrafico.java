package practicaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ejemplo 03 - Dibujo de Graficos con Graphics y Graphics2D
 *
 * Temas del apunte practicados:
 *  - Sobreescritura de paintComponent(Graphics g)
 *  - Herencia de JPanel para crear componentes graficos propios
 *  - Metodos de Graphics: fillOval, fillRect, fillPolygon, drawLine,
 *    drawString, setColor, setFont
 *  - Uso de Graphics2D: antialiasing, BasicStroke, GradientPaint
 *
 * Mejoras propias:
 *  - Paisaje completo: cielo degradado, sol con rayos, montanias,
 *    casa con techo y puerta, arbol, pasto, camino
 *  - Boton para alternar entre modo Dia y Noche (repaint)
 *  - La clase PanelPaisaje extiende JPanel como indica el apunte
 */
public class Ejemplo03_DibujoGrafico extends JFrame {

    private PanelPaisaje panel;

    public Ejemplo03_DibujoGrafico() {
        super("Dibujo de Graficos con Graphics2D");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 460);
        setLocationRelativeTo(null);

        panel = new PanelPaisaje();

        // Boton para cambiar modo dia/noche
        JButton btnModo = new JButton("Cambiar a Noche");
        btnModo.setFont(new Font("SansSerif", Font.BOLD, 12));

        btnModo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.toggleModo();
                // Actualizar texto del boton segun el estado actual
                if (panel.esNoche()) {
                    btnModo.setText("Cambiar a Dia");
                } else {
                    btnModo.setText("Cambiar a Noche");
                }
            }
        });

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.add(btnModo);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);
    }

    // ============================================================
    // Clase interna que extiende JPanel y sobreescribe paintComponent
    // Esto es exactamente lo que muestra el apunte con PanelSol
    // ============================================================
    class PanelPaisaje extends JPanel {

        private boolean noche = false;

        public void toggleModo() {
            noche = !noche;
            repaint(); // Vuelve a llamar a paintComponent
        }

        public boolean esNoche() {
            return noche;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Convertimos a Graphics2D para usar funciones avanzadas
            Graphics2D g2 = (Graphics2D) g;

            // Antialiasing: suaviza los bordes de las figuras
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int horizonte = h * 2 / 3; // Linea del horizonte

            // --- CIELO con degradado (GradientPaint) ---
            Color cieloArriba, cieloAbajo;
            if (!noche) {
                cieloArriba = new Color(100, 180, 240);
                cieloAbajo  = new Color(200, 230, 255);
            } else {
                cieloArriba = new Color(5, 10, 40);
                cieloAbajo  = new Color(20, 30, 80);
            }
            GradientPaint degradadoCielo = new GradientPaint(
                0, 0, cieloArriba, 0, horizonte, cieloAbajo);
            g2.setPaint(degradadoCielo);
            g2.fillRect(0, 0, w, horizonte);

            // --- SOL o LUNA ---
            if (!noche) {
                dibujarSol(g2, w / 5, h / 5);
            } else {
                dibujarLuna(g2, w / 5, h / 5);
                dibujarEstrellas(g2, w, horizonte);
            }

            // --- NUBES ---
            g2.setColor(new Color(255, 255, 255, noche ? 60 : 210));
            dibujarNube(g2, w * 3 / 5, h / 8, 90, 38);
            dibujarNube(g2, w * 4 / 5, h / 6, 65, 28);

            // --- PASTO con degradado ---
            GradientPaint degradadoPasto = new GradientPaint(
                0, horizonte, new Color(70, 150, 50),
                0, h, new Color(35, 90, 25));
            g2.setPaint(degradadoPasto);
            g2.fillRect(0, horizonte, w, h - horizonte);

            // --- MONTANIAS ---
            dibujarMontania(g2, new int[]{0, w / 4, w / 2},
                                new int[]{horizonte, horizonte - h / 3, horizonte},
                                new Color(80, 110, 80));
            dibujarMontania(g2, new int[]{w / 3, w * 2 / 3, w},
                                new int[]{horizonte, horizonte - h * 2 / 5, horizonte},
                                new Color(100, 130, 100));

            // --- CAMINO ---
            g2.setColor(new Color(170, 140, 90));
            int[] xCamino = {w / 2 - 18, w / 2 + 18, w / 2 + 50, w / 2 - 50};
            int[] yCamino = {horizonte, horizonte, h, h};
            g2.fillPolygon(xCamino, yCamino, 4);

            // --- ARBOL ---
            dibujarArbol(g2, w / 2 - 80, horizonte);

            // --- CASA ---
            dibujarCasa(g2, w * 3 / 4, horizonte);

            // --- FIRMA ---
            g2.setColor(new Color(255, 255, 255, 160));
            g2.setFont(new Font("SansSerif", Font.ITALIC, 11));
            g2.drawString("Ejemplo03 - paintComponent + Graphics2D", 8, h - 8);
        }

        // Dibuja el sol con disco y rayos
        private void dibujarSol(Graphics2D g2, int cx, int cy) {
            // Rayos
            g2.setColor(new Color(255, 220, 60, 160));
            g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (double ang = 0; ang < 2 * Math.PI; ang += Math.PI / 8) {
                int x1 = (int) (cx + 36 * Math.cos(ang));
                int y1 = (int) (cy + 36 * Math.sin(ang));
                int x2 = (int) (cx + 55 * Math.cos(ang));
                int y2 = (int) (cy + 55 * Math.sin(ang));
                g2.drawLine(x1, y1, x2, y2);
            }
            // Disco solar
            g2.setStroke(new BasicStroke(1));
            g2.setColor(new Color(255, 210, 0));
            g2.fillOval(cx - 30, cy - 30, 60, 60);
            g2.setColor(new Color(240, 180, 0));
            g2.drawOval(cx - 30, cy - 30, 60, 60);
        }

        // Dibuja la luna con efecto de mordida
        private void dibujarLuna(Graphics2D g2, int cx, int cy) {
            g2.setColor(new Color(235, 235, 200));
            g2.fillOval(cx - 22, cy - 22, 50, 50);
            // "mordida" de la luna con el color del cielo nocturno
            g2.setColor(new Color(10, 18, 55));
            g2.fillOval(cx - 10, cy - 28, 50, 50);
        }

        // Dibuja estrellas aleatorias (posiciones fijas para que no parpadeen)
        private void dibujarEstrellas(Graphics2D g2, int w, int horizonte) {
            g2.setColor(Color.WHITE);
            int[][] pos = {{50, 20}, {120, 45}, {200, 15}, {280, 60},
                           {350, 30}, {420, 55}, {480, 20}, {160, 80},
                           {310, 10}, {430, 75}};
            for (int[] p : pos) {
                if (p[0] < w && p[1] < horizonte) {
                    g2.fillOval(p[0], p[1], 3, 3);
                }
            }
        }

        // Dibuja una nube con tres ovalos superpuestos
        private void dibujarNube(Graphics2D g2, int x, int y, int ancho, int alto) {
            g2.fillOval(x, y, ancho / 2, alto);
            g2.fillOval(x + ancho / 4, y - alto / 3, ancho * 2 / 3, alto);
            g2.fillOval(x + ancho / 2, y, ancho / 2, alto);
        }

        // Dibuja una montania con nieve en la punta
        private void dibujarMontania(Graphics2D g2, int[] xs, int[] ys, Color color) {
            g2.setColor(color);
            g2.fillPolygon(xs, ys, 3);
            // Nieve
            int puntoMedioX = xs[1];
            int puntoMedioY = ys[1];
            g2.setColor(Color.WHITE);
            int[] xN = {puntoMedioX - 22, puntoMedioX, puntoMedioX + 22};
            int[] yN = {puntoMedioY + 35, puntoMedioY, puntoMedioY + 35};
            g2.fillPolygon(xN, yN, 3);
        }

        // Dibuja un arbol con tronco y copa
        private void dibujarArbol(Graphics2D g2, int x, int base) {
            // Tronco
            g2.setColor(new Color(110, 70, 30));
            g2.fillRect(x - 6, base - 45, 12, 45);
            // Copa (dos capas para dar volumen)
            g2.setColor(new Color(40, 130, 40));
            g2.fillOval(x - 32, base - 110, 64, 75);
            g2.setColor(new Color(30, 110, 30));
            g2.fillOval(x - 26, base - 125, 52, 65);
        }

        // Dibuja una casa con paredes, techo y puerta
        private void dibujarCasa(Graphics2D g2, int x, int base) {
            // Paredes
            g2.setColor(new Color(210, 170, 110));
            g2.fillRect(x - 35, base - 60, 70, 60);
            // Techo
            g2.setColor(new Color(160, 55, 55));
            int[] xT = {x - 42, x, x + 42};
            int[] yT = {base - 60, base - 105, base - 60};
            g2.fillPolygon(xT, yT, 3);
            // Puerta
            g2.setColor(new Color(110, 70, 30));
            g2.fillRect(x - 12, base - 28, 24, 28);
            // Ventana
            g2.setColor(new Color(180, 225, 255));
            g2.fillRect(x + 14, base - 52, 16, 16);
            g2.setColor(new Color(90, 90, 90));
            g2.drawRect(x + 14, base - 52, 16, 16);
            g2.drawLine(x + 22, base - 52, x + 22, base - 36);
            g2.drawLine(x + 14, base - 44, x + 30, base - 44);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Ejemplo03_DibujoGrafico().setVisible(true);
            }
        });
    }
}