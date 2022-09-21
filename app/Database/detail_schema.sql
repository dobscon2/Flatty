CREATE TABLE Details (
	account_id integer,
	has_flat char not null check(has_flat in('Y', 'N')),
	degree varchar(100) not null,
	weekly_budget decimal(10,2) not null,
	extra_info varchar(500)
);