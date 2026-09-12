package com.minipc.t1sominipc;

import com.minipc.t1sominipc.model.BCP;
import com.minipc.t1sominipc.model.CPU;
import com.minipc.t1sominipc.model.ConvertidorASM;
import com.minipc.t1sominipc.model.Instruccion;
import com.minipc.t1sominipc.model.Memoria;

import java.util.Arrays;
import java.util.List;

public class T1SOMiniPC {

    public static void main(String[] args) {

        // Líneas quemadas, igual al ejemplo del PDF
        List<String> lineasASM = Arrays.asList(
            "MOV AX, 5",
            "MOV BX, 3",
            "LOAD AX",
            "ADD BX",
            "SUB AX",
            "STORE AX",
            "MOV BX, -8"
        );

        // Armar las piezas de la "computadora"
        Memoria memoria = new Memoria();
        BCP bcp = new BCP(memoria, 1, memoria.getInicioMemoriaUsuario());
        CPU cpu = new CPU(memoria, bcp);

        // Convertir texto -> objetos Instruccion
        ConvertidorASM parser = new ConvertidorASM();
        List<Instruccion> programa = parser.convertirASM(lineasASM);

        System.out.println("Instrucciones parseadas: " + programa.size());
        System.out.println("--------------------------------------------------");

        // Cargar el programa en la CPU (esto internamente carga Memoria también)
        cpu.cargarPrograma(programa);

        // Ejecutar paso a paso, imprimiendo el estado después de cada paso
        int numeroPaso = 1;
        boolean continua = true;

        while (continua) {
            continua = cpu.paso();

            System.out.println("Paso " + numeroPaso + " | PC: " + cpu.getPC()
                    + " | IR: " + cpu.getIRBinario()
                    + " | AC: " + cpu.getAC()
                    + " | AX: " + cpu.getAX()
                    + " | BX: " + cpu.getBX()
                    + " | CX: " + cpu.getCX()
                    + " | DX: " + cpu.getDX()
                    + " | Estado BCP: " + bcp.getEstado()
                    + " | Contador BCP: " + bcp.getContadorInstrucciones());

            numeroPaso++;
        }

        System.out.println("--------------------------------------------------");
        System.out.println("Ejecución terminada.");
    }
}