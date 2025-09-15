# ---------- Stage 1: Build ----------
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace

# Copy graddle wrapper and setup root
COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .
COPY main.gradle .
COPY gradle.properties .
RUN chmod +x ./gradlew

# Copy each layer clean, since gradle needs to see them
COPY applications ./applications
COPY domain ./domain
COPY infrastructure ./infrastructure

# Compile and generate Jar
RUN ./gradlew :app-service:clean :app-service:build -x test


# ---------- Stage 2: Runtime (Distroless) ----------
# FROM gcr.io/distroless/java21-debian12:nonroot AS runtime
# FROM eclipse-temurin:21-jre-jammy AS runtime
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Create user no-root (alpine, jammy)
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

# Copy jar from builder
COPY --from=builder /workspace/applications/app-service/build/libs/*.jar app.jar

# setting JVM for containers
ENV JAVA_TOOL_OPTIONS="\
  -XX:MaxRAMPercentage=75.0 \
  -Djava.security.egd=file:/dev/./urandom \
  -XX:+UseContainerSupport \
  -XX:+ExitOnOutOfMemoryError \
"

# use user no-root (alpine, jammy)
USER appuser

EXPOSE 9080
ENTRYPOINT ["java","-jar","/app/app.jar"]