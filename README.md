# JALIN Developer Test

REST API berbasis Spring Boot sebagai bagian dari developer test JALIN. Aplikasi ini mencakup tiga fitur utama: pencarian data customer, pembacaan laporan status bank dari file teks, dan pengiriman email notifikasi offline bank.

---

## Tech Stack

| Komponen       | Detail                          |
|----------------|---------------------------------|
| Language       | Java 11                         |
| Framework      | Spring Boot 2.7.0               |
| Database       | PostgreSQL                      |
| Migration      | Flyway                          |
| ORM            | Spring Data JPA                 |
| Object Mapper  | Orika                           |
| Mail           | Spring Mail (SMTP Gmail)        |
| Logging        | Log4j 2.17.2                   |
| Build Tool     | Maven                           |
| Utilities      | Lombok                          |

---

## Prasyarat

- Java 11
- Maven 3.x
- PostgreSQL (database: `db_testing`, schema: `jalin`)
- Akun Gmail dengan App Password untuk fitur email

---

## Setup & Konfigurasi

### 1. Clone Repository

```bash
git clone https://github.com/jakabrajadenta/jalin-developer-test.git
cd jalin-developer-test
```

### 2. Konfigurasi `application.properties`

Sesuaikan file `src/main/resources/application.properties`:

```properties
# Server
server.port=8080

# Database PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/db_testing?currentSchema=jalin
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

# Flyway (migrasi otomatis)
spring.flyway.schemas=jalin
spring.flyway.url=jdbc:postgresql://localhost:5432/db_testing
spring.flyway.user=postgres
spring.flyway.password=YOUR_PASSWORD

# Email (Gmail SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_GMAIL@gmail.com
spring.mail.password=YOUR_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

> **Catatan Email:** Gunakan Gmail App Password (bukan password biasa). Aktifkan melalui Google Account > Security > App Passwords.

### 3. Setup Database

Buat database dan schema secara manual di PostgreSQL:

```sql
CREATE DATABASE db_testing;
\c db_testing
CREATE SCHEMA jalin;
```

Flyway akan otomatis menjalankan migrasi saat aplikasi pertama kali dijalankan:
- `V0_0_1__initial_table.sql` — membuat tabel dan data awal
- `V0_0_2__create_procedure.sql` — membuat stored procedure

### 4. Setup File Input (untuk Bank Report)

Buat folder `input/` di root project dan letakkan file `Data Alert.txt` di dalamnya:

```
{project-root}/input/Data Alert.txt
```

Format file (delimiter `;` dan baris baru):

```
MDR;MP;8101;Bank Mandiri Online         ;offline
BTN;MP;8102;Bank BTN                   ;online
BNI;MP;8103;Bank BNI                   ;offline
```

Format kolom: `bankCode;environment;port;bankName;status`

### 5. Jalankan Aplikasi

```bash
mvn spring-boot:run
```

atau build terlebih dahulu:

```bash
mvn clean package
java -jar target/developertest-0.0.1-SNAPSHOT.jar
```

---

## Autentikasi

**Semua endpoint wajib menyertakan header `signature`.**

```
signature: 1A79F67E43C8E64A0231DB8745FDB67902216E071D741DF0F15C95DC983CA8A9
```

Jika signature tidak ada atau tidak valid, API mengembalikan:

```
HTTP 401 Unauthorized
Non-Existent or invalid signature
```

---

## API Endpoints

### 1. GET `/customer` — Cari Data Customer

Mencari data customer berdasarkan nama dan tipe. Tipe menentukan field mana yang disembunyikan dari response.

**Request Header:**

```
signature: 1A79F67E43C8E64A0231DB8745FDB67902216E071D741DF0F15C95DC983CA8A9
Content-Type: application/json
```

**Request Body:**

```json
{
  "nama": "ricky",
  "tipe": "1"
}
```

| Field  | Tipe     | Wajib | Keterangan                                |
|--------|----------|-------|-------------------------------------------|
| `nama` | `String` | Ya    | Nama customer (harus unik dalam data)     |
| `tipe` | `String` | Ya    | `"1"` = sembunyikan telepon, `"2"` = sembunyikan alamat |

**Response Sukses (200 OK):**

Tipe `"1"` (field `telepon` disembunyikan):
```json
{
  "nama": "ricky",
  "alamat": "jakarta",
  "tipe": "1",
  "response": "200",
  "signature": "1A79F67E43C8E64A0231DB8745FDB67902216E071D741DF0F15C95DC983CA8A9"
}
```

Tipe `"2"` (field `alamat` disembunyikan):
```json
{
  "nama": "ricky",
  "telepon": "0822xxx",
  "tipe": "2",
  "response": "200",
  "signature": "1A79F67E43C8E64A0231DB8745FDB67902216E071D741DF0F15C95DC983CA8A9"
}
```

**Response Error (400 Bad Request):**

```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Tipe yang dimasukan salah!",
  "path": "/customer"
}
```

**Data customer yang tersedia (mock):**

| userId | nama    | telepon   | alamat   |
|--------|---------|-----------|----------|
| 1      | ricky   | 0822xxx   | jakarta  |
| 2      | braja   | 0856xxx   | bandung  |

---

### 2. GET `/bank/data-alert` — Laporan Status Bank

Membaca dan mem-parsing file `Data Alert.txt` lalu mengembalikan daftar status koneksi bank.

**Request Header:**

```
signature: 1A79F67E43C8E64A0231DB8745FDB67902216E071D741DF0F15C95DC983CA8A9
```

**Response Sukses (200 OK):**

```json
[
  {
    "bankCode": "MDR",
    "envirotment": "MP",
    "port": "8101",
    "bankName": "Bank Mandiri Online",
    "status": "offline"
  },
  {
    "bankCode": "BNI",
    "envirotment": "MP",
    "port": "8103",
    "bankName": "Bank BNI",
    "status": "online"
  }
]
```

---

### 3. GET `/email/offline-alert` — Kirim Email Notifikasi Offline

Membaca `Data Alert.txt`, memfilter bank dengan status `offline`, lalu mengirim email notifikasi ke masing-masing bank terdaftar.

**Request Header:**

```
signature: 1A79F67E43C8E64A0231DB8745FDB67902216E071D741DF0F15C95DC983CA8A9
```

**Response Sukses:**

```
OK
```

**Contoh email yang dikirim:**

```
Subject: REPORT STATUS OFFLINE

