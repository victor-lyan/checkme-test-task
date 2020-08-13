insert into	clinics (name, phone, address) values 
('Clinic 1', '11111', 'address 1'),
('Clinic 2', '22222', 'address 2'),
('Clinic 3', '33333', 'address 3'),
('Clinic 4', '44444', 'address 4'),
('Clinic 5', '55555', 'address 5');

insert into examinations (name, average_duration_minutes, is_dangerous) values
('Examination 1', 10, false),
('Examination 2', 20, true),
('Examination 3', 30, false),
('Examination 4', 40, false),
('Examination 5', 45, true);

insert into clinicexaminations (price, clinic, examination) values
(100, 1, 1),
(120, 1, 3),
(130, 1, 5),
(210, 2, 1),
(250, 2, 4),
(350, 3, 3),
(350, 3, 5),
(50, 4, 2),
(500, 4, 4),
(1000, 4, 1),
(333, 4, 3),
(278, 4, 5);