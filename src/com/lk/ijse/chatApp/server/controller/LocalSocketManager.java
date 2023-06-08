package com.lk.ijse.chatApp.server.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;

public class LocalSocketManager implements Runnable{

    public Socket socket;
    public DataInputStream dataInputStream;
    public DataOutputStream dataOutputStream;
    public List<LocalSocketManager> localSocketManagerList;
    public String type;

    public LocalSocketManager(Socket socket, List<LocalSocketManager> localSocketManagerList){
        this.socket = socket;
        this.localSocketManagerList = localSocketManagerList;
    }

    @Override
    public void run() {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream= new DataOutputStream(socket.getOutputStream());


            while (socket.isConnected()) {

                this.type = dataInputStream.readUTF();
                if (this.type.equalsIgnoreCase("text")) {

                    sendText();

                }else if(this.type.equalsIgnoreCase("picture")){

                    sendFile();

                }else if(this.type.equalsIgnoreCase("Close")) {

                    System.out.println(localSocketManagerList.size());
                    localSocketManagerList.remove(this);
                    System.out.println(localSocketManagerList.size());
                    this.type = "Text";
                    sendText();
                    Thread.currentThread().isInterrupted();

                }
            }
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendFile() {
        try {

            String userName = dataInputStream.readUTF();

            byte[] sizeAr = new byte[4];
            dataInputStream.read(sizeAr);
            int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

            byte[] imageAr = new byte[size];
            dataInputStream.read(imageAr);

            for (LocalSocketManager localSocketManager : localSocketManagerList) {

                if(!localSocketManager.equals(this)){

                    localSocketManager.dataOutputStream.writeUTF(type);
                    localSocketManager.dataOutputStream.writeUTF(userName);
                    localSocketManager.dataOutputStream.write(sizeAr);
                    localSocketManager.dataOutputStream.write(imageAr);
                    localSocketManager.dataOutputStream.flush();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendText() {
        try {

            String userName = dataInputStream.readUTF();
            String message = dataInputStream.readUTF();
            System.out.println(userName);
            System.out.println(message);

            for (LocalSocketManager localSocketManager : localSocketManagerList) {

                if (!localSocketManager.equals(this)) {

                    localSocketManager.dataOutputStream.writeUTF(type);
                    localSocketManager.dataOutputStream.writeUTF(userName);
                    localSocketManager.dataOutputStream.writeUTF(message);
                    localSocketManager.dataOutputStream.flush();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
