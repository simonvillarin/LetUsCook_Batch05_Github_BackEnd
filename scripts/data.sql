DROP DATABASE IF EXISTS lms;
CREATE DATABASE lms;

\c lms;

DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS program;
DROP TABLE IF EXISTS subject;
DROP TABLE IF EXISTS curriculum;
DROP TABLE IF EXISTS grades;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS professor;
DROP TABLE IF EXISTS professor_load;
DROP TABLE IF EXISTS subject_detail_history;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS evaluation;
DROP TABLE IF EXISTS attendance_subject;
DROP TABLE IF EXISTS attendance_professor;
DROP TABLE IF EXISTS school_calendar;
DROP TABLE IF EXISTS student_history;
DROP TABLE IF EXISTS application;
DROP TABLE IF EXISTS image;

DROP SEQUENCE IF EXISTS student_seq;
DROP SEQUENCE IF EXISTS professor_seq;
DROP SEQUENCE IF EXISTS parent_seq;

CREATE SEQUENCE student_seq START WITH 20000;
CREATE SEQUENCE professor_seq START WITH 1000;
CREATE SEQUENCE parent_seq START WITH 120000;


CREATE TABLE school_calendar(
	calendar_id SERIAL PRIMARY KEY,
	start_enrollement date,
	end_enrollment date,
	start_class date,
	end_class date
);

CREATE TABLE admin (
    admin_id SERIAL PRIMARY KEY,
    firstname VARCHAR(80),
    middlename VARCHAR(80),
    lastname VARCHAR(80),
    suffix VARCHAR(10),
    gender VARCHAR(80),
    civil_status VARCHAR(20),
    birthdate DATE,
    birthplace VARCHAR(255),
    citizenship VARCHAR(80),
    religion VARCHAR(80),
    unit VARCHAR(255),
    street VARCHAR(255),
    subdivision VARCHAR(255),
    barangay VARCHAR(255),
    city VARCHAR(255),
    province VARCHAR(255),
    zipcode INT,
	telephone VARCHAR(15),
	mobile VARCHAR(15),
	email VARCHAR(80),
    image TEXT,
    banner_image TEXT
);

CREATE TABLE account (
    account_id SERIAL PRIMARY KEY,
    user_id INT,
    username VARCHAR(80) UNIQUE,
    password VARCHAR(80),
    pass VARCHAR(80),
    type VARCHAR(15),
    active_deactive boolean
);

CREATE TABLE program (
    program_id SERIAL PRIMARY KEY,
    program_code VARCHAR(10) UNIQUE,
    program_title VARCHAR(255) UNIQUE,
    first_year_first_sem_min NUMERIC(5, 2),
    first_year_first_sem_max NUMERIC(5, 2),
    first_year_second_sem_min NUMERIC(5, 2),
    first_year_second_sem_max NUMERIC(5, 2),
    second_year_first_sem_min NUMERIC(5, 2),
    second_year_first_sem_max NUMERIC(5, 2),
    second_year_second_sem_min NUMERIC(5, 2),
    second_year_second_sem_max NUMERIC(5, 2),
    third_year_first_sem_min NUMERIC(5, 2),
    third_year_first_sem_max NUMERIC(5, 2),
    third_year_second_sem_min NUMERIC(5, 2),
    third_year_second_sem_max NUMERIC(5, 2),
    fourth_year_first_sem_min NUMERIC(5, 2),
    fourth_year_first_sem_max NUMERIC(5, 2),
    fourth_year_second_sem_min NUMERIC(5, 2),
    fourth_year_second_sem_max NUMERIC(5, 2),
    majors INT[],
    minors INT[],
    electives INT[],
    status BOOLEAN
);

CREATE TABLE subject (
    subject_id SERIAL PRIMARY KEY,
    subject_code VARCHAR(10) UNIQUE,
    subject_title VARCHAR(255) UNIQUE,
    units NUMERIC(5, 2),
    pre_requisites INT[],
    type VARCHAR(40),
    active_deactive BOOLEAN
);

