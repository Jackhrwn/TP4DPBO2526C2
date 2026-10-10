import javax.swing.*;                               // Mengimpor komponen GUI Swing
import javax.swing.table.DefaultTableCellRenderer;  // Mengimpor renderer untuk kustomisasi sel tabel
import javax.swing.table.DefaultTableModel;         // Mengimpor model data untuk JTable
import javax.swing.table.JTableHeader;              // Mengimpor komponen header tabel
import java.awt.*;                                  // Mengimpor kelas AWT untuk komponen grafis dan warna
import java.awt.event.ActionEvent;                  // Mengimpor kelas penanganan event tombol
import java.awt.event.ActionListener;               // Mengimpor interface pendengar event tindakan
import java.awt.event.MouseAdapter;                 // Mengimpor kelas adaptor untuk event mouse
import java.awt.event.MouseEvent;                   // Mengimpor kelas penampung informasi event mouse
import java.util.ArrayList;                         // Mengimpor struktur data ArrayList

public class PeopleMenu extends JFrame {
    // component GUI utama
    private JPanel mainPanel;                       // Panel utama penampung seluruh elemen GUI
    private JLabel titleLabel;                      // Label untuk judul aplikasi
    private JTextField idField;                     // Field input untuk ID person
    private JTextField namaField;                   // Field input untuk nama person
    private JTextField tahunLahirField;             // Field input untuk tahun lahir person
    private JTextField emailField;                  // Field input untuk alamat email person
    private JComboBox<String> kategoriComboBox;     // Dropdown pilihan kategori person
    private JComboBox<String> golonganDarahComboBox;// Dropdown pilihan golongan darah person
    private JRadioButton lakiLakiRadioButton;       // Radio button pilihan jenis kelamin Laki-Laki
    private JRadioButton perempuanRadioButton;      // Radio button pilihan jenis kelamin Perempuan
    private ButtonGroup jenisKelaminGroup;          // Grup untuk mengelompokkan radio button jenis kelamin
    private JButton addUpdateButton;                // Tombol untuk menambah atau memperbarui data
    private JButton cancelButton;                   // Tombol untuk membatalkan aksi/reset form
    private JButton deleteButton;                   // Tombol untuk menghapus data terpilih
    private JTable peopleTable;                     // Komponen tabel penampil daftar person

    // data
    private ArrayList<Person> listPerson;           // Penampung objek-objek Person dalam memori
    private int selectedIndex = -1;                 // -1 = tidak ada baris yang dipilih

    public static void main(String[] args) {
        // buat object window
        PeopleMenu menu = new PeopleMenu();

        // atur ukuran window
        menu.setSize(900, 650);

        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // constructor
    public PeopleMenu() {
        // inisialisasi listPerson
        listPerson = new ArrayList<>();

        // isi listPerson
        populateList();

        // isi tabel
        refreshTable();

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 22f));

        // atur isi combo box kategori
        String[] kategoriData = { "???", "Mahasiswa", "Dosen", "Staf", "Alumni" };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // atur isi combo box golongan darah (atribut baru)
        String[] golDarahData = { "???", "A", "B", "AB", "O" };
        golonganDarahComboBox.setModel(new DefaultComboBoxModel<>(golDarahData));

