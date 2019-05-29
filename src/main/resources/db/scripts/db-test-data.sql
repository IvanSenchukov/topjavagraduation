DELETE FROM votes;
DELETE FROM dishes;
DELETE FROM restaurants;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;



INSERT INTO users (name, email, password) VALUES
  ('Admin', 'admin@example.com', 'admin'),
  ('First_User', 'firstuser@example.com', 'password'),
  ('Second_User', 'seconduser@example.com', 'password');


INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_ADMIN', 100000),
  ('ROLE_USER', 100001),
  ('ROLE_USER', 100002);


INSERT INTO restaurants (name) VALUES
  ('McDonnel''s'),
  ('Vabi-Vobble');


INSERT INTO dishes (name, price, date, restaurant_id) VALUES
  ('McDonnel''s Burger',  200, '2019-05-10 00:00:00', 100003),
  ('McDonnel''s Fries',   150, '2019-05-10 00:00:00', 100003),
  ('Vabi-Vobble Sushi',   300, '2019-05-10 00:00:00', 100004),
  ('Vabi-Vobble Sashimi', 200, '2019-05-10 00:00:00', 100004);


INSERT INTO votes (user_id, restaurant_id, date) VALUES
       (100001, 100003, '2019-05-10 00:00:00'),
       (100001, 100004, '2019-05-09 00:00:00'),
       (100002, 100004, '2019-05-10 00:00:00');
