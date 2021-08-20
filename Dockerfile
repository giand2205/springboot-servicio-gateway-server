FROM openjdk:16
VOLUME /tmp
EXPOSE 8090
ADD ./target/springboot-servicio-gateway-server-0.0.1-SNAPSHOT.jar gateway-server.jar
ENTRYPOINT ["java", "-jar", "/gateway-server.jar"]