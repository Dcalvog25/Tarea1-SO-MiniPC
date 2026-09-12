/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.minipc.t1sominipc.model;

import java.util.List;

/**
 *
 * @author garci
 */
public class Memoria {
    
    private int[] memoria;
    private String[] memoriaLabels;
    private int tamanoTotal;
    private int finMemoriaKernel;
    private int inicioMemoriaUsuario;
    private Instruccion[] instrucciones;
    
    public Memoria() {
        
        this(256,64);
    }
    
    public Memoria(int tamanoTotal, int tamanoKernel) {
        this.tamanoTotal = tamanoTotal;
        this.finMemoriaKernel = tamanoKernel - 1;
        this.inicioMemoriaUsuario = tamanoKernel;
        this.memoria = new int[tamanoTotal];
        this.memoriaLabels = new String[tamanoTotal];
        this.instrucciones = new Instruccion[tamanoTotal];
        
        for (int i = 0; i < tamanoTotal; i++) {
            memoria[i] = 0; 
            memoriaLabels[i] = "";
        }
        
    }

    public int getTamanoTotal() {
        return tamanoTotal;
    }

    public int getFinMemoriaKernel() {
        return finMemoriaKernel;
    }

    public int getInicioMemoriaUsuario() {
        return inicioMemoriaUsuario;
    }

    public int[] getMemoria() {
        return memoria;
    }

    public String[] getMemoriaLabels() {
        return memoriaLabels;
    }

    public boolean direccionValida(int direccion) {
        if (direccion < 0 || direccion >= tamanoTotal) {
            return false;
        }
        return true;
    }

    public boolean direccionValidaUsuario(int direccion) {
        if (direccion < inicioMemoriaUsuario || direccion >= tamanoTotal) {
            return false;
        }
        return true;
    }

    public void limpiarMemoria() {
        for (int i = 0; i < tamanoTotal; i++) {
            memoria[i] = 0; 
            memoriaLabels[i] = "";
        }
    }

    public int leer(int direccion) {
        if (!direccionValida(direccion)) {
            return 0; // Dirección inválida, devolver 0
        }
        return memoria[direccion];
    }

    public boolean escribir(int direccion, int valor, String label) {
        if (!direccionValida(direccion)) {
            return false; // Dirección inválida
        }
        memoria[direccion] = valor;
        memoriaLabels[direccion] = label;
        return true;
    }
    
    public void cargarPrograma(List<Instruccion> programa) {
        int direccion = inicioMemoriaUsuario;
        for (Instruccion instr : programa) {
            if (!direccionValidaUsuario(direccion)) {
                break;
            }
            int valorBinario = Integer.parseInt(instr.aBinario(), 2);
            escribir(direccion, valorBinario, instr.getLineaOriginal());
            instrucciones[direccion] = instr;
            direccion++;
        }
    }

    public Instruccion leerInstruccion(int direccion) {
        if (!direccionValida(direccion)) {
            return null; // Dirección inválida
        }
        return instrucciones[direccion];
    }
}
