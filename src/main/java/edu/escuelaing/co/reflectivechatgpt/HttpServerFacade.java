package edu.escuelaing.co.reflectivechatgpt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class HttpServerFacade {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(35000);
        System.out.println("Corriendo por el 35000");
        while(true){
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            String parameter = null;
            while((inputLine = in.readLine()) != null){
                System.out.println("Recibi: " + inputLine);
                if(inputLine.startsWith("GET /consulta?comando=")){
                    System.out.println("Estoy sacando parametro");
                    parameter = inputLine.split("=")[1].split(" ")[0];
                    break;
                }
                if(!in.ready()){
                    break;
                }
            }

            if(parameter != null){
                URL obj = new URL("http://localhost:45000/compreflex?comando="+parameter);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                BufferedReader apiIn = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String response = apiIn.readLine();
                out.println("HTTP/1.1 200 OK\r\n"+"Content-Type: application/json\r\n"+"\r\n"+ response);
            }else{
                System.out.println("Estoy aqui en el html");
                String htmlRaw = getHtml();
                out.println("HTTP/1.1 200 OK\r\n"+"Content-Type: text/html\r\n"+"\r\n"+ htmlRaw);
            }

            out.close();
            in.close();
            clientSocket.close();
        }
    }

    static String getHtml(){
        String outputLine = "<!DOCTYPE html>\n"
         + "<html>\n"
         + "<head>\n"
         + "<meta charset=\"UTF-8\">\n"
         + "<title>Title of the document</title>\n"
         + "</head>\n"
         + "<body>\n"
         + "<h1>Mi propio mensaje</h1>\n"
         + "</body>\n"
         + "</html>\n";
        return outputLine;
    }
}
