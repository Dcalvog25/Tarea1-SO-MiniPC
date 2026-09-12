package com.minipc.t1sominipc.model;

public class BCP {
    private int IDprograma;
    private String estado;
    private int PCactual;
    private int contadorInstrucciones;

    public BCP(int IDprograma, String estado, int PCactual, int contadorInstrucciones) {
        this.IDprograma = IDprograma;
        this.estado = estado;
        this.PCactual = PCactual;
        this.contadorInstrucciones = contadorInstrucciones;
    }

    public int getIDprograma() {
        return IDprograma;
    }

    public String getEstado() {
        return estado;
    }

    public int getPCactual() {
        return PCactual;
    }

    public int getContadorInstrucciones() {
        return contadorInstrucciones;
    }

    public void avanzarContador() {
        this.contadorInstrucciones++;
    }

    public void actualizarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void actualizarPC(int nuevoPC) {
        this.PCactual = nuevoPC;
    }


}
