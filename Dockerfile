FROM eclipse-temurin:17
COPY ./target/workouttracker*.jar /app.jar

ENV spring_profiles_active=local

EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]
