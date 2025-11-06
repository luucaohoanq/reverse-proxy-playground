# ARG to define which service to build. Must be declared before FROM.
ARG SERVICE_PATH
ARG SERVICE_PORT

# =====================================================================
# Stage 1: Build the application (Focus on Caching)
# =====================================================================
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Re-declare ARG
ARG SERVICE_PATH

# 1. Copy all POMs first to create a stable dependency layer
# This is the key to caching. It's verbose, but it's correct.
COPY pom.xml .
COPY .mvn .mvn
#COPY services/pom.xml ./services/

# --- Add ALL your modules here ---
COPY services/CommonLib/pom.xml ./services/CommonLib/
COPY services/MSAccount_SE181513/pom.xml ./services/MSAccount_SE181513/
COPY services/MSBlindBox_SE181513/pom.xml ./services/MSBlindBox_SE181513/
COPY services/MSBrand_SE181513/pom.xml ./services/MSBrand_SE181513/
COPY services/APIGateway_SE181513/pom.xml ./services/APIGateway_SE181513/
# --- End of module list ---

# 2. Download dependencies (this layer is now cached unless a POM changes)
RUN mvn dependency:go-offline -B

# 3. Copy all source code
COPY services/ ./services/

# 4. Build the specific service (only if not already built)
# If JAR exists, skip build; otherwise build
RUN if [ ! -f ${SERVICE_PATH}/target/*.jar ]; then \
    mvn clean package -pl ${SERVICE_PATH} -am -DskipTests; \
    fi

# 5. Extract layers from the JAR
RUN mkdir -p extracted && \
    jar_file=$(ls ${SERVICE_PATH}/target/*.jar 2>/dev/null | grep -v '.original$' | head -n1) && \
    java -Djarmode=layertools -jar "$jar_file" extract --destination extracted

# =====================================================================
# Stage 2: Runtime image (Focus on Size & Security)
# =====================================================================
# Use Google's "distroless" image. It contains only Java, not a full OS.
# https://github.com/GoogleContainerTools/distroless
FROM gcr.io/distroless/java17-debian12
WORKDIR /app

# Re-declare ARG
ARG SERVICE_PORT

# Copy the extracted layers
COPY --from=builder /app/extracted/dependencies/ ./
COPY --from=builder /app/extracted/spring-boot-loader/ ./
COPY --from=builder /app/extracted/snapshot-dependencies/ ./
COPY --from=builder /app/extracted/application/ ./

# --- NOTE: 'curl' is removed ---
# Distroless images do not have a package manager (apk/apt) or shell.
# This means we cannot install 'curl'. This is a security best practice.
# Your health checks should be handled by your container orchestrator (like Kubernetes)
# using TCP probes, or by Spring Boot Actuator's built-in health endpoints.

EXPOSE ${SERVICE_PORT}
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
