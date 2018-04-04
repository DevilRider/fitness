drop table if exists area;
CREATE TABLE area (
  id int(11) NOT NULL IDENTITY,
  pid int(11) NOT NULL,
  name varchar(150) NOT NULL,
  level int(11) NOT NULL,
  zip_code varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
);

insert into area (pid, name, level) values (0, 'province1', 0), (1, 'city1', 1), (2, 'district1', 2), (3, 'village1', 3);