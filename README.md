# KK_CRM - Sistema di Gestione Relazioni con i Clienti (CRM)

Un'applicazione CRM sviluppata in Java (JSP/Servlets) seguendo rigorosamente l'architettura MVC (Model-View-Controller) e il pattern DAO (Data Access Object). Il progetto è interamente containerizzato tramite **Docker**, garantendo un ambiente di sviluppo e deployment isolato, pulito e riproducibile su qualsiasi sistema operativo (Windows, Linux, macOS) senza la necessità di configurazioni locali complesse.

---

## 🛠 Tecnologie Utilizzate

* **Backend:** Java 8, Servlets, JSP
* **Server Web:** Apache Tomcat 9
* **Database:** MySQL 8.0
* **Build Tool:** Maven
* **DevOps & Containerizzazione:** Docker, Docker Compose, GitHub Actions (CI/CD)

---

## 🏗 Architettura Docker (Configurazioni)

L'infrastruttura si basa su due container principali che comunicano su una rete interna isolata. Di seguito sono riportati i file di configurazione esatti utilizzati nel progetto.

### 1. Il `Dockerfile` (Applicazione Java/Tomcat)
Questo file utilizza un approccio "Multi-stage build" per compilare il codice in modo sicuro e rilasciare solo l'artefatto finale (`.war`), mantenendo l'immagine di produzione leggera.

```dockerfile
# Fase 1: Compilazione (Build) con Java 8
FROM maven:3.9-eclipse-temurin-8 AS builder
WORKDIR /app

# Copia il pom.xml e scarica le dipendenze (sfrutta la cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia il codice sorgente e compila l'applicazione
COPY src ./src
RUN mvn clean package -DskipTests

# Fase 2: Esecuzione (Run) con Tomcat e Java 8
FROM tomcat:9.0-jre8

# Rimuove le applicazioni predefinite di Tomcat per pulizia e sicurezza
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia il file .war generato nella cartella webapps di Tomcat come ROOT.war
COPY --from=builder /app/target/kk_crm.war /usr/local/tomcat/webapps/ROOT.war

# Espone la porta di default di Tomcat
EXPOSE 8080

# Avvia il server Tomcat
CMD ["catalina.sh", "run"]
```

### 2. Il `docker-compose.yml` (Orchestrazione)
Questo file gestisce l'avvio simultaneo del database e dell'applicazione web, assicurando che il database venga popolato automaticamente al primo avvio.

```yaml
services:
  # Container del Database
  db_mysql:
    image: mysql:8.0
    container_name: crm-database
    environment:
      MYSQL_ALLOW_EMPTY_PASSWORD: "yes" # Nessuna password richiesta (solo per dev)
      MYSQL_DATABASE: "crm"
    ports:
      - "3307:3306" # Mappatura: Host (3307) -> Container (3306) per evitare conflitti
    volumes:
      - ./kk_crm.sql:/docker-entrypoint-initdb.d/init.sql # Popolamento iniziale
      - db_data:/var/lib/mysql # Persistenza dei dati

  # Container dell'Applicazione Web
  app_crm:
    build: .
    container_name: crm-app
    ports:
      - "8081:8080" # Mappatura: Host (8081) -> Container (8080)
    depends_on:
      - db_mysql # Attende l'avvio del database prima di partire

volumes:
  db_data:
```

### 3. Il `.gitignore`
Garantisce che i file di build, i binari e le configurazioni degli IDE locali non vengano tracciati da Git.

```
target/
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

### IntelliJ IDEA ###
.idea/modules.xml
.idea/jarRepositories.xml
.idea/compiler.xml
.idea/libraries/
*.iws
*.iml
*.ipr

### Eclipse ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache

### NetBeans ###
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/
build/
!**/src/main/**/build/
!**/src/test/**/build/

### VS Code ###
.vscode/

### Mac OS ###
.DS_Store
```

---

## 🚀 Guida all'Avvio Rapido per Sviluppatori

Essendo il progetto basato su container, **non è necessario** avere Java, Tomcat, Maven o MySQL installati sul proprio computer. È richiesto esclusivamente [Docker](https://docs.docker.com/get-docker/) (con Docker Compose) e Git.

### Passaggio 1: Clonare il repository
Aprire il terminale ed eseguire i seguenti comandi:

```bash
git clone https://github.com/brice-kamga/kk_crm.git
cd kk_crm
```

### Passaggio 2: Avviare l'infrastruttura
Il comando seguente scaricherà le immagini, popolerà il database, compilerà il codice Java e avvierà il server in background:

*(Nota per utenti Linux: potrebbe essere necessario anteporre `sudo` ai comandi docker).*

```bash
docker compose up -d --build
```

### Passaggio 3: Accedere all'applicazione
Una volta che i container sono in stato `Running`, l'applicazione sarà disponibile all'indirizzo:

 **http://localhost:8081**

---

## 🛑 Gestione dell'Ambiente

Ecco i comandi utili per gestire il ciclo di vita dell'applicazione:

* **Vedere i log in tempo reale (utile per debug):**
  ```bash
  docker compose logs -f
  ```
* **Fermare l'applicazione senza perdere i dati del DB:**
  ```bash
  docker compose down
  ```
* **Distruggere l'ambiente e resettare completamente il database:**
  ```bash
  docker compose down -v
  ```
