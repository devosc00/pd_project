
# --- !Ups

create table "ACCOUNT" (
	"ID" BIGSERIAL NOT NULL PRIMARY KEY,
	"EMAIL" VARCHAR(254) NOT NULL,
	"PASS" VARCHAR(254) NOT NULL,
	"NAME" VARCHAR(254) NOT NULL,
	"POSITION" VARCHAR(254) NOT NULL,
	"PERMISSION" VARCHAR(254) NOT NULL,
	"COMP_ID" BIGINT NOT NULL);

create table "ACC_DETAIL" (
	"ID" BIGSERIAL NOT NULL PRIMARY KEY,
	"START_DATE" BIGINT NOT NULL,
	"PROJECT_COUNTER" INTEGER NOT NULL,
	"ACC_ID" BIGINT NOT NULL,
	"PROJ_DETAIL_ID" BIGINT NOT NULL);

create table "COMPANY" (
	"ID" BIGSERIAL NOT NULL PRIMARY KEY,
	"NAME" VARCHAR(254) NOT NULL,
	"CITY" VARCHAR(254) NOT NULL,
	"STREET" VARCHAR(254) NOT NULL,
	"PHONE" VARCHAR(254) NOT NULL,
	"CREATE_DATE" BIGINT NOT NULL,
	"ORDERS" INTEGER NOT NULL);

create table "MATERIAL" (
	"ID" BIGSERIAL NOT NULL PRIMARY KEY,
	"NAME" VARCHAR(254) NOT NULL,
	"DATE" BIGINT NOT NULL,
	"TOTAL_AMOUNT" FLOAT NOT NULL);

create table "PROJECT" (
	"ID" BIGSERIAL NOT NULL PRIMARY KEY,
	"NAME" VARCHAR(254) NOT NULL,
	"START_DATE" BIGINT NOT NULL,
	"END_DATE" BIGINT NOT NULL,
	"ORDERED" INTEGER NOT NULL,
	"MAT_AMOUNT" FLOAT NOT NULL,
	"DONE_PARTS" INTEGER NOT NULL,
	"ACC_ID" BIGINT NOT NULL,
	"MAT_ID" BIGINT NOT NULL);

create table "PROJ_DETAIL" (
	"ID" BIGSERIAL NOT NULL PRIMARY KEY,
	"NAME" VARCHAR(254) NOT NULL,
	"TOTAL_START" BIGINT NOT NULL,
	"ITEM_TIME" FLOAT NOT NULL,
	"TOTAL_ITEMS" INTEGER NOT NULL,
	"SALDO" INTEGER NOT NULL,
	"ORDER_COUNTER" INTEGER NOT NULL,
	"TOTAL_MAT" FLOAT NOT NULL,
	"PROJ_ID" BIGINT NOT NULL);

alter table "ACCOUNT" add constraint "COMP_FK" foreign key("COMP_ID") references "COMPANY"("ID") on update NO ACTION on delete NO ACTION;
alter table "ACC_DETAIL" add constraint "ACCOUNT_FK" foreign key("ACC_ID") references "ACCOUNT"("ID") on update NO ACTION on delete NO ACTION;
alter table "ACC_DETAIL" add constraint "PROJ_DETAIL_FK" foreign key("PROJ_DETAIL_ID") references "PROJ_DETAIL"("ID") on update NO ACTION on delete NO ACTION;
alter table "PROJECT" add constraint "ACC_FK" foreign key("ACC_ID") references "ACCOUNT"("ID") on update NO ACTION on delete NO ACTION;
alter table "PROJECT" add constraint "MAT_FK" foreign key("MAT_ID") references "MATERIAL"("ID") on update NO ACTION on delete NO ACTION;
alter table "PROJ_DETAIL" add constraint "PROJ_FK" foreign key("PROJ_ID") references "PROJECT"("ID") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "ACCOUNT" drop constraint "COMP_FK";
alter table "ACC_DETAIL" drop constraint "ACCOUNT_FK";
alter table "ACC_DETAIL" drop constraint "PROJ_DETAIL_FK";
alter table "PROJECT" drop constraint "ACC_FK";
alter table "PROJECT" drop constraint "MAT_FK";
alter table "PROJ_DETAIL" drop constraint "PROJ_FK";
drop table "ACCOUNT";
drop table "ACC_DETAIL";
drop table "COMPANY";
drop table "MATERIAL";
drop table "PROJECT";
drop table "PROJ_DETAIL";

