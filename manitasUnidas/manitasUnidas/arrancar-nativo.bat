@echo off
title Lanzador de Microservicios - Manitas Unidas

echo ====================================================
echo  1. INICIANDO SERVIDOR DE DISCOVERY (EUREKA) - [8761]
echo ====================================================
start "Eureka Server [8761]" cmd /k "java -jar eureka\target\eureka-0.0.1-SNAPSHOT.jar"

echo Esperando 20 segundos a que Eureka este totalmente activo...
timeout /t 20 /nobreak

echo ====================================================
echo  2. INICIANDO NUCLEO DE MICROSERVICIOS
echo ====================================================
start "Usuarios [8081]" cmd /k "java -jar usuarios\target\usuarios-0.0.1-SNAPSHOT.jar"
start "Seguridad [8091]" cmd /k "java -jar seguridad\target\seguridad-0.0.1-SNAPSHOT.jar"
start "Mascotas [8082]" cmd /k "java -jar mascotas\target\mascotas-0.0.1-SNAPSHOT.jar"
start "Refugios [8083]" cmd /k "java -jar refugios\target\refugios-0.0.1-SNAPSHOT.jar"
start "Solicitud [8085]" cmd /k "java -jar solicitud\target\solicitud-0.0.1-SNAPSHOT.jar"
start "FichaVet [8086]" cmd /k "java -jar fichaVet\target\fichaVet-0.0.1-SNAPSHOT.jar"
start "Notificaciones [8084]" cmd /k "java -jar notificaciones\target\notificaciones-0.0.1-SNAPSHOT.jar"
start "Seguimiento [8087]" cmd /k "java -jar seguimiento\target\seguimiento-0.0.1-SNAPSHOT.jar"
start "Blacklist [8089]" cmd /k "java -jar blackList\target\blackList-0.0.1-SNAPSHOT.jar"
start "Denuncias [8088]" cmd /k "java -jar denuncias\target\denuncias-0.0.1-SNAPSHOT.jar"

echo Esperando 35 segundos a que los servicios se registren en Eureka...
timeout /t 35 /nobreak

echo ====================================================
echo  3. INICIANDO API GATEWAY ENRUTADOR - [8090]
echo ====================================================
start "API Gateway [8090]" cmd /k "java -jar apiGateway\target\apiGateway-0.0.1-SNAPSHOT.jar"

echo ====================================================
echo  ¡TODOS LOS SERVICIOS HAN SIDO PROCESADOS!
echo ====================================================
pause