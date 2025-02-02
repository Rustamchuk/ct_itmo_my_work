-- Выведите максимальную температуру по дням для датчика c sensor_id=12 отсортированного по убыванию температуры
-- 100, 100, 99, 99, 99, 99, 97, 96, 95

CREATE INDEX idx_sensor_id ON go_course_db.events(sensor_id);
CREATE INDEX idx_timestamp ON go_course_db.events(timestamp);

SELECT day, MAX(max_temperature) AS max_temperature
FROM (
    SELECT DATE(timestamp) AS day, payload AS max_temperature
    FROM go_course_db.events
    WHERE sensor_id = 12
    ) subquery
GROUP BY day
ORDER BY max_temperature DESC;

/*
-- EXPLAIN ANALYZE для оптимизированного запроса

EXPLAIN ANALYZE
SELECT day, MAX(max_temperature) AS max_temperature
FROM (
  SELECT DATE(timestamp) AS day, payload AS max_temperature
  FROM go_course_db.events
  WHERE sensor_id = 12
) subquery
GROUP BY day
ORDER BY max_temperature DESC;

-- Результат - cost = 259-265
 */