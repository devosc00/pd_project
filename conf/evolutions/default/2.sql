# --- default dataset
# ---!Ups

insert into "COMPANY"("ID", "NAME", "CITY", "STREET", "PHONE", "CREATE_DATE", "ORDERS") 
	values (1010, 'Small', 'Koło','Jasna', 007, '11-11-2011', 0);
insert into "COMPANY"("ID", "NAME", "CITY", "STREET", "PHONE", "CREATE_DATE", "ORDERS") 
	values (4900, 'Big', 'Poznań','Szara',911, '11-11-2011', 0);
insert into "COMPANY"("ID", "NAME", "CITY", "STREET", "PHONE", "CREATE_DATE", "ORDERS") 
	values (1500, 'Hudge', 'New York', 'Black Hole', 112, '11-11-2011', 0);

		
insert into	"ACCOUNT"("ID", "EMAIL" ,"PASS", "NAME", "POSITION", "PERMISSION", "COMP_ID") 
	values (10000, 'robert@example.com', 'secret', 'Robert Adamski', 'admin', 'Administrator',1010 );
insert into "ACCOUNT"("ID", "EMAIL", "PASS", "NAME", "POSITION", "PERMISSION", "COMP_ID") 
	values (20000, 'marcin@example.com', 'secret', 'Marcin Król', 'sprzedaż', 'LocalAdministrator',1010 );
insert into "ACCOUNT"("ID", "EMAIL", "PASS", "NAME", "POSITION", "PERMISSION", "COMP_ID") 
	values (30000, 'przemek@example.com', 'secret', 'Przemek Nowak', 'produkcja', 'LocalAdministrator', 1500 );

insert into "MATERIAL"("ID", "NAME", "DATE", "TOTAL_AMOUNT", "COMP_ID") values (10000, 'Poliamid', '11-11-2011', 0, 1010 );
insert into "MATERIAL"("ID", "NAME", "DATE", "TOTAL_AMOUNT", "COMP_ID") values (20000, 'Polietylen', '11-11-2011', 0, 1500 );


insert into "PROJECT"("ID", "NAME", "START_DATE", "END_DATE", "ORDERED", "MAT_AMOUNT", "DONE_PARTS", "ACC_ID", "MAT_ID") 
	values (10000, 'koło linowe', '11-11-2011', '11-11-2011', 6, 2.5, 0, 10000, 10000);

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