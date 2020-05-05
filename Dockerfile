FROM gradle
WORKDIR /project/
COPY / .
RUN ./gradlew build


FROM openjdk
WORKDIR /app/
COPY --from=0 /project/build/libs/accounts-0.0.1-SNAPSHOT.jar .
CMD java -jar /app/accounts-0.0.1-SNAPSHOT.jar --pg.host=$PG_HOST --pg.port=$PG_PORT
