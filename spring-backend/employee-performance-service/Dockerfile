FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Add a non-root user
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Expose the application port
EXPOSE 8082

# Set volume for temporary files
VOLUME /tmp

# Copy the jar file
COPY --chown=spring:spring target/*.jar app.jar

# Set Java options for container environment
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]