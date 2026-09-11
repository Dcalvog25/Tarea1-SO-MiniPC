/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.minipc.t1sominipc.model;

/**
 *
 * @author garci
 */
public class Memoria {
    
    private int[] memoria;
    private String[] memoriaLabels;
    private int tamañoTotal;
    private int finMemoriaKernel;
    private int inicioMemoriaUsuario;
    
    public Memoria() {
        
        this(256,64);
    }
    
    public Memoria(int tamañoTotal, int tamañoKernel) {
        this.tamañoTotal = tamañoTotal;
        this.finMemoriaKernel = tamañoKernel - 1;
        this.inicioMemoriaUsuario = tamañoKernel;
        this.memoria = new int[tamañoTotal];
        this.memoriaLabels = new String[tamañoTotal];
        
        for (int i = 0; i < tamañoTotal; i++) {
            memoria[i] = 0; 
            memoriaLabels[i] = "";
        }
        
    }

    public int getTamañoTotal() {
        return tamañoTotal;
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
        if (direccion < 0 || direccion >= tamañoTotal) {
            return false;
        }
        return true;
    }

    public boolean direccionValidaUsuario(int direccion) {
        if (direccion < inicioMemoriaUsuario || direccion >= tamañoTotal) {
            return false;
        }
        return true;
    }

    public void limpiarMemoria() {
        for (int i = 0; i < tamañoTotal; i++) {
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
}
