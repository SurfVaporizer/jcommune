ALTER TABLE `PRIVATE_MESSAGE` ADD `READ_FLAG` bool DEFAULT FALSE NOT NULL;
TRUNCATE PRIVATE_MESSAGE;