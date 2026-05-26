-- 1. Все студенты (имя, возраст) с названиями факультетов
SELECT s.name, s.age, f.name AS faculty_name
FROM student s
         JOIN faculty f ON s.faculty_id = f.id;

-- 2. Студенты, у которых есть аватарки
SELECT s.name, s.age
FROM student s
         JOIN avatar a ON a.student_id = s.id;