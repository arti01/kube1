INSERT INTO user_roles (id, role_name, opis) VALUES (5, 'eoddok_rej','dokumenty - rejestracja');
INSERT INTO user_roles (id, role_name, opis) VALUES (6, 'eoddok_odb','dokumenty - odbiór');
INSERT INTO user_roles (id, role_name, opis) VALUES (7, 'eoddok_cfg','dokumenty - konfiguracja');
INSERT INTO user_roles (id, role_name, opis) VALUES (8, 'eoddok_arc','dokumenty - archiwizacja');
INSERT INTO user_roles (id, role_name, opis) VALUES (9, 'eod_ogl','dodawanie ogloszen');
INSERT INTO user_roles (id, role_name, opis) VALUES (10, 'eod_um_cfg','zasoby - konfiguracja');
INSERT INTO user_roles (id, role_name, opis) VALUES (11, 'eod_um_sprz','zasoby');
INSERT INTO user_roles (id, role_name, opis) VALUES (12, 'eod_um_rez','rezerwacja zasobów');
INSERT INTO user_roles (id, role_name, opis) VALUES (13, 'eod_kal_dec','podglad kalendarza osob decyzyjnych');
INSERT INTO user_roles (id, role_name, opis) VALUES (14, 'eod_url_all', 'podgląd wszystkich wn. urlopowych');
INSERT INTO user_roles (id, role_name, opis) VALUES (15, 'eod_um_doc','zasoby - dokumenty');
INSERT INTO user_roles (id, role_name, opis) VALUES (16, 'eod_um_rez_przeg','rezerwacja zasobów - podgląd');
INSERT INTO user_roles (id, role_name, opis) VALUES (17, 'eod_url_sel','podgląd wszystkich wn. o wybranym statusie');

INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (5, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (6, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (7, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (8, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (9, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (10, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (11, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (12, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (13, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (14, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (15, 3);

INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (5, 2);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (6, 2);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (7, 2);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (8, 2);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (9, 2);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (10, 2);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (11, 2);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (12, 2);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (13, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (14, 3);
INSERT INTO uzytkownik_user_roles (role_id, uzytkownik_id ) VALUES (15, 3);

INSERT INTO dc_typ_flow (id, nazwa, opis ) VALUES (1, 'akceptacje', 'dokument podlegający procesowi akceptacji');
INSERT INTO dc_typ_flow (id, nazwa, opis ) VALUES (2, 'informacje', 'dokument do zapoznania się i ewentualnie potwierdzenia');

INSERT INTO dc_dokument_status  VALUES (1, 'nowy', 'dokument po zarejestrowaniu', '#222222', 'dokumenty');
INSERT INTO dc_dokument_status  VALUES (2, 'w trakcie', 'dokument w trakcie akceptacji lub dystrybucji', '#333333', 'dokumenty');
INSERT INTO dc_dokument_status  VALUES (3, 'zamknięty', 'dokument zamknięty', '#444444', 'dokumenty');
INSERT INTO dc_dokument_status  VALUES (4, 'anulowany', 'dokument anulowany', '#555555', 'dokumenty');
INSERT INTO dc_dokument_status  VALUES (5, 'odrzucony', 'dokument odrzucony', '#666666', 'dokumenty');
insert into dc_dokument_status  values (6, 'poczekalnia - do akceptu', 'dokument w poczekalni archiwizacji w trakcie akceptacji', '#777777', 'archiwum');
insert into DC_DOKUMENT_STATUS values (7, 'poczekalnia - zaakceptowany', 'dokument w poczekalni archiwizacji zaakceptowany, gotowy do archiwizacji', '#888888', 'archiwum');
INSERT INTO dc_dokument_status  VALUES (8, 'w archiwum', 'dokument w archiwum', 'brown', 'archiwum');
INSERT INTO dc_dokument_status  VALUES (9, 'wydany', 'dokument wydany z archiwum', '#999999', 'archiwum');
INSERT INTO dc_dokument_status  VALUES (10, 'poddany brakowaniu', 'dokument poddany procesowi brakowania', '#131313', 'archiwum');

INSERT INTO DC_AKCEPT_TYP_KROKU (id, nazwa ) VALUES (1, 'wszyscy muszą zaakceptować');
INSERT INTO DC_AKCEPT_TYP_KROKU (id, nazwa ) VALUES (2, 'wystarczy jeden do akceptacji');

INSERT INTO DC_AKCEPT_STATUS (id, nazwa ) VALUES (1, 'początkowy');
INSERT INTO DC_AKCEPT_STATUS (id, nazwa ) VALUES (2, 'brak akceptu');
INSERT INTO DC_AKCEPT_STATUS (id, nazwa ) VALUES (3, 'częściowa akceptacja');
INSERT INTO DC_AKCEPT_STATUS (id, nazwa ) VALUES (4, 'zaakceptowany');
INSERT INTO DC_AKCEPT_STATUS (id, nazwa ) VALUES (5, 'odrzucony');

INSERT INTO dc_teczka (id, nazwa, opis ) VALUES (1, 'brak teczki', 'brak teczki');
INSERT INTO dc_zrodlo (id, nazwa, opis ) VALUES (1, 'brak źródła', 'brak źródła');
INSERT INTO dc_kontrahenci (id, nazwa, nip_regon, osoba_kontak, www, email, adres, info_dod) VALUES (1, 'brak', null, null, null, null, null, 'brak kontrahenta');

INSERT INTO dc_rodzaj_grupa (id, nazwa, opis) VALUES (1, 'wychodzące', 'dokumenty wychodzące');
INSERT INTO dc_rodzaj_grupa (id, nazwa, opis) VALUES (2, 'przychodzące', 'dokumenty przychodzące');
INSERT INTO dc_rodzaj_grupa (id, nazwa, opis) VALUES (3, 'wewnętrzne', 'dokumenty wewnętrzne');

INSERT INTO config (id, nazwa, opis, wartosc) VALUES (12, 'dirImportSkan', 'katalog importu dokumentow', '/home/arti01/tmp/1');
INSERT INTO config (id, nazwa, opis, wartosc) VALUES (13, 'dirImportSkanBak', 'katalog importu dokumentow', '/home/arti01/tmp/1/bak');
INSERT INTO config (id, nazwa, opis, wartosc) VALUES (14, 'kluczLicencji', 'kluczLicencji', 'kluczLicencji');
INSERT INTO config (id, nazwa, opis, wartosc) VALUES (15, 'dirImportCzasCzyszczenia', 'po ilu dniach usuwac rekordy z importu', '14');

INSERT INTO config(id, nazwa, opis, wartosc) VALUES ((select max(id)+1 from config), 'emailOdbServer', 'serwer poczty do odbioru zalacznikow', 'imap.googlemail.com');
INSERT INTO config(id, nazwa, opis, wartosc) VALUES ((select max(id)+1 from config), 'emailOdbUser', 'nazwa konta poczty do odbioru zalacznikow', 'arti4077@gmail.com');
INSERT INTO config(id, nazwa, opis, wartosc) VALUES ((select max(id)+1 from config), 'emailOdbPass', 'haslo konta poczty do odbioru zalacznikow', 'pasword');
INSERT INTO config(id, nazwa, opis, wartosc) VALUES ((select max(id)+1 from config), 'emailOdbFolder', 'forder odbioru zalacznikow, np. inbox/test', 'inbox/test');
INSERT INTO config(id, nazwa, opis, wartosc) VALUES ((select max(id)+1 from config), 'emailOdbmaxMail', 'max wiadomosci w skrzynce odbiorczej', '30');
INSERT INTO config(id, nazwa, opis, wartosc) VALUES ((select max(id)+1 from config), 'emailOdbkrokPobierania', 'ilosc maili pobierana w jednej iteracji crona', '5');
INSERT INTO config(id, nazwa, opis, wartosc) VALUES ((select max(id)+1 from config), 'dir_repo', 'forder gdzie sa przechowywane dokumenty', 'd:\\tmp');
INSERT INTO config(id, nazwa, opis, wartosc) VALUES ((select max(id)+1 from config), 'mail_smtp_from', 'adres email FROM', 'arti4077@gmail.com');
INSERT INTO config(id, nazwa, opis, wartosc) VALUES ((select max(id)+1 from config), 'rez_all_sp', 'rezerwacje dla wszystkich spolek', 'tak');

INSERT INTO dc_rodzaj_typy_pol VALUES (1, 'liczba');
INSERT INTO dc_rodzaj_typy_pol VALUES (2, 'tekst');
INSERT INTO dc_rodzaj_typy_pol VALUES (3, 'data');

