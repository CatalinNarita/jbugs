# User test data
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Sebastian Daniel', 'Muresan', '490748251558', 'sibicoi@msggroup.com', true, 'pass', 'MuresS');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Catalin', 'Narita', '400748572846', 'catalin.narita@msggroup.com', true, 'pass', 'NaritC');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Olimpiu', 'Stefan', '490747295729', 'stefan.olimpiu@msggroup.com', true, 'pass', 'StefaO');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Botond', 'Tasnadi', '400742935104', 'tasboti@msggroup.com', true, 'pass', 'TasnaB');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Traian', 'Vidrascu', '490746291846', 'traian.vidrascu@msggroup.com', true, 'pass', 'VidraT');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Hannelore', 'Elek', '490748251558', 'hanne@msggroup.com', true, 'pass', 'ElekHa');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('David', 'Laci', '400748572846', 'david@msggroup.com', true, 'pass', 'LaciDa');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Catalin', 'Rusu', '490747295729', 'catalin@msggroup.com', true, 'pass', 'RusuCa');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Adrian', 'Schwartzkopf', '400742935104', 'adrian@msggroup.com', true, 'pass', 'SchwaA');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Andrei', 'Floricel', '490746291846', 'andrei@msggroup.com', true, 'pass', 'FloriA');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Mihaly', 'Fodor', '490748251558', 'mihaly@msggroup.com', true, 'pass', 'FodorM');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Attila', 'Szucz', '400748572846', 'attila@msggroup.com', true, 'pass', 'SzucsA');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Calin', 'Petrindean', '490747295729', 'calin@msggroup.com', true, 'pass', 'PetriC');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Robert', 'Ienei', '400742935104', 'robert@msggroup.com', true, 'pass', 'IeneiR');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Daniel', 'Donea', '490746291846', 'daniel@msggroup.com', true, 'pass', 'DoneaD');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Madalina', 'Dina', '490748251558', 'sibicoi@msggroup.com', true, 'pass', 'DinaMa');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Roberta', 'Haiura', '400748572846', 'roberta@msggroup.com', true, 'pass', 'HaiurR');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Calin', 'Cotet', '490747295729', 'calinC@msggroup.com', true, 'pass', 'CotetC');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Alexandru', 'Bora', '400742935104', 'alexandru@msggroup.com', true, 'pass', 'BoraAl');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Bogdan', 'Sortan', '490746291846', 'bogdan@msggroup.com', true, 'pass', 'SortaB');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Radu', 'Tufisi', '490748251558', 'sibicoi@msggroup.com', true, 'pass', 'TufisR');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Firas', 'Homsi', '400748572846', 'firas@msggroup.com', true, 'pass', 'HomsiF');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Csongor', 'Nemes', '490747295729', 'csongor@msggroup.com', true, 'pass', 'NemesC');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Filip', 'Sandor', '400742935104', 'filip@msggroup.com', true, 'pass', 'SandoF');
INSERT INTO USER (FIRSTNAME, LASTNAME, phonenumber, email, Status, password, username) VALUES ('Paul', 'Bota', '490746291846', 'paul@msggroup.com', true, 'pass', 'BotaPa');

# other tables TODO
# Role data
INSERT INTO Role(ROLENAME) VALUES('ADM');
INSERT INTO Role(ROLENAME) VALUES('PM');
INSERT INTO Role(ROLENAME) VALUES('TM');
INSERT INTO Role(ROLENAME) VALUES('DEV');
INSERT INTO Role(ROLENAME) VALUES('TEST');

#Permission data
INSERT INTO Permission(PERMISSIONNAME,DESCRIPTION) VALUES('PERMISSON_MANAGEMENT','Allows you to add and remove permissions to roles');
INSERT INTO Permission(PERMISSIONNAME,DESCRIPTION) VALUES('USER_MANAGEMENT','Allows to create, activate, deactivate users');
INSERT INTO Permission(PERMISSIONNAME,DESCRIPTION) VALUES('BUG_MANAGEMENT','Allows to create bugs');
INSERT INTO Permission(PERMISSIONNAME,DESCRIPTION) VALUES('BUG_CLOSE','Allows to close a bug');
INSERT INTO Permission(PERMISSIONNAME,DESCRIPTION) VALUES('BUG_EXPORT_PDF','Allows to export a pdf with all the bugs');

#User role
INSERT INTO user_role(id_user,id_role) VALUES(4,1);
INSERT INTO user_role(id_user,id_role) VALUES(5,2);
INSERT INTO user_role(id_user,id_role) VALUES(6,3);
INSERT INTO user_role(id_user,id_role) VALUES(7,4);
INSERT INTO user_role(id_user,id_role) VALUES(8,5);
INSERT INTO user_role(id_user,id_role) VALUES(9,4);
INSERT INTO user_role(id_user,id_role) VALUES(9,5);
INSERT INTO user_role(id_user,id_role) VALUES(10,3);
INSERT INTO user_role(id_user,id_role) VALUES(11,4);
INSERT INTO user_role(id_user,id_role) VALUES(12,2);
INSERT INTO user_role(id_user,id_role) VALUES(13,1);

