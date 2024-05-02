-- Создание таблицы users
DROP TYPE IF EXISTS user_role;
DROP TABLE IF EXISTS users;
CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');
CREATE TABLE users (
                      id SERIAL PRIMARY KEY,
                      email VARCHAR(255) UNIQUE NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      role user_role NOT NULL,
                      firstName VARCHAR(255),
                      lastName VARCHAR(255),
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE users ADD CONSTRAINT unique_login UNIQUE (email);
INSERT INTO users VALUES (0, 'admin@gmail.com','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','ADMIN','Admin', 'Adminov', now(), now());
-- Создание таблицы address_book
DROP TYPE IF EXISTS address_book_state;
DROP TABLE IF EXISTS address_book;
CREATE TYPE address_book_state AS ENUM ('ACTIVE', 'INACTIVE');
CREATE TABLE address_book (
                              id SERIAL PRIMARY KEY,
                              name VARCHAR(255) NOT NULL,
                              state address_book_state NOT NULL ,
                              description TEXT,
                              count_of_contacts INTEGER,
                              creator_id INTEGER REFERENCES users(id),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              additional_info TEXT
);

-- Создание таблицы contact
DROP TABLE IF EXISTS contact;
CREATE TABLE contact (
                         id SERIAL PRIMARY KEY,
                         firstName VARCHAR(255),
                         lastName VARCHAR(255),
    --TODO: gender
                         birthday DATE,
                         isForeigner BOOLEAN,
                         book_id INTEGER REFERENCES address_book(id),
                         additional_info TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы contact_information
CREATE TYPE contact_info_type AS ENUM ('EMAIL', 'TELEGRAM', 'VK', 'WHATSAPP', 'DISCORD');
DROP TABLE IF EXISTS contact_information;
CREATE TABLE contact_information (
                                     id SERIAL PRIMARY KEY,
                                     contact_id INTEGER REFERENCES contact(id),
                                     type contact_info_type NOT NULL,
                                     value VARCHAR(255) NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы message_template
DROP TABLE IF EXISTS message_template;
CREATE TABLE message_template (
                                  id SERIAL PRIMARY KEY,
                                  name VARCHAR(255) NOT NULL,
                                  subject VARCHAR(255) NOT NULL,
                                  body TEXT NOT NULL,
                                  creator_id INTEGER REFERENCES users(id),
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  additional_info TEXT
);

-- Создание таблицы message
DROP TYPE IF EXISTS message_state;
DROP TABLE IF EXISTS message;
CREATE TYPE message_state AS ENUM ('CREATED', 'SENT', 'RECEIVED');
CREATE TABLE message (
                         id SERIAL PRIMARY KEY,
                         subject VARCHAR(255) NOT NULL,
                         template_id INTEGER REFERENCES message_template(id),
                         body TEXT NOT NULL,
                         creator_id INTEGER REFERENCES users(ID),
                         address_book_id INTEGER REFERENCES address_book(id),
                         messenger VARCHAR(20),
                         state message_state NOT NULL ,
                         createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         sentAt TIMESTAMP
);

-- Создание таблицы attachment
DROP TABLE IF EXISTS attachment;
CREATE TABLE attachment (
                            id SERIAL PRIMARY KEY,
                            message_id INTEGER REFERENCES message(id),
                            file_id INTEGER REFERENCES files(id),
                            CONSTRAINT fk_message_id FOREIGN KEY (message_id) REFERENCES message(id),
                            CONSTRAINT fk_file_id FOREIGN KEY (file_id) REFERENCES files(id)
);

-- Создание таблицы files
DROP TABLE IF EXISTS files;
CREATE TABLE files (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      type VARCHAR(50),
                      creator_id INTEGER REFERENCES users(ID),
                      upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      CONSTRAINT fk_creator_id FOREIGN KEY (creator_id) REFERENCES users(id)
);
ALTER TABLE attachment ADD CONSTRAINT fk_message_id FOREIGN KEY (message_id) REFERENCES message(id) ON DELETE RESTRICT;

-- Создание таблицы telegram_data В ДРУГОЙ БАЗЕ ДАННЫХ
DROP TABLE IF EXISTS telegram_data;
CREATE TABLE IF NOT EXISTS telegram_data (
                                             id SERIAL PRIMARY KEY,
                                             user_id BIGINT NOT NULL ,
                                             username VARCHAR(255) NOT NULL
);