-- самый популярный тип датчика abc 160675

SELECT type, COUNT(*) AS count
FROM go_course_db.sensors
GROUP BY type
ORDER BY count DESC
LIMIT 1;