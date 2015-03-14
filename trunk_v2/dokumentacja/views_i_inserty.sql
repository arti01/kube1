/*CREATE VIEW user_roles_view AS 
 SELECT u.adr_email AS username, ur.role_name
   FROM user_roles ur
   JOIN uzytkownik_user_roles uur ON ur.id = uur.role_id
   JOIN uzytkownik u ON u.id = uur.uzytkownik_id;

CREATE VIEW userpass AS 
 SELECT u.adr_email AS username, p.pass AS password
   FROM uzytkownik u
   JOIN passwords p ON p.id = u.haslo_id;

*/

INSERT INTO config VALUES (1, 'eskalujPoMinutach', 'eskaluj wniosek po uplywie minut', '3');
INSERT INTO config VALUES (2, 'mail_smtp_host', 'mail_smtp_host', 'smtp.gmail.com');
INSERT INTO config VALUES (3, 'mail_smtp_socketFactory_port', 'mail_smtp_socketFactory_port', '465');
INSERT INTO config VALUES (4, 'mail_smtp_port', 'mail_smtp_port', '465');
INSERT INTO config VALUES (5, 'czy_ssl', 'czy email ssl', 'tak');
INSERT INTO config VALUES (6, 'email_link', 'link dolaczony do emaila informujacego o urlopie', 'http://test.test.pll');
INSERT INTO config VALUES (7, 'username', 'username', '');
INSERT INTO config VALUES (8, 'password', 'password', '');
INSERT INTO config VALUES (9, 'domysle_haslo', 'domyslne haslo przy zakladaniu usera', 'a');
INSERT INTO config VALUES (10, 'realm_szyfrowanie', 'md5 lub none', 'md5');
INSERT INTO config VALUES (11, 'email_unikalny', '1-nie, 0-tak', '1');


--
-- TOC entry 1929 (class 0 OID 28947)
-- Dependencies: 170 1932
-- Data for Name: wn_rodzaje; Type: TABLE DATA; Schema: public; Owner: eod
--

INSERT INTO wn_rodzaje VALUES (2, 'Okolicznościowy', 'OKO');
INSERT INTO wn_rodzaje VALUES (1, 'Wypoczynkowy', 'WYP');
INSERT INTO wn_rodzaje VALUES (3, 'Krajowa Delegacja', 'KRJ');
INSERT INTO wn_rodzaje VALUES (4, 'Zagraniczna delegacja', 'ZAG');

--
-- TOC entry 1930 (class 0 OID 28952)
-- Dependencies: 171 1932
-- Data for Name: wn_statusy; Type: TABLE DATA; Schema: public; Owner: eod
--

INSERT INTO wn_statusy VALUES (2, '#e8ca79', 'Wysłany', 'UW');
INSERT INTO wn_statusy VALUES (3, '#6fbb3a', 'Zaakceptowany', 'AK');
INSERT INTO wn_statusy VALUES (1, '#9ddcda', 'Utworzony', 'UT');
INSERT INTO wn_statusy VALUES (4, '#e87161', 'Odrzucony', 'OD');
INSERT INTO wn_statusy VALUES (5, '#eaed8e', 'Cofnięty', 'CO');
INSERT INTO wn_statusy VALUES (6, '#C0C0C0', 'Anulowany po akcepcie', 'AN');

--dodawanie adminow z pelnia praw (hasla 'a')
INSERT INTO dzial (id, nazwa) VALUES (3, 'admin');
INSERT INTO dzial (id, nazwa) VALUES (2, 'admin-md5');
INSERT INTO dzial (id, nazwa) VALUES (1, 'Cala Firma');
INSERT INTO dzial (id, nazwa) VALUES (4, 'Przezes Spółka 1');

INSERT INTO spolki (id, nazwa, opis, symbol) VALUES (1, 'Spółka1', 'Spółka1', 'SP1');

INSERT INTO passwords (id, pass) VALUES (3, 'a');
INSERT INTO passwords (id, pass) VALUES (2, '0cc175b9c0f1b6a831c399e269772661');
INSERT INTO passwords (id, pass) VALUES (1, '0cc175b9c0f1b6a831c399e269772661');
INSERT INTO passwords (id, pass) VALUES (4, '0cc175b9c0f1b6a831c399e269772661');

INSERT INTO uzytkownik (id, adr_email, fullname, haslo_id, ext_id, spolka_id) VALUES (3, 'admin@admin.eod', 'admin', 1, '', null);
INSERT INTO uzytkownik (id, adr_email, fullname, haslo_id, ext_id, spolka_id) VALUES (2, 'admin-md5@admin.eod', 'admin-md5', 2, '', null);
INSERT INTO uzytkownik (id, adr_email, fullname, haslo_id, ext_id, spolka_id) VALUES (1, 'generyczny@generyczny', 'generyczny', 3, '', null);
INSERT INTO uzytkownik (id, adr_email, fullname, haslo_id, ext_id, spolka_id) VALUES (4, 'prezesSp1@prezesSp1', 'Przezes  Spółki 1', 4, '', 1);

INSERT INTO struktura (id, st_kier, dzial_id, szef_id, sec_user_id, user_id, usuniety, przyjmowanie_wnioskow, mus_zast) VALUES (3, 1, 3, NULL, NULL, 3, NULL, 1,1);
INSERT INTO struktura (id, st_kier, dzial_id, szef_id, sec_user_id, user_id, usuniety, przyjmowanie_wnioskow, mus_zast) VALUES (1, 1, 1, NULL, NULL, 1, NULL, 1,1);
INSERT INTO struktura (id, st_kier, dzial_id, szef_id, sec_user_id, user_id, usuniety, przyjmowanie_wnioskow, mus_zast) VALUES (2, 1, 2, NULL, NULL, 2, NULL, 1,1);
INSERT INTO struktura (id, st_kier, dzial_id, szef_id, sec_user_id, user_id, usuniety, przyjmowanie_wnioskow, mus_zast) VALUES (4, 1, 4, 1, NULL, 4, NULL, 1,1);

INSERT INTO user_roles (id, role_name, opis) VALUES (2, 'eodstru', 'struktura organizacyjna');
INSERT INTO user_roles (id, role_name, opis) VALUES (1, 'eodurlop', 'wnioski urlopowe');
INSERT INTO user_roles (id, role_name, opis) VALUES (3, 'eoduser', 'dostęp do systemu');
INSERT INTO user_roles (id, role_name, opis) VALUES (4, 'eodadm', 'admnistrator systemu');


INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (1, 1);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (2, 1);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (3, 1);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (4, 1);

INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (1, 2);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (2, 2);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (3, 2);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (4, 2);

INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (1, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (2, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (3, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (4, 3);

INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (1, 4);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (2, 4);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (3, 4);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (4, 4);

-- Uwaga, należy dostosować tabelę i nazwy pól w def. tego widoku
/*CREATE VIEW wn_limity AS 
 SELECT DISTINCT extpass.login AS username, extpass.ulimit
   FROM extpass;*/
