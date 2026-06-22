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
  `Note` varchar(70) DEFAULT NULL,
  `CF_C` char(16) NOT NULL,
  `Deleted` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`Codice`),
  KEY `CF_Cliente_idx` (`CF_C`),
  CONSTRAINT `CF_C` FOREIGN KEY (`CF_C`) REFERENCES `cliente` (`CF`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appuntamento`
--

LOCK TABLES `appuntamento` WRITE;
/*!40000 ALTER TABLE `appuntamento` DISABLE KEYS */;
INSERT INTO `appuntamento` VALUES (10,'2021-12-10','chiamata skype','LNYMGC50A17I507K','N'),(12,'2021-12-10','meet','LNYMGC50A17I507K','N'),(33,'2021-12-02','meet','RSSMRC34563ER34D','N'),(34,'2021-12-16','call zoom','CNSRBRT3456E2E23','N'),(67,'2021-12-18','meet','RSSMRC34563ER34D','N'),(100,'2021-12-18','chiamata skype','RSSMRC34563ER34D','N'),(104,'2021-12-16','meet','DFCLTD88C27T657G','Y'),(105,'2021-12-23','allineamento','DFCLTD88C27T657G','Y'),(106,'2021-12-23','meet','LNYMGC50A17I507K','N'),(107,'2021-12-23','assistenza','RSSMRC34563ER34D','N'),(109,'2021-12-25','meet','ANTNNLC4568EF12F','N'),(110,'2021-12-25','assistenza','DFCLTD88C27T657G','Y'),(111,'2021-12-25','meet','DFCLTD88C27T657G','Y'),(112,'2022-02-24','assistenza','ANTNNLC4568EF12F','N'),(113,'2022-02-28','meet','CNSRBRT3456E2E23','N'),(114,'2022-03-03','chiamata skype','HQFLVH30S27B437M','N'),(115,'2022-03-01','assistenza','ANTNNLC4568EF12F','N'),(116,'2022-03-02','meet','ANTNNLC4568EF12F','N'),(117,'2022-03-03','assistenza','ANTNNLC4568EF12F','N'),(118,'2022-03-03','call zoom','DFCLTD88C27T657G','N'),(119,'2022-03-02','chiamata skype','DFCLTD88C27T657G','Y'),(120,'2022-03-02','chiamata skype','DFCLTD88C27T657G','N'),(121,'2022-03-02','call zoom','DFCLTD88C27T657G','N'),(122,'2022-03-02','assistenza','DFCLTD88C27T657G','N'),(123,'2022-03-02','assistenza','ANTNNLC4568EF12F','N'),(124,'2022-03-02','chiamata skype','DFCLTD88C27T657G','N'),(125,'2022-03-02','call zoom','DFCLTD88C27T657G','N'),(126,'2022-03-02','chiamata skype','DFCLTD88C27T657G','N'),(127,'2022-03-02','allineamento','DFCLTD88C27T657G','N'),(129,'2022-03-16','meet','DFCLTD88C27T657G','N'),(131,'2022-03-17','zoom','BRNDMNC23423E34F','Y'),(132,'2022-03-17','meet','BRNDMNC23423E34F','Y'),(133,'2022-03-17','assistenza','ANTNNLC4568EF12F','N'),(134,'2022-03-17','call','BRNDMNC23423E34F','Y'),(135,'2022-03-17','call','BRNDMNC23423E34F','Y'),(136,'2022-03-17','call','ANTNNLC4568EF12F','N'),(137,'2022-03-17','call','ANTNNLC4568EF12F','N'),(138,'2022-04-28','meet','CNSRBRT3456E2E23','N'),(139,'2022-04-29','zoom','ANTNNLC4568EF12F','N'),(140,'2022-04-29','proroga','DFCLTD88C27T657G','Y'),(141,'2022-04-28','avviso','BRNDMNC23423E34F','N'),(142,'2022-04-29','annotazione','BRNDMNC23423E34F','Y'),(143,'2022-04-28','meet','BRNDMNC23423E34F','N'),(144,'2022-04-29','zoom','BRNDMNC23423E34F','N'),(145,'2022-04-28','meet','SLTRVZ45L08M269O','N'),(146,'2022-05-07','allineamento','XCLBBZ52D05B213C','Y'),(147,'2022-04-29','call','BRNDMNC23423E34F','Y'),(148,'2022-05-03','call','BRNDMNC23423E34F','N'),(149,'2022-05-04','allineamento','BRNDMNC23423E34F','N'),(150,'2022-05-08','Richiamare','DFCLTD88C27T657G','N'),(151,'2022-05-09','Disponibile solo lunedÃ¬','SLTRVZ45L08M269O','Y'),(152,'2022-05-15','richiamare','BRNDMNC23423E34F','N'),(153,'2022-05-15','call meet','DFCLTD88C27T657G','N'),(154,'2022-05-16','chiamare il prima possibile','BRNDMNC23423E34F','N'),(155,'2022-05-15','disponibile solo pomeriggio','ASDFER34DSWDFRCS','Y'),(156,'2022-05-15','appunbtamebti','ASDFER34DSWDFRCS','N'),(157,'2025-11-08','tutto ok','LNYMGC50A17I505H','Y'),(158,'2025-11-08','meet meet','CDBFNJM8967SDFGC','Y'),(159,'2025-11-12','tutto ok','LNYMGC50A17I505H','N'),(160,'2025-11-10','meet meet','CDBFNJM8967SDFGC','N'),(161,'2025-12-05','meet meet','RSSVSC52B07M183C','Y');
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
  `Nome` varchar(20) NOT NULL,
  `Forma` varchar(5) DEFAULT NULL,
  `Indirizzo` varchar(45) NOT NULL,
  `Email` varchar(40) DEFAULT NULL,
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
INSERT INTO `azienda` VALUES ('11111111111','Consulenze','SPA','via Carlo Antonini, 11 Ferrara','consulenzespa@pippo.it','0562451225','informatica','giannipino','Prospect','N'),('12332423423','AziendaSRL','SRL','Via Indirizzo 41','emailposta@email.it','3321245344','meccanica','giannipino','Prospect','N'),('12343234234','aa','aa','aa','aa@aa','11','meccanica','giannipino','Fidelizzato','Y'),('12345321266','IndustrySA','SPA','Via viale 32','indumail@mail.it','3249834788','meccanica','giannipino','Fidelizzato','N'),('12345617912','Good S','SRL','via comitato di liberazione 27','rossi@example.com','3895955437','meccanica','giannipino','Nuovo','Y'),('12345678654','LGA','SRL','via Lago di Misurina, 12 Tivoli','lallohuzeulle-7771@yopmail.com','3203547278','chimica','giannipino','Nuovo','N'),('12345678910','USB CHARG','SNC','via Gianni, 23 Imola','wosofezeuti-1604@yopmail.com','3295748233','metalmeccanica','giannipino','Ripetuto','N'),('12345678912','Z4 Industry','SNC','Via Bianchi, 111 Chieti','z4ind@email.it','2333544533','metallurgica','paolablu','Rischio abbandono','N'),('12345678934','AziendaSPA','SPA','Via Indirizzo 123','email@email.it','3333333212','meccanica','giannipino','Fidelizzato','Y'),('12451355463','ComputerCO','SRL','Via Lago 2','mail@libero.it','3245698677','meccanica','giannipino','Fidelizzato','N'),('12636371273','Aziendas','SPA','Via 123','email@email.com','392821938484','meccanica','giannipino','Fidelizzato','N'),('22222222222','Fratelli Albertini','SPA','Via del Mare 41','fratelli@email.it','3249878677','meccanica','giannipino','Fidelizzato','N'),('23455643454','BORRACCE & CO','SRL','via Speranza, 28 Palermo','tigriwippeupre-9951@yopmail.com','4543245432','meccanica','paolablu','Fidelizzato','N'),('23456323467','KIWI','SPA','via della Pace, 78 Genova','teuquaukotossau-3088@yopmail.com','4542656756','informatica','simonenico','Prospect','Y'),('23523543453','Da Antonio','SRL','Via del Fiume 31','daantonio@email.it','3421232111','meccanica','giannipino','Fidelizzato','N'),('44444444444','hiteco','SRL','Via Bianchi, 111 Chieti','email@email.it','1783292736','meccanica','giannipino','Prospect','N'),('44444444446','LGA','SPA','Via Mario Bianchi, 43 Pesaro','dd@d','6372829199','alimentari','simonenico','Prospect','N'),('44556677882','Unife','SRL','Via saragat','edu@unife.it','052123456','conoscenza','simonenico','Fidelizzato','N'),('67384920217','Pfizer','SRL','via Giulio','mailit@mail.it','0937282918','alimentari','giannipino','Nuovo','N'),('67548392456','Elements','SPA','Via Guido Rosato, 145 Urbino','elementsmail@email.it','0654324666','chimica','simonenico','Fidelizzato','N'),('98345627854','ComputerEng','SRL','Via Mario Bianchi, 43 Pesaro','computeng@gmail.com','0672425776','alimentari','giannipino','Rischio abbandono','N'),('98765432123','Melc Factory','SPA','Via Carlo Mayer, 134 Pescara','melccom@gmail.com','0873324563','metalmeccanica','simonenico','Fidelizzato','N');
/*!40000 ALTER TABLE `azienda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `CF` char(16) NOT NULL,
  `Nome` varchar(20) NOT NULL,
  `Cognome` varchar(25) NOT NULL,
  `Telefono` varchar(20) DEFAULT NULL,
  `Data_Nascita` varchar(10) DEFAULT NULL,
  `Email` varchar(40) DEFAULT NULL,
  `P_Iva_Azienda` varchar(25) NOT NULL,
  `Deleted` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`CF`),
  KEY `P.Iva_Azienda_idx` (`P_Iva_Azienda`),
  CONSTRAINT `P_Iva_Azienda` FOREIGN KEY (`P_Iva_Azienda`) REFERENCES `azienda` (`P_Iva`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES ('ANTNNLC4568EF12F','Luca','Antonini','3297148588','1980-05-30','lucantonini@email.it','12345678912','N'),('ASD3432SAAS21222','Antonio','Delle Vigne','3276437211','2019-06-05','em@ail.it','12345678934','Y'),('ASDFER34DSWDFRCS','Roberto','Ranieri','2348967477','1989-06-14','roberto@virgilio.it','12451355463','N'),('BRNDMNC23423E34F','Domenico','Borani','3451254300','1980-05-12','domborani@email.it','11111111111','N'),('CDBFNJM8967SDFGC','Tito','Antonino','4352312344','1984-02-09','tito@mail.it','12332423423','N'),('CDFGXBSCFVDG345G','aa','aa','2','2022-04-14','a@a','12343234234','Y'),('CDFGXSD56DFGSCBF','Alberto','Albertini','2345643555','1979-06-29','alberto@email.it','22222222222','N'),('CNSRBRT3456E2E23','Roberta','Canosa','3454563233','1993-02-10','robertacan@gmail.com','98765432123','N'),('CSDFQWS234ASDCFV','Antonio','Antonini','3249876588','1991-02-06','anto@libero.it','12345321266','N'),('DFCLTD88C27T657G','Alessia','Ginestra','3854638298','1968-06-21','peusagisube-7859@yopmail.com','98345627854','N'),('DJSBDBSAJSJSJSBD','Nome','Cognome','32647364533','2022-05-14','email@sd.com','12636371273','N'),('DTTLCU99D18A485H','Luca','Di Totto','3282549247','1999-04-18','luca@gmail.com','44556677882','N'),('HBNSFGX567TRWDER','Antonio','Sirollo','3452312566','1997-06-20','antosiro@email.it','23523543453','N'),('HQFLVH30S27B437M','Domitilla','Piccone','6574839234','1969-05-23','criddodeugraya-7878@yopmail.com','23455643454','N'),('LNYMGC50A17I505H','Gianni','Morando','4573829394','1967-03-26','sdibf9osinfid@yopmail.com','67384920217','N'),('LNYMGC50A17I507K','Mattia','Verna','3294758694','2002-12-01','hoissappakitei-2025@yopmail.com','23456323467','N'),('NDKMCR51R66D619M','Simone','Di Foglio','3203322545','1961-01-01','cabuvapreugi-7281@yopmail.com','44444444446','N'),('RSSMRC34563ER34D','Valentino','Rossi','3456734566','1991-04-23','marcorossi@email.it','67548392456','N'),('RSSVSC52B07M183C','Vasco','Rossi','3206573928','1952-02-07','vscrossi@gmail.com','12345678910','N'),('SDERFV25R32E326C','Brice ','Kamga','3895955437','2008-07-18','bricekamga@gmail.fr','12345617912','Y'),('SLTRVZ45L08M269O','Paola','D\'Alicarnasso','3906574944','1950-02-08','zugriqueiluxeu-3743@yopmail.com','44444444444','N'),('XCLBBZ52D05B213C','Sofia','Pantalone','3294758374','1987-07-30','grojugrussibei-5048@yopmail.com','12345678654','N');
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
  `nota` varchar(45) NOT NULL,
  `data` date NOT NULL,
  `utente` varchar(45) NOT NULL,
  `cliente_cf` char(16) NOT NULL,
  `Deleted` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`),
  KEY `cliente_cf_idx` (`cliente_cf`),
  CONSTRAINT `cliente_cf` FOREIGN KEY (`cliente_cf`) REFERENCES `cliente` (`CF`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `note`
--

LOCK TABLES `note` WRITE;
/*!40000 ALTER TABLE `note` DISABLE KEYS */;
INSERT INTO `note` VALUES (1,'richiamare','2021-12-08','paolablu','DFCLTD88C27T657G','N'),(2,'modificare offerta','2021-12-04','simonenico','DFCLTD88C27T657G','N'),(3,'modificare ordine','2021-12-15','giannipino','NDKMCR51R66D619M','N'),(4,'richiamare','2021-12-16','paolablu','XCLBBZ52D05B213C','N'),(5,'richiamare','2021-12-13','giannipino','NDKMCR51R66D619M','N'),(7,'richiesta assistenza','2021-12-12','paolablu','RSSMRC34563ER34D','N'),(8,'richiamare','2021-12-14','simonenico','DFCLTD88C27T657G','N'),(9,'richiesta assistenza','2021-12-09','giannipino','BRNDMNC23423E34F','N'),(11,'richiesta assistenza','2021-12-05','paolablu','BRNDMNC23423E34F','N'),(17,'inserire nuovo piano','2021-12-14','simonenico','DFCLTD88C27T657G','N'),(20,'cancellare offerta','2021-12-01','giannipino','RSSMRC34563ER34D','N'),(23,'modificare offerta','2021-12-08','paolablu','NDKMCR51R66D619M','N'),(25,'richiesta assistenza','2021-12-08','paolablu','RSSMRC34563ER34D','N'),(26,'modificare ordine','2021-11-30','simonenico','DFCLTD88C27T657G','N'),(29,'inserire nuovo piano','2021-12-16','giannipino','DFCLTD88C27T657G','N'),(31,'consulenza','2021-12-01','giannipino','DFCLTD88C27T657G','N'),(33,'consulenza ','2021-12-11','simonenico','RSSMRC34563ER34D','N'),(34,'modificare offerta','2021-12-15','simonenico','NDKMCR51R66D619M','N'),(35,'modificare offerta','2021-12-05','paolablu','CNSRBRT3456E2E23','N'),(36,'richiamare','2021-12-01','paolablu','DTTLCU99D18A485H','N'),(42,'modificare offerta','2022-02-02','paolablu','RSSVSC52B07M183C','N'),(44,'modificare offerta','2022-02-22','giannipino','LNYMGC50A17I507K','N'),(47,'richiamare','2022-02-13','giannipino','RSSMRC34563ER34D','N'),(48,'richiamare','2022-02-23','giannipino','ANTNNLC4568EF12F','N'),(49,'modificare offerta','2022-02-24','paolablu','ANTNNLC4568EF12F','N'),(54,'richiamare','2022-03-02','giannipino','HQFLVH30S27B437M','Y'),(55,'Richiamare','2022-03-02','giannipino','BRNDMNC23423E34F','N'),(56,'non modificare offerta','2022-03-16','giannipino','HQFLVH30S27B437M','N'),(57,'richiamare','2022-03-14','giannipino','HQFLVH30S27B437M','N'),(58,'richiamare','2022-03-15','giannipino','HQFLVH30S27B437M','N'),(59,'richiamare','2022-03-15','giannipino','XCLBBZ52D05B213C','N'),(61,'richiamare','2022-03-17','giannipino','SLTRVZ45L08M269O','N'),(62,'modificare offerta','2022-04-28','giannipino','RSSVSC52B07M183C','N'),(63,'richiamare','2022-05-07','giannipino','SLTRVZ45L08M269O','N'),(64,'modificare offerta','2022-04-29','giannipino','HQFLVH30S27B437M','Y'),(65,'richiesta assistenza','2022-05-03','giannipino','BRNDMNC23423E34F','N'),(66,'richiesta assistenza','2022-05-03','giannipino','ANTNNLC4568EF12F','Y'),(67,'richiamare subito','2022-05-14','paolablu','BRNDMNC23423E34F','N'),(68,'prendere appuntamento','2022-05-10','paolablu','DFCLTD88C27T657G','N'),(69,'prendere appuntamento','2022-05-10','paolablu','DFCLTD88C27T657G','N'),(70,'Richiamare','2022-05-12','giannipino','DFCLTD88C27T657G','N'),(71,'nota','2022-05-14','giannipino','BRNDMNC23423E34F','N'),(72,'Assente domani','2025-11-10','giannipino','ANTNNLC4568EF12F','N'),(73,'Assente domani','2025-11-30','giannipino','ANTNNLC4568EF12F','N');
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
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proposte`
--

LOCK TABLES `proposte` WRITE;
/*!40000 ALTER TABLE `proposte` DISABLE KEYS */;
INSERT INTO `proposte` VALUES (1,'3x2',9,'11111111111','Y'),(2,'free',11,'12345678654','N'),(3,'metà prezzo',12,'98765432123','N'),(4,'free',13,'12345678910','N'),(5,'sconto 20%',14,'23455643454','N'),(6,'sconto 30%',15,'12345678912','N'),(9,'free',15,'98345627854','N'),(10,'metà prezzo',11,'11111111111','N'),(11,'sconto 30%',9,'98765432123','N'),(12,'sconto 30%',13,'98765432123','N'),(13,'metà prezzo',11,'98345627854','N'),(14,'sconto 20%',11,'12345678910','N'),(15,'free',15,'67548392456','N'),(16,'3x2',9,'11111111111','Y'),(17,'metà prezzo',14,'12345678654','N'),(18,'sconto 20%',14,'12345678912','N'),(19,'metà prezzo',13,'67384920217','N'),(20,'3x2',9,'23456323467','N'),(21,'3x2',13,'44556677882','N'),(22,'free',11,'44556677882','N'),(23,'metà prezzo',12,'44444444446','N'),(24,'sconto 20%',12,'23455643454','N'),(25,'metà prezzo',9,'44444444444','N'),(26,'free',11,'23456323467','N'),(27,'metà prezzo',12,'44444444444','N'),(28,'metà prezzo',9,'23456323467','N'),(31,'metà prezzo',45,'11111111111','N'),(32,'metà prezzo',15,'12345678654','Y'),(35,'sconto 20%',10,'11111111111','Y'),(37,'sconto 20%',9,'11111111111','N'),(38,'metà prezzo',12,'44444444444','Y'),(39,'metà prezzo',50,'98345627854','Y'),(41,'sconto 20%',9,'11111111111','Y'),(42,'Prova',9,'12345678910','Y'),(43,'3x2',9,'12345678654','N'),(44,'3x2',10,'11111111111','Y'),(45,'3x2',122,'11111111111','Y'),(46,'proposta',10,'12332423423','Y'),(47,'Off',12,'23523543453','Y'),(48,'Off',9,'11111111111','Y'),(49,'Off',15,'12345678654','Y');
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
INSERT INTO `servizi_consulenza` VALUES (9,'consulenza mezza giornata','N'),(10,'televendita2','Y'),(11,'consulenza completa','N'),(12,'guida passo passo','N'),(13,'assistenza tecnica','N'),(14,'assistenza commerciale','N'),(15,'guida installazione','N'),(20,'amministrazione','Y'),(40,'servizio','Y'),(45,'matematica','N'),(50,'tt','Y'),(51,'Marketing','Y'),(55,'centralino','Y'),(122,'vendita online','Y');
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
  `CF_Utente` char(16) NOT NULL,
  `Deleted` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`Username`),
  KEY `CF_Utente_idx` (`CF_Utente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES ('admin','admin','admin','ADMINC34563ER34D','N'),('antonio96','jimbo','admin','HFKYUI55T789A26Y','N'),('antonioporro','danipirell00','admin','PRLLDNL2345E234E','N'),('brice','741741','admin','RSSERC34563ER34D','N'),('franco126','brioschi','admin','FRCFTG56F45E567T','N'),('francorossi','bordom123','admin','BRNDMNC23423E34F','N'),('gianni70','hainti','admin','GNNHTY67T34S465H','N'),('giannipino','aa','registrato','ZFFTTNTN34564E34','N'),('paolablu','123456','registrato','CNSRBRT3456E2E23','N'),('pubblico','pubblico','pubblico','PUBBLI34563ER34D','N'),('registrato','registrato','registrato','REGIST34563ER34D','N'),('renatopallini','marcored70$','pubblico','RSSMRC34563ER34D','N'),('renatozero','mivendo','pubblico','RNTZRO50A23R456T','N'),('simonenico','lucnt92&','registrato','ANTNNLC4568EF12F','N'),('user2','ss','admin','ASDE3D34543KLKMS','Y');
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

-- Dump completed on 2025-11-30 15:29:55
