
# --- !Ups

create table "ACCOUNT" (
	"ID" BIGSERIAL NOT NULL PRIMARY KEY,
	"EMAIL" VARCHAR(254) NOT NULL,
	"PASS" VARCHAR(254) NOT NULL,
	"NAME" VARCHAR(254) NOT NULL,
	"POSITION" VARCHAR(254),
	"PERMISSION" VARCHAR(254) NOT NULL,
	"COMP_ID" BIGINT NOT NULL);

create table "ACC_DETAIL" (
	"ID" BIGSERIAL NOT NULL PRIMARY KEY,
	"START_DATE" DATE,
	"PROJECT_COUNTER" INTEGER,
	"ACC_ID" BIGINT NOT NULL,
	"PROJ_DETAIL_ID" BIGINT NOT NULL);

create table "COMPANY" (
	"ID" BIGSERIAL NOT NULL PRIMARY KEY,
	"NAME" VARCHAR(254) NOT NULL,
	"CITY" VARCHAR(254),
	"STREET" VARCHAR(254),
	"PHONE" VARCHAR(254),
	"CREATE_DATE" DATE,
	"ORDERS" INTEGER);

create table "MATERIAL" (
	"ID" BIGSERIAL NOT NULL PRIMARY KEY,
	"NAME" VARCHAR(254) NOT NULL,
	"DATE" DATE,
	"TOTAL_AMOUNT" FLOAT,
	"COMP_ID" BIGINT);

create table "PROJECT" (
	"ID" BIGSERIAL NOT NULL PRIMARY KEY,
	"NAME" VARCHAR(254) NOT NULL,
	"START_DATE" DATE,
	"END_DATE" DATE,
	"ORDERED" INTEGER,
	"MAT_AMOUNT" FLOAT,
	"DONE_PARTS" INTEGER,
	"ACC_ID" BIGINT NOT NULL,
	"MAT_ID" BIGINT NOT NULL);

create table "PROJ_DETAIL" (
	"ID" BIGSERIAL NOT NULL PRIMARY KEY,
	"NAME" VARCHAR(254),
	"TOTAL_START" DATE,
	"ITEM_TIME" FLOAT,
	"TOTAL_ITEMS" INTEGER,
	"SALDO" INTEGER,
	"ORDER_COUNTER" INTEGER,
	"TOTAL_MAT" FLOAT,
	"PROJ_ID" BIGINT NOT NULL);

alter table "ACCOUNT" add constraint "COMP_FK" foreign key("COMP_ID") references "COMPANY"("ID") on update NO ACTION on delete NO ACTION;
alter table "ACC_DETAIL" add constraint "ACCOUNT_FK" foreign key("ACC_ID") references "ACCOUNT"("ID") on update NO ACTION on delete NO ACTION;
alter table "ACC_DETAIL" add constraint "PROJ_DETAIL_FK" foreign key("PROJ_DETAIL_ID") references "PROJ_DETAIL"("ID") on update NO ACTION on delete NO ACTION;
alter table "MATERIAL" add constraint "COMP_FK" foreign key("COMP_ID") references "COMPANY"("ID") on update NO ACTION on delete NO ACTION;
alter table "PROJECT" add constraint "ACC_FK" foreign key("ACC_ID") references "ACCOUNT"("ID") on update NO ACTION on delete NO ACTION;
alter table "PROJECT" add constraint "MAT_FK" foreign key("MAT_ID") references "MATERIAL"("ID") on update NO ACTION on delete NO ACTION;
alter table "PROJ_DETAIL" add constraint "PROJ_FK" foreign key("PROJ_ID") references "PROJECT"("ID") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "ACCOUNT" drop constraint "COMP_FK";
alter table "ACC_DETAIL" drop constraint "ACCOUNT_FK";
alter table "ACC_DETAIL" drop constraint "PROJ_DETAIL_FK";
alter table "MATERIAL" drop constraint "COMP_FK";
alter table "PROJECT" drop constraint "ACC_FK";
alter table "PROJECT" drop constraint "MAT_FK";
alter table "PROJ_DETAIL" drop constraint "PROJ_FK";
drop table "ACCOUNT";
drop table "ACC_DETAIL";
drop table "COMPANY";
drop table "MATERIAL";
drop table "PROJECT";
drop table "PROJ_DETAIL";

