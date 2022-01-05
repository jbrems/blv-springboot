FROM adoptopenjdk/openjdk16

WORKDIR springboot

COPY . .

ENV PORT=3000

RUN ./mvnw package

RUN mv ./target/*.jar app.jar

CMD ["java","-jar","app.jar"]