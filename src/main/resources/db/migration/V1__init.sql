CREATE TABLE "user" (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(30),
    created_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES role(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE course (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_by_id BIGINT REFERENCES "user"(id),
    published_at TIMESTAMP
);

CREATE TABLE user_courses (
    user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    course_id BIGINT NOT NULL REFERENCES course(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, course_id)
);

-- Create lesson WITHOUT exam_id FK first
CREATE TABLE lesson (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    video_link VARCHAR(255),
    course_id BIGINT NOT NULL REFERENCES course(id),
    exam_id BIGINT,
    teacher_id BIGINT REFERENCES "user"(id)
);

CREATE TABLE lesson_result (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES "user"(id) ON DELETE CASCADE,
    lesson_id BIGINT REFERENCES lesson(id) ON DELETE CASCADE,
    score INTEGER,
    comment TEXT,
    status VARCHAR(50),
    completed_at TIMESTAMP,
    updated_at TIMESTAMP,
    uploaded_file VARCHAR(255)
);

CREATE TABLE exam (
    id BIGSERIAL PRIMARY KEY,
    lesson_id BIGINT UNIQUE,
    created_by_id BIGINT REFERENCES "user"(id),
    duration_minutes INTEGER,
    location VARCHAR(255),
    timeslot TIMESTAMP
);

CREATE TABLE question (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(255),
    exam_id BIGINT,
    type VARCHAR(32),
    CONSTRAINT fk_exam
        FOREIGN KEY (exam_id)
            REFERENCES exam(id)
            ON DELETE CASCADE
);

CREATE TABLE answer (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(255),
    correct BOOLEAN NOT NULL,
    question_id BIGINT,
    CONSTRAINT fk_question
        FOREIGN KEY(question_id)
            REFERENCES question(id)
            ON DELETE CASCADE
);

CREATE TABLE exam_attempt (
    id BIGSERIAL PRIMARY KEY,
    exam_id BIGINT,
    user_id BIGINT,
    attempt_number INTEGER,
    score INTEGER,
    completed BOOLEAN NOT NULL,
    CONSTRAINT fk_exam
        FOREIGN KEY (exam_id)
            REFERENCES exam(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES "user"(id)
            ON DELETE CASCADE
);

CREATE TABLE question_bank_question (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    owner_id BIGINT REFERENCES "user"(id)
);

CREATE TABLE question_bank_answer (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    correct BOOLEAN NOT NULL,
    question_id BIGINT NOT NULL REFERENCES question_bank_question(id) ON DELETE CASCADE
);

-- Now add the circular FKs for lesson <-> exam
ALTER TABLE lesson
    ADD CONSTRAINT fk_lesson_exam FOREIGN KEY (exam_id) REFERENCES exam(id);

ALTER TABLE exam
    ADD CONSTRAINT fk_exam_lesson FOREIGN KEY (lesson_id) REFERENCES lesson(id) ON DELETE SET NULL;