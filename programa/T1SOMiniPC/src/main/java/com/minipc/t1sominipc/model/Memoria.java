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
    
    private String[] memoria;
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
        this.memoria = new String[tamañoTotal];
        this.memoriaLabels = new String[tamañoTotal];
        
        for (int i = 0; i < tamañoTotal; i++) {
            memoria[i] = "00000000"; // 8 bits vacíos por defecto
            memoriaLabels[i] = "";
        }
        
    }
    
    
}
