-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'owner1','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner2','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (10,'owner2','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner3','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (11,'owner3','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner4','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (12,'owner4','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner5','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (13,'owner5','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner6','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (14,'owner6','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner7','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (15,'owner7','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner8','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (16,'owner8','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner9','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (17,'owner9','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner10','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (18,'owner10','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'vet1','veterinarian');


INSERT INTO users(username,password,enabled) VALUES ('vet2','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (25,'vet2','veterinarian');
INSERT INTO users(username,password,enabled) VALUES ('vet3','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (26,'vet3','veterinarian');
INSERT INTO users(username,password,enabled) VALUES ('vet4','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (27,'vet4','veterinarian');
INSERT INTO users(username,password,enabled) VALUES ('vet5','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (28,'vet5','veterinarian');
INSERT INTO users(username,password,enabled) VALUES ('vet6','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (29,'vet6','veterinarian');


INSERT INTO users(username,password,enabled) VALUES ('economista1','economista1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (4,'economista1','economista');

INSERT INTO users(username,password,enabled) VALUES ('abrgarvil','abrgarvil',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (8,'abrgarvil','owner');

INSERT INTO users(username,password,enabled) VALUES ('dancasnar1','dancasnar1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (9,'dancasnar1','owner');


INSERT INTO vets VALUES (1, 'James', 'Carter','vet1');
INSERT INTO vets VALUES (2, 'Helen', 'Leary','vet2');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas','vet3');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega','vet4');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens','vet5');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins','vet6');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO tipo_enfermedades VALUES (1, 'Rabia');
INSERT INTO tipo_enfermedades VALUES (2, 'Sarna');
INSERT INTO tipo_enfermedades VALUES (3, 'Parvovirus');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner2');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner3');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner4');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner5');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner6');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner7');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner8');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner9');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner10');
INSERT INTO owners VALUES (20, 'Daniel', 'Castroviejo', '2335 Independence La.', 'Waunakee', '6085555487', 'dancasnar1');
INSERT INTO owners VALUES (15, 'abrgarvil', 'Garcia', 'Reina Mercedes', 'Sevilla', '6805555487', 'abrgarvil');

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (19, 'Sly2', '2012-06-08', 1, 20);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (18, 'George', '2012-06-08', 2, 15);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 13, '2013-01-04', 'spayed');

INSERT INTO economista VALUES (1, 'Jose', 'Escobito', 'Muchos', 'economista1');

INSERT INTO gastos(id,titulo,cantidad,fecha,description,economista_id) VALUES (1, 'Material esterilizante', 250, '2020-10-04', 'Gasto correspondiente a la compra de material esterilizante para la clinica',1);
INSERT INTO gastos(id,titulo,cantidad,fecha,description,economista_id) VALUES (2, 'Sueldos', 10250, '2020-10-11', 'Gasto correspondiente a los sueldos de todos los trabajadores de la clinica del mes de Octubre',1);
INSERT INTO gastos(id,titulo,cantidad,fecha,description,economista_id) VALUES (3, 'Arreglar ventana', 80, '2020-10-19', 'Gasto correspondiente a larreglar una ventana de la clinica',1);


INSERT INTO ingresos(id,titulo,cantidad,fecha,description,economista_id) VALUES (3, 'Arreglar ventana', 80, '2020-10-19', 'Gasto correspondiente a larreglar una ventana de la clinica',1);

INSERT INTO comentarios(id,titulo,cuerpo,vet_id,owner_id) VALUES (1,'ComentarioPrueba', 'Esto es un comentario de prueba', 2, 1);
INSERT INTO comentarios(id,titulo,cuerpo,vet_id,owner_id) VALUES (5,'Otro comentario', 'Esto es otro comentario', 1, 1);
INSERT INTO comentarios(id,titulo,cuerpo,vet_id,owner_id) VALUES (2,'ComentarioPrueba', 'Esto es un comentario de prueba', 1, 1);
INSERT INTO comentarios(id,titulo,cuerpo,vet_id,owner_id) VALUES (3,'ComentarioPrueba', 'Esto es un comentario de prueba', 3, 1);
INSERT INTO comentarios(id,titulo,cuerpo,vet_id,owner_id) VALUES (4,'ComentarioPrueba', 'Esto es un comentario de prueba', 4, 1);

INSERT INTO ingresos(id,titulo,cantidad,fecha,description,economista_id) VALUES (1, 'Vacunas', 1250, '2020-10-13', 'Ingreso correspondiente a las vacunas puestas a las mascotas de la clinica del mes de Octubre',1);
INSERT INTO ingresos(id,titulo,cantidad,fecha,description,economista_id) VALUES (2, 'Clases', 800, '2020-10-14', 'Ingresos correspondiente a las clases impartidas en el mes de Noviembre',1);

INSERT INTO vacunas(id, tipoenfermedad_id, fecha, descripcion, pet_id, vet_id) VALUES (1, 1, '2020-01-01', 'Se le ha añadido la vacuna contra la rabia', 1, 1);
INSERT INTO vacunas(id, tipoenfermedad_id, fecha, descripcion, pet_id, vet_id) VALUES (2, 3, '2013-05-01', 'Se le ha añadido la vacuna contra la parvovirus', 2, 1);




