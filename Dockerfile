# Fase 1: Compilazione (Build) con Java 8
FROM maven:3.9-eclipse-temurin-8 AS builder
WORKDIR /app

# Copia il pom.xml e scarica le dipendenze
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia il codice sorgente e compila l'applicazione
COPY src ./src
RUN mvn clean package -DskipTests

# Fase 2: Esecuzione (Run) con Tomcat e Java 8
FROM tomcat:9.0-jre8

# Rimuove le applicazioni predefinite di Tomcat per pulizia e sicurezza
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia il file .war generato (kk_crm.war) nella cartella webapps di Tomcat come ROOT.war
COPY --from=builder /app/target/kk_crm.war /usr/local/tomcat/webapps/ROOT.war

# Espone la porta di default di Tomcat
EXPOSE 8080

# Avvia il server Tomcat
CMD ["catalina.sh", "run"]
