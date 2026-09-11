package com.minipc.t1sominipc.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConvertidorASM {

    public List<Instruccion> convertirASM(List<String> lineasASM) {
        List<Instruccion> instrucciones = new ArrayList<>();

        for (String linea : lineasASM) {
            String[] partes = linea.trim().split("[,\\s]+");
            String operador = partes[0].toUpperCase();

            if (partes.length <= 1) {
                System.out.println("Instrucción incompleta: " + linea);
                continue;
            }

            String registro = partes[1].toUpperCase();
            int valorDireccion = 0;

            if (partes.length > 2) {
                try {
                    valorDireccion = Integer.parseInt(partes[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Valor no numérico en: " + linea);
                    continue;
                }
            }

            if (validarInstruccion(operador, registro, valorDireccion)) {
                instrucciones.add(new Instruccion(operador, registro, valorDireccion, linea));
            } else {
                System.out.println("Instrucción inválida: " + linea);
            }
        }

        return instrucciones;
    }

    private boolean validarInstruccion(String operador, String registro, int valorDireccion) {
        operador = operador.toUpperCase();
        if (!operador.matches("LOAD|STORE|MOV|SUB|ADD")) {
            return false;
        }

        registro = registro.toUpperCase();
        if (!registro.matches("AX|BX|CX|DX")) {
            return false;
        }

        if (valorDireccion < -127 || valorDireccion > 127) {
            return false;
        }

        return true;
    }
    
}
