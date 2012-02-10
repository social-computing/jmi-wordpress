drop table if exists SiteDaily;
drop table if exists SiteInfo;
create table SiteDaily (url varchar(255) not null, count integer not null, day
datetime, primary key (url));
create table SiteInfo (url varchar(255) not null, created datetime, latesturl
varchar(255), quota integer not null, updated datetime, primary key (url));
create index countIndex on SiteDaily (count);
