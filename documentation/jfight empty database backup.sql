-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 30, 2018 at 10:55 PM
-- Server version: 10.1.33-MariaDB
-- PHP Version: 7.2.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jfight`
--
CREATE DATABASE IF NOT EXISTS `jfight` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `jfight`;

-- --------------------------------------------------------

--
-- Table structure for table `character`
--

DROP TABLE IF EXISTS `character`;
CREATE TABLE IF NOT EXISTS `character` (
  `UserId` int(11) NOT NULL,
  `ImagePath` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `HealthPoints` int(11) NOT NULL,
  `Strength` int(11) NOT NULL,
  `Experience` int(11) NOT NULL,
  `Level` int(11) NOT NULL,
  UNIQUE KEY `UserId_2` (`UserId`),
  KEY `UserId` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- RELATIONSHIPS FOR TABLE `character`:
--   `UserId`
--       `user` -> `UserId`
--

-- --------------------------------------------------------

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
CREATE TABLE IF NOT EXISTS `log` (
  `FightId` int(11) NOT NULL,
  `Date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Event created. Logs older than one month are deleting automatically.',
  `User1Id` int(11) DEFAULT NULL,
  `User2Id` int(11) DEFAULT NULL,
  `Log` varchar(1027) COLLATE utf8_unicode_ci NOT NULL,
  UNIQUE KEY `FightId_2` (`FightId`),
  KEY `FightId` (`FightId`),
  KEY `User2Id` (`User2Id`),
  KEY `User1Id` (`User1Id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- RELATIONSHIPS FOR TABLE `log`:
--   `User1Id`
--       `user` -> `UserId`
--   `User2Id`
--       `user` -> `UserId`
--   `FightId`
--       `result` -> `FightId`
--

-- --------------------------------------------------------

--
-- Table structure for table `result`
--

DROP TABLE IF EXISTS `result`;
CREATE TABLE IF NOT EXISTS `result` (
  `FightId` int(11) NOT NULL AUTO_INCREMENT,
  `WinUserId` int(11) DEFAULT NULL COMMENT 'Event created. Once a month rows with all empty UserIds are deleting automatically.',
  `LossUserId` int(11) DEFAULT NULL,
  `TieUser1Id` int(11) DEFAULT NULL,
  `TieUser2Id` int(11) DEFAULT NULL,
  PRIMARY KEY (`FightId`),
  KEY `Win` (`WinUserId`),
  KEY `Loss` (`LossUserId`),
  KEY `TieUser2` (`TieUser2Id`),
  KEY `TieUser1` (`TieUser1Id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- RELATIONSHIPS FOR TABLE `result`:
--   `TieUser1Id`
--       `user` -> `UserId`
--   `TieUser2Id`
--       `user` -> `UserId`
--   `LossUserId`
--       `user` -> `UserId`
--   `WinUserId`
--       `user` -> `UserId`
--

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `UserId` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `eMail` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `AccessLevel` int(11) NOT NULL DEFAULT '1' COMMENT 'Trigger created. Null input value is automatically changing to default 1.',
  PRIMARY KEY (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- RELATIONSHIPS FOR TABLE `user`:
--

--
-- Triggers `user`
--
DROP TRIGGER IF EXISTS `NullTo1`;
DELIMITER $$
CREATE TRIGGER `NullTo1` BEFORE INSERT ON `user` FOR EACH ROW SET NEW.AccessLevel = IFNULL(NEW.AccessLevel, 1)
$$
DELIMITER ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `character`
--
ALTER TABLE `character`
  ADD CONSTRAINT `character_user_fk` FOREIGN KEY (`UserId`) REFERENCES `user` (`UserId`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `log`
--
ALTER TABLE `log`
  ADD CONSTRAINT `logUser1_user_fk` FOREIGN KEY (`User1Id`) REFERENCES `user` (`UserId`) ON DELETE SET NULL ON UPDATE NO ACTION,
  ADD CONSTRAINT `logUser2_user_fk` FOREIGN KEY (`User2Id`) REFERENCES `user` (`UserId`) ON DELETE SET NULL ON UPDATE NO ACTION,
  ADD CONSTRAINT `log_result_fk` FOREIGN KEY (`FightId`) REFERENCES `result` (`FightId`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `result`
--
ALTER TABLE `result`
  ADD CONSTRAINT `ResultTieUser1_user_fk` FOREIGN KEY (`TieUser1Id`) REFERENCES `user` (`UserId`) ON DELETE SET NULL ON UPDATE NO ACTION,
  ADD CONSTRAINT `ResultTieUser2_user_fk` FOREIGN KEY (`TieUser2Id`) REFERENCES `user` (`UserId`) ON DELETE SET NULL ON UPDATE NO ACTION,
  ADD CONSTRAINT `resultLoss_user_fk` FOREIGN KEY (`LossUserId`) REFERENCES `user` (`UserId`) ON DELETE SET NULL ON UPDATE NO ACTION,
  ADD CONSTRAINT `resultWin_user_fk` FOREIGN KEY (`WinUserId`) REFERENCES `user` (`UserId`) ON DELETE SET NULL ON UPDATE NO ACTION;

DELIMITER $$
--
-- Events
--
DROP EVENT `Delete logs older than 1 month`$$
CREATE DEFINER=`Kolia`@`%` EVENT `Delete logs older than 1 month` ON SCHEDULE EVERY 1 DAY STARTS '2018-06-29 03:00:00' ON COMPLETION PRESERVE ENABLE DO DELETE FROM log WHERE Date < (NOW() - INTERVAL 1 MONTH)$$

DROP EVENT `Delete results where both users checked out`$$
CREATE DEFINER=`root`@`localhost` EVENT `Delete results where both users checked out` ON SCHEDULE EVERY 1 MONTH STARTS '2018-07-01 04:00:00' ON COMPLETION PRESERVE ENABLE DO DELETE FROM result WHERE result.Win = NULL AND result.Loss = NULL AND result.TieUser1 = NULL AND result.TieUser2 = NULL$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
