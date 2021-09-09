drop database if exists tiny_theaters;
create database tiny_theaters;
use tiny_theaters ;

create table information (
	info_id int primary key auto_increment,
    phone varchar(15),
    email varchar(100) not null,
    address varchar(250),
    constraint uq_info_email 
		unique (email)
);

create table customer (
	customer_id int primary key auto_increment,
    first_name varchar(45) not null,
    last_name varchar(45) not null,
    info_id int not null,
    constraint fk_customer_info_id
		foreign key (info_id)
        references information(info_id),
	constraint uq_customer_info_id
		unique (info_id)
);

create table reservation (
	reservation_id int primary key auto_increment,
    customer_id int not null,
    constraint fk_reservation_customer_id
		foreign key (customer_id)
        references customer(customer_id)
);

create table theater (
	theater_id int primary key auto_increment,
    theater_name varchar(100) not null,
    num_row int,
    num_col int,
    info_id int not null,
    constraint fk_theater_info_id
		foreign key (info_id)
        references information(info_id),
	constraint uq_theater_name_info_id
		unique (theater_name, info_id)
);

create table seat (
	seat_id int primary key auto_increment,
    theater_id int not null,
    seat_row varchar(5) not null,
    seat_col int not null,
    constraint fk_seat_theater_id
		foreign key (theater_id)
        references theater(theater_id),
	constraint uq_seat_row_col_theater
		unique (theater_id, seat_row, seat_col),
	constraint chk_seat_row
		check( ascii(seat_row) - 64 > 0), 
	constraint chk_seat_col
		check(seat_col > 0)
);

DELIMITER $$
create trigger check_seat
before insert on seat
for each row
begin
	if 
    ((ascii(NEW.seat_row) - 64) >
    (select t.num_row from theater t
    where t.theater_id=NEW.theater_id))
    or
    (NEW.seat_col > 
    (select t.num_col from theater t 
    where t.theater_id=NEW.theater_id))
    then
		signal sqlstate '45000'
        set message_text = 'Warning: seat is out of theater size range';
	end if;
end $$
DELIMITER ;

create table shows (
	show_id int primary key auto_increment,
    show_name varchar(250) not null,
    constraint uq_show_name
		unique (show_name)
);

create table performance (
	performance_id int primary key auto_increment,
    show_id int not null,
    theater_id int not null,
    constraint fk_performance_show_id
		foreign key (show_id)
        references shows(show_id),
	constraint fk_performance_theater_id
		foreign key (theater_id)
        references theater(theater_id),
	constraint uq_performance
		unique (show_id, theater_id)
);

create table price (
	price_id int primary key auto_increment,
    price decimal(5,2) not null,
    start_date date not null,
    end_date date,
    performance_id int not null,
    constraint fk_price_performance_id
		foreign key (performance_id)
        references performance(performance_id),
	constraint uq_price
		unique (start_date, performance_id)
);

create table ticket (
	ticket_id int primary key auto_increment,
    ticket_date date not null,
    seat_id int not null,
    performance_id int not null,
    reservation_id int not null,
    constraint fk_ticket_seat_id
		foreign key (seat_id)
        references seat(seat_id),
	constraint fk_ticket_performance_id
		foreign key (performance_id)
        references performance(performance_id),
	constraint fk_ticket_reservation_id
		foreign key (reservation_id)
        references reservation(reservation_id),
	constraint uq_ticket
		unique (ticket_date, seat_id, performance_id)
);

DROP TABLE IF EXISTS `rcttc_data`;
CREATE TABLE `rcttc_data` (
  `customer_first` text,
  `customer_last` text,
  `customer_email` text,
  `customer_phone` text,
  `customer_address` text,
  `seat` text,
  `show` text,
  `ticket_price` double DEFAULT NULL,
  `date` text,
  `theater` text,
  `theater_address` text,
  `theater_phone` text,
  `theater_email` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