#Role Permission
INSERT INTO role_permission(id_role,id_permission) VALUES(1,1);
INSERT INTO role_permission(id_role,id_permission) VALUES(1,2);
INSERT INTO role_permission(id_role,id_permission) VALUES(1,3);
INSERT INTO role_permission(id_role,id_permission) VALUES(1,4);
INSERT INTO role_permission(id_role,id_permission) VALUES(1,5);
INSERT INTO role_permission(id_role,id_permission) VALUES(2,2);
INSERT INTO role_permission(id_role,id_permission) VALUES(2,3);
INSERT INTO role_permission(id_role,id_permission) VALUES(2,4);
INSERT INTO role_permission(id_role,id_permission) VALUES(2,5);
INSERT INTO role_permission(id_role,id_permission) VALUES(3,3);
INSERT INTO role_permission(id_role,id_permission) VALUES(3,4);
INSERT INTO role_permission(id_role,id_permission) VALUES(3,5);
INSERT INTO role_permission(id_role,id_permission) VALUES(4,3);
INSERT INTO role_permission(id_role,id_permission) VALUES(4,5);
INSERT INTO role_permission(id_role,id_permission) VALUES(5,5);

#Bug data
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('bug','dadsad',0,'1.1','','2017/07/07',0,2,1);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('asdad','kjkj',1,'2.3','','2017/08/09',2,3,2);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Null Pointer Exception','EJB-ul BugManagementBean da un null pointer exception',0,'1.5','','2017/08/30',1,9,10);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Property not found exception','La accesarea paginii de clienti se primeste exceptia din titlu',5,'2.0','','2017/08/28',0,10,11);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Database exception','Se primeste o exceptie de la baza de date la adaugare de useri',2,'1.5','1.7','2017/09/09',2,11,12);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Cannot deploy application','Posibila problema de configurare a proiectului',3,'2.0','2.2','2017/08/18',3,12,13);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('AAAAAAA!!! Syntax error, really?','Eroare de sintaxa in bugs.xhtml',4,'1.0','','2017/08/10',3,13,9);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Adaugare useri','Butonul de adaugare useri nu face nimic in unele cazuri',0,'1.5','','2017/08/15',2,10,12);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Bug de afisare','Diagrama user-ilor nu e corect afisata',5,'1.8','','2017/08/13',2,13,11);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Internationalizare','Labelul "type" din tabela de masini nu e internationalizat',3,'1.0','1.1','2017/08/09',3,10,9);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Bug la export','La exportul in pdf sunt afisate caractere gunoi',0,'2.0','','2017/08/14',2,13,12);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Bug la login','Desi parola si username-ul sunt corecte, utilizatorul nu se poate loga',1,'1.3','','2017/08/18',0,10,12);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Internationalizare','Locale-ul nu se retine la schimbarea paginii',0,'2.3','','2017/08/23',1,9,12);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Generare username','La crearea unui utilizator nou, username-ul este generat gresit',1,'2.0','','2017/08/10',0,9,13);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Loading error','Pagina de welcome nu se incarca',1,'1.5','','2017/08/11',0,11,10);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Download error','Nu se pot downloada atasamentele la bug-uri',0,'1.7','','2017/08/25',1,12,13);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Bugs page is blocking','Bug page is blocking if it has more than 10 bugs displayed',2,'1.5','2.0','2017/08/14',0,11,9);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Permission bug','Utilizatorii pot accesa pagini restrictionate chiar daca n-au permisiunea necesara',5,'1.1','','2017/08/17',0,10,12);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Front end bug','Label-urile sunt aranjate aiurea',0,'1.8','','2017/08/15',2,13,12);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Bug edit bug','When changing the person to resolve the bug, the change is not saved',1,'1.2','','2017/08/13',2,10,13);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('File upload bug','Fisierele sunt incarcate concurent si se poate depasi limita dimensiunii',0,'1.8','','2017/08/20',3,9,13);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Validation bug','Datele nu sunt validate la adaugarea de bug-uri',3,'1.1','1.5','2017/08/11',1,10,11);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Exception handling bug','Nu se afiseaza mesaje de eroare la violarea constrangerilor',0,'1.0','','2017/08/25',1,12,10);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Password error','Utilizatorii pot vedea parola altor utilizatori',0,'1.5','','2017/08/28',0,12,13);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Attachment bug','Atasamentele nu sunt corect salvate in baza de date',0,'1.3','','2017/08/13',1,12,9);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Notification bug','La salvarea notificarilor, id-ul bug-ului nu se salveaza in baza de date',3,'1.5','2.0','2017/08/19',3,10,13);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Statistics diagrams error','Diagramele de statistici ale utilizatorilor sunt toate goale',2,'2.0','2.5','2017/08/11',2,13,12);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Export bug','Bug-urile sunt gresit exportate in excel',1,'1.8','','2017/08/13',3,10,11);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('EJB initialization bug','BugManagementBean are o eroare la initializare',5,'2.0','','2017/08/09',0,10,12);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Page refresh bug','La editarea de bug-uri pe o alta pagina din tabela, se reincarca intreaga pagina si nu-i ok.',0,'2.3','','2017/08/10',3,12,10);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('Back page bug','Cand se apasa pe butonul de back in browser, ne intoarcem la pagina de login',3,'1.5','2.0','2017/08/12',2,11,9);
INSERT INTO Bug(title,description,status,version,fixedinversion,targetdate,severity,createdby_id,assignedto_id) VALUES('User activation bug','La activarea si dezactivarea unui user, schimbarile nu se salveaza in baza de date',2,'1.5','1.7','2017/08/06',1,13,11);

