-- DEV3_FDMEE.TO_ESSBASE_TEMP definition

CREATE TABLE "src"."srctable" 
   (	"BATCH_NAME" VARCHAR2(200) NOT NULL ENABLE, 
	"DATAVIEW" VARCHAR2(8) NOT NULL ENABLE, 
	"YEARs" VARCHAR2(30), 
	"YEAR_NUM" NUMBER, 
	"PERIODs" VARCHAR2(30), 
	"PERIOD_NUM" NUMBER, 
	"SCENARIO" VARCHAR2(80), 
	"VERSION" VARCHAR2(80), 
	"ENTITY" VARCHAR2(80), 
	"PROJECT" VARCHAR2(80), 
	"ACCOUNT" VARCHAR2(80), 
	"CURRENCY" VARCHAR2(80), 
	"BUDGETCENTER" VARCHAR2(80), 
	"PHASE" VARCHAR2(80), 
	"MISC" VARCHAR2(80), 
	"SPARE1" VARCHAR2(80), 
	"SPARE2" VARCHAR2(80), 
	"SPARE3" VARCHAR2(80), 
	"COL1" VARCHAR2(80), 
	"COL11" VARCHAR2(80), 
	"COL111" VARCHAR2(80), 
	"DATA" NUMBER(38,6) NOT NULL ENABLE, 
	"AMOUNT_YTD" NUMBER, 
	"AMOUNT_PTD" NUMBER
   ) ;


  CREATE OR REPLACE FORCE EDITIONABLE VIEW "src"."src_sum_view" ("BATCH_NAME", "DATAVIEW", "YEARs", "YEAR_NUM", "PERIODs", "PERIOD_NUM", "SCENARIO", "VERSION", "ENTITY", "PROJECT", "ACCOUNT", "CURRENCY", "BUDGETCENTER", "PHASE", "MISC", "SPARE1", "SPARE2", "SPARE3", "DATA") AS 
  SELECT BATCH_NAME, 
DATAVIEW, 
"YEARs", YEAR_NUM, PERIODs, PERIOD_NUM, 
SCENARIO, VERSION, ENTITY, PROJECT, 
ACCOUNT, CURRENCY, BUDGETCENTER, PHASE, 
MISC, SPARE1, SPARE2, SPARE3, 
SUM("DATA") AS "DATA"
FROM DEV3_FDMEE.TO_ESSBASE_TEMP
GROUP BY BATCH_NAME, 
DATAVIEW, "YEAR", YEAR_NUM, PERIOD, PERIOD_NUM, 
SCENARIO, VERSION, ENTITY, PROJECT, 
ACCOUNT, CURRENCY, BUDGETCENTER, PHASE, 
MISC, SPARE1, SPARE2, SPARE3
;


CREATE TABLE "target"."targetTable" 
   (	"YEARS" VARCHAR2(30), 
	"PERIOD" VARCHAR2(30), 
	"SCENARIO" VARCHAR2(80), 
	"VERSION" VARCHAR2(80), 
	"ENTITY" VARCHAR2(80), 
	"PROJECT" VARCHAR2(80), 
	"ACCOUNT" VARCHAR2(80), 
	"CURRENCY" VARCHAR2(80), 
	"BUDGETCENTER" VARCHAR2(80), 
	"PHASE" VARCHAR2(80), 
	"MISC" VARCHAR2(80), 
	"SPARE1" VARCHAR2(80), 
	"SPARE2" VARCHAR2(80), 
	"SPARE3" VARCHAR2(80), 
	"DATA" NUMBER(38,6) NOT NULL ENABLE,
	"EXP_TIME" TIMESTAMP (15) DEFAULT SYSTIMESTAMP NOT NULL ENABLE
   ) ;