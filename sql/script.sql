SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `multinunja` DEFAULT CHARACTER SET utf8 ;
USE `multinunja` ;

-- -----------------------------------------------------
-- Table `multinunja`.`score`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `multinunja`.`score` ;

CREATE TABLE IF NOT EXISTS `multinunja`.`score` (
  `name` VARCHAR(45) NOT NULL,
  `score` INT NULL DEFAULT 0,
  CONSTRAINT `fk_score_1`
    FOREIGN KEY (`name`)
    REFERENCES `multinunja`.`user` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `multinunja`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `multinunja`.`user` ;

CREATE TABLE IF NOT EXISTS `multinunja`.`user` (
  `name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  PRIMARY KEY (`name`))
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
