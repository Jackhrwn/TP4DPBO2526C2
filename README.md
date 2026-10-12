# 🙍‍♂️ PeopleData (Java Swing CRUD)

Aplikasi desktop berbasis **Java Swing GUI** untuk mengelola data orang (People) dengan operasi **CRUD** (Create, Read, Update, Delete).

## ❤️ Janji

Saya Jaka Permana Herawan dengan NIM 2509371 mengerjakan Tugas Praktikum 4 pada Mata Kuliah Desain dan Pemrograman Berorientasi Objek (DPBO) untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin

## 🎨 Desain & Fungsi Program

### Atribut data (class `Person`)

| Atribut | Tipe | Component di form | Keterangan |
|---|---|---|---|
| `id` | String | `JTextField` | ID unik orang |
| `nama` | String | `JTextField` | Nama lengkap |
| `tahunLahir` | int | `JTextField` | Harus berupa angka |
| `kategori` | String | `JComboBox` | Mahasiswa / Dosen / Staf / Alumni |
| `golonganDarah` | String | `JComboBox` | **Atribut baru** - A / B / AB / O |
| `jenisKelamin` | String | `JRadioButton` | **Atribut baru** - Laki-Laki / Perempuan (satu pilihan, memakai `ButtonGroup`) |
| `email` | String | `JTextField` | Atribut tambahan, divalidasi formatnya (`nama@domain.com`) |

### Komponen utama form (`PeopleMenu.form`)

- `titleLabel` - judul aplikasi
- `idField`, `namaField`, `tahunLahirField` - input teks
- `kategoriComboBox`, `golonganDarahComboBox` - input pilihan
- `lakiLakiRadioButton`, `perempuanRadioButton` - pilihan jenis kelamin (dikelompokkan dengan `ButtonGroup`)
- `addUpdateButton` - tombol **Add** (mode tambah) / **Update** (mode ubah)
- `cancelButton` - mengosongkan form
- `deleteButton` - menghapus data (hanya tampil saat sebuah baris dipilih)
- `peopleTable` - menampilkan seluruh data

### Fitur tambahan

- **Confirmation prompt** sebelum data dihapus (`JOptionPane.showConfirmDialog`, pilihan Yes/No).
- **Validasi input**: ID/Nama tidak boleh kosong dan tidak boleh ada ID yang sama, Kategori, Golongan Darah, dan Jenis Kelamin harus dipilih, Tahun Lahir harus angka, Email harus berformat valid.
- **Tampilan rapi**: form dibagi menjadi panel "Form Data" (2 kolom), baris tombol, dan panel "Daftar Orang"; tabel memakai header berwarna, baris belang-belang, dan lebar kolom yang diatur.
- **Atribut baru: Golongan Darah** (`JComboBox`) dan **Jenis Kelamin** (`JRadioButton`), keduanya bukan `JTextField`.

## ✨ Alur Program

1. **Start** - `main()` membuat window `PeopleMenu`, lalu constructor mengisi `listPerson` lewat `populateList()` dan menampilkannya di tabel lewat `setTable()`.
2. **Create** - user mengisi form lalu menekan **Add**. Karena `selectedIndex == -1`, program memanggil `insertData()`: input divalidasi, objek `Person` baru ditambahkan ke `listPerson`, tabel diperbarui, form dikosongkan.
3. **Read** - data selalu ditampilkan di tabel. Saat sebuah baris diklik, `selectedIndex` diisi nomor baris, form terisi data baris tersebut, tombol berubah menjadi **Update**, dan tombol **Delete** muncul.
4. **Update** - user mengubah isi form lalu menekan **Update**. Karena `selectedIndex != -1`, program memanggil `updateData()`: data pada `listPerson` diganti, tabel diperbarui, form dikosongkan.
5. **Delete** - user menekan **Delete**. Muncul dialog konfirmasi; jika memilih **Yes**, `deleteData()` menghapus data dari `listPerson` dan tabel diperbarui. Jika memilih **No**, tidak ada yang terjadi.
6. **Cancel** - `clearForm()` mengosongkan form, mengembalikan tombol ke **Add**, menyembunyikan **Delete**, dan mengatur `selectedIndex = -1`.

## 📁 Struktur Folder

```
TP4DPBO2526C.../
├── PeopleData/
│   ├── Person.java
│   ├── PeopleMenu.java
│   └── PeopleMenu.form
├── Dokumentasi/
│   └── (screenshot)
└── README.md
```


## 📷 Dokumentasi Saat Program Dijalankan

### 1. Tampilan awal (Read)
![TampilanAwal](Dokumentasi/Tampilan%20Awal.png)

### 2. Create - menambah data
![Create](Dokumentasi/Tambah%20Data.png)

### 3. Update - mengubah data
![Update](Dokumentasi/Update%20Data.png)

### 4. Delete - dengan konfirmasi
![KonfirmasiDelete](Dokumentasi/Konfirmasi%20Hapus%20Data.png)
![Delete](Dokumentasi/Delete%20Data.png)

### 5. ID dan Nama Kosong
![IDNamaEmpty](Dokumentasi/ID%20dan%20Nama%20Empty.png)

### 6. Kategori dan Golongan Darah Kosong
![KategoriGolrahEmpty](Dokumentasi/Kategori%20dan%20Golrah%20Empty.png)

### 7. Email Tidak Valid
![EmailTidakValid](Dokumentasi/Email%20Tidak%20Valid.png)

### 8. Tahun Lahir Bukan Angka
![TahunLahirBukanAngka](Dokumentasi/Tahun%20Lahir%20Bukan%20Angka.png)

### 9. ID Sudah Digunakan
![IDDigunakan](Dokumentasi/ID%20Sudah%20Digunakan.png)