        // kelompokkan radio button jenis kelamin
        jenisKelaminGroup = new ButtonGroup();
        jenisKelaminGroup.add(lakiLakiRadioButton);
        jenisKelaminGroup.add(perempuanRadioButton);

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // saat tombol delete ditekan -> minta konfirmasi dulu
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pilihan = JOptionPane.showConfirmDialog(
                        null,
                        "Apakah kamu yakin ingin menghapus data ini?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                // hapus hanya jika user memilih "Yes"
                if (pilihan == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // saat salah satu baris tabel ditekan
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = peopleTable.getSelectedRow();

                // abaikan jika klik di area kosong tabel
                if (selectedIndex == -1) {
                    return;
                }

                // simpan value (kolom 0 = "No", data dimulai dari kolom 1)
                String curId = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curTahun = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curGolDarah = peopleTable.getModel().getValueAt(selectedIndex, 5).toString();
                String curJenisKelamin = peopleTable.getModel().getValueAt(selectedIndex, 6).toString();
                String curEmail = peopleTable.getModel().getValueAt(selectedIndex, 7).toString();

                // ubah isi textfield dan combo box
                idField.setText(curId);
                namaField.setText(curNama);
                tahunLahirField.setText(curTahun);
                kategoriComboBox.setSelectedItem(curKategori);
                golonganDarahComboBox.setSelectedItem(curGolDarah);
                emailField.setText(curEmail);
                if (curJenisKelamin.equals("Laki-Laki")) {
                    lakiLakiRadioButton.setSelected(true);
                } else {
                    perempuanRadioButton.setSelected(true);
                }

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");

                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    // mengisi tabel dari listPerson
    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = { "No", "ID", "Nama", "Tahun Lahir", "Kategori", "Gol. Darah", "Jenis Kelamin", "Email" };

        // buat objek tabel (sel tidak bisa diedit langsung)
        DefaultTableModel tmp = new DefaultTableModel(null, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // isi tabel dengan listPerson
        for (int i = 0; i < listPerson.size(); i++) {
            Object[] row = {
                    i + 1,
                    listPerson.get(i).getId(),
                    listPerson.get(i).getNama(),
                    listPerson.get(i).getTahunLahir(),
                    listPerson.get(i).getKategori(),
                    listPerson.get(i).getGolonganDarah(),
                    listPerson.get(i).getJenisKelamin(),
                    listPerson.get(i).getEmail()
            };
            tmp.addRow(row);
        }

        return tmp;
    }

    // memuat ulang isi tabel + menerapkan styling
    // (styling harus diterapkan ulang karena setModel() membuat ulang kolom tabel)
    private void refreshTable() {
        peopleTable.setModel(setTable());
        styleTable();
    }

    // mempercantik tampilan tabel
    private void styleTable() {
        peopleTable.setRowHeight(28);
        peopleTable.setShowVerticalLines(false);
        peopleTable.setShowHorizontalLines(true);
        peopleTable.setGridColor(new Color(225, 228, 232));
        peopleTable.setIntercellSpacing(new Dimension(0, 1));
        peopleTable.setSelectionBackground(new Color(190, 215, 245));
        peopleTable.setSelectionForeground(Color.BLACK);
        peopleTable.setFillsViewportHeight(true);
        peopleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // header: latar gelap, teks putih tebal di tengah
        JTableHeader header = peopleTable.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 32));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
                setHorizontalAlignment(CENTER);
                setOpaque(true);
                setBackground(new Color(44, 62, 80));
                setForeground(Color.WHITE);
                setFont(getFont().deriveFont(Font.BOLD));
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        // isi sel: baris belang-belang, kolom Nama, Kategori & Email rata kiri, sisanya di tengah
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
                setHorizontalAlignment((column == 2 || column == 4 || column == 7) ? LEFT : CENTER);
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 252));
                    setForeground(Color.BLACK);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        };

        // lebar kolom: No, ID, Nama, Tahun Lahir, Kategori, Gol. Darah, Jenis Kelamin, Email
        int[] widths = { 45, 65, 140, 80, 95, 75, 110, 180 };
        for (int i = 0; i < widths.length; i++) {
            peopleTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
            peopleTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        peopleTable.getColumnModel().getColumn(0).setMaxWidth(60);
    }

    // Metode untuk memeriksa apakah ID sudah ada dalam listPerson
    private boolean isIdExists(String id) {
        for (int i = 0; i < listPerson.size(); i++) {                              // Iterasi seluruh data person
            if (i == selectedIndex) continue;                                      // Lewati pengecekan jika baris tersebut sedang di-update
            if (listPerson.get(i).getId().equalsIgnoreCase(id)) {                  // Periksa kecocokan ID (case-insensitive)
                return true;                                                       // ID ditemukan (duplikat)
            }                                                                      
        }                                                                          
        return false;                                                              // ID belum terdaftar (aman)
    }

    // validasi input form; return true jika semua valid
    private boolean validateForm() {
        String inputId = idField.getText().trim();                                 // Membaca teks ID dari field
        if (inputId.isEmpty() || namaField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "ID dan Nama tidak boleh kosong!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (isIdExists(inputId)) {
            JOptionPane.showMessageDialog(null, "ID '" + inputId + "' sudah digunakan! Gunakan ID lain.", "Error", 
                    JOptionPane.ERROR_MESSAGE); 
            return false;                                                                                           
        }
        if (kategoriComboBox.getSelectedIndex() == 0 || golonganDarahComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Kategori dan Golongan Darah harus dipilih!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // jenis kelamin harus dipilih
        if (!lakiLakiRadioButton.isSelected() && !perempuanRadioButton.isSelected()) {
            JOptionPane.showMessageDialog(null, "Jenis Kelamin harus dipilih!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // format email sederhana: nama@domain.tld
        if (!emailField.getText().trim().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            JOptionPane.showMessageDialog(null, "Format email tidak valid!\nContoh: nama@domain.com", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // CREATE
    public void insertData() {
        try {
            if (!validateForm()) {
                return;
            }

            // ambil value dari textfield dan combobox
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            int tahunLahir = Integer.parseInt(tahunLahirField.getText().trim());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String golDarah = golonganDarahComboBox.getSelectedItem().toString();
            String jenisKelamin = lakiLakiRadioButton.isSelected() ? "Laki-Laki" : "Perempuan";
            String email = emailField.getText().trim();

            // tambahkan data ke dalam list
            listPerson.add(new Person(id, nama, tahunLahir, kategori, golDarah, jenisKelamin, email));

            // update tabel
            refreshTable();

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE
    public void updateData() {
        try {
            if (!validateForm()) {
                return;
            }

            // ambil data dari form
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            int tahunLahir = Integer.parseInt(tahunLahirField.getText().trim());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String golDarah = golonganDarahComboBox.getSelectedItem().toString();
            String jenisKelamin = lakiLakiRadioButton.isSelected() ? "Laki-Laki" : "Perempuan";
            String email = emailField.getText().trim();

            // ubah data person di list
            listPerson.get(selectedIndex).setId(id);
            listPerson.get(selectedIndex).setNama(nama);
            listPerson.get(selectedIndex).setTahunLahir(tahunLahir);
            listPerson.get(selectedIndex).setKategori(kategori);
            listPerson.get(selectedIndex).setGolonganDarah(golDarah);
            listPerson.get(selectedIndex).setJenisKelamin(jenisKelamin);
            listPerson.get(selectedIndex).setEmail(email);

            // update tabel
            refreshTable();

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE
    public void deleteData() {
        // hapus data dari list
        listPerson.remove(selectedIndex);

        // update tabel
        refreshTable();

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Delete berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    // mengosongkan form dan mengembalikan ke mode "Add"
    public void clearForm() {
        // kosongkan semua textfield dan combo box
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        emailField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        golonganDarahComboBox.setSelectedIndex(0);
        jenisKelaminGroup.clearSelection();

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;

        // hilangkan highlight baris pada tabel
        peopleTable.clearSelection();
    }

    // data awal (hardcoded)
    private void populateList() {
        listPerson.add(new Person("P001", "Repan", 2003, "Mahasiswa", "A", "Laki-Laki", "repan@student.upi.edu"));
        listPerson.add(new Person("P002", "Bu rosa", 1985, "Dosen", "B", "Perempuan", "rosa@upi.edu"));
        listPerson.add(new Person("P003", "Jamal", 1990, "Staf", "O", "Laki-Laki", "jamal@upi.edu"));
        listPerson.add(new Person("P004", "Rian", 2002, "Mahasiswa", "AB", "Laki-Laki", "rian@student.upi.edu"));
        listPerson.add(new Person("P005", "Marco", 1998, "Alumni", "O", "Laki-Laki", "marco@gmail.com"));
        listPerson.add(new Person("P006", "Pak Herbert", 1980, "Dosen", "A", "Laki-Laki", "herbert@upi.edu"));
        listPerson.add(new Person("P007", "Rizky Febian", 2004, "Mahasiswa", "B", "Laki-Laki", "rizky@student.upi.edu"));
        listPerson.add(new Person("P008", "Maya", 1995, "Staf", "AB", "Perempuan", "maya@upi.edu"));
    }
}