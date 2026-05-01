package practicaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Ejemplo 02 - Manejo de Eventos y Cuadros de Dialogo
 *
 * Temas del apunte practicados:
 *  - Eventos: ActionListener, KeyListener, MouseListener, MouseMotionListener
 *  - Cuadros de dialogo: JOptionPane (showMessageDialog, showInputDialog,
 *    showConfirmDialog) con los distintos tipos de icono
 *  - Acceso a componentes desde el listener (atributos de instancia)
 *  - Clases internas anonimas y lambda-style con ActionListener
 *
 * Mejoras propias:
 *  - Seccion de KeyListener con deteccion de tecla en tiempo real
 *  - Zona de mouse interactiva con cambio de color y contador de clics
 *  - Historial de dialogos usados mostrado en un JTextArea
 *  - Botones de dialogo agrupados con GridLayout
 */
public class Ejemplo02_EventosYDialogos extends JFrame {

    private JTextField  txtNombre;
    private JLabel      lblTecla;
    private JLabel      lblMouse;
    private JLabel      lblClics;
    private JTextArea   areaHistorial;
    private int         contadorClics = 0;

    public Ejemplo02_EventosYDialogos() {
        super("Eventos y Dialogos - Practica Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 520);
        setLocationRelativeTo(null);

        JPanel panelRaiz = new JPanel(new BorderLayout(10, 10));
        panelRaiz.setBorder(new EmptyBorder(12, 12, 12, 12));

        // ===== SECCION 1: ActionListener + JOptionPane =====
        JPanel secDialogos = new JPanel(new BorderLayout(5, 5));
        secDialogos.setBorder(new TitledBorder("1. ActionListener y JOptionPane"));

        JPanel filaEntrada = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        filaEntrada.add(new JLabel("Tu nombre:"));
        txtNombre = new JTextField(14);
        filaEntrada.add(txtNombre);

        // Grilla de botones de dialogo con GridLayout
        JPanel grillaBotones = new JPanel(new GridLayout(2, 3, 6, 6));
        grillaBotones.setBorder(new EmptyBorder(4, 0, 4, 0));

        JButton btnInfo      = crearBoton("Informacion",   new Color(60, 130, 200));
        JButton btnAviso     = crearBoton("Advertencia",   new Color(200, 150, 0));
        JButton btnError     = crearBoton("Error",         new Color(190, 50, 50));
        JButton btnConfirm   = crearBoton("Confirmacion",  new Color(60, 160, 90));
        JButton btnInput     = crearBoton("Entrada",       new Color(130, 60, 180));
        JButton btnSaludar   = crearBoton("Saludar",       new Color(30, 100, 180));

        grillaBotones.add(btnInfo);
        grillaBotones.add(btnAviso);
        grillaBotones.add(btnError);
        grillaBotones.add(btnConfirm);
        grillaBotones.add(btnInput);
        grillaBotones.add(btnSaludar);

        // ActionListeners de cada boton de dialogo
        btnInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    Ejemplo02_EventosYDialogos.this,
                    "Este es un mensaje de informacion general.",
                    "Informacion", JOptionPane.INFORMATION_MESSAGE);
                registrar("Dialogo de Informacion mostrado.");
            }
        });

        btnAviso.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    Ejemplo02_EventosYDialogos.this,
                    "Esta accion podria tener consecuencias.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                registrar("Dialogo de Advertencia mostrado.");
            }
        });

        btnError.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    Ejemplo02_EventosYDialogos.this,
                    "Ocurrio un error inesperado en el sistema.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                registrar("Dialogo de Error mostrado.");
            }
        });

        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int resp = JOptionPane.showConfirmDialog(
                    Ejemplo02_EventosYDialogos.this,
                    "Estas seguro de realizar esta accion?",
                    "Confirmacion", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                String resultado = (resp == JOptionPane.YES_OPTION) ? "SI" : "NO";
                registrar("Confirmacion → respuesta: " + resultado);
            }
        });

        btnInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String entrada = JOptionPane.showInputDialog(
                    Ejemplo02_EventosYDialogos.this,
                    "Ingresa un valor:",
                    "Entrada de datos", JOptionPane.PLAIN_MESSAGE);
                if (entrada != null && !entrada.trim().isEmpty()) {
                    registrar("Entrada recibida: \"" + entrada.trim() + "\"");
                } else {
                    registrar("Entrada cancelada o vacia.");
                }
            }
        });

        // Saludar usa el campo txtNombre (Posibilidad 1 del apunte: atributo de instancia)
        btnSaludar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText().trim();
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        Ejemplo02_EventosYDialogos.this,
                        "El campo de nombre esta vacio.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(
                        Ejemplo02_EventosYDialogos.this,
                        "Hola, " + nombre + "! Bienvenido al sistema.",
                        "Saludo", JOptionPane.INFORMATION_MESSAGE);
                    registrar("Saludo enviado a: " + nombre);
                    txtNombre.setText("");
                }
            }
        });

        secDialogos.add(filaEntrada, BorderLayout.NORTH);
        secDialogos.add(grillaBotones, BorderLayout.CENTER);

        // ===== SECCION 2: KeyListener =====
        JPanel secTeclado = new JPanel(new BorderLayout(5, 5));
        secTeclado.setBorder(new TitledBorder("2. KeyListener"));

        JTextField campoClave = new JTextField();
        campoClave.setFont(new Font("Monospaced", Font.PLAIN, 13));
        lblTecla = new JLabel("Presiona una tecla...");
        lblTecla.setFont(new Font("SansSerif", Font.ITALIC, 12));

        campoClave.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                lblTecla.setText("Tecla: " + KeyEvent.getKeyText(e.getKeyCode())
                    + "   Codigo: " + e.getKeyCode());
            }
        });

        secTeclado.add(campoClave, BorderLayout.CENTER);
        secTeclado.add(lblTecla, BorderLayout.SOUTH);

        // ===== SECCION 3: MouseListener + MouseMotionListener =====
        JPanel secMouse = new JPanel(new BorderLayout(5, 5));
        secMouse.setBorder(new TitledBorder("3. MouseListener y MouseMotionListener"));

        final JPanel zonaMouse = new JPanel();
        zonaMouse.setPreferredSize(new Dimension(0, 65));
        zonaMouse.setBackground(new Color(220, 235, 255));
        zonaMouse.setLayout(new BorderLayout());
        zonaMouse.add(new JLabel("  Interactua con el mouse aqui", SwingConstants.CENTER));

        lblMouse = new JLabel("Posicion: (-, -)   Estado: esperando...");
        lblMouse.setFont(new Font("Monospaced", Font.PLAIN, 12));
        lblClics  = new JLabel("Clics: 0");
        lblClics.setFont(new Font("SansSerif", Font.BOLD, 12));

        JButton btnResetClics = new JButton("Resetear clics");
        btnResetClics.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                contadorClics = 0;
                lblClics.setText("Clics: 0");
                registrar("Contador de clics reseteado.");
            }
        });

        zonaMouse.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                contadorClics++;
                lblClics.setText("Clics: " + contadorClics);
                lblMouse.setText("Posicion: (" + e.getX() + ", " + e.getY() + ")   Estado: PRESIONADO");
                zonaMouse.setBackground(new Color(180, 210, 255));
            }
            public void mouseReleased(MouseEvent e) {
                lblMouse.setText("Posicion: (" + e.getX() + ", " + e.getY() + ")   Estado: SOLTADO");
                zonaMouse.setBackground(new Color(220, 235, 255));
            }
            public void mouseEntered(MouseEvent e) {
                zonaMouse.setBackground(new Color(200, 225, 255));
            }
            public void mouseExited(MouseEvent e) {
                zonaMouse.setBackground(new Color(220, 235, 255));
                lblMouse.setText("Posicion: (-, -)   Estado: fuera de zona");
            }
        });

        zonaMouse.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                lblMouse.setText("Posicion: (" + e.getX() + ", " + e.getY() + ")   Estado: movimiento");
            }
        });

        JPanel infoMouse = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 3));
        infoMouse.add(lblClics);
        infoMouse.add(btnResetClics);

        secMouse.add(zonaMouse, BorderLayout.CENTER);
        secMouse.add(lblMouse, BorderLayout.NORTH);
        secMouse.add(infoMouse, BorderLayout.SOUTH);

        // ===== HISTORIAL (JTextArea) =====
        areaHistorial = new JTextArea(5, 40);
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaHistorial.setBackground(new Color(245, 248, 255));
        JScrollPane scrollHistorial = new JScrollPane(areaHistorial);
        scrollHistorial.setBorder(new TitledBorder("Historial de acciones"));

        // Ensamblado general con BorderLayout + paneles apilados
        JPanel centro = new JPanel(new GridLayout(3, 1, 0, 8));
        centro.add(secDialogos);
        centro.add(secTeclado);
        centro.add(secMouse);

        panelRaiz.add(centro, BorderLayout.CENTER);
        panelRaiz.add(scrollHistorial, BorderLayout.SOUTH);

        setContentPane(panelRaiz);
    }

    // Agrega una linea al JTextArea de historial
    private void registrar(String mensaje) {
        areaHistorial.append("  > " + mensaje + "\n");
        // Scroll automatico al final
        areaHistorial.setCaretPosition(areaHistorial.getDocument().getLength());
    }

    // Metodo auxiliar para crear botones con estilo uniforme
    private JButton crearBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Ejemplo02_EventosYDialogos().setVisible(true);
            }
        });
    }
}