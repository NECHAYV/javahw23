-- Ограничение: возраст студента не может быть меньше 16 лет
ALTER TABLE student ADD CONSTRAINT check_age CHECK (age >= 16);

-- Ограничение: имена студентов должны быть уникальными и не равны нулю
ALTER TABLE student ALTER COLUMN name SET NOT NULL;
ALTER TABLE student ADD CONSTRAINT unique_student_name UNIQUE (name);

-- Ограничение: пара "название" - "цвет" факультета уникальна
ALTER TABLE faculty ADD CONSTRAINT unique_faculty_name_color UNIQUE (name, color);

-- При создании студента без указания возраста автоматически присваивается 20 лет
ALTER TABLE student ALTER COLUMN age SET DEFAULT 20;