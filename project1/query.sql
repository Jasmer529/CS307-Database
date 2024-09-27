--1
--each district
select district, count(*) from stations group by district;
--each line
select line_name, count(c.line_No) as station_count
from connection c
join lines l on c.line_No = l.id
group by l.line_name;
--2
select gender ,count(gender) from passengers group by gender;
--3
select district, count(district) from passengers group by district;
--4
select ttext from outInfo where station_id in(
    select id from stations where station_name = 'Guomao'
    ) and out_info = 'C出入口';

select distinct bus_n from bus where id in (
    select bus_id from connB where Bstation_id in (
            select id from busstation where chukou = 'C出入口'and
                   station_id in (select id from stations where station_name = 'Guomao')));
--5
select name, start_station, end_station, start_time, end_time from rides
    join passengers on user_ride1_id = passengers.id_number where user_ride1_id
            in(select id_number from passengers where name = '朱阳');

--6
select user_ride2_id, start_station, end_station, start_time, end_time from rides2
     where user_ride2_id in(select code from cards where code = '887046252');

--7
select station_name, chinese_name, district, count(out_info) ,line_name as outNum from outInfo
    join stations on station_id = stations.id
    join connection on stations.station_name = connection.station_n
    join lines l on connection.line_No = l.id
                       where station_id in
            (select id from stations where station_name = 'Tanglang') group by stations.id, line_name;

--8
select line_name, start_time, end_time, first_opening, intro, count(station_No) from lines
join connection on lines.id = connection.line_No
where line_name = '5号线' group by line_name, start_time, end_time, first_opening, intro;