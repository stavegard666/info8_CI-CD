# Étape 1 : Utilisation d'une image de base légère
#FROM gcr.io/distroless/java17:nonroot
FROM eclipse-temurin:17-jdk-alpine as build
# Définition du répertoire de travail dans l'image
RUN $JAVA_HOME/bin/jlink \
    --compress=2 \
    --no-header-files \
    --no-man-pages \
    --strip-debug \
    --add-modules java.base,java.logging,java.xml,java.naming,jdk.unsupported,jdk.zipfs,java.desktop \
    --output /custom-jdk
RUN echo "Using Correct Dockerfile!"

FROM alpine:3.19

WORKDIR /app
COPY --from=build /custom-jdk /custom-jdk
# Copie des fichiers nécessaires pour exécuter Quarkus
COPY target/quarkus-app /app/
ENV JAVA_HOME=/custom-jdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

EXPOSE 8081
# Définition du point d'entrée
CMD ["java", "-jar", "/app/quarkus-run.jar"]