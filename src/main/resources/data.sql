DROP TABLE IF EXISTS diffstore;

CREATE TABLE diffstore (
    id INT AUTO_INCREMENT PRIMARY KEY,
    diffid INT NOT NULL,
    diffleft VARCHAR (250) NULL,
    diffright VARCHAR (250) NULL,
    result VARCHAR (250) NULL
)