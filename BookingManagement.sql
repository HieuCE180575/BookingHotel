-- Xóa CSDL cũ nếu đã tồn tại
DROP DATABASE IF EXISTS BookingManagement;
GO

-- Tạo CSDL mới
CREATE DATABASE BookingManagement;
GO

-- Sử dụng CSDL mới
USE BookingManagement;
GO

-- 1. Tạo bảng Admin
DROP TABLE IF EXISTS [Admin];
GO
CREATE TABLE [Admin] (
    AdminID INT IDENTITY(1,1) PRIMARY KEY,
    Username_A NVARCHAR(255) NOT NULL UNIQUE,
    FirstName_A NVARCHAR(255) NOT NULL, -- Fist + last load full name tu dong viet hoa theo dunng form
    LastName_A NVARCHAR(255) NOT NULL,
    Password_A NVARCHAR(255) NOT NULL, -- load MD5 pass chung toan he thong 123456@Fpt it nhat 8 ki tu có ki tu đat biet va co chu cai 
    Phone_A NVARCHAR(255) NOT NULL, -- 10 so bat dau 0
    Email_A NVARCHAR(255) NOT NULL, --@gmail
    Sex_A INT NOT NULL, -- Nam 0 nu 1
    Dob_A DATE NOT NULL, --date of birth 
    Role_A INT NOT NULL -- admin 2, manager 1s
);
GO

-- 2. Tạo bảng Customer
DROP TABLE IF EXISTS [Customer];
GO
CREATE TABLE [Customer] (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    Username NVARCHAR(255) NOT NULL UNIQUE,
    FirstName NVARCHAR(255) NOT NULL,
    LastName NVARCHAR(255) NOT NULL,
    [Password] NVARCHAR(255) NOT NULL,
    Phone NVARCHAR(255) NOT NULL,
    Email NVARCHAR(255) NOT NULL,
    Sex INT NOT NULL,
    Dob DATE NOT NULL,
    [Role] INT NOT NULL --role 0
);
GO

-- 3. Tạo bảng City
DROP TABLE IF EXISTS [City];
GO
CREATE TABLE [City] (
    CityID INT IDENTITY(1,1) PRIMARY KEY,
    [Name_city] VARCHAR(50) NOT NULL,
    Country VARCHAR(50) NOT NULL
);
GO

-- 4. Tạo bảng Hotel
DROP TABLE IF EXISTS [Hotel];
GO
CREATE TABLE [Hotel] (
    HotelID INT IDENTITY(1,1) PRIMARY KEY,
    Hotel_Name NVARCHAR(100) NOT NULL,
    [Address] NVARCHAR(100) NOT NULL, --HCM
    Rating DECIMAL(10,2) NOT NULL, --Trung bình của tất cả rating của review --
    [description_H] NVARCHAR(255) NOT NULL, 
    Phone NVARCHAR(255) NOT NULL,
    Email NVARCHAR(255) NOT NULL,
    CityID INT NOT NULL,
	--List đánh giá cao nhất 3 cái --
	-- load hình ảnh từ image tầm 10 cái list 
	
    FOREIGN KEY (CityID) REFERENCES City(CityID)
);
GO

-- 5. Tạo bảng Roomtype
DROP TABLE IF EXISTS [Roomtype];
GO
CREATE TABLE [Roomtype] (
    RoomtypeID INT IDENTITY(1,1) PRIMARY KEY,
    HotelID INT NOT NULL,
    Name_Roomtype VARCHAR(50) NOT NULL,
    Capacity DECIMAL(10,2) NOT NULL,
    [Description_R] NVARCHAR(255) NOT NULL,
    PricePerNight DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (HotelID) REFERENCES Hotel(HotelID)
);
GO

-- 6. Tạo bảng Room
DROP TABLE IF EXISTS [Room];
GO
CREATE TABLE [Room] (
    RoomID INT IDENTITY(1,1) PRIMARY KEY, 
	Description_R nvarchar(50) not NULL,
    Room_Number VARCHAR(10) NOT NULL,
    Room_Status INT NOT NULL, -- valible 0 , inavilable 1--
    [Floor] INT NOT NULL,
    RoomtypeID INT NOT NULL,
    FOREIGN KEY (RoomtypeID) REFERENCES Roomtype(RoomtypeID)
	-- qua image URL bên room lên--
	--qua roomtype load thông tin details lên
	--Name_Roomtype, Capacity, Description, PricePerNight 
);
GO

-- 7. Tạo bảng Voucher
DROP TABLE IF EXISTS [Voucher];
GO
CREATE TABLE [Voucher] (
    VoucherID INT IDENTITY(1,1) PRIMARY KEY,
    Description_V NVARCHAR(255) NOT NULL,
    DiscountValue DECIMAL(10,2) NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL
);
GO

-- 8. Tạo bảng Booking
DROP TABLE IF EXISTS [Booking];
GO
CREATE TABLE [Booking] (
    BookingID INT IDENTITY(1,1) PRIMARY KEY,
    Checkin_Date DATE NOT NULL,
    Checkout_Date DATE NOT NULL,
    VAT DECIMAL(10,2) NOT NULL,
    TimeBooking DATETIME NOT NULL,
    Username NVARCHAR(255) NOT NULL,
    Status_B NVARCHAR(50) NOT NULL,
    RoomID INT NOT NULL,
    VoucherID INT NOT NULL,
    FOREIGN KEY (RoomID) REFERENCES Room(RoomID),
    FOREIGN KEY (VoucherID) REFERENCES Voucher(VoucherID),
    FOREIGN KEY (Username) REFERENCES Customer(Username)
);
GO

