insert into uzytkownik select max(id)+1, '', '', 'tttttttt', 551, 249103 from uzytkownik where id=(select max(id) from uzytkownik);
select max(id)+1, '', '', 'tttttttt', 551, 249103 from struktura where id=(select max(id) from uzytkownik);
insert into struktura
select max(id)+1,'',1,0,null,232201,null,172201,max(id)+1,1 from struktura where id=(select max(id) from struktura);

--delete from struktura where id not in (select id from uzytkownik)
--delete from uzytkownik where id not in (select id from struktura)
