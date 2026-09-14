package com.minipc.t1sominipc.controller;

import com.minipc.t1sominipc.model.BCP;
import com.minipc.t1sominipc.model.CPU;
import com.minipc.t1sominipc.model.ConvertidorASM;
import com.minipc.t1sominipc.model.Instruccion;
import com.minipc.t1sominipc.model.Memoria;
import com.minipc.t1sominipc.view.MiniPCFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ControllerMiniPC {

    private Memoria memoria;
    private BCP bcp;
    private CPU cpu;
    private ConvertidorASM parser;
    private MiniPCFrame vista;

    private List<Instruccion> programaActual;
    private boolean procesoAdmitido = false; // true una vez que ya se escribió a RAM

    public ControllerMiniPC(MiniPCFrame vista) {
        this.vista = vista;
        this.parser = new ConvertidorASM();
        inicializarMaquina(256, 64);
        registrarEventos();
        actualizarVista();
    }

    private void inicializarMaquina(int tamanoRAM, int tamanoKernel) {
        memoria = new Memoria(tamanoRAM, tamanoKernel);
        bcp = new BCP(memoria,  1, memoria.getInicioMemoriaUsuario());
        cpu = new CPU(memoria, bcp);
    }

    private void registrarEventos() {
        vista.getBtnCargarArchivo().addActionListener(e -> cargarArchivo());
        vista.getBtnPasoAPaso().addActionListener(e -> ejecutarPaso());
        vista.getBtnEjecutarTodo().addActionListener(e -> ejecutarTodo());
        vista.getBtnLimpiarReset().addActionListener(e -> limpiarTodo());
        vista.getBtnAplicarConfig().addActionListener(e -> aplicarConfiguracion());
    }

    // ===================== CARGA: SOLO RECONOCE, NO TOCA MEMORIA =====================

    private void cargarArchivo() {
        if (procesoHayQueResetear()) {
            JOptionPane.showMessageDialog(vista,
                    "Hay un proceso activo. Da clic en 'Limpiar / Reset' antes de cargar otro archivo.",
                    "Proceso en curso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos ASM", "asm"));
        int resultado = chooser.showOpenDialog(vista);

        if (resultado != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File archivo = chooser.getSelectedFile();

        try {
            List<String> lineas = Files.readAllLines(archivo.toPath());
            programaActual = parser.convertirASM(lineas);

            actualizarTablaPrograma();
            bcp.actualizarEstado("Nuevo");
            vista.getLblPID().setText("PID 1");
            vista.getLblEstadoProceso().setText("Preparando memoria para el proceso...");

            deshabilitarTodosLosBotones();

            Timer timerAdmision = new Timer(900, e -> completarAdmision());
            timerAdmision.setRepeats(false);
            timerAdmision.start();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(vista,
                    "No se pudo leer el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error en el programa: " + ex.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void completarAdmision() {
        cpu.cargarPrograma(programaActual);
        bcp.actualizarEstado("Listo");
        actualizarTablaMemoria();
        procesoAdmitido = true;

        vista.getBtnPasoAPaso().setEnabled(true);
        vista.getBtnEjecutarTodo().setEnabled(true);
        vista.getBtnLimpiarReset().setEnabled(true);
        // btnCargarArchivo y btnConfigurarMemoria siguen deshabilitados: ya hay un proceso en RAM

        actualizarVista();
    }

    private void deshabilitarTodosLosBotones() {
        vista.getBtnCargarArchivo().setEnabled(false);
        vista.getBtnPasoAPaso().setEnabled(false);
        vista.getBtnEjecutarTodo().setEnabled(false);
        vista.getBtnConfigurarMemoria().setEnabled(false);
        vista.getBtnLimpiarReset().setEnabled(false);
    }

    // =====================c AQUÍ SÍ SE ESCRIBE A MEMORIA =====================

    private void admitirProceso() {
        cpu.cargarPrograma(programaActual); // escribe a Memoria y pone estado "Listo"
        actualizarTablaMemoria();
        procesoAdmitido = true;
        vista.getBtnConfigurarMemoria().setEnabled(false); // se bloquea mientras el proceso está activo
    }

    // ===================== EJECUCIÓN =====================

    private void ejecutarPaso() {
        if (programaActual == null) {
            JOptionPane.showMessageDialog(vista, "Primero carga un archivo .asm",
                    "Sin programa", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean continua = cpu.paso(); // internamente ya pone "Ejecutando" antes de correr
        actualizarVista();

        if (!continua) {
            vista.getBtnConfigurarMemoria().setEnabled(true);
            vista.getBtnCargarArchivo().setEnabled(true);
            JOptionPane.showMessageDialog(vista, "Programa terminado",
                    "Ejecución finalizada", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void ejecutarTodo() {
        if (programaActual == null) {
            JOptionPane.showMessageDialog(vista, "Primero carga un archivo .asm",
                    "Sin programa", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean continua = true;
        while (continua) {
            continua = cpu.paso();
        }
        actualizarVista();
        vista.getBtnConfigurarMemoria().setEnabled(true);
        vista.getBtnCargarArchivo().setEnabled(true);
    }

    // ===================== RESET Y CONFIGURACIÓN =====================

    private void limpiarTodo() {
        inicializarMaquina(memoria.getTamanoTotal(), memoria.getInicioMemoriaUsuario());
        programaActual = null;
        procesoAdmitido = false;

        vista.getModeloPrograma().setRowCount(0);
        vista.getModeloMemoria().setRowCount(0);
        vista.getLblPID().setText("PID --");

        vista.getBtnCargarArchivo().setEnabled(true);
        vista.getBtnPasoAPaso().setEnabled(true);
        vista.getBtnEjecutarTodo().setEnabled(true);
        vista.getBtnConfigurarMemoria().setEnabled(true);
        vista.getBtnLimpiarReset().setEnabled(true);

        actualizarVista();
    }

    private void aplicarConfiguracion() {
        int nuevoTamano = (Integer) vista.getSpinnerTamanoRAM().getValue();
        int nuevoKernel = (Integer) vista.getSpinnerKernel().getValue();

        if (nuevoKernel >= nuevoTamano) {
            JOptionPane.showMessageDialog(vista,
                    "El espacio de kernel debe ser menor que el tamaño total de RAM",
                    "Configuración inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(nuevoKernel>= (nuevoTamano-16)){
            JOptionPane.showMessageDialog(vista,
                    "El espacio de kernel debe dejar al menos 16 direcciones para el usuario",
                    "Configuración inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        inicializarMaquina(nuevoTamano, nuevoKernel);
        programaActual = null;
        procesoAdmitido = false;

        vista.getModeloPrograma().setRowCount(0);
        vista.getModeloMemoria().setRowCount(0);
        vista.getLblPID().setText("PID --");
        actualizarVista();
    }

    // ===================== EXTRAS =====================

    private boolean procesoHayQueResetear() {
        return procesoAdmitido && !"Terminado".equals(bcp.getEstado());
    }

    private void actualizarTablaPrograma() {
        DefaultTableModel modelo = vista.getModeloPrograma();
        modelo.setRowCount(0);
        for (Instruccion instr : programaActual) {
            modelo.addRow(new Object[]{instr.getLineaOriginal(), formatearBinario(instr.aBinario())});
        }
    }

    private void actualizarTablaMemoria() {
        DefaultTableModel modelo = vista.getModeloMemoria();
        modelo.setRowCount(0);
        int inicio = memoria.getInicioMemoriaUsuario();
        int fin = inicio + programaActual.size();

        for (int direccion = inicio; direccion < fin; direccion++) {
            Instruccion instr = memoria.leerInstruccion(direccion);
            String textoInstr = (instr != null) ? instr.getLineaOriginal() : "";
            modelo.addRow(new Object[]{direccion, textoInstr, memoria.leer(direccion)});
        }
    }

    private void actualizarVista() {
        vista.getLblPC().setText(String.valueOf(cpu.getPC()));
        vista.getLblIR().setText(cpu.getIRBinario());
        vista.getLblAC().setText(String.valueOf(cpu.getAC()));
        vista.getLblAX().setText(String.valueOf(cpu.getAX()));
        vista.getLblBX().setText(String.valueOf(cpu.getBX()));
        vista.getLblCX().setText(String.valueOf(cpu.getCX()));
        vista.getLblDX().setText(String.valueOf(cpu.getDX()));
        vista.getLblEstadoProceso().setText(bcp.getEstado());
    }

    private String formatearBinario(String binario) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < binario.length(); i++) {
            sb.append(binario.charAt(i));
            if ((i == 3 || i == 7) && i != binario.length() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}