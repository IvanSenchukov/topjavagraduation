DELETE FROM votes;
DELETE FROM dishes;
DELETE FROM restaurants;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;



INSERT INTO users (name, email, password) VALUES
  ('Admin', 'admin@gmail.com', 'admin'),
  ('First User', 'firstuser@yandex.ru', 'password'),
  ('Second User', 'seconduser@yandex.ru', 'password');


INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_ADMIN', 100000),
  ('ROLE_USER', 100001),
  ('ROLE_USER', 100002);


INSERT INTO restaurants (name) VALUES
  ('McDonnel''s'),
  ('Vabi-Vobble');


INSERT INTO dishes (name, price, date, restaurant_id) VALUES
  ('McDonnel''s Burger',  200, '2019-05-10', 100003),
  ('McDonnel''s Fries',   150, '2019-05-10', 100003),
  ('Vabi-Vobble Sushi',   300, '2019-05-10', 100004),
  ('Vabi-Vobble Sashimi', 200, '2019-05-10', 100004);


INSERT INTO votes (user_id, restaurant_id, date_time)
VALUES (100001, 100003, '2019-05-10 08:15:00'),
       (100002, 100004, '2019-05-10 09:45:00');
