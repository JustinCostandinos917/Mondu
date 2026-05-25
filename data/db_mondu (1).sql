-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 24, 2026 at 04:25 PM
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
-- Database: `db_mondu`
--

-- --------------------------------------------------------

--
-- Table structure for table `absensi`
--

CREATE TABLE `absensi` (
  `id_absensi` varchar(10) NOT NULL,
  `nis` varchar(20) DEFAULT NULL,
  `sakit` int(11) DEFAULT 0,
  `izin` int(11) DEFAULT 0,
  `alpha` int(11) DEFAULT 0,
  `semester` enum('Ganjil','Genap') DEFAULT NULL,
  `tahun_ajaran` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `absensi`
--

INSERT INTO `absensi` (`id_absensi`, `nis`, `sakit`, `izin`, `alpha`, `semester`, `tahun_ajaran`) VALUES
('ABS001', '23123456', 1, 0, 0, 'Ganjil', '2025/2026'),
('ABS002', '23123457', 0, 1, 0, 'Ganjil', '2025/2026'),
('ABS003', '23123458', 0, 0, 0, 'Ganjil', '2025/2026');

-- --------------------------------------------------------

--
-- Table structure for table `catatan_walikelas`
--

CREATE TABLE `catatan_walikelas` (
  `id_catatan` varchar(10) NOT NULL,
  `nis` varchar(20) DEFAULT NULL,
  `semester` enum('Ganjil','Genap') DEFAULT NULL,
  `tahun_ajaran` varchar(20) DEFAULT NULL,
  `catatan` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `catatan_walikelas`
--

INSERT INTO `catatan_walikelas` (`id_catatan`, `nis`, `semester`, `tahun_ajaran`, `catatan`) VALUES
('CTL001', '23123456', 'Ganjil', '2025/2026', 'Siswa aktif dan disiplin dalam mengikuti pembelajaran.'),
('CTL002', '23123457', 'Ganjil', '2025/2026', 'Siswa perlu meningkatkan ketepatan waktu.'),
('CTL003', '23123458', 'Ganjil', '2025/2026', 'Siswa memiliki prestasi akademik yang baik.');

-- --------------------------------------------------------

--
-- Table structure for table `guru`
--

CREATE TABLE `guru` (
  `nuptk` varchar(30) NOT NULL,
  `id_user` varchar(10) NOT NULL,
  `no_hp` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `guru`
--

INSERT INTO `guru` (`nuptk`, `id_user`, `no_hp`) VALUES
('123456789001', 'USR002', '081234567890'),
('123456789002', 'USR003', '081298765432'),
('123456789003', 'USR008', '081345678901'),
('123456789004', 'USR009', '081398765432'),
('123456789005', 'USR010', '081234567893'),
('123456789006', 'USR007', '081298765434'),
('123456789007', 'USR011', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `jadwal`
--

CREATE TABLE `jadwal` (
  `id_jadwal` varchar(10) NOT NULL,
  `id_kelas` varchar(10) DEFAULT NULL,
  `id_mapel` varchar(10) DEFAULT NULL,
  `nuptk` varchar(20) NOT NULL,
  `id_jam` int(11) DEFAULT NULL,
  `hari` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jadwal`
--

INSERT INTO `jadwal` (`id_jadwal`, `id_kelas`, `id_mapel`, `nuptk`, `id_jam`, `hari`) VALUES
('JDW001', 'KLS001', 'MPL001', '123456789001', 2, 'Senin'),
('JDW002', 'KLS001', 'MPL001', '123456789001', 1, 'Senin'),
('JDW003', 'KLS001', 'MPL002', '123456789002', 3, 'Senin'),
('JDW004', 'KLS001', 'MPL003', '123456789003', 5, 'Senin'),
('JDW005', 'KLS001', 'MPL007', '123456789006', 6, 'Senin'),
('JDW006', 'KLS001', 'MPL004', '123456789004', 7, 'Senin'),
('JDW007', 'KLS001', 'MPL008', '123456789006', 8, 'Senin'),
('JDW008', 'KLS001', 'MPL006', '123456789005', 10, 'Senin'),
('JDW009', 'KLS001', 'MPL005', '123456789005', 11, 'Senin'),
('JDW010', 'KLS001', 'MPL001', '123456789001', 12, 'Senin'),
('JDW011', 'KLS002', 'MPL007', '123456789006', 1, 'Senin'),
('JDW012', 'KLS002', 'MPL006', '123456789005', 2, 'Senin'),
('JDW013', 'KLS002', 'MPL004', '123456789004', 3, 'Senin'),
('JDW014', 'KLS002', 'MPL002', '123456789002', 5, 'Senin'),
('JDW015', 'KLS002', 'MPL008', '123456789006', 6, 'Senin'),
('JDW016', 'KLS002', 'MPL003', '123456789003', 7, 'Senin'),
('JDW017', 'KLS002', 'MPL005', '123456789005', 8, 'Senin'),
('JDW018', 'KLS002', 'MPL001', '123456789001', 10, 'Senin'),
('JDW019', 'KLS002', 'MPL007', '123456789006', 11, 'Senin'),
('JDW020', 'KLS002', 'MPL006', '123456789005', 12, 'Senin'),
('JDW021', 'KLS003', 'MPL002', '123456789002', 1, 'Senin'),
('JDW022', 'KLS003', 'MPL008', '123456789006', 2, 'Senin'),
('JDW023', 'KLS003', 'MPL006', '123456789005', 3, 'Senin'),
('JDW024', 'KLS003', 'MPL005', '123456789005', 5, 'Senin'),
('JDW025', 'KLS003', 'MPL001', '123456789001', 6, 'Senin'),
('JDW026', 'KLS003', 'MPL004', '123456789004', 7, 'Senin'),
('JDW027', 'KLS003', 'MPL007', '123456789006', 8, 'Senin'),
('JDW028', 'KLS003', 'MPL003', '123456789003', 10, 'Senin'),
('JDW029', 'KLS003', 'MPL002', '123456789002', 11, 'Senin'),
('JDW030', 'KLS003', 'MPL008', '123456789006', 12, 'Senin'),
('JDW031', 'KLS004', 'MPL008', '123456789006', 1, 'Senin'),
('JDW032', 'KLS004', 'MPL005', '123456789005', 2, 'Senin'),
('JDW033', 'KLS004', 'MPL006', '123456789005', 3, 'Senin'),
('JDW034', 'KLS004', 'MPL007', '123456789006', 5, 'Senin'),
('JDW035', 'KLS004', 'MPL002', '123456789002', 6, 'Senin'),
('JDW036', 'KLS004', 'MPL004', '123456789004', 7, 'Senin'),
('JDW037', 'KLS004', 'MPL001', '123456789001', 8, 'Senin'),
('JDW038', 'KLS004', 'MPL003', '123456789003', 10, 'Senin'),
('JDW039', 'KLS004', 'MPL008', '123456789006', 11, 'Senin'),
('JDW040', 'KLS004', 'MPL005', '123456789005', 12, 'Senin'),
('JDW041', 'KLS005', 'MPL001', '123456789001', 1, 'Senin'),
('JDW042', 'KLS005', 'MPL004', '123456789004', 2, 'Senin'),
('JDW043', 'KLS005', 'MPL006', '123456789005', 3, 'Senin'),
('JDW044', 'KLS005', 'MPL003', '123456789003', 5, 'Senin'),
('JDW045', 'KLS005', 'MPL005', '123456789005', 6, 'Senin'),
('JDW046', 'KLS005', 'MPL002', '123456789002', 7, 'Senin'),
('JDW047', 'KLS005', 'MPL007', '123456789006', 8, 'Senin'),
('JDW048', 'KLS005', 'MPL008', '123456789006', 10, 'Senin'),
('JDW049', 'KLS005', 'MPL001', '123456789001', 11, 'Senin'),
('JDW050', 'KLS005', 'MPL004', '123456789004', 12, 'Senin'),
('JDW051', 'KLS006', 'MPL002', '123456789002', 1, 'Senin'),
('JDW052', 'KLS006', 'MPL007', '123456789006', 2, 'Senin'),
('JDW053', 'KLS006', 'MPL004', '123456789004', 3, 'Senin'),
('JDW054', 'KLS006', 'MPL005', '123456789005', 5, 'Senin'),
('JDW055', 'KLS006', 'MPL001', '123456789001', 6, 'Senin'),
('JDW056', 'KLS006', 'MPL003', '123456789003', 7, 'Senin'),
('JDW057', 'KLS006', 'MPL006', '123456789005', 8, 'Senin'),
('JDW058', 'KLS006', 'MPL008', '123456789006', 10, 'Senin'),
('JDW059', 'KLS006', 'MPL002', '123456789002', 11, 'Senin'),
('JDW060', 'KLS006', 'MPL007', '123456789006', 12, 'Senin'),
('JDW061', 'KLS001', 'MPL003', '123456789003', 1, 'Selasa'),
('JDW062', 'KLS001', 'MPL004', '123456789004', 6, 'Selasa'),
('JDW063', 'KLS001', 'MPL003', '123456789003', 2, 'Selasa'),
('JDW064', 'KLS001', 'MPL002', '123456789002', 5, 'Rabu'),
('JDW065', 'KLS003', 'MPL004', '123456789004', 1, 'Selasa');

-- --------------------------------------------------------

--
-- Table structure for table `jam_pelajaran`
--

CREATE TABLE `jam_pelajaran` (
  `id_jam` int(11) NOT NULL,
  `jam_mulai` time DEFAULT NULL,
  `jam_selesai` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jam_pelajaran`
--

INSERT INTO `jam_pelajaran` (`id_jam`, `jam_mulai`, `jam_selesai`) VALUES
(1, '07:00:00', '07:40:00'),
(2, '07:40:00', '08:20:00'),
(3, '08:20:00', '09:00:00'),
(4, '09:00:00', '09:20:00'),
(5, '09:20:00', '10:00:00'),
(6, '10:00:00', '10:40:00'),
(7, '10:40:00', '11:20:00'),
(8, '11:20:00', '12:00:00'),
(9, '12:00:00', '12:20:00'),
(10, '12:20:00', '13:00:00'),
(11, '13:00:00', '13:40:00'),
(12, '13:40:00', '14:20:00');

-- --------------------------------------------------------

--
-- Table structure for table `kelas`
--

CREATE TABLE `kelas` (
  `id_kelas` varchar(10) NOT NULL,
  `nama_kelas` varchar(20) NOT NULL,
  `tingkat` enum('7','8','9') NOT NULL,
  `nuptk_walikelas` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kelas`
--

INSERT INTO `kelas` (`id_kelas`, `nama_kelas`, `tingkat`, `nuptk_walikelas`) VALUES
('KLS001', '7A', '7', '123456789001'),
('KLS002', '7B', '7', '123456789002'),
('KLS003', '8A', '8', '123456789003'),
('KLS004', '8B', '8', '123456789004'),
('KLS005', '9A', '9', '123456789005'),
('KLS006', '9B', '9', '123456789006');

-- --------------------------------------------------------

--
-- Table structure for table `mata_pelajaran`
--

CREATE TABLE `mata_pelajaran` (
  `id_mapel` varchar(10) NOT NULL,
  `nama_mapel` varchar(100) NOT NULL,
  `nuptk_guru` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mata_pelajaran`
--

INSERT INTO `mata_pelajaran` (`id_mapel`, `nama_mapel`, `nuptk_guru`) VALUES
('MPL001', 'Matematika', '123456789001'),
('MPL002', 'Bahasa Indonesia', '123456789002'),
('MPL003', 'Bahasa Inggris', '123456789003'),
('MPL004', 'Ilmu Pengetahuan Alam (IPA)', '123456789004'),
('MPL005', 'Ilmu Pengetahuan Sosial (IPS)', '123456789005'),
('MPL006', 'Informatika', '123456789005'),
('MPL007', 'Pendidikan Pancasila (PPKn)', '123456789006'),
('MPL008', 'Seni Budaya', '123456789006');

-- --------------------------------------------------------

--
-- Table structure for table `nilai`
--

CREATE TABLE `nilai` (
  `id_nilai` varchar(10) NOT NULL,
  `nis` varchar(20) DEFAULT NULL,
  `id_mapel` varchar(10) DEFAULT NULL,
  `tugas` decimal(5,2) DEFAULT NULL,
  `uts` decimal(5,2) DEFAULT NULL,
  `uas` decimal(5,2) DEFAULT NULL,
  `nilai_akhir` decimal(5,2) DEFAULT NULL,
  `semester` enum('Ganjil','Genap') DEFAULT NULL,
  `tahun_ajaran` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nilai`
--

INSERT INTO `nilai` (`id_nilai`, `nis`, `id_mapel`, `tugas`, `uts`, `uas`, `nilai_akhir`, `semester`, `tahun_ajaran`) VALUES
('NIL001', '23123456', 'MPL001', 85.00, 88.00, 90.00, 88.00, 'Ganjil', '2025/2026'),
('NIL002', '23123456', 'MPL002', 80.00, 82.00, 85.00, 82.33, 'Ganjil', '2025/2026'),
('NIL003', '23123457', 'MPL001', 78.00, 80.00, 84.00, 80.67, 'Ganjil', '2025/2026');

-- --------------------------------------------------------

--
-- Table structure for table `siswa`
--

CREATE TABLE `siswa` (
  `nis` varchar(20) NOT NULL,
  `id_user` varchar(10) NOT NULL,
  `nisn` varchar(20) DEFAULT NULL,
  `jenis_kelamin` enum('L','P') DEFAULT NULL,
  `tanggal_lahir` date DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `id_kelas` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `siswa`
--

INSERT INTO `siswa` (`nis`, `id_user`, `nisn`, `jenis_kelamin`, `tanggal_lahir`, `alamat`, `id_kelas`) VALUES
('23123456', 'USR004', '9988776655', 'L', '2011-05-10', 'Jakarta', 'KLS001'),
('23123457', 'USR005', '9988776656', 'L', '2012-05-13', 'Tangerang', 'KLS001'),
('23123458', 'USR006', '9988776657', 'P', '2011-02-14', 'Bekasi', 'KLS002');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_pengumuman`
--

CREATE TABLE `tbl_pengumuman` (
  `id_pengumuman` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `judul` varchar(255) NOT NULL,
  `konten` text NOT NULL,
  `tanggal_post` date NOT NULL,
  `link_pdf` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id_user` varchar(10) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `role` enum('Admin','Guru','Wali Kelas','Siswa') NOT NULL,
  `foto_profil` varchar(255) DEFAULT 'default_avatar.png',
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id_user`, `username`, `email`, `password`, `nama_lengkap`, `role`, `foto_profil`, `created_at`) VALUES
('USR001', 'admin123', 'admin@mondu.com', 'admin', 'Admin Mondu', 'Admin', NULL, '2026-05-23 21:35:52'),
('USR002', 'budi', 'user2@gmail.com', 'guru123', 'Budi Setiawan, S.Pd', 'Guru', 'default_avatar.png', '2026-05-23 21:09:31'),
('USR003', 'siti', 'siti@mondu.com', 'siti.guru', 'Siti Rahma', 'Guru', NULL, '2026-05-23 21:35:52'),
('USR004', 'siswa123', 'justin.costandinos@gmail.com', 'siswa123', 'Justin Costandinos', 'Siswa', 'default_avatar.png', '2026-05-23 21:09:31'),
('USR005', 'andi', 'andi@mondu.com', '23123457', 'Andi Saputra', 'Siswa', NULL, '2026-05-23 21:35:52'),
('USR006', 'cindy', 'cindy@mondu.com', '23123458', 'Cindy Natalie', 'Siswa', NULL, '2026-05-23 21:35:52'),
('USR007', 'iwan', 'iwan@mondu.com', 'iwan.guru', 'Iwan Kurniawan, S.Pd', 'Guru', NULL, '2026-05-23 23:25:00'),
('USR008', 'riniyani', 'rini@mondu.com', 'rini.guru', 'Rini Handayani, M.Pd', 'Guru', 'default_avatar.png', '2026-05-23 23:25:00'),
('USR009', 'eko', 'eko@mondu.com', 'eko.guru', 'Eko Prasetyo, S.Kom', 'Guru', NULL, '2026-05-23 23:25:00'),
('USR010', 'dewi', 'dewi@mondu.com', 'dewi.guru', 'Dewi Lestari, S.Si', 'Guru', 'default_avatar.png', '2026-05-23 23:25:00'),
('USR011', 'heri', 'heri@gmail.com', '258', 'Heri Yanto, S.TI', 'Guru', 'default_avatar.png', '2026-05-24 02:13:58');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `absensi`
--
ALTER TABLE `absensi`
  ADD PRIMARY KEY (`id_absensi`),
  ADD KEY `nis` (`nis`);

--
-- Indexes for table `catatan_walikelas`
--
ALTER TABLE `catatan_walikelas`
  ADD PRIMARY KEY (`id_catatan`),
  ADD KEY `nis` (`nis`);

--
-- Indexes for table `guru`
--
ALTER TABLE `guru`
  ADD PRIMARY KEY (`nuptk`),
  ADD UNIQUE KEY `id_user` (`id_user`);

--
-- Indexes for table `jadwal`
--
ALTER TABLE `jadwal`
  ADD PRIMARY KEY (`id_jadwal`),
  ADD KEY `id_kelas` (`id_kelas`),
  ADD KEY `id_mapel` (`id_mapel`),
  ADD KEY `fk_jadwal_guru_nuptk` (`nuptk`),
  ADD KEY `fk_jadwal_jam` (`id_jam`);

--
-- Indexes for table `jam_pelajaran`
--
ALTER TABLE `jam_pelajaran`
  ADD PRIMARY KEY (`id_jam`);

--
-- Indexes for table `kelas`
--
ALTER TABLE `kelas`
  ADD PRIMARY KEY (`id_kelas`),
  ADD KEY `nuptk_walikelas` (`nuptk_walikelas`);

--
-- Indexes for table `mata_pelajaran`
--
ALTER TABLE `mata_pelajaran`
  ADD PRIMARY KEY (`id_mapel`),
  ADD KEY `nuptk_guru` (`nuptk_guru`);

--
-- Indexes for table `nilai`
--
ALTER TABLE `nilai`
  ADD PRIMARY KEY (`id_nilai`),
  ADD KEY `nis` (`nis`),
  ADD KEY `id_mapel` (`id_mapel`);

--
-- Indexes for table `siswa`
--
ALTER TABLE `siswa`
  ADD PRIMARY KEY (`nis`),
  ADD UNIQUE KEY `id_user` (`id_user`),
  ADD UNIQUE KEY `nisn` (`nisn`),
  ADD KEY `id_kelas` (`id_kelas`);

--
-- Indexes for table `tbl_pengumuman`
--
ALTER TABLE `tbl_pengumuman`
  ADD PRIMARY KEY (`id_pengumuman`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `email_2` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_pengumuman`
--
ALTER TABLE `tbl_pengumuman`
  MODIFY `id_pengumuman` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `absensi`
--
ALTER TABLE `absensi`
  ADD CONSTRAINT `absensi_ibfk_1` FOREIGN KEY (`nis`) REFERENCES `siswa` (`nis`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `catatan_walikelas`
--
ALTER TABLE `catatan_walikelas`
  ADD CONSTRAINT `catatan_walikelas_ibfk_1` FOREIGN KEY (`nis`) REFERENCES `siswa` (`nis`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `guru`
--
ALTER TABLE `guru`
  ADD CONSTRAINT `fk_guru_user` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `jadwal`
--
ALTER TABLE `jadwal`
  ADD CONSTRAINT `fk_jadwal_guru_nuptk` FOREIGN KEY (`nuptk`) REFERENCES `guru` (`nuptk`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_jadwal_jam` FOREIGN KEY (`id_jam`) REFERENCES `jam_pelajaran` (`id_jam`) ON UPDATE CASCADE,
  ADD CONSTRAINT `jadwal_ibfk_1` FOREIGN KEY (`id_kelas`) REFERENCES `kelas` (`id_kelas`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `jadwal_ibfk_2` FOREIGN KEY (`id_mapel`) REFERENCES `mata_pelajaran` (`id_mapel`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `kelas`
--
ALTER TABLE `kelas`
  ADD CONSTRAINT `kelas_ibfk_1` FOREIGN KEY (`nuptk_walikelas`) REFERENCES `guru` (`nuptk`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `mata_pelajaran`
--
ALTER TABLE `mata_pelajaran`
  ADD CONSTRAINT `mata_pelajaran_ibfk_1` FOREIGN KEY (`nuptk_guru`) REFERENCES `guru` (`nuptk`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `nilai`
--
ALTER TABLE `nilai`
  ADD CONSTRAINT `nilai_ibfk_1` FOREIGN KEY (`nis`) REFERENCES `siswa` (`nis`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `nilai_ibfk_2` FOREIGN KEY (`id_mapel`) REFERENCES `mata_pelajaran` (`id_mapel`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `siswa`
--
ALTER TABLE `siswa`
  ADD CONSTRAINT `siswa_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `siswa_ibfk_2` FOREIGN KEY (`id_kelas`) REFERENCES `kelas` (`id_kelas`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
