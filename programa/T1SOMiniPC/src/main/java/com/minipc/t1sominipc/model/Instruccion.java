package com.minipc.t1sominipc.model;

public class Instruccion {

    private String operador;
    private String registro;
    private int valorDireccion;
    private String lineaOriginal; 

    public Instruccion(String operador, String registro, int valorDireccion, String lineaOriginal) {
        this.operador = operador;
        this.registro = registro;
        this.valorDireccion = valorDireccion;
        this.lineaOriginal = lineaOriginal;
    }

    public String getOperador() {
        return operador;
    }

    public String getRegistro() {
        return registro;
    }

    public int getValorDireccion() {
        return valorDireccion;
    }

    public String getLineaOriginal() {
        return lineaOriginal;
    }

    public String aBinario() {
        String operacion = obtenerCodigoOperacion(operador);
        String registroBinario = obtenerCodigoRegistro(registro);
        String direccionBinaria = String.format("%08d", Integer.parseInt(Integer.toBinaryString(valorDireccion)));
        return operacion + registroBinario + direccionBinaria;
    }


    public String obtenerCodigoOperacion(String operador) {
        switch (operador) {
            case "LOAD":
                return "0001";
            case "STORE":
                return "0010";
            case "MOV":
                return "0011";
            case "SUB":
                return "0100";
            case "ADD":
                return "0101";
            default:
                throw new IllegalArgumentException("Operador no reconocido: " + operador);
        }
    }

    public String obtenerCodigoRegistro(String registro) {
        switch (registro) {
            case "AX":
                return "0001";
            case "BX":
                return "0010";
            case "CX":
                return "0011";
            case "DX":
                return "0100";
            default:
                throw new IllegalArgumentException("Operador no reconocido: " + registro);
        }
    }
}
