package com.minipc.t1sominipc.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MiniPCFrame extends JFrame {

    // Paleta de colores - azul oscuro
    private static final Color BG_DARK = new Color(11, 29, 51);
    private static final Color BG_CARD = new Color(16, 42, 71);
    private static final Color BG_BUTTON_PRIMARY = new Color(30, 92, 151);
    private static final Color BG_BUTTON_SECONDARY = new Color(18, 58, 94);
    private static final Color TEXT_LIGHT = new Color(232, 238, 247);
    private static final Color TEXT_MUTED = new Color(143, 166, 196);
    private static final Color ACCENT_GREEN = new Color(111, 207, 151);
    private static final Color ACCENT_RED = new Color(240, 166, 166);
    private static final Color BORDER_COLOR = new Color(30, 63, 95);

    // Componentes que el Controller va a necesitar
    private JButton btnCargarArchivo;
    private JButton btnPasoAPaso;
    private JButton btnEjecutarTodo;
    private JButton btnConfigurarMemoria;
    private JButton btnLimpiarReset;

    private JDialog dialogoConfigMemoria;
    private JSpinner spinnerTamanoRAM;
    private JSpinner spinnerKernel;
    private JButton btnAplicarConfig;

    private DefaultTableModel modeloPrograma;
    private DefaultTableModel modeloMemoria;
    private JTable tablaPrograma;
    private JTable tablaMemoria;

    private JLabel lblPID;
    private JLabel lblEstadoProceso;
    private JLabel lblPC, lblIR, lblAC, lblAX, lblBX, lblCX, lblDX;

    public MiniPCFrame() {
        setTitle("Mini PC Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(12, 12));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(BG_DARK);
        panelSuperior.add(crearEncabezado());
        panelSuperior.add(Box.createVerticalStrut(12));
        panelSuperior.add(crearBarraBotones());

        add(panelSuperior, BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelProceso(), BorderLayout.EAST);

        crearDialogoConfigMemoria();
    }

    // ===================== ENCABEZADO =====================

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);

        JLabel titulo = new JLabel("Mini PC Simulator");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(TEXT_LIGHT);

        JLabel subtitulo = new JLabel("Principios de Sistemas Operativos");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitulo.setForeground(TEXT_MUTED);

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setBackground(BG_DARK);
        textos.add(titulo);
        textos.add(subtitulo);

        panel.add(textos, BorderLayout.WEST);
        return panel;
    }

    // ===================== BARRA DE BOTONES HORIZONTAL =====================

    private JPanel crearBarraBotones() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 0));
        panel.setBackground(BG_DARK);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        btnCargarArchivo = crearBoton("Cargar archivo .asm", BG_BUTTON_PRIMARY, TEXT_LIGHT);
        btnPasoAPaso = crearBoton("Paso a paso", BG_BUTTON_SECONDARY, TEXT_LIGHT);
        btnEjecutarTodo = crearBoton("Ejecutar todo", BG_BUTTON_SECONDARY, ACCENT_GREEN);
        btnConfigurarMemoria = crearBoton("Configurar memoria", BG_BUTTON_SECONDARY, TEXT_LIGHT);
        btnLimpiarReset = crearBoton("Limpiar / Reset", BG_BUTTON_SECONDARY, ACCENT_RED);

        panel.add(btnCargarArchivo);
        panel.add(btnPasoAPaso);
        panel.add(btnEjecutarTodo);
        panel.add(btnConfigurarMemoria);
        panel.add(btnLimpiarReset);

        // Abrir el diálogo es puramente visual, no requiere al Controller
        btnConfigurarMemoria.addActionListener(e -> dialogoConfigMemoria.setVisible(true));

        return panel;
    }

    // ===================== VENTANA EMERGENTE DE CONFIGURACIÓN =====================

    private void crearDialogoConfigMemoria() {
        dialogoConfigMemoria = new JDialog(this, "Configurar memoria", true);
        dialogoConfigMemoria.setSize(320, 260);
        dialogoConfigMemoria.setLocationRelativeTo(this);
        dialogoConfigMemoria.getContentPane().setBackground(BG_CARD);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Configuración de memoria RAM");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setForeground(TEXT_LIGHT);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTamano = crearEtiquetaCampo("Tamaño total de RAM");
        lblTamano.setAlignmentX(Component.LEFT_ALIGNMENT);
        spinnerTamanoRAM = new JSpinner(new SpinnerNumberModel(256, 32, 1024, 32));
        spinnerTamanoRAM.setAlignmentX(Component.LEFT_ALIGNMENT);
        spinnerTamanoRAM.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel lblKernel = crearEtiquetaCampo("Espacio para Kernel");
        lblKernel.setAlignmentX(Component.LEFT_ALIGNMENT);
        spinnerKernel = new JSpinner(new SpinnerNumberModel(64, 8, 512, 8));
        spinnerKernel.setAlignmentX(Component.LEFT_ALIGNMENT);
        spinnerKernel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        btnAplicarConfig = crearBoton("Aplicar configuración", BG_BUTTON_PRIMARY, TEXT_LIGHT);
        btnAplicarConfig.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Cerrar el diálogo es responsabilidad de la vista; leer los valores es del Controller
        btnAplicarConfig.addActionListener(e -> dialogoConfigMemoria.setVisible(false));

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(16));
        panel.add(lblTamano);
        panel.add(spinnerTamanoRAM);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblKernel);
        panel.add(spinnerKernel);
        panel.add(Box.createVerticalStrut(18));
        panel.add(btnAplicarConfig);

        dialogoConfigMemoria.add(panel);
    }

    // ===================== TABLAS CENTRALES =====================

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 0));
        panel.setBackground(BG_DARK);

        panel.add(crearPanelPrograma());
        panel.add(crearPanelMemoria());

        return panel;
    }

    private JPanel crearPanelPrograma() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG_DARK);

        panel.add(crearEtiquetaSeccion("PROGRAMA CARGADO"), BorderLayout.NORTH);

        modeloPrograma = new DefaultTableModel(new Object[]{"Instrucción ASM", "Código binario"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaPrograma = crearTablaEstilizada(modeloPrograma);

        JScrollPane scroll = new JScrollPane(tablaPrograma);
        scroll.getViewport().setBackground(BG_CARD);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelMemoria() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG_DARK);

        panel.add(crearEtiquetaSeccion("MEMORIA PRINCIPAL (RAM)"), BorderLayout.NORTH);

        modeloMemoria = new DefaultTableModel(new Object[]{"Posición", "Instrucción ASM", "Valor en memoria"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaMemoria = crearTablaEstilizada(modeloMemoria);

        JScrollPane scroll = new JScrollPane(tablaMemoria);
        scroll.getViewport().setBackground(BG_CARD);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JTable crearTablaEstilizada(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setBackground(BG_CARD);
        tabla.setForeground(TEXT_LIGHT);
        tabla.setGridColor(BORDER_COLOR);
        tabla.setRowHeight(24);
        tabla.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tabla.setSelectionBackground(BG_BUTTON_PRIMARY);
        tabla.setSelectionForeground(TEXT_LIGHT);

        tabla.getTableHeader().setBackground(BG_DARK);
        tabla.getTableHeader().setForeground(TEXT_MUTED);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        tabla.setFillsViewportHeight(true);

        return tabla;
    }

    // ===================== PANEL DE PROCESO =====================

    private JPanel crearPanelProceso() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_DARK);
        panel.setPreferredSize(new Dimension(240, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

        panel.add(crearEtiquetaSeccion("PROCESO"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearTarjetaProceso());
        panel.add(Box.createVerticalStrut(20));
        panel.add(crearEtiquetaSeccion("REGISTROS CPU"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearGridRegistros());
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel crearTarjetaProceso() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(240, 100));

        lblPID = new JLabel("PID --");
        lblPID.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblPID.setForeground(TEXT_LIGHT);

        lblEstadoProceso = new JLabel("Esperando archivo");
        lblEstadoProceso.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblEstadoProceso.setForeground(TEXT_MUTED);

        panel.add(lblPID);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblEstadoProceso);

        return panel;
    }

    private JPanel crearGridRegistros() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBackground(BG_DARK);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(240, 220));

        lblPC = crearValorRegistro();
        lblIR = crearValorRegistro();
        lblAC = crearValorRegistro();
        lblAX = crearValorRegistro();
        lblBX = crearValorRegistro();
        lblCX = crearValorRegistro();
        lblDX = crearValorRegistro();

        panel.add(crearTarjetaRegistro("PC", lblPC));
        panel.add(crearTarjetaRegistro("IR", lblIR));
        panel.add(crearTarjetaRegistro("AC", lblAC));
        panel.add(crearTarjetaRegistro("AX", lblAX));
        panel.add(crearTarjetaRegistro("BX", lblBX));
        panel.add(crearTarjetaRegistro("CX", lblCX));
        panel.add(crearTarjetaRegistro("DX", lblDX));

        return panel;
    }

    private JPanel crearTarjetaRegistro(String nombre, JLabel valor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblNombre.setForeground(TEXT_MUTED);

        panel.add(lblNombre);
        panel.add(valor);
        return panel;
    }

    private JLabel crearValorRegistro() {
        JLabel lbl = new JLabel("0");
        lbl.setFont(new Font("Monospaced", Font.BOLD, 14));
        lbl.setForeground(ACCENT_GREEN);
        return lbl;
    }

    // ===================== EXTRAS DE ESTILO =====================

    private JLabel crearEtiquetaSeccion(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JLabel crearEtiquetaCampo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MUTED);
        return lbl;
    }

    private JButton crearBoton(String texto, Color fondo, Color textoColor) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(textoColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        return btn;
    }

    // ===================== PARA EL CONTROLLER =====================

    public JButton getBtnCargarArchivo() { 
        return btnCargarArchivo; 
    }
    public JButton getBtnPasoAPaso() { 
        return btnPasoAPaso; 
    }
    public JButton getBtnEjecutarTodo() { 
        return btnEjecutarTodo; 
    }
    public JButton getBtnLimpiarReset() { 
        return btnLimpiarReset; 
    }
    public JButton getBtnAplicarConfig() { 
        return btnAplicarConfig; 
    }
    public JButton getBtnConfigurarMemoria() { 
        return btnConfigurarMemoria; 
    }
    public JSpinner getSpinnerTamanoRAM() { 
        return spinnerTamanoRAM; 
    }
    public JSpinner getSpinnerKernel() { 
        return spinnerKernel; 
    }
    public DefaultTableModel getModeloPrograma() { 
        return modeloPrograma; 
    }
    public DefaultTableModel getModeloMemoria() { 
        return modeloMemoria; 
    }
    public JLabel getLblPID() { 
        return lblPID; 
    }
    public JLabel getLblEstadoProceso() { 
        return lblEstadoProceso; 
    }
    public JLabel getLblPC() { 
        return lblPC; 
    }
    public JLabel getLblIR() { 
        return lblIR; 
    }
    public JLabel getLblAC() { 
        return lblAC; 
    }
    public JLabel getLblAX() { 
        return lblAX; 
    }
    public JLabel getLblBX() { 
        return lblBX; 
    }
    public JLabel getLblCX() { 
        return lblCX; 
    }
    public JLabel getLblDX() { 
        return lblDX; 
    }

    // Prueba rápida de solo la vista, sin Controller todavía
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MiniPCFrame frame = new MiniPCFrame();
            frame.setVisible(true);
        });
    }
}