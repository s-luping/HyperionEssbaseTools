-- MYSQL
-- DEV3_FDMEE.TO_ESSBASE_TEMP definition
CREATE TABLE `to_essbase_temp` (
  `YEARS` varchar(30) DEFAULT NULL,
  `PERIODS` varchar(30) DEFAULT NULL,
  `SCENARIO` varchar(80) DEFAULT NULL,
  `VERSION` varchar(80) DEFAULT NULL,
  `ENTITY` varchar(80) DEFAULT NULL,
  `PROJECT` varchar(80) DEFAULT NULL,
  `ACCOUNT` varchar(80) DEFAULT NULL,
  `CURRENCY` varchar(80) DEFAULT NULL,
  `BUDGETCENTER` varchar(80) DEFAULT NULL,
  `PHASE` varchar(80) DEFAULT NULL,
  `MISC` varchar(80) DEFAULT NULL,
  `SPARE1` varchar(80) DEFAULT NULL,
  `SPARE2` varchar(80) DEFAULT NULL,
  `SPARE3` varchar(80) DEFAULT NULL,
  `DATA` float DEFAULT NULL,
  `BATCH_NAME` varchar(80) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ODS_HP.export_temp definition

CREATE TABLE `export_temp` (
  `YEARS` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PERIODS` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SCENARIO` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `VERSION` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ENTITY` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PROJECT` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ACCOUNT` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CURRENCY` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BUDGETCENTER` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PHASE` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MISC` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SPARE1` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SPARE2` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SPARE3` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DATA` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

