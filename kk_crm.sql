-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: crm
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appuntamento`
--

DROP TABLE IF EXISTS `appuntamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appuntamento` (
  `Codice` int NOT NULL AUTO_INCREMENT,
  `Data` date DEFAULT NULL,
  `Note` varchar(100) DEFAULT NULL,
  `CF_C` varchar(20) NOT NULL,
  `Deleted` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`Codice`),
  KEY `CF_Cliente_idx` (`CF_C`),
  CONSTRAINT `CF_C` FOREIGN KEY (`CF_C`) REFERENCES `cliente` (`CF`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appuntamento`
--

LOCK TABLES `appuntamento` WRITE;
/*!40000 ALTER TABLE `appuntamento` DISABLE KEYS */;
INSERT INTO `appuntamento` VALUES (1,'2025-12-05','Incontro Riservato Palazzo Chigi','PREMIER00A01H501Z','N'),(2,'2025-12-08','Visita stabilimento Maranello','ENZOFER80A01H501X','N'),(3,'2025-12-15','Consiglio di Amministrazione','DIRBANC70A01H501Y','N'),(4,'2026-01-10','Rinnovo convenzione sanitaria','DOCROSS50A01H501W','N'),(5,'2025-10-01','Primo contatto conoscitivo','MRARSS80A01F205A','N'),(6,'2025-10-15','Analisi requisiti software','MRARSS80A01F205A','N'),(7,'2025-12-05','Consegna preventivo App','MRARSS80A01F205A','N'),(8,'2025-12-07','Pranzo di lavoro','GLLVRD85A01F205B','N'),(9,'2025-12-10','Verifica bilancio','ANNBNC90A01F205C','N'),(10,'2025-12-04','Meeting Green Energy','ELNCOL90A01F205I','N'),(11,'2025-12-06','Visita Hotel','ANDRIC95A01F205J','N'),(12,'2025-11-25','Urgenza ricambi','RBRCON80A01F205M','N'),(13,'2025-12-05','Presentazione Campagna Social','VLRSNT95A01F205P','N'),(14,'2025-11-10','Consulenza Estetica','LISBAR80A01F205S','N'),(15,'2025-12-05','Valutazione Immobili','TOMSGS85A01F205T','N'),(16,'2025-12-09','Pitch Startup','IREGEN90A01F205U','N');
/*!40000 ALTER TABLE `appuntamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `azienda`
--

DROP TABLE IF EXISTS `azienda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `azienda` (
  `P_Iva` varchar(25) NOT NULL,
  `Nome` varchar(40) NOT NULL,
  `Forma` varchar(5) DEFAULT NULL,
  `Indirizzo` varchar(60) NOT NULL,
  `Email` varchar(60) DEFAULT NULL,
  `Telefono` varchar(45) DEFAULT NULL,
  `Cat_merce` varchar(45) DEFAULT NULL,
  `Assigned` varchar(45) NOT NULL,
  `Tipologia` varchar(45) NOT NULL,
  `Deleted` char(1) NOT NULL,
  PRIMARY KEY (`P_Iva`),
  KEY `Assigned_idx` (`Assigned`),
  CONSTRAINT `Assigned` FOREIGN KEY (`Assigned`) REFERENCES `utente` (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `azienda`
--

LOCK TABLES `azienda` WRITE;
/*!40000 ALTER TABLE `azienda` DISABLE KEYS */;
INSERT INTO `azienda` VALUES ('IT01111111111','TechNova','SRL','Via Roma 1, Milano','info@technova.it','021111111','Informatica','mario.rossi','Fidelizzato','N'),('IT01111111112','Edilizia Futura','SPA','Via Po 22, Torino','contact@edilfut.it','011222333','Edilizia','mario.rossi','Ripetuto','N'),('IT01111111113','Studio Legale A&B','STD','Piazza Duomo, MI','segreteria@ab.it','023334445','Servizi','mario.rossi','Prospect','N'),('IT01111111114','AgriBio','SNC','Contrada Verde, BO','bio@agribio.com','051555666','Agricoltura','mario.rossi','Rischio abbandono','N'),('IT01111111115','MetalMecc 2000','SRL','Zona Ind. 4, BS','info@metal2000.it','030777888','Industria','mario.rossi','Fidelizzato','N'),('IT02222222221','Fashion Style','SPA','Via della Moda, FI','style@fashion.it','055123456','Moda','laura.verdi','Fidelizzato','N'),('IT02222222222','Logistica Rapida','SRL','Interporto, BO','log@rapida.it','051987654','Trasporti','laura.verdi','Ripetuto','N'),('IT02222222223','Green Power','SPA','Via Sole 10, RM','energy@green.it','065554443','Energia','laura.verdi','Fidelizzato','N'),('IT02222222224','Hotel Bellavista','SRL','Lungomare, RN','info@bellavista.it','0541222333','Turismo','laura.verdi','Prospect','N'),('IT03333333331','AutoRicambi','SRL','Via Motori 8, TO','parts@auto.it','011567890','Automotive','luca.bianchi','Rischio abbandono','N'),('IT03333333332','Farmacia Centrale','SNC','Corso Italia, MI','farma@centrale.it','026789012','Sanità','luca.bianchi','Fidelizzato','N'),('IT03333333333','Sport Life','ASD','Via Stadio 1, RM','info@sportlife.it','067890123','Sport','luca.bianchi','Prospect','N'),('IT03333333334','Media Agency','SRL','Via Tortona, MI','creative@media.it','027890123','Marketing','luca.bianchi','Nuovo','N'),('IT04444444441','Beauty Center','SAS','Via Bellezza, MI','info@beauty.it','028901234','Estetica','elena.neri','Fidelizzato','N'),('IT04444444442','Immobiliare Re','SRL','Piazza Affari, MI','sales@re.it','029012345','Immobiliare','elena.neri','Prospect','N'),('IT04444444443','Startup Innova','SRL','Incubatore, TN','ceo@innova.it','046123456','Informatica','elena.neri','Nuovo','N'),('IT04444444444','Ceramiche DArte','SNC','Via Forni, FA','art@ceramiche.it','054678901','Artigianato','elena.neri','Fidelizzato','N'),('IT99999999991','Governo Italiano','PA','Via del Parlamento, RM','info@gov.it','0611122235','meccanica','admin','Fidelizzato','N'),('IT99999999992','Ferrari Auto','SPA','Maranello, MO','corse@ferrari.it','0536000111','Automotive','admin','Fidelizzato','N'),('IT99999999993','Banca Centrale','SPA','Piazza Affari, MI','dir@banca.it','0288899900','Finanza','admin','Ripetuto','N'),('IT99999999994','Ospedale Maggiore','AZ','Via Salute, BO','sanita@ospedale.it','0513334445','Sanità','admin','Nuovo','N');
/*!40000 ALTER TABLE `azienda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `CF` varchar(20) NOT NULL,
  `Nome` varchar(30) NOT NULL,
  `Cognome` varchar(30) NOT NULL,
  `Telefono` varchar(20) DEFAULT NULL,
  `Data_Nascita` varchar(10) DEFAULT NULL,
  `Email` varchar(60) DEFAULT NULL,
  `P_Iva_Azienda` varchar(25) NOT NULL,
  `Deleted` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`CF`),
  KEY `P_Iva_Azienda_idx` (`P_Iva_Azienda`),
  CONSTRAINT `P_Iva_Azienda` FOREIGN KEY (`P_Iva_Azienda`) REFERENCES `azienda` (`P_Iva`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES ('ANDRIC95A01F205J','Andrea','Ricci','3330000000','1995-10-10','a.ricci@bellavista.it','IT02222222224','N'),('ANNBNC90A01F205C','Anna','Bianchi','3333333333','1990-03-03','a.bianchi@studioab.it','IT01111111113','N'),('CLRFER80A01F205G','Chiara','Ferrara','3337777777','1980-07-07','c.ferrara@fashion.it','IT02222222221','N'),('DIRBANC70A01H501Y','Ignazio','Visco','3330000003','1955-11-21','gov@banca.it','IT99999999993','N'),('DNLMAR90A01F205O','Daniele','Martini','3335656565','1990-03-15','d.martini@sportlife.it','IT03333333333','N'),('DOCROSS50A01H501W','Dott.','Rossi','3330000004','1970-02-14','primario@ospedale.it','IT99999999994','N'),('ELNCOL90A01F205I','Elena','Colombo','3339999999','1990-09-09','e.colombo@green.it','IT02222222223','N'),('ENZOFER80A01H501X','Piero','Ferrari','3330000002','1960-05-22','piero@ferrari.it','IT99999999992','N'),('FABSOR95A01F205V','Fabio','Sorrentino','3333434343','1995-10-20','f.sorrentino@ceramiche.it','IT04444444444','N'),('FRCROM85A01F205H','Francesco','Romano','3338888888','1985-08-08','f.romano@rapida.it','IT02222222222','N'),('GLLVRD85A01F205B','Giulio','Verdi','3332222222','1985-02-02','g.verdi@edilfut.it','IT01111111112','N'),('IREGEN90A01F205U','Irene','Genovese','3332323232','1990-09-20','i.genovese@innova.it','IT04444444443','N'),('LISBAR80A01F205S','Lisa','Barbieri','3330101010','1980-07-20','l.barbieri@beauty.it','IT04444444441','N'),('MRARSS80A01F205A','Marco','Russo','3331111111','1980-01-01','m.russo@technova.it','IT01111111111','N'),('PREMIER00A01H501Z','Mario','Draghi','3330000001','1947-09-03','premier@gov.it','IT99999999991','N'),('PTRNER95A01F205D','Pietro','Neri','3334444444','1995-04-04','p.neri@agribio.com','IT01111111114','N'),('RBRCON80A01F205M','Roberto','Conti','3333434343','1980-01-15','r.conti@auto.it','IT03333333331','N'),('SLVDEP85A01F205N','Silvia','De Paolis','3334545454','1985-02-15','s.depaolis@centrale.it','IT03333333332','N'),('SRAMRR00A01F205E','Sara','Marino','3335555555','2000-05-05','s.marino@metal2000.it','IT01111111115','N'),('TOMSGS85A01F205T','Tommaso','Soglia','3331212121','1985-08-20','t.soglia@re.it','IT04444444442','N'),('VLRSNT95A01F205P','Valeria','Santi','3336767676','1995-04-15','v.santi@media.it','IT03333333334','N');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `note` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nota` varchar(255) NOT NULL,
  `data` date NOT NULL,
  `utente` varchar(45) NOT NULL,
  `cliente_cf` varchar(20) NOT NULL,
  `Deleted` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`),
  KEY `cliente_cf_idx` (`cliente_cf`),
  CONSTRAINT `cliente_cf` FOREIGN KEY (`cliente_cf`) REFERENCES `cliente` (`CF`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `note`
--

LOCK TABLES `note` WRITE;
/*!40000 ALTER TABLE `note` DISABLE KEYS */;
INSERT INTO `note` VALUES (1,'Confermato budget statale per progetto','2025-11-30','elena.neri','SRAMRR00A01F205E','N'),(2,'Richiesto massimo riserbo su nuovi modelli','2025-11-28','admin','ENZOFER80A01H501X','N'),(3,'Attenzione: rischio fusione bancaria','2025-12-01','luca.bianchi','SLVDEP85A01F205N','N'),(4,'Cliente molto interessato','2025-10-01','mario.rossi','MRARSS80A01F205A','N'),(5,'Richiede sconto del 5%','2025-11-02','mario.rossi','GLLVRD85A01F205B','N'),(6,'Non risponde al telefono','2025-11-15','mario.rossi','PTRNER95A01F205D','N'),(7,'Confermato ordine Inverno','2025-09-21','laura.verdi','CLRFER80A01F205G','N'),(8,'Interessati ai pannelli solari','2025-11-05','laura.verdi','ELNCOL90A01F205I','N'),(9,'Urgenza magazzino','2025-11-25','luca.bianchi','RBRCON80A01F205M','N'),(10,'Campagna Facebook approvata','2025-11-30','luca.bianchi','VLRSNT95A01F205P','N'),(11,'Richiede personale qualificato','2025-11-12','elena.neri','LISBAR80A01F205S','N'),(12,'Non risponde al telefono','2025-11-30','elena.neri','FABSOR95A01F205V','N');
/*!40000 ALTER TABLE `note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proposte`
--

DROP TABLE IF EXISTS `proposte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proposte` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tipo` varchar(45) NOT NULL,
  `codice_servizio` int NOT NULL,
  `PIva` varchar(45) NOT NULL,
  `Deleted` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`),
  KEY `codice_servizio_idx` (`codice_servizio`),
  KEY `PIva_idx` (`PIva`),
  CONSTRAINT `codice_servizio` FOREIGN KEY (`codice_servizio`) REFERENCES `servizi_consulenza` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PIva` FOREIGN KEY (`PIva`) REFERENCES `azienda` (`P_Iva`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proposte`
--

LOCK TABLES `proposte` WRITE;
/*!40000 ALTER TABLE `proposte` DISABLE KEYS */;
INSERT INTO `proposte` VALUES (1,'Digitalizzazione PA',106,'IT99999999991','N'),(2,'Cybersecurity Industriale',107,'IT99999999992','N'),(3,'Audit Finanziario',103,'IT99999999993','N'),(4,'Sviluppo App iOS',108,'IT01111111111','N'),(5,'Consulenza Fiscale 2026',104,'IT01111111112','N'),(6,'GDPR Compliance',106,'IT01111111113','N'),(7,'Campagna Influencer',111,'IT02222222221','N'),(8,'Ottimizzazione Giri',115,'IT02222222222','N'),(9,'Certificazione Green',116,'IT02222222223','Y'),(10,'SEO Strategy',110,'IT03333333332','N'),(11,'Social Media Plan',111,'IT03333333333','N'),(12,'Ricerca Estetiste',113,'IT04444444441','N'),(13,'Business Plan',102,'IT04444444443','N');
/*!40000 ALTER TABLE `proposte` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servizi_consulenza`
--

DROP TABLE IF EXISTS `servizi_consulenza`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servizi_consulenza` (
  `id` int NOT NULL,
  `tipo_servizio` varchar(45) NOT NULL,
  `Deleted` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servizi_consulenza`
--

LOCK TABLES `servizi_consulenza` WRITE;
/*!40000 ALTER TABLE `servizi_consulenza` DISABLE KEYS */;
INSERT INTO `servizi_consulenza` VALUES (101,'Analisi di Mercato','N'),(102,'Business Plan Start-up','N'),(103,'Controllo di Gestione','N'),(104,'Ottimizzazione Fiscale','N'),(105,'Revisione Legale','N'),(106,'GDPR & Compliance','N'),(107,'Sicurezza Informatica','N'),(108,'Sviluppo Web/App','N'),(109,'Digital Marketing','N'),(110,'SEO & SEM','N'),(111,'Gestione Social Media','N'),(112,'Formazione HR','N'),(113,'Selezione Personale','N'),(114,'Coaching Aziendale','N'),(115,'Logistica & Supply','N'),(116,'Certificazione ISO','N'),(117,'Brevetti e Marchi','N'),(118,'Internazionalizzazione','N'),(119,'Finanza Agevolata','N'),(120,'Mergers & Acquisitions','N');
/*!40000 ALTER TABLE `servizi_consulenza` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utente` (
  `Username` varchar(40) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `Tipo` varchar(30) NOT NULL,
  `CF_Utente` varchar(20) NOT NULL,
  `Deleted` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES ('admin','admin','admin','ADM0000000000001','N'),('elena.neri','pass123','registrato','NRELNA92S55H501D','N'),('laura.verdi','pass123','admin','VRDLRA85M50H501B','N'),('luca.bianchi','pass123','registrato','BNCLCU90T15H501C','Y'),('mario.rossi','pass123','registrato','RSSMRA80A01H501A','N');
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-24 16:16:44
