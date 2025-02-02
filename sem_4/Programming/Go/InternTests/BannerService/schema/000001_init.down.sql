-- Удаление триггера
DROP TRIGGER IF EXISTS update_banners_updated_at ON banners;

-- Удаление функции
DROP FUNCTION IF EXISTS update_updated_at_column();

-- Удаление таблицы
DROP TABLE IF EXISTS banners;