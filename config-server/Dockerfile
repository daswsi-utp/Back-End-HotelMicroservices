ARG MODULE=config-server

FROM eclipse-temurin:21-jdk as BUILDER
WORKDIR /build
COPY . .
RUN ./mvnw clean package -pl ${MODULE} -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /build/${MODULE}/target/*.jar app.jar

EXPOSE 8888
ENTRYPOINT ["java","-jar","/app.jar"]
