CREATE TABLE `USER_CONTACT` (
  `CONTACT_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `VALUE` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  `TYPE` VARCHAR(255) NOT NULL,
  `ICON` VARCHAR(255) NOT NULL,
  `USER_ID` BIGINT(20) NOT NULL,
  `LIST_INDEX` int(11) DEFAULT NULL,
  PRIMARY KEY (`CONTACT_ID`),
  KEY `FK_USER` (`USER_ID`),
  CONSTRAINT `FK_USER_REF` FOREIGN KEY (`USER_ID`) REFERENCES `USERS` (`ID`)
) ENGINE=INNODB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;