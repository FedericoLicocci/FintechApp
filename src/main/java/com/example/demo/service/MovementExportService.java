package com.example.demo.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.RequiredArgsConstructor;

import com.example.demo.model.Movement;

//@RequiredArgsConstructor
@Service
public class MovementExportService {

    private final MovementService movementService;

    public MovementExportService(MovementService movementService) {
        this.movementService = movementService;
    }

    public void exportToPdf(Integer senderId, OutputStream outputStream) throws IOException {
        List<Movement> movements = movementService.getLast5MovementsBySenderId(senderId);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.add(new Paragraph("Ultimi 5 Movimenti"));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(2);
            table.addCell("Data");
            //table.addCell("Descrizione");
            table.addCell("Importo");

            for (Movement m : movements) {
                table.addCell(m.getDate().toString());
                //table.addCell(m.getDescription());
                table.addCell(m.getAmount().toString());
            }

            document.add(table);
        } catch (DocumentException e) {
            throw new IOException("Errore durante la generazione del PDF", e);
        } finally {
            document.close();
        }
    }

    public void exportToExcel(Integer senderId, OutputStream outputStream) throws IOException {
        List<Movement> movements = movementService.getLast5MovementsBySenderId(senderId);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ultimi Movimenti");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Data");
            //headerRow.createCell(1).setCellValue("Descrizione");
            headerRow.createCell(2).setCellValue("Importo");

            int rowIdx = 1;
            for (Movement m : movements) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(m.getDate().toString());
                //row.createCell(1).setCellValue(m.getDescription());
                row.createCell(2).setCellValue(m.getAmount().doubleValue());
            }

            workbook.write(outputStream);
        }
    }
}
