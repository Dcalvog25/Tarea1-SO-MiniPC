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
        String valorBinario = valorABinario(valorDireccion);
        return operacion + registroBinario + valorBinario;
    }

    private String valorABinario(int valor) {
        int signo = 0;
        if (valor < 0) {
            signo = 1;
        }
        int magnitud = Math.abs(valor);
        String binario7bits = String.format("%7s", Integer.toBinaryString(magnitud)).replace(' ', '0');
        return signo + binario7bits;
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
