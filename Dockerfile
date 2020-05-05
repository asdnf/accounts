FROM gradle

WORKDIR /project/

RUN gradle build



FROM openjdk

WORKDIR /app/

COPY --from=0 /project/build/libs/accounts-0.0.1-SNAPSHOT.jar .

CMD java -jar /app/accounts-0.0.1-SNAPSHOT.jar
