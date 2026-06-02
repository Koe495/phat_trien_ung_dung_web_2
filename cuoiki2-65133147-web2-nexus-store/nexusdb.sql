-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: nexus_db
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `color_id` int DEFAULT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_cart_users` (`user_id`),
  KEY `fk_cart_products` (`product_id`),
  KEY `fk_cart_colors` (`color_id`),
  CONSTRAINT `fk_cart_colors` FOREIGN KEY (`color_id`) REFERENCES `product_colors` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_cart_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cart_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
INSERT INTO `cart_items` VALUES (24,2,1,3,1);
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `slug` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `slug` (`slug`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Điện thoại thông minh','phones',NULL,'2026-06-01 08:50:07'),(2,'Thiết bị đeo & Sức khỏe','wearables',NULL,'2026-06-01 08:50:07'),(3,'Phụ kiện','accessories',NULL,'2026-06-01 08:50:07');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faqs`
--

DROP TABLE IF EXISTS `faqs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faqs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `topic_id` int NOT NULL,
  `question` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `answer` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_published` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_faqs_topics` (`topic_id`),
  CONSTRAINT `fk_faqs_topics` FOREIGN KEY (`topic_id`) REFERENCES `support_topics` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faqs`
--

LOCK TABLES `faqs` WRITE;
/*!40000 ALTER TABLE `faqs` DISABLE KEYS */;
INSERT INTO `faqs` VALUES (1,1,'Làm sao để chuyển dữ liệu từ điện thoại cũ sang NEXUS?','Bạn có thể sử dụng ứng dụng Nexus Switch được tích hợp sẵn khi khởi động máy lần đầu.',1),(2,2,'Chi phí thay pin chính hãng là bao nhiêu?','Chi phí thay pin chính hãng tùy thuộc vào dòng máy, dao động từ 1.500.000đ đến 2.500.000đ và được bảo hành 6 tháng.',1);
/*!40000 ALTER TABLE `faqs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `color_id` int DEFAULT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `price` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_items_orders` (`order_id`),
  KEY `fk_items_products` (`product_id`),
  KEY `fk_items_colors` (`color_id`),
  CONSTRAINT `fk_items_colors` FOREIGN KEY (`color_id`) REFERENCES `product_colors` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_items_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_items_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,1,2,NULL,1,34990000),(2,1,1,3,2,22990000),(3,2,1,1,1,22990000),(4,3,1,2,1,22990000),(5,4,2,NULL,1,34990000),(6,4,1,1,2,22990000),(7,5,2,NULL,1,34990000),(8,6,2,NULL,1,34990000),(9,7,12,NULL,1,6990000),(10,8,24,NULL,1,2190000),(11,8,1,2,1,22990000);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `total_amount` double DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shipping_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `payment_method` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `shipping_email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shipping_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shipping_phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_orders_users` (`user_id`),
  CONSTRAINT `fk_orders_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,2,80970000,'completed',', Binh Phu 2','saved_card','2026-06-02 02:28:09','admin@nexus.com','admin test','0987654321'),(2,2,22990000,'pending',', Binh Phu 2','saved_card','2026-06-02 02:28:30','admin@nexus.com','admin test','0987654321'),(3,2,22990000,'pending','Nha Trang city, Vietnam','cod','2026-06-02 02:29:00','admin@nexus.com','admin test','0987654321'),(4,2,80970000,'pending',', Binh Phu 2','saved_bank','2026-06-02 02:39:10','admin@nexus.com','admin test','0987654321'),(5,2,34990000,'pending','Nha Trang city, Vietnam','saved_bank','2026-06-02 02:41:59','admin@nexus.com','admin test','0987654321'),(6,2,34990000,'pending','Nha Trang city, Vietnam','saved_bank','2026-06-02 02:42:49','admin@nexus.com','admin test','0987654321'),(7,2,6990000,'pending','Nha Trang city, Vietnam','saved_bank','2026-06-02 06:16:17','admin@nexus.com','admin test','0987654321'),(8,2,25180000,'pending','Nha Trang city, Vietnam','saved_card','2026-06-02 06:18:01','admin@nexus.com','admin test','0987654321');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_banks`
--

DROP TABLE IF EXISTS `payment_banks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_banks` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `bank_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `bank_account` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `bank_owner` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `bank_branch` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_banks_users` (`user_id`),
  CONSTRAINT `fk_banks_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_banks`
--

LOCK TABLES `payment_banks` WRITE;
/*!40000 ALTER TABLE `payment_banks` DISABLE KEYS */;
INSERT INTO `payment_banks` VALUES (1,2,'ABC','123456789','Phan Nhat Tan','','2026-06-02 08:05:54');
/*!40000 ALTER TABLE `payment_banks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_cards`
--

DROP TABLE IF EXISTS `payment_cards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_cards` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `card_holder` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `card_number` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `card_expiry` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `card_network` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_cards_users` (`user_id`),
  CONSTRAINT `fk_cards_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_cards`
--

LOCK TABLES `payment_cards` WRITE;
/*!40000 ALTER TABLE `payment_cards` DISABLE KEYS */;
INSERT INTO `payment_cards` VALUES (2,2,'MB VISA CREDIT','**** **** **** 7560','09/28','','2026-06-02 07:50:09'),(4,2,'MB VISA CREDIT','**** **** **** 9990','09/28','','2026-06-02 08:02:31');
/*!40000 ALTER TABLE `payment_cards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_colors`
--

DROP TABLE IF EXISTS `product_colors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_colors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `color_hex` varchar(7) COLLATE utf8mb4_unicode_ci NOT NULL,
  `color_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_colors_products` (`product_id`),
  CONSTRAINT `fk_colors_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_colors`
--

LOCK TABLES `product_colors` WRITE;
/*!40000 ALTER TABLE `product_colors` DISABLE KEYS */;
INSERT INTO `product_colors` VALUES (1,1,'#1d1d1f','Space Black'),(2,1,'#ffffff','White Silver'),(3,1,'#0066cc','Ocean Blue'),(4,3,'#000000','Obsidian'),(5,3,'#ff3b30','Sport Red');
/*!40000 ALTER TABLE `product_colors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `slug` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `spec` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` double NOT NULL,
  `badge` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `thumbnail_bg` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `slug` (`slug`),
  KEY `fk_products_categories` (`category_id`),
  CONSTRAINT `fk_products_categories` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,1,'Nexus X Standard','nexus-x-standard','Liquid Retina 6.1-inch, Camera 48MP.',22990000,'Thế hệ mới','linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)','active','2026-06-01 08:50:07','2026-06-01 08:50:07'),(2,1,'Nexus X Pro Max','nexus-x-pro-max','Màn hình lớn 6.9-inch, zoom quang học 10x.',34990000,'Premium Edition','linear-gradient(135deg, #ff0844 0%, #ffb199 100%)','active','2026-06-01 08:50:07','2026-06-01 08:50:07'),(3,2,'Nexus Watch Classic','nexus-watch-classic','Vỏ thép đánh bóng, vòng bezel xoay vật lý.',8990000,'Doanh nhân','linear-gradient(135deg, #3E5151 0%, #DECBA4 100%)','active','2026-06-01 08:50:07','2026-06-01 08:50:07'),(4,1,'Nexus X Pro','nexus-x-pro','Chip Neural X1, Khung Titan vũ trụ.',29990000,'Nổi bật nhất','linear-gradient(135deg, #1e3c72 0%, #2a5298 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(5,1,'Nexus Fold 2','nexus-fold-2','Màn hình OLED 8-inch gập siêu phẳng.',44990000,'Màn hình gập','linear-gradient(135deg, #11998e 0%, #38ef7d 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(6,1,'Nexus Lite Z','nexus-lite-z','Pin 2 ngày, sắc màu trẻ trung năng động.',12490000,'Bán chạy nhất','linear-gradient(135deg, #fc00ff 0%, #00dbde 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(7,1,'Nexus Pocket','nexus-pocket','Mạnh mẽ gọn gàng 5.4-inch dễ dàng cầm nắm.',18490000,'Mini Series','linear-gradient(135deg, #ff9966 0%, #ff5e62 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(8,1,'Nexus Flip','nexus-flip','Gập dọc gọn gàng, màn phụ ngoài cực rộng.',25990000,'Gập vỏ sò','linear-gradient(135deg, #E0C3FC 0%, #8EC5FC 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(9,1,'Nexus Cinema','nexus-cinema','Ghi hình ProRes 4K, hỗ trợ lưu trữ rời USB-C.',39990000,'Creator','linear-gradient(135deg, #3a7bd5 0%, #3a6073 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(10,1,'Nexus Active','nexus-active','Chống va đập quân đội, vỏ cao su hấp thụ lực.',15990000,'Thể thao','linear-gradient(135deg, #F857A6 0%, #FF5858 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(11,1,'Nexus One Gold','nexus-one-gold','Hợp kim mạ vàng 24K, giới hạn 500 chiếc.',89990000,'Giới hạn','linear-gradient(135deg, #ffe259 0%, #ffa751 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(12,2,'Watch Classic','watch-classic','Mặt tròn cổ điển, viền thép không gỉ.',6990000,'Bán chạy','linear-gradient(135deg, #3E5151 0%, #DECBA4 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(13,2,'Watch Sport Pro','watch-sport-pro','GPS độc lập, kháng nước 50ATM.',8490000,'Mới','linear-gradient(135deg, #00c6ff 0%, #0072ff 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(14,2,'Band Fit Plus','band-fit-plus','Theo dõi nhịp tim 24/7, pin 14 ngày.',1290000,'Giá tốt','linear-gradient(135deg, #f85032 0%, #e73827 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(15,2,'Watch Ultra X','watch-ultra-x','Vỏ Titan, dành cho vận động viên chuyên nghiệp.',19990000,'Đỉnh cao','linear-gradient(135deg, #141E30 0%, #243B55 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(16,2,'Watch Elegant','watch-elegant','Dây da đà điểu, màn hình Sapphire.',11990000,'Thời trang','linear-gradient(135deg, #D3CCE3 0%, #E9E4F0 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(17,2,'Kids Watch 4G','kids-watch-4g','Định vị trẻ em, gọi video call 4G.',2490000,'Cho bé','linear-gradient(135deg, #FF9A9E 0%, #FECFEF 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(18,2,'Health Tracker','health-tracker','Đo ECG và huyết áp đạt chuẩn y tế.',4990000,'Sức khỏe','linear-gradient(135deg, #a8ff78 0%, #78ffd6 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(19,2,'Sleep Ring','sleep-ring','Nhẫn thông minh theo dõi giấc ngủ chuyên sâu.',5990000,'Đột phá','linear-gradient(135deg, #4b6cb7 0%, #182848 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(20,2,'Watch SE','watch-se','Mọi tính năng cơ bản với mức giá dễ tiếp cận.',5490000,'Sinh viên','linear-gradient(135deg, #FF416C 0%, #FF4B2B 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(21,2,'Smart Glasses','smart-glasses','Kính thông minh AR hiển thị thông báo.',14990000,'Tương lai','linear-gradient(135deg, #000000 0%, #434343 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(22,3,'Nexus Buds Pro','nexus-buds-pro','Chống ồn chủ động ANC, âm thanh vòm 360.',4590000,'Âm thanh','linear-gradient(135deg, #ECE9E6 0%, #FFFFFF 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(23,3,'Nexus Buds Lite','nexus-buds-lite','Xuyên âm, pin 24h kèm hộp sạc.',1990000,'Bán chạy','linear-gradient(135deg, #e0c3fc 0%, #8ec5fc 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(24,3,'Sạc không dây 3-in-1','wireless-charger-3in1','Sạc cùng lúc Điện thoại, Đồng hồ, Tai nghe.',2190000,'Tiện ích','linear-gradient(135deg, #8e9eab 0%, #eef2f3 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(25,3,'Củ sạc GaN 65W','charger-gan-65w','Công nghệ GaN siêu nhỏ gọn, sạc siêu tốc.',990000,'Thiết yếu','linear-gradient(135deg, #f12711 0%, #f5af19 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(26,3,'Cáp bọc dù USB-C 2m','cable-usbc-2m','Siêu bền, hỗ trợ truyền dữ liệu 40Gbps.',490000,'Bền bỉ','linear-gradient(135deg, #1d976c 0%, #93f9b9 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(27,3,'Pin dự phòng 10.000mAh','powerbank-10k','Tích hợp sạc không dây MagSafe.',1290000,'Du lịch','linear-gradient(135deg, #2bc0e4 0%, #eaecc6 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(28,3,'Ốp lưng Da thật','leather-case','Da cừu dập vân cao cấp, lót nỉ chống xước.',1490000,'Sang trọng','linear-gradient(135deg, #870000 0%, #190a05 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(29,3,'Ốp lưng Trong suốt','clear-case','Chống ố vàng 12 tháng, bảo vệ viền camera.',590000,'Cơ bản','linear-gradient(135deg, #E8CBC0 0%, #636FA4 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(30,3,'Giá đỡ xe hơi tự động','car-mount-auto','Cảm biến hồng ngoại tự động kẹp điện thoại.',890000,'Lái xe','linear-gradient(135deg, #30E8BF 0%, #FF8235 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40'),(31,3,'Dây Watch Silicon','watch-band-silicone','Cao su nén dẻo dai thân thiện da tay thể thao.',1190000,'Dây đeo','linear-gradient(135deg, #D3CCE3 0%, #E9E4F0 100%)','active','2026-06-01 10:46:40','2026-06-01 10:46:40');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `support_topics`
--

DROP TABLE IF EXISTS `support_topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `support_topics` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `support_topics`
--

LOCK TABLES `support_topics` WRITE;
/*!40000 ALTER TABLE `support_topics` DISABLE KEYS */;
INSERT INTO `support_topics` VALUES (1,'Nexus ID & Bảo mật',NULL),(2,'Bảo hành & Sửa chữa',NULL);
/*!40000 ALTER TABLE `support_topics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'customer',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Phan','Tan','admin@nexus.com','0123456789','$2a$10$ihWdN0UuqpPJc/kCn.l54OWaHRrEprAU12qxALJcQGcfq5q2O6yiK','admin','2026-06-02 02:35:13'),(2,'test','user1','user@nexus.com','0987654321','$2a$10$fCfl/oG3Fs6yyY9MfgFmOO2zjc52leUP3XNMZFM4Y/ro/w3hwAPo2','customer','2026-06-02 07:07:59');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-03  0:19:57
