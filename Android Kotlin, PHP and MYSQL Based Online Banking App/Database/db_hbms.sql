-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 16, 2022 at 12:58 PM
-- Server version: 10.4.18-MariaDB
-- PHP Version: 8.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_hbms`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `Admin_Name` varchar(100) NOT NULL,
  `Admin_Email` varchar(100) NOT NULL,
  `Admin_Password` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `franchise_manager`
--

CREATE TABLE `franchise_manager` (
  `Franchise_Manager_Name` varchar(100) NOT NULL,
  `Franchise_Manager_Email` varchar(100) NOT NULL,
  `Franchise_Manager_Password` varchar(50) NOT NULL,
  `Franchise_Manager_PhoneNo` varchar(100) NOT NULL,
  `Franchise_Manager_Register_Date` datetime DEFAULT current_timestamp(),
  `Franchise_Manager_Balance` double DEFAULT NULL,
  `Franchise_Manager_WithDraw` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `request`
--

CREATE TABLE `request` (
  `Request_Id` int(11) NOT NULL,
  `Request_Message` varchar(255) NOT NULL,
  `Request_Amount` double NOT NULL,
  `Respond_Message` varchar(255) NOT NULL,
  `Respond_Amount` double NOT NULL,
  `Request_Date` datetime DEFAULT current_timestamp(),
  `Requester_Email` varchar(100) DEFAULT NULL,
  `Responder_Email` varchar(100) DEFAULT NULL,
  `Request_Status` tinyint(1) DEFAULT 0,
  `Respond_Date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE `transaction` (
  `Transaction_Id` int(11) NOT NULL,
  `Transaction_Amount` double NOT NULL,
  `Transaction_Date` datetime DEFAULT current_timestamp(),
  `Transaction_Purpose` varchar(250) DEFAULT NULL,
  `Sender_Email` varchar(100) NOT NULL,
  `Receiver_Email` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `User_Name` varchar(100) NOT NULL,
  `User_Email` varchar(100) NOT NULL,
  `User_Password` varchar(50) NOT NULL,
  `User_PhoneNo` varchar(50) NOT NULL,
  `User_Balance` double DEFAULT NULL,
  `User_Register_Date` datetime DEFAULT current_timestamp(),
  `User_OTP` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`Admin_Email`);

--
-- Indexes for table `franchise_manager`
--
ALTER TABLE `franchise_manager`
  ADD PRIMARY KEY (`Franchise_Manager_Email`);

--
-- Indexes for table `request`
--
ALTER TABLE `request`
  ADD PRIMARY KEY (`Request_Id`),
  ADD KEY `Requester_Email` (`Requester_Email`),
  ADD KEY `Responder_Email` (`Responder_Email`);

--
-- Indexes for table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`Transaction_Id`),
  ADD KEY `Sender_Email` (`Sender_Email`),
  ADD KEY `Receiver_Email` (`Receiver_Email`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`User_Email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `request`
--
ALTER TABLE `request`
  MODIFY `Request_Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `transaction`
--
ALTER TABLE `transaction`
  MODIFY `Transaction_Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=114;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `request`
--
ALTER TABLE `request`
  ADD CONSTRAINT `request_ibfk_1` FOREIGN KEY (`Requester_Email`) REFERENCES `user` (`User_Email`),
  ADD CONSTRAINT `request_ibfk_2` FOREIGN KEY (`Responder_Email`) REFERENCES `user` (`User_Email`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
