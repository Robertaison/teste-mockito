CREATE TABLE mocks.LEILAO (
 id int auto_increment,
 descricao varchar(255),
 data DATE,
 encerrado BOOLEAN,
 primary key(id)
);

CREATE TABLE mocks.LANCES (
 id int auto_increment,
 leilao_id int,
 valor DECIMAL(13,2),
 primary key(id)
);

ALTER TABLE mocks.LANCES ADD CONSTRAINT leilao_fk FOREIGN KEY (leilao_id) REFERENCES mocks.LEILAO (id);

CREATE TABLE mocks.USUARIO (
  id int auto_increment,
  lances_id int,
  nome varchar(255),
  primary key(id)
);

ALTER TABLE mocks.USUARIO ADD CONSTRAINT lance_fk FOREIGN KEY (lances_id) REFERENCES mocks.LANCES (id);