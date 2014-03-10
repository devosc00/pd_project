# --- default dataset
# ---!Ups

insert into "COMPANY"("ID", "NAME", "CITY", "STREET", "PHONE", "CREATE_DATE", "ORDERS") 
	values (10100, 'Administrator Company', 'Koło','Jasna', 007, '11-11-2011', 0);
insert into "COMPANY"("ID", "NAME", "CITY", "STREET", "PHONE", "CREATE_DATE", "ORDERS") 
	values (20200, 'Big Pz Test', 'Poznań','Szara',911, '11-11-2011', 0);
insert into "COMPANY"("ID", "NAME", "CITY", "STREET", "PHONE", "CREATE_DATE", "ORDERS") 
	values (15000, 'Hudge Pz Test', 'New York', 'Black Hole', 112, '11-11-2011', 0);

		
insert into	"ACCOUNT"("ID", "EMAIL" ,"PASS", "NAME", "POSITION", "PERMISSION", "COMP_ID") 
	values (10000, 'robert@example.com', 'secret', 'Robert Adamski', 'admin', 'Administrator',10100 );


insert into "ACCOUNT"("ID", "EMAIL", "PASS", "NAME", "POSITION", "PERMISSION", "COMP_ID") 
	values (20000, 'marcin@example.com', 'secret', 'Marcin Król', 'sprzedaż', 'LocalAdministrator',15000 );
insert into "ACCOUNT"("ID", "EMAIL", "PASS", "NAME", "POSITION", "PERMISSION", "COMP_ID") 
	values (30000, 'przemek@example.com', 'secret', 'Przemek Nowak', 'produkcja', 'Operator', 15000 );

insert into	"ACCOUNT"("ID", "EMAIL" ,"PASS", "NAME", "POSITION", "PERMISSION", "COMP_ID") 
	values (40000, 'krzysiek@example.com', 'secret', 'Krzyś', 'sprzedaż', 'SalesForce',15000 );
insert into "ACCOUNT"("ID", "EMAIL", "PASS", "NAME", "POSITION", "PERMISSION", "COMP_ID") 
	values (50000, 'bob@example.com', 'secret', 'Robert Król', 'sprzedaż', 'LocalAdministrator',20200 );

insert into "MATERIAL"("ID", "NAME", "DATE", "TOTAL_AMOUNT", "COMP_ID") values (10000, 'Poliamid', '11-11-2011', 0, 15000 );
insert into "MATERIAL"("ID", "NAME", "DATE", "TOTAL_AMOUNT", "COMP_ID") values (30000, 'Aluminium', '11-11-2011', 0, 20200 );
insert into "MATERIAL"("ID", "NAME", "DATE", "TOTAL_AMOUNT", "COMP_ID") values (20000, 'Polietylen', '11-11-2011', 0, 15000 );


insert into "PROJECT"("ID", "NAME", "START_DATE", "END_DATE", "ORDERED", "MAT_AMOUNT", "DONE_PARTS", "ACC_ID", "MAT_ID") 
	values (10000, 'koło linowe', '11-11-2011', '11-11-2011', 6, 2.5, 0, 20000, 10000);


insert into "PROJECT"("ID", "NAME", "START_DATE", "END_DATE", "ORDERED", "MAT_AMOUNT", "DONE_PARTS", "ACC_ID", "MAT_ID") 
	values (20000, 'koło linowe', '11-11-2011', '11-11-2011', 6, 2.5, 0, 40000, 10000);

insert into "PROJECT"("ID", "NAME", "START_DATE", "END_DATE", "ORDERED", "MAT_AMOUNT", "DONE_PARTS", "ACC_ID", "MAT_ID") 
	values (30000, 'koło linowe', '11-11-2011', '11-11-2011', 6, 2.5, 0, 30000, 20000);


insert into "PROJ_DETAIL" ("ID", "NAME", "TOTAL_START", "ITEM_TIME", "TOTAL_ITEMS", "SALDO", "ORDER_COUNTER",
"TOTAL_MAT", "PROJ_ID") values (10000, 'koło linowe', '11-11-2011', 4.3, 0, 0, 0, 0.0, 10000);

insert into "ACC_DETAIL" ("ID", "START_DATE", "PROJECT_COUNTER", "ACC_ID", "PROJ_DETAIL_ID")
	values (10000, '11-11-2011', 0, 10000, 10000);	






# --- !Downs
delete from "ACC_DETAIL";
delete from "PROJ_DETAIL";
delete from "PROJECT";
delete from "ACCOUNT";
delete from "MATERIAL";
delete from "COMPANY";