/*
SQLyog Ultimate v12.4.1 (64 bit)
MySQL - 5.7.21-log : Database - lianjia_1
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `analysis_history` */

CREATE TABLE `analysis_history` (
  `district_id` bigint(20) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `avg_price` bigint(20) DEFAULT NULL,
  `deal_amount` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `analysis_second_hand` */

CREATE TABLE `analysis_second_hand` (
  `district_id` bigint(20) DEFAULT NULL,
  `update_date` date DEFAULT NULL,
  `sale_amount` bigint(20) DEFAULT NULL,
  `avg_price` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `biz` */

CREATE TABLE `biz` (
  `id` bigint(20) DEFAULT NULL,
  `city_id` bigint(20) DEFAULT NULL,
  `district_id` varchar(255) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `pin_yin` varchar(200) DEFAULT NULL,
  `communities_count` int(11) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `communities_updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `community` */

CREATE TABLE `community` (
  `id` bigint(20) NOT NULL,
  `city_id` bigint(20) DEFAULT NULL,
  `district_id` bigint(20) DEFAULT NULL,
  `biz_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `building_year` varchar(255) DEFAULT NULL,
  `building_type` varchar(255) DEFAULT NULL,
  `second_hand_quantity` int(11) DEFAULT NULL,
  `second_hand_unit_price` int(11) DEFAULT NULL,
  `detail` text,
  `updated_at` datetime DEFAULT NULL,
  `page_fetched_at` datetime DEFAULT NULL,
  `fetch_history_time` datetime DEFAULT NULL,
  `fetch_second_hand_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `district_id` (`district_id`),
  KEY `biz_id` (`biz_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `district` */

CREATE TABLE `district` (
  `id` bigint(20) NOT NULL,
  `city_id` bigint(20) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `pin_yin` varchar(100) DEFAULT NULL,
  `biz_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `history_data` */

CREATE TABLE `history_data` (
  `id` varchar(30) NOT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `district_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `deal_date` varchar(50) DEFAULT NULL,
  `total_price` varchar(50) DEFAULT NULL,
  `unit_price` varchar(20) DEFAULT NULL,
  `deal_records` varchar(300) DEFAULT NULL,
  `square` varchar(20) DEFAULT NULL,
  `layout_type` varchar(11) DEFAULT NULL,
  `floor` varchar(50) DEFAULT NULL,
  `direct` varchar(11) DEFAULT NULL,
  `has_lift` varchar(20) DEFAULT NULL,
  `build_year` varchar(20) DEFAULT NULL,
  `decoration` varchar(20) DEFAULT NULL,
  `heating_type` varchar(20) DEFAULT NULL,
  `building_type` varchar(20) DEFAULT NULL,
  `building_struct` varchar(20) DEFAULT NULL,
  `limit_year` varchar(20) DEFAULT NULL,
  `transact_type` varchar(20) DEFAULT NULL,
  `own_type` varchar(20) DEFAULT NULL,
  `house_type` varchar(20) DEFAULT NULL,
  `tax_info` varchar(20) DEFAULT NULL,
  `hang_price` varchar(20) DEFAULT NULL,
  `hang_time` varchar(20) DEFAULT NULL,
  `hang_duration` varchar(20) DEFAULT NULL,
  `update_price_count` varchar(20) DEFAULT NULL,
  `daikan_count` varchar(20) DEFAULT NULL,
  `forcus_count` varchar(20) DEFAULT NULL,
  `browse_count` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `community_id` (`community_id`),
  KEY `district_id` (`district_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `second_hand_data_02_28` */

CREATE TABLE `second_hand_data_02_28` (
  `id` varchar(30) NOT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `district_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `deal_date` varchar(50) DEFAULT NULL,
  `total_price` varchar(50) DEFAULT NULL,
  `unit_price` varchar(20) DEFAULT NULL,
  `deal_records` varchar(300) DEFAULT NULL,
  `square` varchar(20) DEFAULT NULL,
  `layout_type` varchar(11) DEFAULT NULL,
  `floor` varchar(50) DEFAULT NULL,
  `direct` varchar(11) DEFAULT NULL,
  `has_lift` varchar(20) DEFAULT NULL,
  `build_year` varchar(20) DEFAULT NULL,
  `decoration` varchar(20) DEFAULT NULL,
  `heating_type` varchar(20) DEFAULT NULL,
  `building_type` varchar(20) DEFAULT NULL,
  `building_struct` varchar(20) DEFAULT NULL,
  `limit_year` varchar(20) DEFAULT NULL,
  `transact_type` varchar(20) DEFAULT NULL,
  `own_type` varchar(20) DEFAULT NULL,
  `house_type` varchar(20) DEFAULT NULL,
  `tax_info` varchar(20) DEFAULT NULL,
  `location_detail` varchar(50) DEFAULT NULL,
  `hang_price` varchar(20) DEFAULT NULL,
  `hang_time` varchar(20) DEFAULT NULL,
  `hang_duration` varchar(20) DEFAULT NULL,
  `update_price_count` varchar(20) DEFAULT NULL,
  `daikan_count` varchar(100) DEFAULT NULL,
  `forcus_count` varchar(20) DEFAULT NULL,
  `browse_count` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `community_id` (`community_id`),
  KEY `district_id` (`district_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `second_hand_data_03_02` */

CREATE TABLE `second_hand_data_03_02` (
  `id` varchar(30) NOT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `district_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `deal_date` varchar(50) DEFAULT NULL,
  `total_price` varchar(50) DEFAULT NULL,
  `unit_price` varchar(20) DEFAULT NULL,
  `deal_records` varchar(300) DEFAULT NULL,
  `square` varchar(20) DEFAULT NULL,
  `layout_type` varchar(11) DEFAULT NULL,
  `floor` varchar(50) DEFAULT NULL,
  `direct` varchar(11) DEFAULT NULL,
  `has_lift` varchar(20) DEFAULT NULL,
  `build_year` varchar(20) DEFAULT NULL,
  `decoration` varchar(20) DEFAULT NULL,
  `heating_type` varchar(20) DEFAULT NULL,
  `building_type` varchar(20) DEFAULT NULL,
  `building_struct` varchar(20) DEFAULT NULL,
  `limit_year` varchar(20) DEFAULT NULL,
  `transact_type` varchar(20) DEFAULT NULL,
  `own_type` varchar(20) DEFAULT NULL,
  `house_type` varchar(20) DEFAULT NULL,
  `tax_info` varchar(20) DEFAULT NULL,
  `location_detail` varchar(50) DEFAULT NULL,
  `hang_price` varchar(20) DEFAULT NULL,
  `hang_time` varchar(20) DEFAULT NULL,
  `hang_duration` varchar(20) DEFAULT NULL,
  `update_price_count` varchar(20) DEFAULT NULL,
  `daikan_count` varchar(100) DEFAULT NULL,
  `forcus_count` varchar(20) DEFAULT NULL,
  `browse_count` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
