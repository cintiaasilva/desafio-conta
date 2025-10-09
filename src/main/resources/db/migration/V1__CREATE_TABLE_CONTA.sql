CREATE TABLE tb_conta (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    valor_original DECIMAL(19, 2) NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE NOT NULL,
    valor_ajustado DECIMAL(19, 2) NOT NULL,
    dias_de_atraso INT NOT NULL
);