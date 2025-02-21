package edu.escuelaing.co.reflectivechatgpt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class GtpReflexive {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(45000);
        System.out.println("Corriendo por el 45000");
        while(true){
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            String parameter = null;
            while((inputLine = in.readLine()) != null){
                System.out.println("Recibi: " + inputLine);
                if(inputLine.startsWith("GET /compreflex?comando")){
                    System.out.println("Estoy sacando parametro");
                    parameter = inputLine.split("=")[1].split(" ")[0];
                    break;
                }
                if(!in.ready()){
                    break;
                }
            }

            if(parameter != null){
                
                out.println("HTTP/1.1 200 OK\r\n"+"Content-Type: application/json\r\n"+"\r\n"+ parameter);
            }

            out.close();
            in.close();
            clientSocket.close();
        }
    }
}
