DELETE FROM votes;
DELETE FROM dishes;
DELETE FROM restaurants;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;



INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');


INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);


INSERT INTO restaurants (name) VALUES
  ('Vabi-Vobble'),
  ('McDonnel''s');


-- INSERT INTO dishes (name, price, date, restaurant_id) VALUES
--   ('McDonnel''s Burger',  200, '2019-05-10', 100000),/*todo - check restaurant_id*/
--   ('McDonnel''s Fries',   150, '2019-05-10', 100000),/*todo - check restaurant_id*/
--   ('Vabi-Vobble Sushi',   300, '2019-05-10', 100001),/*todo - check restaurant_id*/
--   ('Vabi-Vobble Sashimi', 200, '2019-05-10', 100001);/*todo - check restaurant_id*/
--
--
-- INSERT INTO votes (user_id, restaurant_id, dateTime)
-- VALUES (100000, 100000, '2019-05-10 08:15:00'),/*todo - check restaurant_id*/
--        (100000, 100000, '2019-05-10 09:45:00');/*todo - check restaurant_id*/
