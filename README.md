## Parcial 1 - Arep

Para correr el programa (despues de clonar el repositorio y acceder al directorio):
```
    mvn clean install
    mvn package
    
```
Abre una terminal y coloca:
```
    java -cp "target\classes\" edu.escuelaing.co.reflectivechatgpt.GtpReflexive
```
Abre otra terminal para el segundo servidor:
```
    mvn exec:java
```
Tiene que hacerse asi para el correcto funcionamiento, despues es accesible desde ````http://localhost:35000```

#Pruebas

Todos los parametros van sin espacio para su correcto funcionamiento:
1. Con un parametro:
```Class(java.lang.Math)```
[imagen1](/images/example1.png)
2. Con dos parametros:
```invoke(java.lang.System,getenv)```
[imagen1](/images/example2.png)
3. Con tres parametros:
```unaryInvoke(java.lang.Math,sin,int,3)```
4. Con varios parametros:
```binaryInvoke(java.lang.Math,max,double,4.5,double,-3.7)```
