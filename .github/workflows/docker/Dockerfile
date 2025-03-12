FROM openjdk:17-jdk-slim

ARG JARS_DIR=app-jars
WORKDIR /app

# Copy all JAR files to the container
COPY ${JARS_DIR}/ /app/jars/

# Create a script to execute all JARs
RUN echo '#!/bin/sh' > /app/run-all-jars.sh && \
    echo 'for jar in /app/jars/**/*.jar; do' >> /app/run-all-jars.sh && \
    echo '  echo "Running $jar..."' >> /app/run-all-jars.sh && \
    echo '  java -jar "$jar" &' >> /app/run-all-jars.sh && \
    echo 'done' >> /app/run-all-jars.sh && \
    echo 'wait' >> /app/run-all-jars.sh && \
    chmod +x /app/run-all-jars.sh

# Set the entry point to run all JARs
ENTRYPOINT ["/app/run-all-jars.sh"]
