CREATE TABLE IF NOT EXISTS banners (
                         banner_id SERIAL PRIMARY KEY,
                         Is_Active BOOLEAN NOT NULL,
                         Feature_ID INTEGER NOT NULL,
                         Tag_IDs INTEGER[] NOT NULL,
                         Content JSONB NOT NULL,
                         Created_At TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         Updated_At TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Создание функции для автоматического обновления поля Updated_At
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW."updated_at" = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language 'plpgsql';

-- Создание триггера, который вызывает функцию update_updated_at_column при каждом обновлении записи
CREATE TRIGGER update_banners_updated_at BEFORE UPDATE
    ON Banners FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();