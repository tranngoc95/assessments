drop database if exists solar_panel_test;
create database solar_panel_test;
use solar_panel_test;

create table solar_panel(
	solar_id int primary key auto_increment,
    section varchar(100) not null,
    panel_row int not null,
    panel_col int not null,
    year_installed int not null,
    material varchar(45) not null,
    is_tracking boolean not null,
    constraint uq_panel_section_row_col
		unique (section, panel_row, panel_col)
);

delimiter //
create procedure set_known_good_state()
begin
    truncate table solar_panel;

    -- Add test data.
    insert into solar_panel
        (section, panel_row, panel_col, year_installed, material, is_tracking)
    values
        ('Sunflower',1,1,2014,'CIGS',true),
        ('Rose',2,1,2018,'GIGS',true),
        ('Jasmine',3,3,2015,'POLYSI',false);
end //
-- 4. Change the statement terminator back to the original.
delimiter ;