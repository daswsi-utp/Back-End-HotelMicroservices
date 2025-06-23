FROM eclipse-temurin:21-jdk as BUILDER
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/app.jar"]
