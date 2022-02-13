CREATE TABLE pessoa (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL ,
    ativo BOOLEAN NOT NULL ,
    logradouro VARCHAR(30),
    numero VARCHAR(10),
    complemento VARCHAR(30),
    bairro VARCHAR(30),
    cep VARCHAR(15),
    cidade VARCHAR(50),
    estado VARCHAR(2)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;
