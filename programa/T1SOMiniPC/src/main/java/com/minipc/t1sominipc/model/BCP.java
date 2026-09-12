package com.minipc.t1sominipc.model;

public class BCP {

    private static final int POS_PID = 0;
    private static final int POS_ESTADO = 1;
    private static final int POS_PC = 2;
    private static final int POS_AC = 3;
    private static final int POS_BASE = 4;
    private static final int POS_CONTADOR = 5;

    private Memoria memoria;

    public BCP(Memoria memoria, int pid, int baseUsuario) {
        this.memoria = memoria;

        if(POS_CONTADOR > memoria.getFinMemoriaKernel()) {
            throw new IllegalArgumentException("La memoria es demasiado pequeña para almacenar el BCP.");
        }
        memoria.escribir(POS_PID, pid, "PID");
        memoria.escribir(POS_ESTADO, 0, "Estado"); // 0 = Nuevo
        memoria.escribir(POS_PC, baseUsuario, "PC");
        memoria.escribir(POS_AC, 0, "AC");
        memoria.escribir(POS_BASE, baseUsuario, "Base");
        memoria.escribir(POS_CONTADOR, 0, "Contador");
    }

    public void actualizarEstado(String estado) {
        memoria.escribir(POS_ESTADO, estadoACodigo(estado), "Estado");
    }

    public void avanzarContador() {
        int actual = memoria.leer(POS_CONTADOR);
        memoria.escribir(POS_CONTADOR, actual + 1, "Contador");
    }

    public int getContadorInstrucciones() {
        return memoria.leer(POS_CONTADOR);
    }

    public String getEstado() {
        return codigoAEstado(memoria.leer(POS_ESTADO));
    }
   
    public void reiniciar(int baseUsuario) {
        memoria.escribir(POS_ESTADO, 1, "Estado");   // 1 = Listo directamente
        memoria.escribir(POS_PC, baseUsuario, "PC");
        memoria.escribir(POS_AC, 0, "AC");
        memoria.escribir(POS_BASE, baseUsuario, "Base");
        memoria.escribir(POS_CONTADOR, 0, "Contador");
    }

    private int estadoACodigo(String estado) {
        switch (estado) {
            case "Nuevo": 
                return 0;
            case "Listo": 
                return 1;
            case "Ejecutando": 
                return 2;
            case "Terminado": 
                return 3;
            default: 
                throw new IllegalArgumentException("Estado no reconocido: " + estado);
        }
    }

    private String codigoAEstado(int codigo) {
        switch (codigo) {
            case 0: 
                return "Nuevo";
            case 1: 
                return "Listo";
            case 2: 
                return "Ejecutando";
            case 3: 
                return "Terminado";
            default: 
                return "Desconocido";
        }
    }
}