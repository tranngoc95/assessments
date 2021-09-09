drop database if exists solar_panel_db;
create database solar_panel_db;
use solar_panel_db;

create table solar_panel(
	solar_id int primary key auto_increment,
    section varchar(100) not null,
    panel_row int not null,
    panel_col int not null,
    year_installed int not null,
    material varchar(45) not null,
    is_tracking bit not null,
    constraint uq_panel_section_row_col
		unique (section, panel_row, panel_col)
);

delimiter //
create procedure set_known_good_state()
begin
    truncate table solar_panel;

    -- Add test data.
    insert into solar_panel
        (solar_id, section, panel_row, panel_col, year_installed, material, is_tracking)
    values
        (1,'Sunflower',1,1,2014,'CIGS',true),
		(2,'Rose',2,1,2018,'GIGS',true),
		(3,'Jasmine',3,3,2015,'POLYSI',false),
		(4,'Jasmine',32,23,2011,'ASI',false),
		(5,'Rose',10,10,2020,'CDTE',false),
		(6,'Orchid',21,213,2012,'POLYSI',true);
end //
-- 4. Change the statement terminator back to the original.
delimiter ;

call set_known_good_state();