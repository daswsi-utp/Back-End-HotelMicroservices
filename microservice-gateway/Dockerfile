ARG MODULE=microservice-gateway

FROM eclipse-temurin:21-jdk as BUILDER
WORKDIR /app
COPY . .
RUN ./mvnw -pl ${MODULE} -am clean package -DskipTests
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /build/${MODULE}/target/*.jar app.jar

EXPOSE 8090
ENTRYPOINT ["java","-jar","/app.jar"]
