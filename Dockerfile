ENV RUNTIME_OPTS --enable-preview
FROM navikt/java:14
COPY target/*.jar app.jar
