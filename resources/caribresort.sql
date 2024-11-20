-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 01, 2024 at 05:32 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `caribresort`
--

-- --------------------------------------------------------

--
-- Table structure for table `drinks`
--

CREATE TABLE `drinks` (
  `drinkID` varchar(10) NOT NULL,
  `drinkName` varchar(30) NOT NULL,
  `unitPrice` double NOT NULL,
  `isAlcoholic` tinyint(1) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `drinks`
--

INSERT INTO `drinks` (`drinkID`, `drinkName`, `unitPrice`, `isAlcoholic`, `quantity`) VALUES
('CC1003N', 'Coco Cola', 6, 0, 15),
('MJ1004A', 'Mojito', 8, 1, 50),
('OJ1001N', 'Orange Juice', 5, 0, 25),
('PC1005A', 'Pina Colada', 10, 1, 40),
('PP1002N', 'Pepsi', 7, 0, 13),
('RP1006A', 'Rum Punch', 12, 1, 62),
('TS1007A', 'Tequila Sunrise', 11.5, 1, 34),
('WT1000N', 'Water', 6.5, 0, 80);

-- --------------------------------------------------------

--
-- Table structure for table `orderdetails`
--

CREATE TABLE `orderdetails` (
  `orderDetailsID` int(11) NOT NULL,
  `orderID` int(11) NOT NULL,
  `drinkID` varchar(10) NOT NULL,
  `quantity` int(11) NOT NULL,
  `totalCost` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orderdetails`
--

INSERT INTO `orderdetails` (`orderDetailsID`, `orderID`, `drinkID`, `quantity`, `totalCost`) VALUES
(1, 123, 'OJ1001N', 2, 10),
(2, 123, 'CC1003N', 3, 18),
(3, 123, 'RP1006A', 1, 12);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `orderID` int(11) NOT NULL,
  `orderDate` datetime NOT NULL,
  `status` varchar(15) NOT NULL DEFAULT 'isPrepare',
  `orderTotal` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='This Table will store all of the final data values relating to Orders';

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`orderID`, `orderDate`, `status`, `orderTotal`) VALUES
(123, '2024-10-25 02:25:00', 'isPrepare', 40);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `drinks`
--
ALTER TABLE `drinks`
  ADD PRIMARY KEY (`drinkID`);

--
-- Indexes for table `orderdetails`
--
ALTER TABLE `orderdetails`
  ADD PRIMARY KEY (`orderDetailsID`,`orderID`),
  ADD KEY `fk_DrinkID` (`drinkID`),
  ADD KEY `fk_OrderID` (`orderID`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`orderID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `orderdetails`
--
ALTER TABLE `orderdetails`
  ADD CONSTRAINT `fk_DrinkID` FOREIGN KEY (`drinkID`) REFERENCES `drinks` (`drinkID`),
  ADD CONSTRAINT `fk_OrderID` FOREIGN KEY (`orderID`) REFERENCES `orders` (`orderID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
