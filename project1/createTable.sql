create table if not exists cards(
    code char(9) not null primary key ,
    money DECIMAL(10, 2),
    create_time timestamp not null
);

create table if not exists passengers(
    name varchar(100) not null ,
    id_number varchar(18) primary key not null ,
    phone_number varchar(11),
    gender varchar(10),
    district varchar(100)
);

create table if not exists stations (
    id serial primary key,
    station_name varchar(100) not null unique,
    district varchar(100) not null,
    intro text,
    chinese_name varchar(100)
);

create table if not exists rides(
    id serial primary key,
    user_ride1_id varchar(18) references passengers(id_number),
    start_station varchar(100) references stations(station_name),
    end_station varchar(100) references stations(station_name),
    price integer,
    start_time timestamp not null ,
    end_time timestamp not null,
    unique (start_time, end_time, user_ride1_id)
);

create table if not exists rides2(
    id serial primary key ,
    user_ride2_id varchar(9) references cards(code),
    start_station varchar(100) references stations(station_name),
    end_station varchar(100) references stations(station_name),
    price integer,
    start_time timestamp not null,
    end_time timestamp not null,
    unique (start_time, end_time, user_ride2_id)
);


create table if not exists lines (
    id serial primary key ,
    line_name varchar(100) not null unique,
    start_time time not null ,
    end_time time not null ,
    intro text,
    mileage decimal(10, 3),
    color varchar(50),
    first_opening date,
    url varchar(255)
);
create table if not exists connection (
    line_No Integer not null references lines(id),
    station_No Integer not null,
    station_n varchar(100) not null references stations(station_name),
    primary key (line_No, station_No)
);

create table if not exists outInfo (
    id serial primary key,
    out_info varchar(100) not null,
    outNo Integer not null,
    ttext varchar(10000) not null,
    station_id Integer not null references stations(id),
    unique (out_info, station_id)
);

create table if not exists busStation (
    id serial primary key,
    busStationName varchar(100) not null,
    chukou varchar(100) not null ,
    station_id Integer not null ,
    unique (chukou, station_id, busStationName),
    constraint fk_outInfo foreign key (chukou, station_id) references outInfo (out_info, station_id)
);

create table if not exists bus (
    id serial primary key,
    bus_n varchar(100) not null unique
);

create table if not exists connB (
     bus_id Integer not null references bus(id),
     Bstation_id Integer not null references busStation(id),
     primary key (Bstation_id, bus_id)
);