CREATE TABLE curriculum (
    curriculum_id SERIAL PRIMARY KEY,
    program_id INT,
    subject_id INT,
    sem VARCHAR(20),
    year_level VARCHAR(20),
    active_deactive BOOLEAN,
    FOREIGN KEY (program_id) REFERENCES program (program_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subject (subject_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE parent (
	parent_id INT DEFAULT nextval('parent_seq') PRIMARY KEY,
	parent_no VARCHAR(80) UNIQUE,
	firstname VARCHAR(80),
    middlename VARCHAR(80),
    lastname VARCHAR(80),
    suffix VARCHAR(10),
    address TEXT,
	contact VARCHAR(15),
	email VARCHAR(80),
	relationship VARCHAR(80),
	image TEXT,
	banner TEXT,
	active_deactive BOOLEAN
);

CREATE TABLE application (
    app_id INT DEFAULT nextval('student_seq') PRIMARY KEY,
    student_id INT,
    program_id INT,
    year_level VARCHAR(80),
    sem VARCHAR(80),
    academic_year VARCHAR(80),
    firstname VARCHAR(80),
    middlename VARCHAR(80),
    lastname VARCHAR(80),
    suffix VARCHAR(10),
    gender VARCHAR(80),
    civil_status VARCHAR(20),
    birthdate DATE,
    birthplace VARCHAR(255),
    citizenship VARCHAR(80),
    religion VARCHAR(80),
    unit VARCHAR(255),
    street VARCHAR(255),
    subdivision VARCHAR(255),
    barangay VARCHAR(255),
    city VARCHAR(255),
    province VARCHAR(255),
    zipcode INT,
	telephone VARCHAR(15),
	mobile VARCHAR(15),
	email VARCHAR(80),
	last_school_attended VARCHAR(255),
	program_taken VARCHAR(255),
	last_sem VARCHAR(80),
	last_year_level VARCHAR(80),
	last_school_year VARCHAR(80),
	date_of_graduation DATE,
	parent_firstname VARCHAR(80),
	parent_middlename VARCHAR(80),
	parent_lastname VARCHAR(80),
	parent_suffix VARCHAR(80),
	parent_address TEXT,
	parent_contact VARCHAR(15),
	parent_email VARCHAR(80),
	parent_relationship VARCHAR(80),
	application_date DATE,
	status VARCHAR(25),
    FOREIGN KEY (program_id) REFERENCES program(program_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE professor (
    professor_id INT DEFAULT nextval('professor_seq') PRIMARY KEY,
    professor_no VARCHAR(80) UNIQUE,
    firstname VARCHAR(80),
    middlename VARCHAR(80),
    lastname VARCHAR(80),
    suffix VARCHAR(10),
    fullname VARCHAR(255),
    gender VARCHAR(80),
    civil_status VARCHAR(20),
    birthdate DATE,
    birthplace VARCHAR(255),
    citizenship VARCHAR(80),
    religion VARCHAR(80),
    unit VARCHAR(255),
    street VARCHAR(255),
    subdivision VARCHAR(255),
    barangay VARCHAR(255),
    city VARCHAR(255),
    province VARCHAR(255),
    zipcode INT,
	telephone VARCHAR(15),
	mobile VARCHAR(15),
	email VARCHAR(80),
    work VARCHAR(80),
    status VARCHAR(20),
    image TEXT,
    banner TEXT,
    active_deactive BOOLEAN
);

CREATE TABLE schedule (
    sched_id SERIAL PRIMARY KEY,
    subject_id INT,
    day VARCHAR(20),
   	start_time time,
   	end_time time,
   	section varchar(15),
    room VARCHAR(15),
    professor_id INT,
    active_deactive BOOLEAN,
    FOREIGN KEY (professor_id) REFERENCES professor(professor_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subject (subject_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE student (
    student_id INT DEFAULT nextval('student_seq') PRIMARY KEY,
    student_no VARCHAR(80) UNIQUE,
    program_id INT,
    sched_id INT[],
    temp_sched_id INT[],
    year_level VARCHAR(80),
    sem VARCHAR(80),
    academic_year VARCHAR(80),
    firstname VARCHAR(80),
    middlename VARCHAR(80),
    lastname VARCHAR(80),
    suffix VARCHAR(10),
    gender VARCHAR(80),
    civil_status VARCHAR(20),
    birthdate DATE,
    birthplace VARCHAR(255),
    citizenship VARCHAR(80),
    religion VARCHAR(80),
    unit VARCHAR(255),
    street VARCHAR(255),
    subdivision VARCHAR(255),
    barangay VARCHAR(255),
    city VARCHAR(255),
    province VARCHAR(255),
    zipcode INT,
	telephone VARCHAR(15),
	mobile VARCHAR(15),
	email VARCHAR(80),
	last_school_attended VARCHAR(255),
	program_taken VARCHAR(255),
	last_sem VARCHAR(80),
	last_year_level VARCHAR(80),
	last_school_year VARCHAR(80),
	date_of_graduation DATE,
	parent_id INT,
	image TEXT,
	banner TEXT,
	date_enrolled DATE,
	app_id INT,
    active_deactive BOOLEAN,
    FOREIGN KEY (program_id) REFERENCES program(program_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES parent(parent_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (app_id) REFERENCES application(app_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE student_history(
	history_id SERIAL PRIMARY KEY,
	student_id INT,
	sched_id INT[],
	year_level VARCHAR(20),
	sem VARCHAR(20),
	academic_year VARCHAR(20),
	active_deactive boolean,
	FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE professor_load (
    load_id SERIAL PRIMARY KEY,
    professor_id INT,
	sched_id INT,
	FOREIGN KEY (professor_id) REFERENCES professor(professor_id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (sched_id) REFERENCES schedule(sched_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE grades (
    grade_id SERIAL PRIMARY KEY,
    student_id INT,
    professor_id INT,
    subject_id INT,
    prelim NUMERIC(5, 2),
    midterm NUMERIC(5, 2),
    finals NUMERIC(5, 2),
    sem VARCHAR(20),
    year_level VARCHAR(20),
    academic_year VARCHAR(20),
    comment TEXT,
    remarks VARCHAR(20),
    date_modified DATE,
    status BOOLEAN,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (professor_id) REFERENCES professor(professor_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE subject_detail_history (
    history_id serial PRIMARY KEY,
    student_id INT,
    professor_id INT,
    subject_id INT,
    year_level VARCHAR(20),
    sem VARCHAR(20),
    academic_year VARCHAR(20),
    section VARCHAR(20),
    sched_id INT,
    active_deactive boolean,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (professor_id) REFERENCES professor(professor_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (sched_id) REFERENCES schedule(sched_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE evaluation (
    eval_id SERIAL PRIMARY KEY,
    subject_id INT,
    student_id INT,
    answers TEXT[],
    FOREIGN KEY (subject_id) REFERENCES subject (subject_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE ON UPDATE CASCADE 
);

CREATE TABLE attendance_student (
    attendance_id SERIAL PRIMARY KEY,
    student_id INT,
    subject_id INT,
    sem VARCHAR(20),
    year_level VARCHAR(20),
    academic_year VARCHAR(20),
    active_deactive BOOLEAN,
    date DATE,
    time time,
    status VARCHAR(20),
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE attendance_professor (
    attendance_id SERIAL PRIMARY KEY,
    professor_id INT,
    date DATE,
    time time,
    status BOOLEAN,
    FOREIGN KEY (professor_id) REFERENCES professor(professor_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE room(
	room_id SERIAL PRIMARY KEY,
	room_number VARCHAR(20),
	room_capacity INT,
	num_of_students INT,
	active_deactive BOOLEAN
);

CREATE TABLE section(
	section_id SERIAL PRIMARY KEY,
	program_id INT,
	section_name varchar(80),
	section VARCHAR(80),
	active_deactive BOOLEAN
);

CREATE TABLE eval (
    eval_id SERIAL PRIMARY KEY,
    section_id INT,
    subject_id INT,
    active_deactive BOOLEAN,
    FOREIGN KEY (subject_id) REFERENCES subject (subject_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (section_id) REFERENCES section (section_id) ON DELETE CASCADE ON UPDATE CASCADE 
);

CREATE TABLE image (
    filename TEXT UNIQUE,
    mime_type VARCHAR(30),
    data bytea
);