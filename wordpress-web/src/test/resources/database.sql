drop table if exists SiteDaily;
drop table if exists SiteInfo;
create table SiteDaily (id integer not null auto_increment, count integer not null, day datetime, url varchar(255), primary key (id));
create table SiteInfo (url varchar(255) not null, created datetime, latesturl varchar(255), quota integer not null, updated datetime, primary key (url));
create index dayIndex on SiteDaily (day);
create index countIndex on SiteDaily (count);
create index urlIndex on SiteDaily (url);