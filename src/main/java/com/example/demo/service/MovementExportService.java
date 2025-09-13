package com.example.demo.service;

//Import delle librerie Java
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

//Import librerie spring
import org.springframework.stereotype.Service;

//Import libreria iText (PDF)
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

//Import librerie apache (Excel)
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.RequiredArgsConstructor;
//Import classi del progetto
import com.example.demo.model.Movement;

//@RequiredArgsConstructor
@Service
public class MovementExportService {


    private final MovementService movementService;

    public MovementExportService(MovementService movementService) {
        this.movementService = movementService;
    }

    //Esporta gli ultimi 5 movimenti in formato PDF
    public void exportToPdf(String ibanSender, OutputStream outputStream) throws IOException {

        //Recupera gli ultimi 5 movimenti
        List<Movement> movements = movementService.getLast5ByIbanSenderOrderByDateDesc(ibanSender);

        Document document = new Document();
        try {
            //Collega il documento PDF all output stream
            PdfWriter.getInstance(document, outputStream);
            document.open();

            //Titolo del documento + spazio
            document.add(new Paragraph("Ultimi 5 Movimenti"));
            document.add(new Paragraph(" "));

            //Generazione della tabella, con 4 colonne
            PdfPTable table = new PdfPTable(4);
            table.addCell("Destinatario");
            table.addCell("Importo");
            table.addCell("Causale");
            table.addCell("Data");

            //Aggiunta dei dati dei movimenti nella tabella
            for (Movement m : movements) {

                table.addCell(m.getIbanReceiver());
                table.addCell(m.getAmount().toString());
                table.addCell(m.getCausale());
                table.addCell(m.getExecutionDate().toString());
            }

            //Inserisce la tabella nel documento
            document.add(table);
        } catch (DocumentException e) {
            //Mostra un messaggio in caso di errore durante la generazione del PDF
            throw new IOException("Errore durante la generazione del PDF", e);
        } finally {
            //Chiude il documento
            document.close();
        }
    }

    //Esporta gli ultimi 5 movimenti in formato Excel
    public void exportToExcel(String ibanSender, OutputStream outputStream) throws IOException {
        List<Movement> movements = movementService.getLast5ByIbanSenderOrderByDateDesc(ibanSender);

        try (Workbook workbook = new XSSFWorkbook()) {
            //Genera un foglio Excel denominato "Ultimi movimenti"
            Sheet sheet = workbook.createSheet("Ultimi Movimenti");

            //Aggiunge i nomi delle colonne
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Destinatario");
            headerRow.createCell(1).setCellValue("Importo");
            headerRow.createCell(2).setCellValue("Causale");
            headerRow.createCell(3).setCellValue("Data");

            //Riempie il file con i dati estratti, una riga per ogni movimento bancario
            int rowIdx = 1;
            for (Movement m : movements) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(m.getIbanReceiver());
                row.createCell(1).setCellValue(m.getAmount().doubleValue());
                row.createCell(2).setCellValue(m.getCausale());
                row.createCell(3).setCellValue(m.getExecutionDate().toString());
            }

            //Scrive il file nello stream di output
            workbook.write(outputStream);
        }
    }

}

