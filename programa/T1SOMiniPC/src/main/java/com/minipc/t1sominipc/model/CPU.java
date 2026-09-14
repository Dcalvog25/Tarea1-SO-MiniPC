package com.minipc.t1sominipc.model;

import java.util.List;

public class CPU {

    private int AX;
    private int BX;
    private int CX;
    private int DX;

    private int PC; // Program Counter
    private int IR; // Instruction Register
    private int AC; // Accumulator

   

    private Memoria memoria;
    private BCP bcp;

    public CPU(Memoria memoria, BCP bcp) {
        this.memoria = memoria;
        this.bcp = bcp;
        inicializarRegistros();
    }

    private void inicializarRegistros() {
        AX = 0;
        BX = 0;
        CX = 0;
        DX = 0;
        this.PC = memoria.getInicioMemoriaUsuario();
        this.IR = 0;
        this.AC = 0;
    }
    
    public void cargarPrograma(List<Instruccion> programa) {
        memoria.limpiarMemoriaUsuario();
        memoria.cargarPrograma(programa);
        inicializarRegistros();
        bcp.reiniciar(memoria.getInicioMemoriaUsuario());
    }

    public boolean paso() {
        Instruccion actual = memoria.leerInstruccion(PC); 

        if (actual == null) {
            bcp.actualizarEstado("Terminado");
            return false;
        }

        IR = Integer.parseInt(actual.aBinario(), 2);
        ejecutar(actual);
        PC++;
        bcp.actualizarEstado("Ejecutando");
      
        bcp.avanzarContador();
        bcp.actualizarRegistros(PC, AC, AX, BX, CX, DX);
        return true;
    }

    public void ejecutar(Instruccion instruccion) {
        String registro = instruccion.getRegistro();
        switch(instruccion.getOperador()) {
            case "LOAD":
                AC = getRegistro(registro);
                break;
            case "STORE":
                setRegistro(registro, AC);
                break;
            case "MOV":
                setRegistro(registro, instruccion.getValorDireccion());
                break;
            case "SUB":
                AC -= getRegistro(registro);
                break;
            case "ADD":
                AC += getRegistro(registro);
                break;
            default:
                throw new IllegalArgumentException("Operador no reconocido: " + instruccion.getOperador());
        }   
    }

    private int getRegistro(String registro) {
        switch(registro) {
            case "AX": return AX;
            case "BX": return BX;
            case "CX": return CX;
            case "DX": return DX;
            default: throw new IllegalArgumentException("Registro no reconocido: " + registro);
        }
    }


    private void setRegistro(String registro, int valor) {
        switch(registro) {
            case "AX": 
                AX = valor; 
                break;
            case "BX": 
                BX = valor; 
                break;
            case "CX": 
                CX = valor; 
                break;
            case "DX": 
                DX = valor; 
                break;
            default: throw new IllegalArgumentException("Registro no reconocido: " + registro);
        }
    }

    public int getAX() {
        return AX;
    }

    public int getBX() {
        return BX;
    }

    public int getCX() {
        return CX;
    }

    public int getDX() {
        return DX;
    }

    public int getPC() {
        return PC;
    }

    public int getIR() {
        return IR;
    }

    public int getAC() {
        return AC;
    }

    public String getIRBinario() {
        return String.format("%16s", Integer.toBinaryString(IR)).replace(' ', '0');
    }


    
}
