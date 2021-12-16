--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS categoria;
CREATE TABLE categoria (
  id int NOT NULL AUTO_INCREMENT,
  nombre varchar(40) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Table structure for table `articulo`
--

DROP TABLE IF EXISTS articulo;
CREATE TABLE articulo (
  codigo bigint NOT NULL,
  nombre varchar(50) NOT NULL,
  descripcion text NOT NULL,
  categoria_id int NOT NULL,
  cantidad int NOT NULL,
  PRIMARY KEY (codigo),
  KEY articulo_categoria_FK (categoria_id),
  CONSTRAINT articulo_categoria_FK FOREIGN KEY (categoria_id) REFERENCES categoria (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




INSERT INTO articulo VALUES (7746685421345,'PAPAS','Picosas',1,5);
INSERT INTO articulo VALUES (7854155744151,'Mascarilla','Mascarilla de yogurt',1,2);



--
-- Dumping data for table `categoria`
--

INSERT INTO categoria VALUES (1,'Picosas');

--
-- Dumping routines for database 'inventarioqr'
--
 
DELIMITER ;;
CREATE DEFINER=root@localhost PROCEDURE eliminar_articulo(
p_codigo BIGINT,
OUT p_error INT)
BEGIN 
DECLARE v_errorCodigo INT;
SELECT COUNT(*) INTO  v_errorCodigo FROM articulo WHERE codigo = p_codigo;
IF v_errorCodigo = 1 THEN
DELETE FROM articulo WHERE  codigo = p_codigo;
SET p_error = 1;
ELSE
SET p_error = 0;
END IF;
END ;;
DELIMITER ;
 
DELIMITER ;;
CREATE DEFINER=root@localhost PROCEDURE insertar_articulo(
	p_codigo varchar(13),
	p_nombre varchar(50),
    p_descripcion text,
    p_categoria int,
    p_cantidad int,
    out p_errorArticulo int
)
BEGIN
DECLARE verificarCodigo INT;
SELECT COUNT(*) INTO verificarCodigo FROM articulo WHERE codigo = p_codigo; 
IF verificarCodigo = 0 THEN 
INSERT INTO articulo VALUES (p_codigo, p_nombre, p_descripcion, p_categoria, p_cantidad);
SET p_errorArticulo = 0;
ELSE 
SET p_errorArticulo = 1;
END IF;
END ;;
DELIMITER ;
 
DELIMITER ;;
CREATE DEFINER=root@localhost PROCEDURE pdo_consulta(
Out p_count INT)
BEGIN 
SET p_count = 0;
SELECT COUNT(*) INTO p_count FROM articulo;
SELECT A.codigo, A.nombre AS nombreArticulo, A.descripcion, C.nombre AS nombreCategoria, A.cantidad  FROM articulo A INNER JOIN categoria C ON A.categoria_id = C.id;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=root@localhost PROCEDURE pdo_consultaCategoria(
OUT p_count INT)
BEGIN 
SET p_count = 0;
SELECT COUNT(*) INTO p_count FROM categoria;
SELECT id, nombre FROM categoria;
END ;;
DELIMITER ;
 
DELIMITER ;;
CREATE DEFINER=root@localhost PROCEDURE pdo_consultaPorCategoria(
p_categoria INT,
OUT p_errorCategoria INT,
OUT p_count INT
)
BEGIN 
DECLARE verificarCategoria INT;
SET p_count = 0;
SELECT COUNT(*) INTO verificarCategoria FROM categoria WHERE id = p_categoria; 
IF verificarCategoria = 1 THEN 
SELECT COUNT(*) INTO p_count FROM articulo WHERE categoria_id = verificarCategoria;
SELECT  A.codigo, A.nombre AS nombreArticulo, A.descripcion, C.nombre AS nombreCategoria, A.cantidad  FROM articulo A INNER JOIN categoria C ON A.categoria_id = C.id WHERE categoria_id = verificarCategoria;
SET p_errorCategoria = 0;
ELSE 
SET p_errorCategoria = 1;
END IF;
END ;;
DELIMITER ;
 
DELIMITER ;;
CREATE DEFINER=root@localhost PROCEDURE pdo_consultaPorCodigo(
p_codigo VARCHAR(13),
OUT p_error INT   
)
BEGIN 
DECLARE verificarCodigo INT;
SELECT COUNT(*) INTO verificarCodigo FROM articulo WHERE codigo = p_codigo; 
IF verificarCodigo = 1 THEN 
SELECT  A.codigo, A.nombre AS nombreArticulo, A.descripcion, C.nombre AS nombreCategoria, A.cantidad  FROM articulo A INNER JOIN categoria C ON A.categoria_id = C.id WHERE codigo = p_codigo;
SET p_error = 0;
ELSE 
SET p_error = 1;
END IF;
END ;;
DELIMITER ;