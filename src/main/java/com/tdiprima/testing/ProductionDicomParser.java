package com.tdiprima.testing;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.util.TagUtils;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * For production-grade DICOM parsing, I recommend using dcm4che3 - the 
 * industry-standard Java library for robust DICOM handling.
 * 
 * @author tdiprima
 */
public class ProductionDicomParser {

    private static final Logger LOGGER = Logger.getLogger(ProductionDicomParser.class.getName());

    public static void parseAndLogDicomFile(String filePath) {
        File dicomFile = new File(filePath);

        try (DicomInputStream dis = new DicomInputStream(dicomFile)) {
            // Configure input stream for comprehensive parsing
            dis.setIncludeBulkData(DicomInputStream.IncludeBulkData.URI);

            // Read entire dataset
            Attributes attributes = dis.readDataset(-1, -1);

            // Patient Information
            logTag(attributes, Tag.PatientName);
            logTag(attributes, Tag.PatientID);
            logTag(attributes, Tag.PatientBirthDate);

            // Study Details
            logTag(attributes, Tag.StudyInstanceUID);
            logTag(attributes, Tag.StudyDate);
            logTag(attributes, Tag.Modality);

            // Series Information
            logTag(attributes, Tag.SeriesInstanceUID);
            logTag(attributes, Tag.SeriesDescription);

            // Log transfer syntax for debugging
            String transferSyntax = dis.getTransferSyntax();
            LOGGER.info("Transfer Syntax: " + transferSyntax);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "DICOM parsing failed", e);
        }
    }

    private static void logTag(Attributes attrs, int tag) {
        String tagName = TagUtils.toHexString(tag);
        String value = attrs.getString(tag, "");

        if (!value.isEmpty()) {
            LOGGER.info(MessageFormat.format(
                    "Tag {0} ({1}): {2}",
                    tagName,
                    tag,
                    //                attrs.getTag(tag), 
                    value
            ));
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            LOGGER.severe("Provide DICOM file path");
            System.exit(1);
        }
        parseAndLogDicomFile(args[0]);
    }
}
