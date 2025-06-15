-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: projeeweb
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `reservas`
--

DROP TABLE IF EXISTS `reservas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservas` (
  `id_reserva` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `id_sala` int NOT NULL,
  `data_hora_inicio` datetime NOT NULL,
  `data_hora_fim` datetime NOT NULL,
  `finalidade` text NOT NULL,
  `status_reserva` enum('PENDENTE','CONFIRMADA','CANCELADA') DEFAULT 'PENDENTE',
  `data_criacao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_reserva`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_sala` (`id_sala`),
  CONSTRAINT `reservas_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`),
  CONSTRAINT `reservas_ibfk_2` FOREIGN KEY (`id_sala`) REFERENCES `salas` (`id_sala`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservas`
--

LOCK TABLES `reservas` WRITE;
/*!40000 ALTER TABLE `reservas` DISABLE KEYS */;
INSERT INTO `reservas` VALUES (4,1,1,'2025-06-30 16:40:00','2025-06-30 17:50:00','Apresentação de projeto da Turma B','CANCELADA','2025-06-06 23:38:35'),(5,1,5,'2025-06-07 08:00:00','2025-06-07 10:00:00','Palestra sobre vazamentos de dados','CONFIRMADA','2025-06-06 23:42:46'),(6,2,3,'2025-06-16 16:40:00','2025-06-16 19:20:00','Desenvolvimento de um aplicativo.','PENDENTE','2025-06-06 23:43:47'),(7,1,1,'2025-06-17 10:00:00','2025-06-17 14:00:00','Aplicação da prova de recuperação','PENDENTE','2025-06-06 23:47:36'),(8,1,1,'2025-06-08 16:40:00','2025-06-08 17:40:00','Aplicação de prova','PENDENTE','2025-06-07 00:30:27'),(10,4,1,'2025-06-16 16:40:00','2025-06-16 17:40:00','Treinamento','PENDENTE','2025-06-07 00:33:26'),(11,1,1,'2025-06-17 16:40:00','2025-06-17 17:40:00','Apresentação','PENDENTE','2025-06-07 00:34:22');
/*!40000 ALTER TABLE `reservas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salas`
--

DROP TABLE IF EXISTS `salas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salas` (
  `id_sala` int NOT NULL AUTO_INCREMENT,
  `nome_sala` varchar(100) NOT NULL,
  `capacidade` int NOT NULL,
  `descricao` text,
  `disponivel` tinyint(1) DEFAULT '1',
  `equipamentos` text,
  `data_criacao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_sala`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salas`
--

LOCK TABLES `salas` WRITE;
/*!40000 ALTER TABLE `salas` DISABLE KEYS */;
INSERT INTO `salas` VALUES (1,'Sala 101',20,'Sala de reuniões pequena',1,'Projetor, Quadro branco','2025-06-02 16:05:58'),(2,'Sala 102',30,'Sala de reuniões média',1,'Projetor, TV, Quadro branco','2025-06-02 16:05:58'),(3,'Laboratório 1',25,'Laboratório de informática',1,'Computadores, Projetor','2025-06-02 16:05:58'),(4,'Laboratório 2',25,'Laboratório de informática',1,'Computadores, Projetor','2025-06-02 16:05:58'),(5,'Auditório',100,'Auditório principal',1,'Sistema de som, Projetor, Palco','2025-06-02 16:05:58'),(6,'Sala 203',15,'Sala de aula',1,'1x - Projetor','2025-06-06 23:12:10');
/*!40000 ALTER TABLE `salas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nome_usuario` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `tipo_usuario` enum('ADMIN','USUARIO') DEFAULT 'USUARIO',
  `ativo` tinyint(1) DEFAULT '1',
  `data_criacao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Administrador','admin@sistema.com','admin123','ADMIN',1,'2025-06-02 16:05:58'),(2,'João Silva','joao@email.com','123456','USUARIO',1,'2025-06-02 16:05:58'),(3,'Maria Santos','maria@email.com','123456','USUARIO',1,'2025-06-02 16:05:58'),(4,'Arthur Junior','arthur@email.com','Arthur123','USUARIO',1,'2025-06-06 23:10:29');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-15 16:21:06
