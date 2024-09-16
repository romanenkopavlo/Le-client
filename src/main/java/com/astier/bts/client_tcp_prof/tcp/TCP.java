/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astier.bts.client_tcp_prof.tcp;


import com.astier.bts.client_tcp_prof.HelloController;
import javafx.application.Platform;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


import static javafx.scene.paint.Color.RED;

/**
 * @author Michael
 */
public class TCP extends Thread {
    int port;
    InetAddress serveur;
    Socket socket;
    boolean marche = false;
    boolean connection = false;
    PrintStream out;
    BufferedReader in;

    HelloController fxmlCont;

    public TCP() {
    }

    public TCP(InetAddress serveur, int port, HelloController fxmlCont) {
        this.port = port;
        this.serveur = serveur;
        this.fxmlCont = fxmlCont;
        System.out.println("@ serveur: " + serveur + " port: " + port);
    }

    public void connection() throws IOException {
        if (!this.isAlive()) {
            socket = new Socket(serveur, port);
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.start();
            marche = true;
        }
    }

    public void deconnection() throws InterruptedException, IOException {
        updateMessage("EXIT");
        marche = false;
        out.close();
        in.close();
        socket.close();
    }

    public void requette(String laRequette) throws IOException, InterruptedException {

        if (laRequette.equalsIgnoreCase("exit")) {
            out.print(laRequette);
            deconnection();
            fxmlCont.voyant.setFill(RED);
        } else {
            out.println(laRequette);
            System.out.println("la requette " + laRequette);
        }
    }

    public void run() {
        while (marche) {
            try {
                String message;
                message = in.readLine();
                updateMessage(message);
            } catch (IOException ignored) {}
        }
    }

    protected void updateMessage(String message) {
        Platform.runLater(() -> fxmlCont.TextAreaReponses.appendText("    MESSAGE SERVEUR >  \n      " + message + "\n"));
    }
}