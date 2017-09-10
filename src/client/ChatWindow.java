package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatWindow extends JFrame {
    private JTextArea chatWindow;
    private JTextField msg;
    private JButton sendButton;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ChatWindow(){
        setTitle("Live chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100,100,350,330);
        setResizable(false);

        setLayout(null);

        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        JScrollPane scroll = new JScrollPane(chatWindow);
        scroll.setBounds(10,10,330,200);
        add(scroll);

        msg = new JTextField();
        msg.setBounds(10,215,330,30);
        add(msg);

        sendButton = new JButton("Отправить");
        sendButton.setBounds(240,250,100,20);
        add(sendButton);

        //Подключение к серверу

        try {
            socket = new Socket("localhost",8021);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Обработчики

        ActionListener sendMsg = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!msg.getText().trim().isEmpty()){
                    try {
                        out.writeUTF(msg.getText());
                        out.flush();
                        msg.setText("");
                        msg.grabFocus();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };

        msg.addActionListener(sendMsg);
        sendButton.addActionListener(sendMsg);

//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                try {
//                    out.flush();
//                    out.close();
//                    in.close();
//                    socket.close();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//
//                super.windowClosing(e);
//            }
//        });

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        chatWindow.append(in.readUTF());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread1.start();


        setVisible(true);

    }

    public static void main(String[] args) {
        new ChatWindow();
    }
}
