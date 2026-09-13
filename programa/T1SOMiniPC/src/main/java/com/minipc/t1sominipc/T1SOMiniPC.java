package com.minipc.t1sominipc;

import com.minipc.t1sominipc.controller.ControllerMiniPC;
import com.minipc.t1sominipc.view.MiniPCFrame;

import javax.swing.*;

public class T1SOMiniPC {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MiniPCFrame vista = new MiniPCFrame();
            new ControllerMiniPC(vista); // conecta los botones con la lógica
            vista.setVisible(true);
        });
    }
}