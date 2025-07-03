-- Insertar roles
INSERT INTO roles (id, name) VALUES 
(1, 'ROLE_USER'),
(2, 'ROLE_MODERATOR'),
(3, 'ROLE_ADMIN');

-- Insertar usuario administrador (contraseña: password)
INSERT INTO usuarios (username, email, password, nombre, apellido, enabled, created_at, updated_at) VALUES 
('admin', 'admin@example.com', '$2a$10$asMkKKLLiP7QxflO9hW5eO0sm0/VHC9Jp2nVN4kRlGNb.nFd/CCjy', 'Administrador', 'Sistema', 1, NOW(), NOW());

-- Asignar rol admin al usuario admin
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (1, 3);

-- Insertar categorías de prueba
INSERT INTO categorias (nombre, descripcion, activa) VALUES 
('Computadoras', 'Equipos de cómputo y accesorios', 1),
('Periféricos', 'Accesorios para computadoras', 1),
('Audio', 'Equipos de sonido y audio', 1),
('Monitores', 'Pantallas y monitores', 1);

-- Datos de prueba para productos con categoría
INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id) VALUES 
('Laptop HP', 'Laptop HP Pavilion 15.6 pulgadas, Intel Core i5', 899.99, 10, 1),
('Mouse Logitech', 'Mouse inalámbrico Logitech MX Master 3', 99.99, 25, 2),
('Teclado Mecánico', 'Teclado mecánico RGB con switches azules', 149.99, 15, 2),
('Monitor Samsung', 'Monitor Samsung 24 pulgadas Full HD', 299.99, 8, 4),
('Auriculares Sony', 'Auriculares inalámbricos Sony WH-1000XM4', 349.99, 12, 3);
