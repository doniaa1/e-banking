CREATE TABLE customer
(
  id                BIGINT AUTO_INCREMENT NOT NULL,
  login             VARCHAR(255) NOT NULL,
  full_name         VARCHAR(100) NOT NULL,
  mother_name       VARCHAR(100) NOT NULL,
  national_id       VARCHAR(12)  NOT NULL,
  date_of_birth     date         NOT NULL,
  gender            VARCHAR(255) NOT NULL,
  phone_number      VARCHAR(15)  NOT NULL,
  email             VARCHAR(255) NOT NULL,
  address_line_1    VARCHAR(200) NOT NULL,
  employment_status VARCHAR(255) NOT NULL,
  registration_date datetime     NOT NULL,
  last_update       datetime     NOT NULL,
  nationality       VARCHAR(255) NOT NULL,
  city              VARCHAR(255) NOT NULL,
  CONSTRAINT pk_customer PRIMARY KEY (id)
);

