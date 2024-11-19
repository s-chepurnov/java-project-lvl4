INSERT INTO url
  (name, created_at)
VALUES
  ('https://en.hexlet.io', '2022-01-01 13:57:40');

INSERT INTO url_check
  (status_code, title, h1, description, url_id, created_at)
VALUES
  (200, 'some title', 'some h1', 'some description', 1, '2022-01-01 13:57:40');
