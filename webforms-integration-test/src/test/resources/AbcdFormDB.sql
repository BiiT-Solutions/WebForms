CREATE DATABASE  IF NOT EXISTS `abcdtest` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `abcdtest`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: vagrant-mysql.biit-solutions.com    Database: abcdtest
-- ------------------------------------------------------
-- Server version	5.6.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `TestAnswerMultiCheckBox_multiCheckBoxValue`
--

DROP TABLE IF EXISTS `TestAnswerMultiCheckBox_multiCheckBoxValue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TestAnswerMultiCheckBox_multiCheckBoxValue` (
  `TestAnswerMultiCheckBox_ID` bigint(20) NOT NULL,
  `multiCheckBoxValue` varchar(255) DEFAULT NULL,
  KEY `FK_9hig9ck1w4ry8gscespat7k7i` (`TestAnswerMultiCheckBox_ID`),
  CONSTRAINT `FK_9hig9ck1w4ry8gscespat7k7i` FOREIGN KEY (`TestAnswerMultiCheckBox_ID`) REFERENCES `test_answer_multi_checkbox` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TestAnswerMultiCheckBox_multiCheckBoxValue`
--

LOCK TABLES `TestAnswerMultiCheckBox_multiCheckBoxValue` WRITE;
/*!40000 ALTER TABLE `TestAnswerMultiCheckBox_multiCheckBoxValue` DISABLE KEYS */;
/*!40000 ALTER TABLE `TestAnswerMultiCheckBox_multiCheckBoxValue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram`
--

DROP TABLE IF EXISTS `diagram`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_cqcepkojqmp1r8a42yb1hh1c4` (`ID`),
  UNIQUE KEY `UK_i991a76ub6cc04w67skutnlft` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram`
--

LOCK TABLES `diagram` WRITE;
/*!40000 ALTER TABLE `diagram` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_biit_text`
--

DROP TABLE IF EXISTS `diagram_biit_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_biit_text` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `fill` varchar(255) DEFAULT NULL,
  `fontSize` varchar(255) DEFAULT NULL,
  `stroke` varchar(255) DEFAULT NULL,
  `strokeWidth` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_jcm4u3t3e37kkjb5mltbs5mha` (`ID`),
  UNIQUE KEY `UK_26e5ksw8ie3l7k0gtpdghwxr8` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_biit_text`
--

LOCK TABLES `diagram_biit_text` WRITE;
/*!40000 ALTER TABLE `diagram_biit_text` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_biit_text` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_calculation`
--

DROP TABLE IF EXISTS `diagram_calculation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_calculation` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `embeds` varchar(255) DEFAULT NULL,
  `jointjsId` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `z` int(11) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `angle` float NOT NULL,
  `tooltip` varchar(255) DEFAULT NULL,
  `biitText_ID` bigint(20) DEFAULT NULL,
  `position_ID` bigint(20) DEFAULT NULL,
  `size_ID` bigint(20) DEFAULT NULL,
  `expression_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_telsgfj7x5yb9vr3tyes22dfo` (`ID`),
  UNIQUE KEY `UK_skg33psaqtjwx1oevy1wp7tfa` (`comparationId`),
  KEY `FK_psofb7hd6w4ofxlvoas1fae45` (`expression_ID`),
  KEY `FK_1uyl1hy99cv9fsblw0vty8nva` (`biitText_ID`),
  KEY `FK_g827oda16qin30e31hhplvhyl` (`position_ID`),
  KEY `FK_5km22on0tjdp4ni437u0cqvx1` (`size_ID`),
  KEY `FK_4hddxi4jsy1lyvul13uh1im7i` (`parent_ID`),
  CONSTRAINT `FK_1uyl1hy99cv9fsblw0vty8nva` FOREIGN KEY (`biitText_ID`) REFERENCES `diagram_biit_text` (`ID`),
  CONSTRAINT `FK_4hddxi4jsy1lyvul13uh1im7i` FOREIGN KEY (`parent_ID`) REFERENCES `diagram` (`ID`),
  CONSTRAINT `FK_5km22on0tjdp4ni437u0cqvx1` FOREIGN KEY (`size_ID`) REFERENCES `diagram_sizes` (`ID`),
  CONSTRAINT `FK_g827oda16qin30e31hhplvhyl` FOREIGN KEY (`position_ID`) REFERENCES `diagram_points` (`ID`),
  CONSTRAINT `FK_psofb7hd6w4ofxlvoas1fae45` FOREIGN KEY (`expression_ID`) REFERENCES `expressions_chain` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_calculation`
--

LOCK TABLES `diagram_calculation` WRITE;
/*!40000 ALTER TABLE `diagram_calculation` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_calculation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_child`
--

DROP TABLE IF EXISTS `diagram_child`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_child` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `embeds` varchar(255) DEFAULT NULL,
  `jointjsId` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `z` int(11) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `angle` float NOT NULL,
  `tooltip` varchar(255) DEFAULT NULL,
  `biitText_ID` bigint(20) DEFAULT NULL,
  `position_ID` bigint(20) DEFAULT NULL,
  `size_ID` bigint(20) DEFAULT NULL,
  `diagram_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_g7xdiesweik5op9e9onvswkbc` (`ID`),
  UNIQUE KEY `UK_r7oc8jgmhkeagu3emxy90ttof` (`comparationId`),
  KEY `FK_6ox0s0pktxy9rvcdmdcju248e` (`diagram_ID`),
  KEY `FK_s4ju47278gra0mv0udmlfm30p` (`biitText_ID`),
  KEY `FK_5eniu52wmx99g8daejx2ea7wy` (`position_ID`),
  KEY `FK_mk7mxfcjnf05307b1wgoj9tbr` (`size_ID`),
  KEY `FK_qs6gusu4xfg3ntskiiw6i8nlk` (`parent_ID`),
  CONSTRAINT `FK_5eniu52wmx99g8daejx2ea7wy` FOREIGN KEY (`position_ID`) REFERENCES `diagram_points` (`ID`),
  CONSTRAINT `FK_6ox0s0pktxy9rvcdmdcju248e` FOREIGN KEY (`diagram_ID`) REFERENCES `diagram` (`ID`),
  CONSTRAINT `FK_mk7mxfcjnf05307b1wgoj9tbr` FOREIGN KEY (`size_ID`) REFERENCES `diagram_sizes` (`ID`),
  CONSTRAINT `FK_qs6gusu4xfg3ntskiiw6i8nlk` FOREIGN KEY (`parent_ID`) REFERENCES `diagram` (`ID`),
  CONSTRAINT `FK_s4ju47278gra0mv0udmlfm30p` FOREIGN KEY (`biitText_ID`) REFERENCES `diagram_biit_text` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_child`
--

LOCK TABLES `diagram_child` WRITE;
/*!40000 ALTER TABLE `diagram_child` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_child` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_fork`
--

DROP TABLE IF EXISTS `diagram_fork`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_fork` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `embeds` varchar(255) DEFAULT NULL,
  `jointjsId` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `z` int(11) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `angle` float NOT NULL,
  `tooltip` varchar(255) DEFAULT NULL,
  `biitText_ID` bigint(20) DEFAULT NULL,
  `position_ID` bigint(20) DEFAULT NULL,
  `size_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_hsf0g6nuli7nv6ypx60d3n4dg` (`ID`),
  UNIQUE KEY `UK_efh14aqc52m2bt7suefmq12qn` (`comparationId`),
  KEY `FK_7yh670hkt93t0pcttv0bq1uuc` (`biitText_ID`),
  KEY `FK_eii7oliuam2g30sl5dtq1pjle` (`position_ID`),
  KEY `FK_hsgneuh3yuqhqv4kogkhp91u8` (`size_ID`),
  KEY `FK_635sdu3m0ivan4vb6f86e6ws0` (`parent_ID`),
  CONSTRAINT `FK_635sdu3m0ivan4vb6f86e6ws0` FOREIGN KEY (`parent_ID`) REFERENCES `diagram` (`ID`),
  CONSTRAINT `FK_7yh670hkt93t0pcttv0bq1uuc` FOREIGN KEY (`biitText_ID`) REFERENCES `diagram_biit_text` (`ID`),
  CONSTRAINT `FK_eii7oliuam2g30sl5dtq1pjle` FOREIGN KEY (`position_ID`) REFERENCES `diagram_points` (`ID`),
  CONSTRAINT `FK_hsgneuh3yuqhqv4kogkhp91u8` FOREIGN KEY (`size_ID`) REFERENCES `diagram_sizes` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_fork`
--

LOCK TABLES `diagram_fork` WRITE;
/*!40000 ALTER TABLE `diagram_fork` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_fork` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_fork_expression_value_tree_object_reference`
--

DROP TABLE IF EXISTS `diagram_fork_expression_value_tree_object_reference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_fork_expression_value_tree_object_reference` (
  `diagram_fork_ID` bigint(20) NOT NULL,
  `references_ID` bigint(20) NOT NULL,
  UNIQUE KEY `UK_41wpgkw0p4oj65dmnb34e4lqj` (`references_ID`),
  KEY `FK_k4ysmnavvafierl7bdith4l1j` (`diagram_fork_ID`),
  CONSTRAINT `FK_k4ysmnavvafierl7bdith4l1j` FOREIGN KEY (`diagram_fork_ID`) REFERENCES `diagram_fork` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_fork_expression_value_tree_object_reference`
--

LOCK TABLES `diagram_fork_expression_value_tree_object_reference` WRITE;
/*!40000 ALTER TABLE `diagram_fork_expression_value_tree_object_reference` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_fork_expression_value_tree_object_reference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_links`
--

DROP TABLE IF EXISTS `diagram_links`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_links` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `embeds` varchar(255) DEFAULT NULL,
  `jointjsId` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `z` int(11) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `attrs` longtext,
  `manhattan` bit(1) NOT NULL,
  `smooth` bit(1) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `vertices` longtext,
  `expressionChain_ID` bigint(20) DEFAULT NULL,
  `source_ID` bigint(20) DEFAULT NULL,
  `target_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_h604hokhqdqbq8jnjxeupxokf` (`ID`),
  UNIQUE KEY `UK_ph2m4o4wkkd1ls8xxffxf0q4` (`comparationId`),
  KEY `FK_f9u34y5vofhcg8gcr88wm8ox1` (`expressionChain_ID`),
  KEY `FK_4uc5itybcysh76lvtc9ty3469` (`source_ID`),
  KEY `FK_7jd18fn2cgk256ih0iyoorfti` (`target_ID`),
  KEY `FK_82fd22e255jkopdyoep9xmk2y` (`parent_ID`),
  CONSTRAINT `FK_4uc5itybcysh76lvtc9ty3469` FOREIGN KEY (`source_ID`) REFERENCES `diagram_nodes` (`ID`),
  CONSTRAINT `FK_7jd18fn2cgk256ih0iyoorfti` FOREIGN KEY (`target_ID`) REFERENCES `diagram_nodes` (`ID`),
  CONSTRAINT `FK_82fd22e255jkopdyoep9xmk2y` FOREIGN KEY (`parent_ID`) REFERENCES `diagram` (`ID`),
  CONSTRAINT `FK_f9u34y5vofhcg8gcr88wm8ox1` FOREIGN KEY (`expressionChain_ID`) REFERENCES `expressions_chain` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_links`
--

LOCK TABLES `diagram_links` WRITE;
/*!40000 ALTER TABLE `diagram_links` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_links` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_nodes`
--

DROP TABLE IF EXISTS `diagram_nodes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_nodes` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `jointjsId` varchar(255) DEFAULT NULL,
  `port` varchar(255) DEFAULT NULL,
  `selector` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_8a8ejpu1o9334lnd8qaxmngtc` (`ID`),
  UNIQUE KEY `UK_r73mc67afakc2crvohrek8qk4` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_nodes`
--

LOCK TABLES `diagram_nodes` WRITE;
/*!40000 ALTER TABLE `diagram_nodes` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_nodes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_points`
--

DROP TABLE IF EXISTS `diagram_points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_points` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_hlburchqkc9q4ahhvbtm8r9p9` (`ID`),
  UNIQUE KEY `UK_nmcm8lwfgn9lwd2kg6e77mlpx` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_points`
--

LOCK TABLES `diagram_points` WRITE;
/*!40000 ALTER TABLE `diagram_points` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_points` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_repeat`
--

DROP TABLE IF EXISTS `diagram_repeat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_repeat` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `embeds` varchar(255) DEFAULT NULL,
  `jointjsId` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `z` int(11) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `angle` float NOT NULL,
  `tooltip` varchar(255) DEFAULT NULL,
  `biitText_ID` bigint(20) DEFAULT NULL,
  `position_ID` bigint(20) DEFAULT NULL,
  `size_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_cmfc1l3o05vfhv0pt6mgqm3nd` (`ID`),
  UNIQUE KEY `UK_5jgfcctfdg2s0qbkuaekf6m0g` (`comparationId`),
  KEY `FK_aqu17m49nfn0yojvr0wndem3j` (`biitText_ID`),
  KEY `FK_f4cg4oy7tlbbcheb3ec7045yo` (`position_ID`),
  KEY `FK_qu2ktgi18pqa3t86hk44xksfx` (`size_ID`),
  KEY `FK_erdsxlq0j84okd36bay5dt6q9` (`parent_ID`),
  CONSTRAINT `FK_aqu17m49nfn0yojvr0wndem3j` FOREIGN KEY (`biitText_ID`) REFERENCES `diagram_biit_text` (`ID`),
  CONSTRAINT `FK_erdsxlq0j84okd36bay5dt6q9` FOREIGN KEY (`parent_ID`) REFERENCES `diagram` (`ID`),
  CONSTRAINT `FK_f4cg4oy7tlbbcheb3ec7045yo` FOREIGN KEY (`position_ID`) REFERENCES `diagram_points` (`ID`),
  CONSTRAINT `FK_qu2ktgi18pqa3t86hk44xksfx` FOREIGN KEY (`size_ID`) REFERENCES `diagram_sizes` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_repeat`
--

LOCK TABLES `diagram_repeat` WRITE;
/*!40000 ALTER TABLE `diagram_repeat` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_repeat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_rule`
--

DROP TABLE IF EXISTS `diagram_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_rule` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `embeds` varchar(255) DEFAULT NULL,
  `jointjsId` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `z` int(11) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `angle` float NOT NULL,
  `tooltip` varchar(255) DEFAULT NULL,
  `biitText_ID` bigint(20) DEFAULT NULL,
  `position_ID` bigint(20) DEFAULT NULL,
  `size_ID` bigint(20) DEFAULT NULL,
  `rule_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_o4hrkbic99uuo11mqks328fv9` (`ID`),
  UNIQUE KEY `UK_l4rl73pva0mblx0df1ntrb3m7` (`comparationId`),
  KEY `FK_fxsyldssewd7iifwrr4f985d5` (`rule_ID`),
  KEY `FK_nbxgk4j5653j1qa0bw4y70w65` (`biitText_ID`),
  KEY `FK_aglllff4oo9xy4r8knqyu8v3k` (`position_ID`),
  KEY `FK_7dwjxlkwog3pq4dvcmhx9sssw` (`size_ID`),
  KEY `FK_h1iqc4v2ldablgwfng2000fcv` (`parent_ID`),
  CONSTRAINT `FK_7dwjxlkwog3pq4dvcmhx9sssw` FOREIGN KEY (`size_ID`) REFERENCES `diagram_sizes` (`ID`),
  CONSTRAINT `FK_aglllff4oo9xy4r8knqyu8v3k` FOREIGN KEY (`position_ID`) REFERENCES `diagram_points` (`ID`),
  CONSTRAINT `FK_fxsyldssewd7iifwrr4f985d5` FOREIGN KEY (`rule_ID`) REFERENCES `rule` (`ID`),
  CONSTRAINT `FK_h1iqc4v2ldablgwfng2000fcv` FOREIGN KEY (`parent_ID`) REFERENCES `diagram` (`ID`),
  CONSTRAINT `FK_nbxgk4j5653j1qa0bw4y70w65` FOREIGN KEY (`biitText_ID`) REFERENCES `diagram_biit_text` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_rule`
--

LOCK TABLES `diagram_rule` WRITE;
/*!40000 ALTER TABLE `diagram_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_sink`
--

DROP TABLE IF EXISTS `diagram_sink`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_sink` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `embeds` varchar(255) DEFAULT NULL,
  `jointjsId` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `z` int(11) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `angle` float NOT NULL,
  `tooltip` varchar(255) DEFAULT NULL,
  `biitText_ID` bigint(20) DEFAULT NULL,
  `position_ID` bigint(20) DEFAULT NULL,
  `size_ID` bigint(20) DEFAULT NULL,
  `expression_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_cjl3d4py1hd5d5tvo23yd780y` (`ID`),
  UNIQUE KEY `UK_bo43xa2d2veyl1w7o8xfjse1m` (`comparationId`),
  KEY `FK_mq3iht3l8ij5iuild1l44ejeu` (`expression_ID`),
  KEY `FK_rektcafnj1qj3khm8w8sxyx8i` (`biitText_ID`),
  KEY `FK_abyk770ndq91g7yecoi8h3pjt` (`position_ID`),
  KEY `FK_ff3w8r2n4ux3vmd2tr1lc00sd` (`size_ID`),
  KEY `FK_g13n7i3embg63j1pkswrlhvbb` (`parent_ID`),
  CONSTRAINT `FK_abyk770ndq91g7yecoi8h3pjt` FOREIGN KEY (`position_ID`) REFERENCES `diagram_points` (`ID`),
  CONSTRAINT `FK_ff3w8r2n4ux3vmd2tr1lc00sd` FOREIGN KEY (`size_ID`) REFERENCES `diagram_sizes` (`ID`),
  CONSTRAINT `FK_g13n7i3embg63j1pkswrlhvbb` FOREIGN KEY (`parent_ID`) REFERENCES `diagram` (`ID`),
  CONSTRAINT `FK_mq3iht3l8ij5iuild1l44ejeu` FOREIGN KEY (`expression_ID`) REFERENCES `expressions_chain` (`ID`),
  CONSTRAINT `FK_rektcafnj1qj3khm8w8sxyx8i` FOREIGN KEY (`biitText_ID`) REFERENCES `diagram_biit_text` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_sink`
--

LOCK TABLES `diagram_sink` WRITE;
/*!40000 ALTER TABLE `diagram_sink` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_sink` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_sizes`
--

DROP TABLE IF EXISTS `diagram_sizes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_sizes` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `height` int(11) NOT NULL,
  `width` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_ipg7ga5eq6253uw0hwppg86ub` (`ID`),
  UNIQUE KEY `UK_lpwfoljk42ognggvtk6m0w75j` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_sizes`
--

LOCK TABLES `diagram_sizes` WRITE;
/*!40000 ALTER TABLE `diagram_sizes` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_sizes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_source`
--

DROP TABLE IF EXISTS `diagram_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_source` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `embeds` varchar(255) DEFAULT NULL,
  `jointjsId` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `z` int(11) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `angle` float NOT NULL,
  `tooltip` varchar(255) DEFAULT NULL,
  `biitText_ID` bigint(20) DEFAULT NULL,
  `position_ID` bigint(20) DEFAULT NULL,
  `size_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_ff46qqbboep27fpldjifb16ou` (`ID`),
  UNIQUE KEY `UK_2skl3aj5ivhq9i7j7vun9eak3` (`comparationId`),
  KEY `FK_d1l2nioib46nwd01bhbofv6ny` (`biitText_ID`),
  KEY `FK_17lhs8umd0536jewa7ht9hbs2` (`position_ID`),
  KEY `FK_3h8kcg1497rr1y30r9kb4b90m` (`size_ID`),
  KEY `FK_a2hmqaospl98o7hmxdvuh093f` (`parent_ID`),
  CONSTRAINT `FK_17lhs8umd0536jewa7ht9hbs2` FOREIGN KEY (`position_ID`) REFERENCES `diagram_points` (`ID`),
  CONSTRAINT `FK_3h8kcg1497rr1y30r9kb4b90m` FOREIGN KEY (`size_ID`) REFERENCES `diagram_sizes` (`ID`),
  CONSTRAINT `FK_a2hmqaospl98o7hmxdvuh093f` FOREIGN KEY (`parent_ID`) REFERENCES `diagram` (`ID`),
  CONSTRAINT `FK_d1l2nioib46nwd01bhbofv6ny` FOREIGN KEY (`biitText_ID`) REFERENCES `diagram_biit_text` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_source`
--

LOCK TABLES `diagram_source` WRITE;
/*!40000 ALTER TABLE `diagram_source` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagram_table`
--

DROP TABLE IF EXISTS `diagram_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram_table` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `embeds` varchar(255) DEFAULT NULL,
  `jointjsId` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `z` int(11) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `angle` float NOT NULL,
  `tooltip` varchar(255) DEFAULT NULL,
  `biitText_ID` bigint(20) DEFAULT NULL,
  `position_ID` bigint(20) DEFAULT NULL,
  `size_ID` bigint(20) DEFAULT NULL,
  `table_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_rdrkegb0jykxqg5jxtb2fbhc5` (`ID`),
  UNIQUE KEY `UK_8f7iwv857341khhqhi12jharn` (`comparationId`),
  KEY `FK_m64qi6jykhdxghdvv760geh30` (`table_ID`),
  KEY `FK_7iejtd4odckryjuy5lu68ebm5` (`biitText_ID`),
  KEY `FK_m0co9nwuhqbwjf59ha3ltisma` (`position_ID`),
  KEY `FK_odmfi3t11xmwe0hnevh2ymxek` (`size_ID`),
  KEY `FK_n4ug9duflxy9px6g0fb8uoq6w` (`parent_ID`),
  CONSTRAINT `FK_7iejtd4odckryjuy5lu68ebm5` FOREIGN KEY (`biitText_ID`) REFERENCES `diagram_biit_text` (`ID`),
  CONSTRAINT `FK_m0co9nwuhqbwjf59ha3ltisma` FOREIGN KEY (`position_ID`) REFERENCES `diagram_points` (`ID`),
  CONSTRAINT `FK_m64qi6jykhdxghdvv760geh30` FOREIGN KEY (`table_ID`) REFERENCES `rule_decision_table` (`ID`),
  CONSTRAINT `FK_n4ug9duflxy9px6g0fb8uoq6w` FOREIGN KEY (`parent_ID`) REFERENCES `diagram` (`ID`),
  CONSTRAINT `FK_odmfi3t11xmwe0hnevh2ymxek` FOREIGN KEY (`size_ID`) REFERENCES `diagram_sizes` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram_table`
--

LOCK TABLES `diagram_table` WRITE;
/*!40000 ALTER TABLE `diagram_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagram_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `elements_of_diagram`
--

DROP TABLE IF EXISTS `elements_of_diagram`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `elements_of_diagram` (
  `diagram_ID` bigint(20) NOT NULL,
  `diagramObjects_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`diagram_ID`,`diagramObjects_ID`),
  UNIQUE KEY `UK_40p43sjr90wg4lyo7gwcbybok` (`diagramObjects_ID`),
  CONSTRAINT `FK_i75jp9q1ac01gmqrc3ur49f0g` FOREIGN KEY (`diagram_ID`) REFERENCES `diagram` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elements_of_diagram`
--

LOCK TABLES `elements_of_diagram` WRITE;
/*!40000 ALTER TABLE `elements_of_diagram` DISABLE KEYS */;
/*!40000 ALTER TABLE `elements_of_diagram` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_function`
--

DROP TABLE IF EXISTS `expression_function`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_function` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_7hl0l4oq4hokig8i3g30hv3s4` (`ID`),
  UNIQUE KEY `UK_lbyra8yeo77q9okjte7yh5mck` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_function`
--

LOCK TABLES `expression_function` WRITE;
/*!40000 ALTER TABLE `expression_function` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_operator_logic`
--

DROP TABLE IF EXISTS `expression_operator_logic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_operator_logic` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `currentValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_5nv4rm0dcwpx72pidk99uqdcv` (`ID`),
  UNIQUE KEY `UK_joflju3ccpgq1r4y7ro8egx3g` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_operator_logic`
--

LOCK TABLES `expression_operator_logic` WRITE;
/*!40000 ALTER TABLE `expression_operator_logic` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_operator_logic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_operator_math`
--

DROP TABLE IF EXISTS `expression_operator_math`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_operator_math` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `currentValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_blgg3hvajwkh0url70tu2l3nc` (`ID`),
  UNIQUE KEY `UK_8thp00x8evlgl3w2guass30ql` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_operator_math`
--

LOCK TABLES `expression_operator_math` WRITE;
/*!40000 ALTER TABLE `expression_operator_math` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_operator_math` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_plugin_method`
--

DROP TABLE IF EXISTS `expression_plugin_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_plugin_method` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `pluginInterface` varchar(255) DEFAULT NULL,
  `pluginMethodName` varchar(255) DEFAULT NULL,
  `pluginName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_6hn9bd5cyjqtohgaybvt22ujp` (`ID`),
  UNIQUE KEY `UK_tnq0p980rjfkyf5sal1oq6290` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_plugin_method`
--

LOCK TABLES `expression_plugin_method` WRITE;
/*!40000 ALTER TABLE `expression_plugin_method` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_plugin_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_symbol`
--

DROP TABLE IF EXISTS `expression_symbol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_symbol` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_d5pgsndfldjl77mxwesg47oyy` (`ID`),
  UNIQUE KEY `UK_sw4rg5w1mhkjtuhmshj7jeh5w` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_symbol`
--

LOCK TABLES `expression_symbol` WRITE;
/*!40000 ALTER TABLE `expression_symbol` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_symbol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_value_boolean`
--

DROP TABLE IF EXISTS `expression_value_boolean`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_value_boolean` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `value` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_8ji8c5v2rtu64hu7fd6w16wds` (`ID`),
  UNIQUE KEY `UK_9ji3f3ooll872c0bitchknl29` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_value_boolean`
--

LOCK TABLES `expression_value_boolean` WRITE;
/*!40000 ALTER TABLE `expression_value_boolean` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_value_boolean` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_value_custom_variable`
--

DROP TABLE IF EXISTS `expression_value_custom_variable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_value_custom_variable` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `reference_ID` bigint(20) DEFAULT NULL,
  `variable_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_g4ftxu9duvw2yoonn0mjb5sku` (`ID`),
  UNIQUE KEY `UK_6be9j8k8bp0uudvgtk61e4tu4` (`comparationId`),
  KEY `FK_nfs9s2t4plx74r3n105o09sjx` (`variable_ID`),
  CONSTRAINT `FK_nfs9s2t4plx74r3n105o09sjx` FOREIGN KEY (`variable_ID`) REFERENCES `form_custom_variables` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_value_custom_variable`
--

LOCK TABLES `expression_value_custom_variable` WRITE;
/*!40000 ALTER TABLE `expression_value_custom_variable` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_value_custom_variable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_value_generic_custom_variable`
--

DROP TABLE IF EXISTS `expression_value_generic_custom_variable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_value_generic_custom_variable` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `variable_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_1ilcykdnhd0ixr9iaomtrog3h` (`ID`),
  UNIQUE KEY `UK_72ucvxvkp3dgy4ci55hda72p5` (`comparationId`),
  KEY `FK_4u49ngxh67i7rqxx3xb7379yi` (`variable_ID`),
  CONSTRAINT `FK_4u49ngxh67i7rqxx3xb7379yi` FOREIGN KEY (`variable_ID`) REFERENCES `form_custom_variables` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_value_generic_custom_variable`
--

LOCK TABLES `expression_value_generic_custom_variable` WRITE;
/*!40000 ALTER TABLE `expression_value_generic_custom_variable` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_value_generic_custom_variable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_value_generic_variable`
--

DROP TABLE IF EXISTS `expression_value_generic_variable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_value_generic_variable` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_cqsa891g9cu7egmcxep74o4gh` (`ID`),
  UNIQUE KEY `UK_1qdyf7b9pdbtg7ugajlcbuy99` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_value_generic_variable`
--

LOCK TABLES `expression_value_generic_variable` WRITE;
/*!40000 ALTER TABLE `expression_value_generic_variable` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_value_generic_variable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_value_global_variable`
--

DROP TABLE IF EXISTS `expression_value_global_variable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_value_global_variable` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `globalVariable_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_gbcgljgnhew0ftkmyyjo9ef0k` (`ID`),
  UNIQUE KEY `UK_thug7lch6aqrx4iwq0dtwabrr` (`comparationId`),
  KEY `FK_nycuefmhokoiteo0n5b257gmo` (`globalVariable_ID`),
  CONSTRAINT `FK_nycuefmhokoiteo0n5b257gmo` FOREIGN KEY (`globalVariable_ID`) REFERENCES `global_variables` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_value_global_variable`
--

LOCK TABLES `expression_value_global_variable` WRITE;
/*!40000 ALTER TABLE `expression_value_global_variable` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_value_global_variable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_value_number`
--

DROP TABLE IF EXISTS `expression_value_number`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_value_number` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `value` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_4iv29s41m049yhnm25ykovg2j` (`ID`),
  UNIQUE KEY `UK_5pr6kfc8ssvjg8bcoxs7iaxo6` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_value_number`
--

LOCK TABLES `expression_value_number` WRITE;
/*!40000 ALTER TABLE `expression_value_number` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_value_number` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_value_postal_code`
--

DROP TABLE IF EXISTS `expression_value_postal_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_value_postal_code` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `text` text,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_jnl1ff1sox87eqvsa3w1nugx9` (`ID`),
  UNIQUE KEY `UK_65y7nqlko6hgjo6b3go7lwa82` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_value_postal_code`
--

LOCK TABLES `expression_value_postal_code` WRITE;
/*!40000 ALTER TABLE `expression_value_postal_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_value_postal_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_value_string`
--

DROP TABLE IF EXISTS `expression_value_string`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_value_string` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `text` text,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_ssjam0lpu4ptut6q7uiyinb46` (`ID`),
  UNIQUE KEY `UK_imteh9r9im4ctld1b9b5kkacw` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_value_string`
--

LOCK TABLES `expression_value_string` WRITE;
/*!40000 ALTER TABLE `expression_value_string` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_value_string` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_value_systemdate`
--

DROP TABLE IF EXISTS `expression_value_systemdate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_value_systemdate` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `value` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_mj1pnbvm8gv3gngq2yh15502f` (`ID`),
  UNIQUE KEY `UK_co43pbqqg7civhr3g8p752mt8` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_value_systemdate`
--

LOCK TABLES `expression_value_systemdate` WRITE;
/*!40000 ALTER TABLE `expression_value_systemdate` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_value_systemdate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_value_timestamp`
--

DROP TABLE IF EXISTS `expression_value_timestamp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_value_timestamp` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `value` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_kju72l70wu9w63min4j3y59jw` (`ID`),
  UNIQUE KEY `UK_ser5ax80v1g2co9dbdqstv81l` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_value_timestamp`
--

LOCK TABLES `expression_value_timestamp` WRITE;
/*!40000 ALTER TABLE `expression_value_timestamp` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_value_timestamp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression_value_tree_object_reference`
--

DROP TABLE IF EXISTS `expression_value_tree_object_reference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expression_value_tree_object_reference` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `reference_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_57lm65prgqk32q1w0rd3jptux` (`ID`),
  UNIQUE KEY `UK_an3mu4deq3muvltuwdawkfbhc` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression_value_tree_object_reference`
--

LOCK TABLES `expression_value_tree_object_reference` WRITE;
/*!40000 ALTER TABLE `expression_value_tree_object_reference` DISABLE KEYS */;
/*!40000 ALTER TABLE `expression_value_tree_object_reference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expressions_chain`
--

DROP TABLE IF EXISTS `expressions_chain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expressions_chain` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_l2k0vcoohq76m4cl2k8g3y9hc` (`ID`),
  UNIQUE KEY `UK_qageeu0ehecelhxmn5qdv93am` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expressions_chain`
--

LOCK TABLES `expressions_chain` WRITE;
/*!40000 ALTER TABLE `expressions_chain` DISABLE KEYS */;
/*!40000 ALTER TABLE `expressions_chain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expressions_chain_expression_basic`
--

DROP TABLE IF EXISTS `expressions_chain_expression_basic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expressions_chain_expression_basic` (
  `expressions_chain_ID` bigint(20) NOT NULL,
  `expressions_ID` bigint(20) NOT NULL,
  UNIQUE KEY `UK_6n86noaf7rpgu2qagq951m5da` (`expressions_ID`),
  KEY `FK_5u04lt24nd5qdcqdc9s5htsy8` (`expressions_chain_ID`),
  CONSTRAINT `FK_5u04lt24nd5qdcqdc9s5htsy8` FOREIGN KEY (`expressions_chain_ID`) REFERENCES `expressions_chain` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expressions_chain_expression_basic`
--

LOCK TABLES `expressions_chain_expression_basic` WRITE;
/*!40000 ALTER TABLE `expressions_chain_expression_basic` DISABLE KEYS */;
/*!40000 ALTER TABLE `expressions_chain_expression_basic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_custom_variables`
--

DROP TABLE IF EXISTS `form_custom_variables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form_custom_variables` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `defaultValue` varchar(255) DEFAULT NULL,
  `name` varchar(190) DEFAULT NULL,
  `scope` varchar(190) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `form` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_891b13251e3pkyk1k5vccpmnh` (`ID`),
  UNIQUE KEY `UK_sq1dj9kjhkv951lr0o3pu42fc` (`comparationId`),
  KEY `FK_ev3h2dj07tfxm6xw6d5v03fb` (`form`),
  CONSTRAINT `FK_ev3h2dj07tfxm6xw6d5v03fb` FOREIGN KEY (`form`) REFERENCES `tree_forms` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_custom_variables`
--

LOCK TABLES `form_custom_variables` WRITE;
/*!40000 ALTER TABLE `form_custom_variables` DISABLE KEYS */;
/*!40000 ALTER TABLE `form_custom_variables` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `global_variable_data_date`
--

DROP TABLE IF EXISTS `global_variable_data_date`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `global_variable_data_date` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `validFrom` datetime DEFAULT NULL,
  `validTo` datetime DEFAULT NULL,
  `value` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_mylp5k1001col2n4rpdshrea7` (`ID`),
  UNIQUE KEY `UK_sptr6kn7psib0u6dqt8r7stkq` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `global_variable_data_date`
--

LOCK TABLES `global_variable_data_date` WRITE;
/*!40000 ALTER TABLE `global_variable_data_date` DISABLE KEYS */;
/*!40000 ALTER TABLE `global_variable_data_date` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `global_variable_data_number`
--

DROP TABLE IF EXISTS `global_variable_data_number`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `global_variable_data_number` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `validFrom` datetime DEFAULT NULL,
  `validTo` datetime DEFAULT NULL,
  `value` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_ksal0k0qkvv2v95wvysufcap3` (`ID`),
  UNIQUE KEY `UK_jbkxahvrsxikil188orj2bion` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `global_variable_data_number`
--

LOCK TABLES `global_variable_data_number` WRITE;
/*!40000 ALTER TABLE `global_variable_data_number` DISABLE KEYS */;
/*!40000 ALTER TABLE `global_variable_data_number` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `global_variable_data_postalcode`
--

DROP TABLE IF EXISTS `global_variable_data_postalcode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `global_variable_data_postalcode` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `validFrom` datetime DEFAULT NULL,
  `validTo` datetime DEFAULT NULL,
  `postalCode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_o8e2he19y5wsa4bxopiih4ewp` (`ID`),
  UNIQUE KEY `UK_1g8ysiehft4rrd3aiheq1cktx` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `global_variable_data_postalcode`
--

LOCK TABLES `global_variable_data_postalcode` WRITE;
/*!40000 ALTER TABLE `global_variable_data_postalcode` DISABLE KEYS */;
/*!40000 ALTER TABLE `global_variable_data_postalcode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `global_variable_data_set`
--

DROP TABLE IF EXISTS `global_variable_data_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `global_variable_data_set` (
  `global_variables_ID` bigint(20) NOT NULL,
  `variableData_ID` bigint(20) NOT NULL,
  UNIQUE KEY `UK_2o1wa9axcmlhv1c8knoh3b7bw` (`variableData_ID`),
  KEY `FK_lwed03o1lmh4t449lt1lk8k3b` (`global_variables_ID`),
  CONSTRAINT `FK_lwed03o1lmh4t449lt1lk8k3b` FOREIGN KEY (`global_variables_ID`) REFERENCES `global_variables` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `global_variable_data_set`
--

LOCK TABLES `global_variable_data_set` WRITE;
/*!40000 ALTER TABLE `global_variable_data_set` DISABLE KEYS */;
/*!40000 ALTER TABLE `global_variable_data_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `global_variable_data_text`
--

DROP TABLE IF EXISTS `global_variable_data_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `global_variable_data_text` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `validFrom` datetime DEFAULT NULL,
  `validTo` datetime DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_t91aqfdhy6wnrrg749k2een9u` (`ID`),
  UNIQUE KEY `UK_mowohsw3lf7j5hh676yr34en8` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `global_variable_data_text`
--

LOCK TABLES `global_variable_data_text` WRITE;
/*!40000 ALTER TABLE `global_variable_data_text` DISABLE KEYS */;
/*!40000 ALTER TABLE `global_variable_data_text` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `global_variables`
--

DROP TABLE IF EXISTS `global_variables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `global_variables` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `format` int(11) DEFAULT NULL,
  `name` varchar(190) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_nggea8kl5cb894e5du3cwsrd0` (`ID`),
  UNIQUE KEY `UK_kr7p6k3u1po5mbamq95rvh6gj` (`comparationId`),
  UNIQUE KEY `UK_ba2w3ms6v9agn6ac5ois703u2` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `global_variables`
--

LOCK TABLES `global_variables` WRITE;
/*!40000 ALTER TABLE `global_variables` DISABLE KEYS */;
/*!40000 ALTER TABLE `global_variables` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (18);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule`
--

DROP TABLE IF EXISTS `rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rule` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `actions_ID` bigint(20) DEFAULT NULL,
  `conditions_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_8rqluiaunf9galin639sd894c` (`ID`),
  UNIQUE KEY `UK_bmfhvbjf4kaugtlg6wom9crg9` (`comparationId`),
  KEY `FK_3327iu3vsaiyi6xxv47du4nte` (`actions_ID`),
  KEY `FK_d3x0ml61bkukmrngffyh2coko` (`conditions_ID`),
  CONSTRAINT `FK_3327iu3vsaiyi6xxv47du4nte` FOREIGN KEY (`actions_ID`) REFERENCES `expressions_chain` (`ID`),
  CONSTRAINT `FK_d3x0ml61bkukmrngffyh2coko` FOREIGN KEY (`conditions_ID`) REFERENCES `expressions_chain` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule`
--

LOCK TABLES `rule` WRITE;
/*!40000 ALTER TABLE `rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_decision_table`
--

DROP TABLE IF EXISTS `rule_decision_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rule_decision_table` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_bumgbl5omimxtvvhvthstr882` (`ID`),
  UNIQUE KEY `UK_20los6ndm6d9errmf4erx9f12` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_decision_table`
--

LOCK TABLES `rule_decision_table` WRITE;
/*!40000 ALTER TABLE `rule_decision_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `rule_decision_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_decision_table_row`
--

DROP TABLE IF EXISTS `rule_decision_table_row`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rule_decision_table_row` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `action_ID` bigint(20) DEFAULT NULL,
  `conditions_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_ondmt5ex56yathpd70q11lwgg` (`ID`),
  UNIQUE KEY `UK_r7njhoe2s46aht07exgq5hp5f` (`comparationId`),
  KEY `FK_mqhpiw889x6dvrni1kbdr6n8u` (`action_ID`),
  KEY `FK_85ewg5m9454k3316wo7ypnxq3` (`conditions_ID`),
  CONSTRAINT `FK_85ewg5m9454k3316wo7ypnxq3` FOREIGN KEY (`conditions_ID`) REFERENCES `expressions_chain` (`ID`),
  CONSTRAINT `FK_mqhpiw889x6dvrni1kbdr6n8u` FOREIGN KEY (`action_ID`) REFERENCES `expressions_chain` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_decision_table_row`
--

LOCK TABLES `rule_decision_table_row` WRITE;
/*!40000 ALTER TABLE `rule_decision_table_row` DISABLE KEYS */;
/*!40000 ALTER TABLE `rule_decision_table_row` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_decision_table_rule_decision_table_row`
--

DROP TABLE IF EXISTS `rule_decision_table_rule_decision_table_row`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rule_decision_table_rule_decision_table_row` (
  `rule_decision_table_ID` bigint(20) NOT NULL,
  `rules_ID` bigint(20) NOT NULL,
  UNIQUE KEY `UK_9357dg3c4e5y9gkaggcdxrfnb` (`rules_ID`),
  KEY `FK_dk0yu4ajw581l5dpebmouwuce` (`rule_decision_table_ID`),
  CONSTRAINT `FK_9357dg3c4e5y9gkaggcdxrfnb` FOREIGN KEY (`rules_ID`) REFERENCES `rule_decision_table_row` (`ID`),
  CONSTRAINT `FK_dk0yu4ajw581l5dpebmouwuce` FOREIGN KEY (`rule_decision_table_ID`) REFERENCES `rule_decision_table` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_decision_table_rule_decision_table_row`
--

LOCK TABLES `rule_decision_table_rule_decision_table_row` WRITE;
/*!40000 ALTER TABLE `rule_decision_table_rule_decision_table_row` DISABLE KEYS */;
/*!40000 ALTER TABLE `rule_decision_table_rule_decision_table_row` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_answer_input_date`
--

DROP TABLE IF EXISTS `test_answer_input_date`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_answer_input_date` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `dateValue` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_cctx6br59kggymqx6ltv65iv9` (`ID`),
  UNIQUE KEY `UK_8r0jv8nwetnhcx3mwixg6uhn6` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_answer_input_date`
--

LOCK TABLES `test_answer_input_date` WRITE;
/*!40000 ALTER TABLE `test_answer_input_date` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_answer_input_date` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_answer_input_number`
--

DROP TABLE IF EXISTS `test_answer_input_number`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_answer_input_number` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `inputValue` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_rrf27v5jka8vvm7pvp3846qac` (`ID`),
  UNIQUE KEY `UK_aljs5lkmjtgb0ys9oxac2jo5m` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_answer_input_number`
--

LOCK TABLES `test_answer_input_number` WRITE;
/*!40000 ALTER TABLE `test_answer_input_number` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_answer_input_number` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_answer_input_postalcode`
--

DROP TABLE IF EXISTS `test_answer_input_postalcode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_answer_input_postalcode` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `inputValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_9n169jx3xa3jv40g097bb6k4g` (`ID`),
  UNIQUE KEY `UK_gmbj48islivqbak8uhqn4acra` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_answer_input_postalcode`
--

LOCK TABLES `test_answer_input_postalcode` WRITE;
/*!40000 ALTER TABLE `test_answer_input_postalcode` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_answer_input_postalcode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_answer_input_text`
--

DROP TABLE IF EXISTS `test_answer_input_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_answer_input_text` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `inputValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_hdccu2j9fjpphcxxt6wsuqo1v` (`ID`),
  UNIQUE KEY `UK_fm38doewevroqbh9w9yl6uy45` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_answer_input_text`
--

LOCK TABLES `test_answer_input_text` WRITE;
/*!40000 ALTER TABLE `test_answer_input_text` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_answer_input_text` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_answer_multi_checkbox`
--

DROP TABLE IF EXISTS `test_answer_multi_checkbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_answer_multi_checkbox` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_u2axqvpcnrfbj8tflcs5v8qu` (`ID`),
  UNIQUE KEY `UK_neljyd1uorqx4rh8nwh89pofm` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_answer_multi_checkbox`
--

LOCK TABLES `test_answer_multi_checkbox` WRITE;
/*!40000 ALTER TABLE `test_answer_multi_checkbox` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_answer_multi_checkbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_answer_radio_button`
--

DROP TABLE IF EXISTS `test_answer_radio_button`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_answer_radio_button` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `radioButtonValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_eakcce3ybd0073kdajy217yju` (`ID`),
  UNIQUE KEY `UK_rrv3rw7jasepphc1943fqgis5` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_answer_radio_button`
--

LOCK TABLES `test_answer_radio_button` WRITE;
/*!40000 ALTER TABLE `test_answer_radio_button` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_answer_radio_button` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_scenario`
--

DROP TABLE IF EXISTS `test_scenario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_scenario` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `formId` bigint(20) NOT NULL,
  `formLabel` varchar(1000) NOT NULL,
  `formOrganization` double NOT NULL,
  `name` varchar(190) DEFAULT NULL,
  `testScenarioForm_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_j17qvfqb5wcp4c3bgknvdii31` (`ID`),
  UNIQUE KEY `UK_eh6es7t34ldoxns3sswdj6vku` (`comparationId`),
  UNIQUE KEY `UK_49e2vwe0pem0fb31dxr3ed6b0` (`name`,`formId`),
  KEY `FK_thnxvj85s3nk6u7k6f91dfbr6` (`testScenarioForm_ID`),
  CONSTRAINT `FK_thnxvj85s3nk6u7k6f91dfbr6` FOREIGN KEY (`testScenarioForm_ID`) REFERENCES `test_scenario_form` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_scenario`
--

LOCK TABLES `test_scenario` WRITE;
/*!40000 ALTER TABLE `test_scenario` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_scenario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_scenario_category`
--

DROP TABLE IF EXISTS `test_scenario_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_scenario_category` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `label` varchar(1000) DEFAULT NULL,
  `name` varchar(190) DEFAULT NULL,
  `originalReference` varchar(190) NOT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_sxenkuftpjjmjl0x3ivk62wix` (`ID`),
  UNIQUE KEY `UK_1ln5mm1bq3hr60pot8sy0n1aq` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_scenario_category`
--

LOCK TABLES `test_scenario_category` WRITE;
/*!40000 ALTER TABLE `test_scenario_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_scenario_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_scenario_form`
--

DROP TABLE IF EXISTS `test_scenario_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_scenario_form` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `label` varchar(1000) DEFAULT NULL,
  `name` varchar(190) DEFAULT NULL,
  `originalReference` varchar(190) NOT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `organizationId` double NOT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_9r8dmwthkqgo9m88ojlm64v2q` (`ID`),
  UNIQUE KEY `UK_evdf8bumu8vh0fbr289dn3q2d` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_scenario_form`
--

LOCK TABLES `test_scenario_form` WRITE;
/*!40000 ALTER TABLE `test_scenario_form` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_scenario_form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_scenario_group`
--

DROP TABLE IF EXISTS `test_scenario_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_scenario_group` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `label` varchar(1000) DEFAULT NULL,
  `name` varchar(190) DEFAULT NULL,
  `originalReference` varchar(190) NOT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `repeatable` bit(1) NOT NULL,
  `addEnabled` bit(1) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_rrp9g1ti6yd73kgp0mf1gtyol` (`ID`),
  UNIQUE KEY `UK_hoi1e3fa5bm2kj18od8m9r6x6` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_scenario_group`
--

LOCK TABLES `test_scenario_group` WRITE;
/*!40000 ALTER TABLE `test_scenario_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_scenario_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_scenario_question`
--

DROP TABLE IF EXISTS `test_scenario_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_scenario_question` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `label` varchar(1000) DEFAULT NULL,
  `name` varchar(190) DEFAULT NULL,
  `originalReference` varchar(190) NOT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `testAnswer_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_fdol3obofu4t5i6nh6flvwado` (`ID`),
  UNIQUE KEY `UK_rbe8hkt0j1069ml9qxt8324hw` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_scenario_question`
--

LOCK TABLES `test_scenario_question` WRITE;
/*!40000 ALTER TABLE `test_scenario_question` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_scenario_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tree_answers`
--

DROP TABLE IF EXISTS `tree_answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tree_answers` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `label` varchar(1000) DEFAULT NULL,
  `name` varchar(190) DEFAULT NULL,
  `originalReference` varchar(190) NOT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_413vxa542h86uqy4uvcnv6y2x` (`ID`),
  UNIQUE KEY `UK_5xuj3de6ide6evpo4sijpqa4o` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tree_answers`
--

LOCK TABLES `tree_answers` WRITE;
/*!40000 ALTER TABLE `tree_answers` DISABLE KEYS */;
INSERT INTO `tree_answers` VALUES (5,'808207c4-4995-45bd-ad06-68f49e4ce8f5',15743,'2015-02-18 11:08:41','2015-02-18 11:09:07',15743,'','answer1','808207c4-4995-45bd-ad06-68f49e4ce8f5',0,4),(6,'f29d6c00-71e6-41d4-b31e-875850ad6d80',15743,'2015-02-18 11:08:42','2015-02-18 11:09:02',15743,'','answer11','f29d6c00-71e6-41d4-b31e-875850ad6d80',0,5),(7,'6c88ec71-173f-49b6-9626-608b9682d45b',15743,'2015-02-18 11:08:42','2015-02-18 11:09:07',15743,'','answer12','6c88ec71-173f-49b6-9626-608b9682d45b',1,5),(8,'6ab13f4f-86ec-4a97-92a5-7cc0fb82940a',15743,'2015-02-18 11:08:42','2015-02-18 11:09:25',15743,'','answer2','6ab13f4f-86ec-4a97-92a5-7cc0fb82940a',1,4),(9,'de7eccf9-426e-4f77-acec-fac8604c3c4e',15743,'2015-02-18 11:08:43','2015-02-18 11:09:15',15743,'','answer21','de7eccf9-426e-4f77-acec-fac8604c3c4e',0,8),(10,'5a817cb0-274b-4308-8d1d-6f2c6b59bcf3',15743,'2015-02-18 11:08:43','2015-02-18 11:09:19',15743,'','answer22','5a817cb0-274b-4308-8d1d-6f2c6b59bcf3',1,8);
/*!40000 ALTER TABLE `tree_answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tree_categories`
--

DROP TABLE IF EXISTS `tree_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tree_categories` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `label` varchar(1000) DEFAULT NULL,
  `name` varchar(190) DEFAULT NULL,
  `originalReference` varchar(190) NOT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_ec3bvy7lletc6jmyvyfwuroqv` (`ID`),
  UNIQUE KEY `UK_gtcyh8mle277igwtb5dvhjkr1` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tree_categories`
--

LOCK TABLES `tree_categories` WRITE;
/*!40000 ALTER TABLE `tree_categories` DISABLE KEYS */;
INSERT INTO `tree_categories` VALUES (2,'84ea9e0f-fc97-4556-aae3-2669d90bf9be',15743,'2015-02-18 11:08:35','2015-02-18 11:10:01',15743,'','Category1','84ea9e0f-fc97-4556-aae3-2669d90bf9be',0,1),(14,'07da1ec1-a22f-4228-9e6a-af6fc70b1854',15743,'2015-02-18 11:09:47','2015-02-18 11:10:19',15743,'','Category2','07da1ec1-a22f-4228-9e6a-af6fc70b1854',1,1);
/*!40000 ALTER TABLE `tree_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `tree_elements_v`
--

DROP TABLE IF EXISTS `tree_elements_v`;
/*!50001 DROP VIEW IF EXISTS `tree_elements_v`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `tree_elements_v` (
  `form_ID` tinyint NOT NULL,
  `form_name` tinyint NOT NULL,
  `form_label` tinyint NOT NULL,
  `form_version` tinyint NOT NULL,
  `form_sortSeq` tinyint NOT NULL,
  `category_ID` tinyint NOT NULL,
  `category_name` tinyint NOT NULL,
  `category_sortSeq` tinyint NOT NULL,
  `group_ID` tinyint NOT NULL,
  `question_ID` tinyint NOT NULL,
  `question_name` tinyint NOT NULL,
  `question_sortSeq` tinyint NOT NULL,
  `question_answerType` tinyint NOT NULL,
  `question_answerFormat` tinyint NOT NULL,
  `answer_ID` tinyint NOT NULL,
  `answer_name` tinyint NOT NULL,
  `answer_sortSeq` tinyint NOT NULL,
  `subanswer_ID` tinyint NOT NULL,
  `subanswer_name` tinyint NOT NULL,
  `subanswer_sortSeq` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tree_forms`
--

DROP TABLE IF EXISTS `tree_forms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tree_forms` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `label` varchar(190) DEFAULT NULL,
  `name` varchar(190) DEFAULT NULL,
  `originalReference` varchar(190) NOT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `organizationId` double NOT NULL,
  `version` int(11) DEFAULT NULL,
  `availableFrom` datetime NOT NULL,
  `availableTo` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_plkq2e2pj19uak2ncrgf1ft6v` (`ID`),
  UNIQUE KEY `UK_k9mhkly9g8lqwf1m9esm50y6m` (`comparationId`),
  UNIQUE KEY `UK_iwgivd7sy9sfbjyj0hlyccrxt` (`label`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tree_forms`
--

LOCK TABLES `tree_forms` WRITE;
/*!40000 ALTER TABLE `tree_forms` DISABLE KEYS */;
INSERT INTO `tree_forms` VALUES (1,'6f8d9cf0-a618-437c-8fe7-ac06bb19c8ed',15743,'2015-02-18 11:08:31','2015-02-18 11:10:19',15743,'FormTest','','6f8d9cf0-a618-437c-8fe7-ac06bb19c8ed',0,NULL,15707,1,'2015-02-18 00:00:00',NULL,'DESIGN');
/*!40000 ALTER TABLE `tree_forms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tree_forms_diagram`
--

DROP TABLE IF EXISTS `tree_forms_diagram`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tree_forms_diagram` (
  `tree_forms_ID` bigint(20) NOT NULL,
  `diagrams_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`tree_forms_ID`,`diagrams_ID`),
  UNIQUE KEY `UK_otbxhecixo9rbriamr8v44nik` (`diagrams_ID`),
  CONSTRAINT `FK_h95kubmo9q1w4k54fxnk74kyb` FOREIGN KEY (`tree_forms_ID`) REFERENCES `tree_forms` (`ID`),
  CONSTRAINT `FK_otbxhecixo9rbriamr8v44nik` FOREIGN KEY (`diagrams_ID`) REFERENCES `diagram` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tree_forms_diagram`
--

LOCK TABLES `tree_forms_diagram` WRITE;
/*!40000 ALTER TABLE `tree_forms_diagram` DISABLE KEYS */;
/*!40000 ALTER TABLE `tree_forms_diagram` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tree_forms_expressions_chain`
--

DROP TABLE IF EXISTS `tree_forms_expressions_chain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tree_forms_expressions_chain` (
  `tree_forms_ID` bigint(20) NOT NULL,
  `expressionChains_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`tree_forms_ID`,`expressionChains_ID`),
  UNIQUE KEY `UK_melu1cfpayuydi8fv6gxnoufq` (`expressionChains_ID`),
  CONSTRAINT `FK_5wekloqjwnu88bfqpn292su2w` FOREIGN KEY (`tree_forms_ID`) REFERENCES `tree_forms` (`ID`),
  CONSTRAINT `FK_melu1cfpayuydi8fv6gxnoufq` FOREIGN KEY (`expressionChains_ID`) REFERENCES `expressions_chain` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tree_forms_expressions_chain`
--

LOCK TABLES `tree_forms_expressions_chain` WRITE;
/*!40000 ALTER TABLE `tree_forms_expressions_chain` DISABLE KEYS */;
/*!40000 ALTER TABLE `tree_forms_expressions_chain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tree_forms_rule`
--

DROP TABLE IF EXISTS `tree_forms_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tree_forms_rule` (
  `tree_forms_ID` bigint(20) NOT NULL,
  `rules_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`tree_forms_ID`,`rules_ID`),
  UNIQUE KEY `UK_t8v6e3oyk0k56toxk97afkpvc` (`rules_ID`),
  CONSTRAINT `FK_o7o97qsjclwv4fu4n6dajk1on` FOREIGN KEY (`tree_forms_ID`) REFERENCES `tree_forms` (`ID`),
  CONSTRAINT `FK_t8v6e3oyk0k56toxk97afkpvc` FOREIGN KEY (`rules_ID`) REFERENCES `rule` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tree_forms_rule`
--

LOCK TABLES `tree_forms_rule` WRITE;
/*!40000 ALTER TABLE `tree_forms_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `tree_forms_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tree_forms_rule_decision_table`
--

DROP TABLE IF EXISTS `tree_forms_rule_decision_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tree_forms_rule_decision_table` (
  `tree_forms_ID` bigint(20) NOT NULL,
  `tableRules_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`tree_forms_ID`,`tableRules_ID`),
  UNIQUE KEY `UK_b274bmp72bu1n40rl4k5kvhas` (`tableRules_ID`),
  CONSTRAINT `FK_b274bmp72bu1n40rl4k5kvhas` FOREIGN KEY (`tableRules_ID`) REFERENCES `rule_decision_table` (`ID`),
  CONSTRAINT `FK_mb6o16g0xsjlo3nx5xoc0i0g9` FOREIGN KEY (`tree_forms_ID`) REFERENCES `tree_forms` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tree_forms_rule_decision_table`
--

LOCK TABLES `tree_forms_rule_decision_table` WRITE;
/*!40000 ALTER TABLE `tree_forms_rule_decision_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `tree_forms_rule_decision_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tree_groups`
--

DROP TABLE IF EXISTS `tree_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tree_groups` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `label` varchar(1000) DEFAULT NULL,
  `name` varchar(190) DEFAULT NULL,
  `originalReference` varchar(190) NOT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `repeatable` bit(1) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_sfdvxxi1k3p9pqsjl5nhmgdp` (`ID`),
  UNIQUE KEY `UK_sno2xl7o9nxmt3xh48ywus36u` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tree_groups`
--

LOCK TABLES `tree_groups` WRITE;
/*!40000 ALTER TABLE `tree_groups` DISABLE KEYS */;
INSERT INTO `tree_groups` VALUES (3,'dfcf32fb-868d-4952-a7b9-0adc82e48690',15743,'2015-02-18 11:08:36','2015-02-18 11:10:01',15743,'','group1','dfcf32fb-868d-4952-a7b9-0adc82e48690',0,2,'\0'),(11,'15e4a070-233a-4823-b41f-207f7eeb1aba',15743,'2015-02-18 11:09:25','2015-02-18 11:10:01',15743,'','group2','15e4a070-233a-4823-b41f-207f7eeb1aba',1,3,'');
/*!40000 ALTER TABLE `tree_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `tree_groups_v`
--

DROP TABLE IF EXISTS `tree_groups_v`;
/*!50001 DROP VIEW IF EXISTS `tree_groups_v`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `tree_groups_v` (
  `group_ID` tinyint NOT NULL,
  `group1_parent_ID` tinyint NOT NULL,
  `group1_ID` tinyint NOT NULL,
  `group1_name` tinyint NOT NULL,
  `group1_sortSeq` tinyint NOT NULL,
  `group2_ID` tinyint NOT NULL,
  `group2_name` tinyint NOT NULL,
  `group2_sortSeq` tinyint NOT NULL,
  `group3_ID` tinyint NOT NULL,
  `group3_name` tinyint NOT NULL,
  `group3_sortSeq` tinyint NOT NULL,
  `group4_ID` tinyint NOT NULL,
  `group4_name` tinyint NOT NULL,
  `group4_sortSeq` tinyint NOT NULL,
  `group5_ID` tinyint NOT NULL,
  `group5_name` tinyint NOT NULL,
  `group5_sortSeq` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tree_questions`
--

DROP TABLE IF EXISTS `tree_questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tree_questions` (
  `ID` bigint(20) NOT NULL,
  `comparationId` varchar(190) NOT NULL,
  `createdBy` double DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatedBy` double DEFAULT NULL,
  `label` varchar(1000) DEFAULT NULL,
  `name` varchar(190) DEFAULT NULL,
  `originalReference` varchar(190) NOT NULL,
  `sortSeq` bigint(20) NOT NULL,
  `parent_ID` bigint(20) DEFAULT NULL,
  `answerFormat` varchar(255) DEFAULT NULL,
  `answerType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_9lkt55st6up2vyh38lrmu0dc5` (`ID`),
  UNIQUE KEY `UK_nu1epukynjltak450rhyp6eu0` (`comparationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tree_questions`
--

LOCK TABLES `tree_questions` WRITE;
/*!40000 ALTER TABLE `tree_questions` DISABLE KEYS */;
INSERT INTO `tree_questions` VALUES (4,'9641c566-05a5-42a7-95e6-08d91db01863',15743,'2015-02-18 11:08:36','2015-02-18 11:09:25',15743,'','question1','9641c566-05a5-42a7-95e6-08d91db01863',0,3,'TEXT','RADIO'),(12,'cb6838f7-08c4-4f36-80ac-785f5e473538',15743,'2015-02-18 11:09:26','2015-02-18 11:09:57',15743,'','question1','cb6838f7-08c4-4f36-80ac-785f5e473538',0,11,'TEXT','INPUT'),(13,'73aba529-f3c1-4370-8d48-63c669794f81',15743,'2015-02-18 11:09:27','2015-02-18 11:10:01',15743,'','question3','73aba529-f3c1-4370-8d48-63c669794f81',1,11,'DATE','INPUT'),(15,'749b6a34-e4b5-438d-bfd4-91237d09a6d4',15743,'2015-02-18 11:09:49','2015-02-18 11:10:05',15743,'','question4','749b6a34-e4b5-438d-bfd4-91237d09a6d4',0,14,'TEXT','INPUT'),(16,'5b1dc2aa-32b1-4e7b-af72-0156bf6f7507',15743,'2015-02-18 11:09:49','2015-02-18 11:10:09',15743,'','question5','5b1dc2aa-32b1-4e7b-af72-0156bf6f7507',1,14,'TEXT','INPUT'),(17,'f00e2291-f6f8-4deb-bf60-91589ed094a4',15743,'2015-02-18 11:09:51','2015-02-18 11:10:19',15743,'','question6','f00e2291-f6f8-4deb-bf60-91589ed094a4',2,14,'TEXT','INPUT');
/*!40000 ALTER TABLE `tree_questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `tree_elements_v`
--

/*!50001 DROP TABLE IF EXISTS `tree_elements_v`*/;
/*!50001 DROP VIEW IF EXISTS `tree_elements_v`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `tree_elements_v` AS select `f`.`ID` AS `form_ID`,`f`.`name` AS `form_name`,`f`.`label` AS `form_label`,`f`.`version` AS `form_version`,`f`.`sortSeq` AS `form_sortSeq`,`c`.`ID` AS `category_ID`,`c`.`name` AS `category_name`,`c`.`sortSeq` AS `category_sortSeq`,`grps`.`group_ID` AS `group_ID`,`q`.`ID` AS `question_ID`,`q`.`name` AS `question_name`,`q`.`sortSeq` AS `question_sortSeq`,`q`.`answerType` AS `question_answerType`,`q`.`answerFormat` AS `question_answerFormat`,`a`.`ID` AS `answer_ID`,`a`.`name` AS `answer_name`,`a`.`sortSeq` AS `answer_sortSeq`,`sa`.`ID` AS `subanswer_ID`,`sa`.`name` AS `subanswer_name`,`sa`.`sortSeq` AS `subanswer_sortSeq` from ((`tree_forms` `f` join (`tree_categories` `c` left join `tree_groups_v` `grps` on((`c`.`ID` = `grps`.`group1_parent_ID`)))) join ((`tree_questions` `q` left join `tree_answers` `a` on((`q`.`ID` = `a`.`parent_ID`))) left join `tree_answers` `sa` on((`a`.`ID` = `sa`.`parent_ID`)))) where (((`c`.`ID` = `q`.`parent_ID`) or (`grps`.`group1_ID` = `q`.`parent_ID`) or (`grps`.`group2_ID` = `q`.`parent_ID`) or (`grps`.`group3_ID` = `q`.`parent_ID`) or (`grps`.`group4_ID` = `q`.`parent_ID`) or (`grps`.`group5_ID` = `q`.`parent_ID`)) and (`f`.`ID` = `c`.`parent_ID`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `tree_groups_v`
--

/*!50001 DROP TABLE IF EXISTS `tree_groups_v`*/;
/*!50001 DROP VIEW IF EXISTS `tree_groups_v`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `tree_groups_v` AS select ifnull(`g5`.`ID`,ifnull(`g4`.`ID`,ifnull(`g3`.`ID`,ifnull(`g2`.`ID`,`g1`.`ID`)))) AS `group_ID`,`g1`.`parent_ID` AS `group1_parent_ID`,`g1`.`ID` AS `group1_ID`,`g1`.`name` AS `group1_name`,`g1`.`sortSeq` AS `group1_sortSeq`,`g2`.`ID` AS `group2_ID`,`g2`.`name` AS `group2_name`,`g2`.`sortSeq` AS `group2_sortSeq`,`g3`.`ID` AS `group3_ID`,`g3`.`name` AS `group3_name`,`g3`.`sortSeq` AS `group3_sortSeq`,`g4`.`ID` AS `group4_ID`,`g4`.`name` AS `group4_name`,`g4`.`sortSeq` AS `group4_sortSeq`,`g5`.`ID` AS `group5_ID`,`g5`.`name` AS `group5_name`,`g5`.`sortSeq` AS `group5_sortSeq` from (`tree_categories` `c1` join ((((`tree_groups` `g1` left join `tree_groups` `g2` on((`g1`.`ID` = `g2`.`parent_ID`))) left join `tree_groups` `g3` on((`g2`.`ID` = `g3`.`parent_ID`))) left join `tree_groups` `g4` on((`g3`.`ID` = `g4`.`parent_ID`))) left join `tree_groups` `g5` on((`g4`.`ID` = `g5`.`parent_ID`)))) where (`c1`.`ID` = `g1`.`parent_ID`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-02-18 11:11:50
