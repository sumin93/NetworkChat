package server;

import client.ChatWindow;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ServerSocket server = null;
        Socket socket = null;
        String msg;

        try {
            server = new ServerSocket(8021);
            System.out.println("Ожидание подключения клиента...");
            socket = server.accept();
            System.out.println("Клиент подключился успешно.");


            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                Scanner input;
                String serverMsg;
                @Override
                public void run() {
                    while(true){
                        input = new Scanner(System.in);
                        serverMsg = input.nextLine();
                        try {
                            out.writeUTF("Сервер: " + serverMsg + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            while (true){
                msg = in.readUTF();
                System.out.println("Клиент: " + msg);
                out.writeUTF("Вы: " + msg + "\n");
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
