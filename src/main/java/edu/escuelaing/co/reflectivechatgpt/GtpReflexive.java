package edu.escuelaing.co.reflectivechatgpt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GtpReflexive {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
        ServerSocket serverSocket = new ServerSocket(45000);
        System.out.println("Corriendo por el 45000");
        while(true){
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            String parameter = null;
            while((inputLine = in.readLine()) != null){
                if(inputLine.startsWith("GET /compreflex?comando")){
                    parameter = inputLine.split("=")[1].split(" ")[0];
                    break;
                }
                if(!in.ready()){
                    break;
                }
            }

            if(parameter != null){
                String command = getCommand(parameter);
                List<String> params = getParameters(parameter);
                String resultado = getResult(command, params);
                out.println("HTTP/1.1 200 OK\r\n"+"Content-Type: application/json\r\n"+"\r\n"+ 
                    "{\"respuesta \":\""+ resultado +"\"}");
            }

            out.close();
            in.close();
            clientSocket.close();
        }
    }

    static String getCommand(String parameter){
        System.out.println("---------------");
        System.out.println("comando a ejecutar: " + parameter.substring(0, parameter.indexOf('(')));
        return parameter.substring(0, parameter.indexOf(')'));
    }

    static List<String> getParameters(String parameter){
        List<String> params = new ArrayList<>();
        String paramsConcatenated = parameter.substring(parameter.indexOf('(')+1,parameter.indexOf(')'));
        String[] paramsString = paramsConcatenated.split(",");
        for(String s:paramsString){
            params.add(s);
        }
        System.out.println("---------------");
        System.out.println("Parametros extraidos: "+ params);
        return params;
    }

    // 1. Class([class name]): Retorna una lista de campos declarados y métodos declarados check
    // 2. invoke([class name],[method name]): retorna el resultado de la invocación del método.  Ejemplo: invoke(java.lang.System, getenv).
    // 3. unaryInvoke([class name],[method name],[paramtype],[param value]): retorna el resultado de la invocación del método. paramtype = int | double | String.
        // Ejemplos:
        // - unaryInvoke(java.lang.Math,abs,int,3)
        // - unaryInvoke(java.lang.Integer, valueOf, String, "3")
    // 3. binaryInvoke([class name],[method name],[paramtype 1],[param value], [paramtype 1],[param value],): retorna el resultado de la invocación del método. paramtype = int | double | String. Ejemplos:
        // - binaryInvoke(java.lang.Math, max, double, 4.5, double, -3.7)
        // - binaryInvoke(java.lang.Integer,  add, int, 6, int, -3)
    static String getResult(String command, List<String> parametros) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException{
        Class<?> c = Class.forName(parametros.get(0));

        if(parametros.size() == 1){
            List<String> metodosString = new ArrayList<>();
            Method[] metodos = c.getMethods();
            for(Method m: metodos){
                metodosString.add(m.toString());
            }
            String resultado = null;
            for(String s: metodosString){
                resultado+=s;
            }
            System.out.println(resultado);
            return resultado;
        }else if (parametros.size() ==2){
            Method main = c.getDeclaredMethod(parametros.get(1), null);
            Object response = main.invoke(null);
            String answer = response.toString();
            return answer;
        }else if (parametros.size() == 4){ //unaryInvoke([class name],[method name],[paramtype],[param value]):
            String tipoParametro = parametros.get(2);
            Method main = null;
            if(tipoParametro.equals("int")){
                System.out.println("Estoy en int");
                main = c.getDeclaredMethod("abs", Integer.class);
            }else if(tipoParametro.equals("double")){
                System.out.println("Estoy en double");
                main = c.getDeclaredMethod(parametros.get(1), double.class);
            }else {
                System.out.println("Estoy en String");
                main = c.getDeclaredMethod(parametros.get(1), String.class);
                
            }
            Object response = main.invoke(c.getCanonicalName(), parametros.get(3));
            String answer = response.toString();
            System.out.println(answer);
            return answer;
        }else if(parametros.size() == 6){
            Method main = c.getDeclaredMethod(parametros.get(1), null);
            Object response = main.invoke(null);
            String answer = response.toString();
            return answer;
        }

        // Class<?> c = Class.forName();
	    // Method main = c.getDeclaredMethod("main", argTypes);
  	    // String[] mainArgs = Arrays.copyOfRange(args, 1, args.length);
	    // main.invoke(null, (Object)mainArgs);
        return null;
    }
        
}
