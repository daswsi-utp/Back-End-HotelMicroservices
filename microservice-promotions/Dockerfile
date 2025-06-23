FROM eclipse-temurin:21-jdk as builder
WORKDIR /app
COPY . .
#RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
ARG MODULE=microservice-promotions
WORKDIR /app
#COPY --from=builder ${MODULE}/target/*.jar app.jar
COPY ${MODULE}/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
