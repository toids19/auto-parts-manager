package org.example.View;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.Model.Bill;
import org.example.Repository.BillRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@SuppressWarnings("ALL")
public class BillListView extends JPanel {

    private DefaultTableModel tableModel;
    private final BillRepository billRepository;

    public BillListView() {
        billRepository = new BillRepository();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        refreshBillsTable();

        JTable billTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        billTable.setPreferredScrollableViewportSize(new Dimension(600, 200));

        JScrollPane scrollPane = new JScrollPane(billTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

        JButton refreshButton = new JButton("REFRESH");
        JButton generateInvoiceButton = new JButton("GENEREAZĂ FACTURĂ");

        refreshButton.addActionListener(e -> refreshBillsTable());
        generateInvoiceButton.addActionListener(e -> generateSelectedInvoice(billTable));

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(refreshButton);
        buttonPanel.add(generateInvoiceButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshBillsTable() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        List<Bill> bills = billRepository.getAllBillsForCurrentUser();

        if (!bills.isEmpty()) {
            Class<?> billClass = bills.get(0).getClass();
            for (java.lang.reflect.Field field : billClass.getDeclaredFields()) {
                tableModel.addColumn(field.getName());
            }

            for (Bill bill : bills) {
                Object[] rowData = new Object[billClass.getDeclaredFields().length];
                int i = 0;
                for (java.lang.reflect.Field field : billClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
                        rowData[i] = field.get(bill);
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                    i++;
                }
                tableModel.addRow(rowData);
            }
        }
    }

    private void generateSelectedInvoice(JTable billTable) {
        int selectedRow = billTable.getSelectedRow();
        if (selectedRow != -1) {
            Bill selectedBill = getBillFromSelectedRow(selectedRow);
            generatePDFInvoice(selectedBill);
        } else {
            JOptionPane.showMessageDialog(this, "Te rog selectează o comandă pentru a genera factura.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Bill getBillFromSelectedRow(int selectedRow) {
        Bill bill = null;
        try {
            bill = new Bill(
                    (Long) tableModel.getValueAt(selectedRow, 0),
                    (String) tableModel.getValueAt(selectedRow, 1),
                    (int) tableModel.getValueAt(selectedRow, 2),
                    (double) tableModel.getValueAt(selectedRow, 3),
                    (String) tableModel.getValueAt(selectedRow, 4),
                    (String) tableModel.getValueAt(selectedRow, 5),
                    (String) tableModel.getValueAt(selectedRow, 6),
                    (String) tableModel.getValueAt(selectedRow, 7),
                    (String) tableModel.getValueAt(selectedRow, 8),
                    (Timestamp) tableModel.getValueAt(selectedRow, 9)
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bill;
    }


    public  void generatePDFInvoice(Bill bill) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Load the logo image
            PDImageXObject logo = PDImageXObject.createFromFile("/Users/moldovanutudor/Desktop/PT/PT2024_30421_Moldovanu_Tudor/pt2024_30421_moldovanu_tudor_assignment_3/Pictures/novocar_negru.png", document);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            float rowHeight = 20; // Increased row height to prevent overlapping
            float cellMargin = 5;

            // Draw logo
            contentStream.drawImage(logo, margin, yStart - logo.getHeight(), logo.getWidth(), logo.getHeight());

            // Move cursor below the logo
            yPosition -= logo.getHeight() + margin;

            String[][] content = {
                    {"Numarul Comenzii:", bill.getOrderId().toString()},
                    {"Numele Produsului:", replaceProblematicCharacters(bill.getProductName())},
                    {"Cantitate:", Integer.toString(bill.getQuantity())},
                    {"Pret Total:", Double.toString(bill.getTotalPrice())},
                    {"Prenume Utilizator:", replaceProblematicCharacters(bill.getFirstName())},
                    {"Nume Utilizator:", replaceProblematicCharacters(bill.getLastName())},
                    {"Adresa:", replaceProblematicCharacters(bill.getAddress())},
                    {"Numar de Telefon:", replaceProblematicCharacters(bill.getPhoneNumber())},
                    {"Comanda creata la:", bill.getCreatedAt().toString()},
                    {"", ""},
                    {"", ""},
                    {"", ""},
                    {"", ""},
                    {"Va multumim pentru achizitie!", ""},
                    {"", ""},
                    {"", ""},
                    {"", ""},
                    {"", ""},
                    {"", ""},
                    {"", ""},
                    {"Novocar S.R.L.", ""},
                    {"Str. Mihai Eminescu, nr. 13", ""},
                    {"Bucuresti, Romania", ""},
                    {"CUI: 12345678", ""},
                    {"IBAN: RO12BTRL1234567890123456", ""},
                    {"Banca Transilvania", ""},
                    {"SWIFT: BTRLRO22", ""},
                    {"Telefon: 0123456789", ""},
                    {"Email:contact@novocar.ro", ""}
            };

            drawTable(contentStream, tableWidth, yPosition, margin, rowHeight, cellMargin, content);

            contentStream.close();

            // Specify the destination directory
            String saveDirectory = "/Users/moldovanutudor/Desktop/PT/PT2024_30421_Moldovanu_Tudor/pt2024_30421_moldovanu_tudor_assignment_3/GeneratedBills";
            File destinationDirectory = new File(saveDirectory);
            if (!destinationDirectory.exists()) {
                destinationDirectory.mkdirs(); // Create the directory if it doesn't exist
            }

            // Save the PDF invoice to the specified directory
            String fileName = "Invoice_" + bill.getOrderId() + ".pdf";
            File destinationFile = new File(destinationDirectory, fileName);
            document.save(destinationFile);

            document.close();

            // Display success message
            JOptionPane.showMessageDialog(this, "Factura a fost generata cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la generarea facturii.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    public  void drawTable(PDPageContentStream contentStream, float tableWidth, float yStart, float margin,
                           float rowHeight, float cellMargin, String[][] content) throws IOException {
        final int rows = content.length;
        final int cols = 2; // Assuming 2 columns: Label and Value

        // Calculating column width
        final float colWidth = tableWidth / (float) cols;

        // Drawing rows
        float y = yStart;
        for (int i = 0; i < rows; i++) {
            float x = margin;
            for (int j = 0; j < cols; j++) {
                String cellValue = content[i][j];
                contentStream.beginText();
                contentStream.moveTextPositionByAmount(x + cellMargin, y - rowHeight + cellMargin);
                contentStream.drawString(cellValue);
                contentStream.endText();
                x += colWidth;
            }
            y -= rowHeight;
        }
    }

    public  String replaceProblematicCharacters(String input) {
        // Replace problematic characters with ones supported by the font
        return input.replaceAll("ă", "a"); // Replace 'ă' with 'a', adjust as needed
    }




}
