FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY *.jar CreditYaAuthService.jar
RUN chown -R appuser:appgroup /app
ENV JAVA_OPTS="-XX:+AlwaysPreTouch \
               -XX:+ExitOnOutOfMemoryError \
               -XX:MaxRAMPercentage=80 \
               -Djava.security.egd=file:/dev/./urandom"
USER appuser
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar CreditYaAuthService.jar" ]