-- 9. Tạo bảng Payment
DROP TABLE IF EXISTS [Payment];
GO
CREATE TABLE [Payment] (
    PaymentsID INT IDENTITY(1,1) PRIMARY KEY,
    BookingID INT,
    Amount DECIMAL(10,2),
    PaymentMethod VARCHAR(50),
    Status_P INT,
    TimeProcess DATETIME,
    FOREIGN KEY (BookingID) REFERENCES Booking(BookingID)
);
GO

-- 10. Tạo bảng Invoice
DROP TABLE IF EXISTS [Invoice];
GO
CREATE TABLE [Invoice] (
    InvoiceID INT IDENTITY(1,1) PRIMARY KEY,
    BookingID INT NOT NULL,
    IssueDate DATE NOT NULL,
    TotalAmount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (BookingID) REFERENCES Booking(BookingID)
);
GO

-- 11. Tạo bảng Image
DROP TABLE IF EXISTS [Image];
GO
CREATE TABLE [Image] (
    ImageID INT IDENTITY(1,1) PRIMARY KEY,
    HotelID INT NOT NULL,
    CityID INT NOT NULL,
    RoomtypeID INT NOT NULL,
    ImageURL VARCHAR(255) NOT NULL,
    Caption VARCHAR(255) NOT NULL,
    FOREIGN KEY (HotelID) REFERENCES Hotel(HotelID),
    FOREIGN KEY (CityID) REFERENCES City(CityID),
    FOREIGN KEY (RoomtypeID) REFERENCES Roomtype(RoomtypeID)
);
GO

-- 12. Tạo bảng Reviews
DROP TABLE IF EXISTS [Reviews];
GO
CREATE TABLE [Reviews] (
    ReviewsID INT IDENTITY(1,1) PRIMARY KEY,
    HotelID INT NOT NULL,
    Rating TINYINT NOT NULL, -- rating cus-- 
    Username NVARCHAR(255) NOT NULL,
    Comment TEXT NOT NULL,
    TimeReview DATETIME NOT NULL,
    FOREIGN KEY (HotelID) REFERENCES Hotel(HotelID),
    FOREIGN KEY (Username) REFERENCES Customer(Username)
);
GO

-- 13. Tạo bảng Service
DROP TABLE IF EXISTS [Service];
GO
CREATE TABLE [Service] (
    ServiceID INT IDENTITY(1,1) PRIMARY KEY,
    HotelID INT NOT NULL,
    ServiceName NVARCHAR(255) NOT NULL,
    FOREIGN KEY (HotelID) REFERENCES Hotel(HotelID)
);
GO

INSERT INTO City ([Name_city], Country) VALUES 
('Ha Noi', 'Viet Nam'),
('Ho Chi Minh', 'Viet Nam'),
('Da Nang', 'Viet Nam');

-- Thêm dữ liệu vào bảng Admin
INSERT INTO [Admin] (Username_A, FirstName_A, LastName_A, Password_A, Phone_A, Email_A, Sex_A, Dob_A, Role_A)
VALUES
('admin001', 'Nguyen', 'VanA', HASHBYTES('MD5', '123456@Fpt'), '0912345678', 'vana@gmail.com', 0, '1985-01-15', 2),
('manager001', 'Tran', 'ThiB', HASHBYTES('MD5', '123456@Fpt'), '0908765432', 'thib@gmail.com', 1, '1990-06-20', 1);
-- Thêm dữ liệu vào bảng Customer
INSERT INTO [Customer] (Username, FirstName, LastName, [Password], Phone, Email, Sex, Dob, [Role])
VALUES
--('customer001', 'Le', 'MinhC', HASHBYTES('MD5', '123456@Fpt'), '0987654321', 'minhc@gmail.com', 0, '1995-03-10', 0),
--('customer002', 'Pham', 'NgocD', HASHBYTES('MD5', '123456@Fpt'), '0978123456', 'ngocd@gmail.com', 1, '2000-07-25', 0);
('customer003', 'Le', 'MinhC', '5075dc36e6fb5b2f19a38ac28d8a7fca', '0987654781', 'minyhc@gmail.com', 0, '1995-03-10', 0),
('customer004', 'Pham', 'NgocrtD','5075dc36e6fb5b2f19a38ac28d8a7fca', '0978823456', 'ngofcyhd@gmail.com', 1, '2001-04-25', 0);
--('customer005', 'Le', 'MinhrtC','5075dc36e6fb5b2f19a38ac28d8a7fca', '0987654021', 'minhacy@gmail.com', 0, '1995-06-10', 0),
--('customer006', 'Pham', 'NgocrtD','5075dc36e6fb5b2f19a38ac28d8a7fca', '0979123456', 'ngocfrrd@gmail.com', 1, '2000-08-25', 0);

UPDATE Customer
SET [PassWord] = '5075dc36e6fb5b2f19a38ac28d8a7fca'
WHERE [PassWord] = '  ';

Delete Customer
where UserName ='customer006'

select * from Customer