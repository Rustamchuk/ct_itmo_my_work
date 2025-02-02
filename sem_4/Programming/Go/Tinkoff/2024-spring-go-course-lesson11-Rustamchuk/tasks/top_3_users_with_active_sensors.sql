-- топ 3 пользователей у которых больше всех активных датчиков Aiden, Elijah, Daniel

SELECT u.id, u.name, COUNT(s.id) AS active_sensors_count
FROM go_course_db.users u
JOIN go_course_db.sensors_users su ON u.id = su.user_id
JOIN go_course_db.sensors s ON su.sensor_id = s.id AND s.is_active = true
GROUP BY u.id, u.name
ORDER BY active_sensors_count DESC
LIMIT 3;