Selamat Siang Rekan Bank MDR,

Mohon bantuan untuk Sign on pada envi berikut:

 -Envi MP Port 8101 terpantau Offline

Terima Kasih
```

**Bank terdaftar (mapping bankCode → email):**

| Bank Code | Email Tujuan               |
|-----------|---------------------------|
| MDR       | brajamobilelegends@gmail.com |
| BTN       | brajamobilelegends@gmail.com |
| BNI       | brajamobilelegends@gmail.com |
| MTP       | brajamobilelegends@gmail.com |
| BCA       | brajamobilelegends@gmail.com |

---

## Struktur Database

### Tabel `jalin.table_jalin`

Data transaksi dari sisi JALIN.

| Kolom         | Tipe         | Keterangan                  |
|---------------|--------------|-----------------------------|
| `id`          | BIGINT (PK)  | Auto-generated              |
| `card_number` | VARCHAR(20)  | Nomor kartu                 |
| `iss`         | VARCHAR(20)  | Kode bank penerbit (Issuer) |
| `acq`         | VARCHAR(20)  | Kode bank acquirer          |
| `dest`        | VARCHAR(20)  | Kode bank tujuan            |
| `status`      | VARCHAR(20)  | Status transaksi            |

### Tabel `jalin.table_bank`

Data transaksi dari sisi masing-masing bank.

| Kolom         | Tipe         | Keterangan                  |
|---------------|--------------|-----------------------------|
| `id`          | BIGINT (PK)  | Auto-generated              |
| `card_number` | VARCHAR(20)  | Nomor kartu                 |
| `iss`         | VARCHAR(20)  | Kode bank penerbit          |
| `acq`         | VARCHAR(20)  | Kode bank acquirer          |
| `dest`        | VARCHAR(20)  | Kode bank tujuan            |
| `status`      | VARCHAR(20)  | Status dari sisi bank       |
| `source`      | VARCHAR(20)  | Sumber (kode bank pelapor)  |

### Tabel `jalin.table_summary`

Tabel rekonsiliasi yang diisi via stored procedure.

| Kolom          | Tipe         | Keterangan                          |
|----------------|--------------|-------------------------------------|
| `id`           | BIGINT (PK)  | Auto-generated                      |
| `card_number`  | VARCHAR(20)  | Nomor kartu                         |
| `iss`          | VARCHAR(20)  | Kode bank penerbit                  |
| `acq`          | VARCHAR(20)  | Kode bank acquirer                  |
| `dest`         | VARCHAR(20)  | Kode bank tujuan                    |
| `status_jalin` | VARCHAR(20)  | Status menurut JALIN                |
| `status_iss`   | VARCHAR(20)  | Status menurut bank penerbit        |
| `status_acq`   | VARCHAR(20)  | Status menurut bank acquirer        |
| `status_dest`  | VARCHAR(20)  | Status menurut bank tujuan          |

### Stored Procedure

`insert_table_summary()` — melakukan rekonsiliasi data dari `table_jalin` dan `table_bank` ke `table_summary`.

```sql
CALL insert_table_summary();
```

---

## Struktur Project

```
src/main/java/com/jalin/developertest/
├── configuration/
│   ├── HttpHeaderConfiguration.java     # Filter validasi header signature
│   ├── JalinConfiguration.java          # Konfigurasi tambahan (mail, dll)
│   └── mapper/
│       └── MockCustomerToCustomerResponseDtoMapper.java  # Orika custom mapper
├── controller/
│   ├── BankController.java              # GET /bank/data-alert
│   ├── CustomerController.java          # GET /customer
│   ├── EmailController.java             # GET /email/offline-alert
│   └── advice/
│       └── GeneralExceptioAdvice.java   # Global exception handler
├── dto/
│   ├── CustomerDto.java                 # Response customer
│   ├── CustomerRequestDto.java          # Request customer
│   ├── DataAlertDto.java                # Data status bank
│   ├── HttpHeaderDto.java               # DTO untuk header
│   ├── MockCustomerDto.java             # Data mock customer
│   └── error/
│       └── DefaultErrorAttributes.java  # Format error response
└── service/
    ├── BankReportService.java           # Baca & parse file Data Alert
    ├── CustomerService.java             # Logika pencarian customer
    ├── EmailService.java                # Pengiriman email notifikasi
    └── HttpService.java                 # Menyimpan header per request (RequestScope)

src/main/resources/
├── application.properties
└── db/migration/
    ├── V0_0_1__initial_table.sql        # Buat tabel & seed data awal
    └── V0_0_2__create_procedure.sql     # Stored procedure rekonsiliasi
```

---

## Catatan Implementasi

- **Signature filter** berlaku untuk semua endpoint. Untuk membatasi hanya pada endpoint tertentu, filter dapat dipindahkan menggunakan `GenericFilterBean` dengan URL pattern matching.
- **Data customer** saat ini menggunakan data mock (hardcoded). Tidak terhubung ke database.
- **Stored procedure** `insert_table_summary()` tersedia tetapi perlu dipanggil secara manual atau dijadwalkan sesuai kebutuhan.
- Field `null` tidak akan muncul di JSON response karena konfigurasi `spring.jackson.default-property-inclusion=NON_NULL`.
