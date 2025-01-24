package com.tdiprima.testing;

import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.data.Attributes;
import java.io.File;
import java.io.IOException;

/**
 * For Java DICOM file reading, I recommend using the dcm4che library.
 * Here's a robust approach to prevent silent failures.
 * 
 * @author tdiprima
 */
public class DicomReader {

    public static void main(String[] args) {
        try {
            File dicomFile = new File("file.dcm");
            DicomInputStream dis = new DicomInputStream(dicomFile);
            Attributes attributes = dis.readDataset(-1, -1);
            System.out.println("attributes:\n" + attributes);

            // Log or print file contents to verify reading
            System.out.println("Successfully read DICOM file: " + dicomFile.getName());
            dis.close();
        } catch (IOException e) {
            // Explicitly log the error
            e.printStackTrace();
            System.err.println("Failed to read DICOM file: " + e.getMessage());
        }
    }
}
