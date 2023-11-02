# ecommerce-backend

 -- Scripts utilizados para criar o banco:
 
CREATE DATABASE predize;

USE predize;

CREATE TABLE produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL,
    quantidade INT NOT NULL,
    foto VARCHAR(255)
);

INSERT INTO produtos (nome, preco, quantidade, foto) VALUES
('Produto Teste A', 10.99, 100, 'produto_teste_a.jpg'),
('Produto Teste B', 19.99, 50, 'produto_teste_b.jpg');

CREATE TABLE carrinho_de_compras (
    id INT AUTO_INCREMENT PRIMARY KEY,
	data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE carrinho_produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    carrinho_id INT,
    produto_id INT,
    quantidade_produto_adicionada INT
);


-- scripts para consulta:
SELECT * FROM produtos;
SELECT * FROM carrinho_de_compras;
SELECT * FROM carrinho_produtos;

select * FROM carrinho_produtos cp
inner join produtos p on cp.produto_id = p.id;
