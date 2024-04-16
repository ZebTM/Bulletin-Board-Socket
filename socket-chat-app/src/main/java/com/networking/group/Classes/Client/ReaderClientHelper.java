package com.networking.group.Classes.Client;

import java.io.BufferedReader;
import java.io.IOException;

public class ReaderClientHelper implements Runnable {
    private BufferedReader serverReader;
    private Boolean threadOpen = true;
    public ReaderClientHelper(BufferedReader serverReader) {
        this.serverReader = serverReader;
    }

    public void run() {
        try {
            readServer();
        } catch (IOException exception) {
            System.out.println(exception.getStackTrace());
        }
    }

    private void readServer() throws IOException {
        String response;
        while (threadOpen) {
            response = serverReader.readLine();
            System.out.println(response);
        }
        return;
    }

    public void closeThread() {
        threadOpen = false;
    }
}
