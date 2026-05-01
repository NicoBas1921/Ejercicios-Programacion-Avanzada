package practicaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ejemplo 01 - Formulario de Registro Mejorado
 *
 * Temas del apunte practicados:
 *  - Creacion de ventanas con JFrame
 *  - Componentes: JLabel, JTextField, JButton, JCheckBox, JRadioButton, JComboBox
 *  - Layout Managers: BorderLayout, GridLayout, FlowLayout, GridBagLayout
 *  - Paneles anidados con JPanel
 *
 * Mejoras propias:
 *  - Validacion de campos al guardar
 *  - Mensaje de estado con color segun resultado
 *  - Boton limpiar que resetea todos los campos
 *  - Selector de rol con JRadioButton en ButtonGroup
 */
public class Ejemplo01_FormularioMejorado extends JFrame {

    // Declaramos los campos como atributos para acceder desde los listeners
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtEmail;
    private JTextField txtDia;
    private JTextField txtMes;
    private JTextField txtAnio;
    private JComboBox  cbPais;
    private JCheckBox  chkTerminos;
    private JRadioButton rbAdmin;
    private JRadioButton rbUsuario;
    private JRadioButton rbInvitado;
    private JLabel lblEstado;

    public Ejemplo01_FormularioMejorado() {
        super("Formulario de Registro de Usuarios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 420);
        setLocationRelativeTo(null);

        // Panel raiz con margen
        JPanel panelRaiz = new JPanel(new BorderLayout(10, 10));
        panelRaiz.setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- NORTE: Titulo ---
        JLabel lblTitulo = new JLabel("Registro de nuevo usuario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(30, 80, 160));
        panelRaiz.add(lblTitulo, BorderLayout.NORTH);

        // --- CENTRO: Formulario con GridBagLayout ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(new TitledBorder("Datos personales"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(5, 5, 5, 5);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.WEST;

        txtNombre   = new JTextField(18);
        txtApellido = new JTextField(18);
        txtEmail    = new JTextField(18);

        // Fila 0 - Nombre
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panelForm.add(txtNombre, gbc);

        // Fila 1 - Apellido
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelForm.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panelForm.add(txtApellido, gbc);

        // Fila 2 - Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelForm.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panelForm.add(txtEmail, gbc);

        // Fila 3 - Fecha de nacimiento (panel anidado con FlowLayout)
        txtDia  = new JTextField(2);
        txtMes  = new JTextField(2);
        txtAnio = new JTextField(4);
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        panelFecha.add(txtDia);
        panelFecha.add(new JLabel("/"));
        panelFecha.add(txtMes);
        panelFecha.add(new JLabel("/"));
        panelFecha.add(txtAnio);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelForm.add(new JLabel("Fecha nac.:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panelForm.add(panelFecha, gbc);

        // Fila 4 - Pais (JComboBox)
        String[] paises = {"Argentina", "Brasil", "Chile", "Uruguay", "Paraguay", "Otro"};
        cbPais = new JComboBox(paises);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panelForm.add(new JLabel("Pais:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panelForm.add(cbPais, gbc);

        // Fila 5 - Rol (JRadioButton con ButtonGroup)
        rbAdmin    = new JRadioButton("Administrador");
        rbUsuario  = new JRadioButton("Usuario", true); // seleccionado por defecto
        rbInvitado = new JRadioButton("Invitado");
        ButtonGroup grupRol = new ButtonGroup();
        grupRol.add(rbAdmin);
        grupRol.add(rbUsuario);
        grupRol.add(rbInvitado);

        JPanel panelRol = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelRol.add(rbAdmin);
        panelRol.add(rbUsuario);
        panelRol.add(rbInvitado);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        panelForm.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panelForm.add(panelRol, gbc);

        // Fila 6 - Terminos (JCheckBox)
        chkTerminos = new JCheckBox("Acepto los terminos y condiciones");
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panelForm.add(chkTerminos, gbc);
        gbc.gridwidth = 1;

        // Fila 7 - Label de estado (feedback visual)
        lblEstado = new JLabel(" ");
        lblEstado.setFont(new Font("SansSerif", Font.ITALIC, 12));
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panelForm.add(lblEstado, gbc);
        gbc.gridwidth = 1;

        panelRaiz.add(panelForm, BorderLayout.CENTER);

        // --- SUR: Botones (FlowLayout centrado) ---
        JButton btnGuardar  = new JButton("Guardar Usuario");
        JButton btnLimpiar  = new JButton("Limpiar");

        btnGuardar.setBackground(new Color(46, 139, 87));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 12));

        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                validarYGuardar();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelRaiz.add(panelBotones, BorderLayout.SOUTH);

        setContentPane(panelRaiz);
    }

    // Valida los campos y muestra el resultado en lblEstado
    private void validarYGuardar() {
        String nombre   = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String email    = txtEmail.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            lblEstado.setText("ERROR: Nombre y Apellido son obligatorios.");
            lblEstado.setForeground(Color.RED);
            return;
        }
        if (email.isEmpty() || !email.contains("@")) {
            lblEstado.setText("ERROR: Ingresa un email valido.");
            lblEstado.setForeground(Color.RED);
            return;
        }
        if (!chkTerminos.isSelected()) {
            lblEstado.setText("AVISO: Debes aceptar los terminos y condiciones.");
            lblEstado.setForeground(new Color(180, 100, 0));
            return;
        }

        // Determinar rol seleccionado
        String rol = "Usuario";
        if (rbAdmin.isSelected())    rol = "Administrador";
        if (rbInvitado.isSelected()) rol = "Invitado";

        lblEstado.setText("OK: " + nombre + " " + apellido + " registrado como " + rol + ".");
        lblEstado.setForeground(new Color(0, 120, 0));
    }

    // Resetea todos los campos al estado inicial
    private void limpiarFormulario() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtDia.setText("");
        txtMes.setText("");
        txtAnio.setText("");
        cbPais.setSelectedIndex(0);
        chkTerminos.setSelected(false);
        rbUsuario.setSelected(true);
        lblEstado.setText(" ");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Ejemplo01_FormularioMejorado().setVisible(true);
            }
        });
    }
}