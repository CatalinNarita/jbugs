package edu.msg.ro.export.pdf;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import edu.msg.ro.business.bug.dto.BugDTO;

/**
 * Created by catal on 5/12/2017.
 */
public class PDF{

    public PDF(){}

    public void generate(List<BugDTO> bugs){

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("user.home") + "\\bugs.pdf"));
        }catch(Exception e){
            e.printStackTrace();
        }
        document.open();

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100.0f);
        
        float[] columnWidths = {10.0f,20.0f,12.0f,15.0f,10.0f,10.0f,10.0f,10.0f,10.0f};

        Font smallfont = new Font(Font.FontFamily.HELVETICA, 7);

        PdfPCell cell1 = new PdfPCell(new Phrase("Title",smallfont));
        PdfPCell cell2 = new PdfPCell(new Phrase("Description",smallfont));
        PdfPCell cell3 = new PdfPCell(new Phrase("Status",smallfont));
        PdfPCell cell4 = new PdfPCell(new Phrase("Severity",smallfont));
        PdfPCell cell5 = new PdfPCell(new Phrase("Version",smallfont));
        PdfPCell cell6 = new PdfPCell(new Phrase("Target Date",smallfont));
        PdfPCell cell7 = new PdfPCell(new Phrase("Assigned To",smallfont));
        PdfPCell cell8 = new PdfPCell(new Phrase("Created By",smallfont));
        PdfPCell cell9 = new PdfPCell(new Phrase("Fixed in Version",smallfont));

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        table.addCell(cell6);
        table.addCell(cell7);
        table.addCell(cell8);
        table.addCell(cell9);

        for(BugDTO b:bugs){
        	String targetDate = new SimpleDateFormat("yyyy-MM-dd").format(b.getTargetDate());

            table.addCell(new PdfPCell(new Phrase(b.getTitle(), smallfont)));
            table.addCell(new PdfPCell(new Phrase(b.getDescription(), smallfont)));
            table.addCell(new PdfPCell(new Phrase(b.getStatus().toString(), smallfont)));
            table.addCell(new PdfPCell(new Phrase(b.getSeverity(), smallfont)));
            table.addCell(new PdfPCell(new Phrase(b.getVersion(), smallfont)));
            table.addCell(new PdfPCell(new Phrase(targetDate, smallfont)));
            table.addCell(new PdfPCell(new Phrase(b.getAssignedTo().getUsername(), smallfont)));
            table.addCell(new PdfPCell(new Phrase(b.getCreatedBy().getUsername(), smallfont)));
            table.addCell(new PdfPCell(new Phrase(b.getFixedInVersion(), smallfont)));
        }
        try {
            document.add(table);
        }catch(Exception e){
            e.printStackTrace();
        }
        document.close();

    }

}