package edu.msg.ro.export.csv;

import edu.msg.ro.business.bug.dto.BugDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CSV  {
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final Object[] FILE_HEADER = {"Title", "Description", "Status", "Severity", "Version", "Target Date", "Assigned To", "Created By", "Fixed in Version"};

    public CSV() {
    }

    public void generate(List<BugDTO> bugs){
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        try {
            fileWriter = new FileWriter(System.getProperty("user.home") + "\\bugs.csv");
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(FILE_HEADER);
            for(BugDTO b:bugs){
                List<String> list = new ArrayList<>();
                String targetDate = new SimpleDateFormat("yyyy-MM-dd").format(b.getTargetDate());

                list.add(b.getTitle());
                list.add(b.getDescription());
                list.add(b.getStatus().toString());
                list.add(b.getSeverity());
                list.add(b.getVersion());
                list.add(targetDate);
                list.add(b.getAssignedTo().getUsername());
                list.add(b.getCreatedBy().getUsername());
                list.add(b.getFixedInVersion());
                csvFilePrinter.printRecord(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
