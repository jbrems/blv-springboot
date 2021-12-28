FROM adoptopenjdk/openjdk16

WORKDIR springboot

COPY . .

RUN ./mvnw package

RUN mv ./target/*.jar app.jar

CMD ["java","-jar","app.jar"]