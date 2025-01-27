package com.tdiprima.medicalimageprocessing;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Read and process DICOM medical image files.
 * 
 * @author tdiprima
 */
public class DCM4CHE_Demo {

    public static void main(String[] args) throws Exception {
        File dicomFile = new File("src/main/resources/test_image_dcm4che.dcm");
        try (DicomInputStream dis = new DicomInputStream(dicomFile)) {
            Attributes dataset = dis.readDataset(-1, -1);
            System.out.println("DICOM Metadata: ");
            dataset.accept((attr, tag, vr, value) -> {
                System.out.println(attr);
                return true;
            }, false);
        } catch (IOException e) {
            System.err.println("Error reading DICOM file: " + e.getMessage());
        }
    }
}
