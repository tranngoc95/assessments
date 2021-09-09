use tiny_theaters;

-- 	   Find all performances in the last quarter of 2021 (Oct. 1, 2021 - Dec. 31 2021).
select distinct 
t.ticket_date performance_date, s.show_name, th.theater_name
from ticket t 
inner join performance p on p.performance_id=t.performance_id
inner join shows s on s.show_id=p.show_id
inner join theater th on th.theater_id=p.theater_id
where t.ticket_date between '2021-10-01' and '2021-12-31';

--     List customers without duplication.
select * from customer
order by last_name, first_name;

--     Find all customers without a .com email address.
select * from customer c
inner join information i on c.info_id=i.info_id
where i.email not like '%.com';

--     Find the three cheapest shows.
select s.show_name, min(pr.price) from shows s
inner join performance pe on pe.show_id=s.show_id
inner join price pr on pr.performance_id=pe.performance_id
		and pr.end_date is null
group by s.show_name
order by pr.price
limit 3;

--     List customers and the show they're attending with no duplication.
select distinct 
c.customer_id,
concat(c.first_name, " ", c.last_name) customer_name,
s.show_name 
from customer c 
inner join reservation r on r.customer_id=c.customer_id
inner join ticket t on t.reservation_id=r.reservation_id
inner join performance p on p.performance_id=t.performance_id
inner join shows s on s.show_id=p.show_id;

--     List customer, show, theater, and seat number in one query.
select 
c.customer_id,
concat(c.first_name, " ", c.last_name) customer_name,
sh.show_name, 
th.theater_name,
concat(s.seat_row, s.seat_col) seat_number
from customer c 
inner join reservation r on r.customer_id=c.customer_id
inner join ticket t on t.reservation_id=r.reservation_id
inner join performance p on p.performance_id=t.performance_id
inner join shows sh on sh.show_id=p.show_id
inner join theater th on th.theater_id=p.theater_id
inner join seat s on s.seat_id=t.seat_id;

--     Find customers without an address.
select * from customer c
inner join information i on c.info_id=i.info_id
where i.address = "" or i.address is null;

--     Recreate the spreadsheet data with a single query.
select 
c.first_name customer_first,
c.last_name customer_last,
ic.email customer_email,
ic.phone customer_phone,
ic.address customer_address,
concat(s.seat_row,s.seat_col) seat,
sh.show_name `show`,
pr.price ticket_price,
t.ticket_date `date`,
th.theater_name theater,
it.address theater_address,
it.phone theater_phone,
it.email theater_email
from customer c 
inner join information ic on c.info_id=ic.info_id
inner join reservation r on c.customer_id=r.customer_id
inner join ticket t on t.reservation_id=r.reservation_id
inner join seat s on s.seat_id=t.seat_id
inner join performance pe on pe.performance_id=t.performance_id
inner join shows sh on sh.show_id=pe.show_id
inner join theater th on th.theater_id=pe.theater_id
inner join information it on th.info_id=it.info_id
inner join price pr on pr.performance_id=pe.performance_id
	and ((t.ticket_date between pr.start_date and pr.end_date) 
    or (t.ticket_date > pr.start_date and pr.end_date is null));

--     Count total tickets purchased per customer.
select 
c.customer_id, 
concat(c.first_name, c.last_name) customer_name,
count(*) ticket_count
from customer c 
inner join reservation r on r.customer_id=c.customer_id
inner join ticket t on t.reservation_id=r.reservation_id
group by c.customer_id;

--     Calculate the total revenue per show based on tickets sold.
select
sh.show_name, 
sum(pr.price) revenue_per_show
from ticket t
inner join performance p on p.performance_id=t.performance_id
inner join shows sh on sh.show_id=p.show_id
inner join price pr on pr.performance_id=p.performance_id
	and 
    ((t.ticket_date between pr.start_date and pr.end_date) or
    (t.ticket_date > pr.start_date and pr.end_date is null))
group by sh.show_name;

--     Calculate the total revenue per theater based on tickets sold.
select 
th.theater_name,
sum(pr.price) revenue_per_theater
from ticket t
inner join performance p on p.performance_id=t.performance_id
inner join theater th on th.theater_id=p.theater_id
inner join price pr on pr.performance_id=p.performance_id
	and 
    ((t.ticket_date between pr.start_date and pr.end_date) or
    (t.ticket_date > pr.start_date and pr.end_date is null))
group by th.theater_name;

--     Who is the biggest supporter of RCTTC? Who spent the most in 2021?
select 
c.customer_id,
concat(c.first_name, " ", c.last_name) customer_name,
sum(pr.price) total_spent
from customer c
inner join reservation r on r.customer_id=c.customer_id
inner join ticket t on t.reservation_id=r.reservation_id
	and year(t.ticket_date)=2021
inner join performance p on p.performance_id=t.performance_id
inner join price pr on pr.performance_id=p.performance_id
	and 
    ((t.ticket_date between pr.start_date and pr.end_date) or
    (t.ticket_date > pr.start_date and pr.end_date is null))
group by c.customer_id
order by total_spent desc
limit 1;

