import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class FormularioSwing extends JFrame {

    private JTextField txtNombre, txtApellido, txtDni, txtPasaporte, txtTelefono, txtCp, txtDomicilio;
    private JButton btnValidar, btnLimpiar, btnCerrar;

    public FormularioSwing() {
        // 1. Apariencia del Sistema (Lo hace más "bonito")
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        setTitle("UDA - Registro de Contactos");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel Principal con margen
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(panelPrincipal);

        // --- PANEL DE CAMPOS (GridBagLayout para mejor control) ---
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos Personales", TitledBorder.LEFT, TitledBorder.TOP));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10); // Espaciado entre componentes

        // Inicializar componentes
        txtNombre = new JTextField(20);
        txtApellido = new JTextField(20);
        txtDni = new JTextField(20);
        txtPasaporte = new JTextField(20);
        txtTelefono = new JTextField(20);
        txtCp = new JTextField(20);
        txtDomicilio = new JTextField(20);

        // Agregar al panel (Fila 0 a 6)
        agregarFila(panelCampos, "Nombre:", txtNombre, gbc, 0);
        agregarFila(panelCampos, "Apellido:", txtApellido, gbc, 1);
        agregarFila(panelCampos, "DNI:", txtDni, gbc, 2);
        agregarFila(panelCampos, "Pasaporte:", txtPasaporte, gbc, 3);
        agregarFila(panelCampos, "Teléfono:", txtTelefono, gbc, 4);
        agregarFila(panelCampos, "Cód. Postal:", txtCp, gbc, 5);
        agregarFila(panelCampos, "Domicilio:", txtDomicilio, gbc, 6);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        // --- PANEL DE BOTONES ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnValidar = new JButton("Validar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrar = new JButton("Cerrar");
        
        panelBotones.add(btnValidar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCerrar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        configurarEventos();
    }

    // Método auxiliar para no repetir código de diseño
    private void agregarFila(JPanel p, String label, JTextField tf, GridBagConstraints gbc, int fila) {
        gbc.gridy = fila;
        gbc.gridx = 0; p.add(new JLabel(label), gbc);
        gbc.gridx = 1; p.add(tf, gbc);
    }

    private void configurarEventos() {
        btnCerrar.addActionListener(e -> dispose());
        
        btnLimpiar.addActionListener(e -> {
            txtNombre.setText(""); txtApellido.setText(""); txtDni.setText("");
            txtPasaporte.setText(""); txtTelefono.setText(""); txtCp.setText("");
            txtDomicilio.setText("");
        });

        // VALIDACIONES EN TIEMPO REAL (Nivel de caracteres)
        
        // Letras solamente y máx 20
        KeyAdapter soloLetras = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && c != ' ' || ((JTextField)e.getSource()).getText().length() >= 20) e.consume();
            }
        };
        txtNombre.addKeyListener(soloLetras);
        txtApellido.addKeyListener(soloLetras);

        // DNI: Solo números y máx 8
        txtDni.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) || txtDni.getText().length() >= 8) e.consume();
            }
        });

        // CP: Solo números y máx 4
        txtCp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) || txtCp.getText().length() >= 4) e.consume();
            }
        });

        // Domicilio: Máx 50
        txtDomicilio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (txtDomicilio.getText().length() >= 50) e.consume();
            }
        });

        // VALIDACIÓN AL PULSAR EL BOTÓN (Nivel de campo)
        btnValidar.addActionListener(e -> {
            try {
                validarTodo();
                JOptionPane.showMessageDialog(this, "Datos cargados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void validarTodo() throws Exception {
        // Validar DNI vs Pasaporte
        boolean tieneDni = !txtDni.getText().isEmpty();
        boolean tienePas = !txtPasaporte.getText().isEmpty();

        if (tieneDni && tienePas) throw new Exception("Debe ingresar DNI o Pasaporte, pero no ambos.");
        if (!tieneDni && !tienePas) throw new Exception("Debe completar DNI o Pasaporte.");

        // Validar Rango DNI
        if (tieneDni) {
            int dniVal = Integer.parseInt(txtDni.getText());
            if (dniVal < 10000000 || dniVal > 60000000) throw new Exception("DNI debe estar entre 10.000.000 y 60.000.000");
        }

        // Validar Pasaporte (Ej: N39392288)
        if (tienePas) {
            String p = txtPasaporte.getText();
            if (!Character.isLetter(p.charAt(0)) || p.length() < 9) throw new Exception("Pasaporte inválido. Ejemplo: N39392288");
            int numPas = Integer.parseInt(p.substring(1));
            if (numPas < 10000000 || numPas > 60000000) throw new Exception("Número de pasaporte fuera de rango.");
        }

        // Validar CP (4 dígitos)
        if (txtCp.getText().length() != 4) throw new Exception("El Código Postal debe tener 4 dígitos.");

        // Validar Teléfono (Regex: permite números, +, (, ), -)
        String tel = txtTelefono.getText();
        if (!tel.matches("[0-9+() \\-]+") || tel.length() < 6) {
            throw new Exception("Formato de teléfono inválido. Use números, +, () o -");
        }
    }
}