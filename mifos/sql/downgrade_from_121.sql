alter table LOOKUP_VALUE modify column LOOKUP_ID INTEGER NOT NULL;

update DATABASE_VERSION set DATABASE_VERSION = 120 where DATABASE_VERSION = 121;
