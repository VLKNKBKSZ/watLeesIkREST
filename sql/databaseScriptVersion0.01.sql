#Als een admin een boek verwijderd, worden het boek ook verwijderd van elke boekenlijst.

ALTER TABLE `watleesik`.`profile_book` 
DROP FOREIGN KEY `FKafktj8w1u4wnie8wevasq1kpa`;
ALTER TABLE `watleesik`.`profile_book` 
ADD CONSTRAINT `FKafktj8w1u4wnie8wevasq1kpa`
  FOREIGN KEY (`book_id`)
  REFERENCES `watleesik`.`book` (`id`)
  ON DELETE CASCADE;
  
