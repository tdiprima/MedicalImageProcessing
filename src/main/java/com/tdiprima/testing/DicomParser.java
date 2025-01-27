package com.tdiprima.testing;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses DICOM files to read and validate tags, value representations (VR), 
 * and metadata structures.
 * 
 * @author tdiprima
 */
public class DicomParser {

    // DICOM Value Representations (VR)
    private static final Map<String, String> VR_TYPES = new HashMap<>() {
        {
            put("AE", "Application Entity");
            put("AS", "Age String");
            put("AT", "Attribute Tag");
            put("CS", "Code String");
            put("DA", "Date");
            put("DS", "Decimal String");
            put("DT", "Date Time");
            put("FL", "Floating Point Single");
            put("FD", "Floating Point Double");
            put("IS", "Integer String");
            put("LO", "Long String");
            put("LT", "Long Text");
            put("OB", "Other Byte");
            put("OD", "Other Double");
            put("OF", "Other Float");
            put("OL", "Other Long");
            put("OV", "Other Very Long");
            put("PN", "Person Name");
            put("SH", "Short String");
            put("SL", "Signed Long");
            put("SQ", "Sequence");
            put("SS", "Signed Short");
            put("ST", "Short Text");
            put("TM", "Time");
            put("UC", "Unlimited Characters");
            put("UI", "Unique Identifier");
            put("UL", "Unsigned Long");
            put("UN", "Unknown");
            put("UR", "URI/URL");
            put("US", "Unsigned Short");
            put("UT", "Unlimited Text");
        }
    };

    public static void parseDicomFile(String filePath) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath))) {
            // Skip 128-byte preamble
            bis.skip(128);

            // Check DICM header
            byte[] dicmHeader = new byte[4];
            bis.read(dicmHeader);
            if (!"DICM".equals(new String(dicmHeader))) {
                throw new IOException("Invalid DICOM file");
            }

            // Parse tags
            while (bis.available() > 0) {
                byte[] tagBytes = new byte[4];
                bis.read(tagBytes);

                // Convert to group and element
                int group = ByteBuffer.wrap(tagBytes).order(ByteOrder.LITTLE_ENDIAN).getShort(0) & 0xFFFF;
                int element = ByteBuffer.wrap(tagBytes).order(ByteOrder.LITTLE_ENDIAN).getShort(2) & 0xFFFF;

                // Read VR
                byte[] vrBytes = new byte[2];
                bis.read(vrBytes);
                String vr = new String(vrBytes);

                // Validate VR
                if (!VR_TYPES.containsKey(vr)) {
                    System.err.println("Unknown VR: " + vr);
                    continue;
                }

                // Read length (depends on VR)
                int length;
                if (vr.equals("OB") || vr.equals("OW") || vr.equals("SQ") || vr.equals("UN")) {
                    bis.skip(2); // reserved bytes
                    length = ByteBuffer.wrap(bis.readNBytes(4)).order(ByteOrder.LITTLE_ENDIAN).getInt();
                } else {
                    length = ByteBuffer.wrap(vrBytes).order(ByteOrder.LITTLE_ENDIAN).getShort(2) & 0xFFFF;
                }

                System.out.printf("Tag: (%04X,%04X), VR: %s (%s), Length: %d%n",
                        group, element, vr, VR_TYPES.get(vr), length);

                // Skip actual data for this example
                bis.skip(length);
            }
        }
    }

    public static void main(String[] args) {
        try {
            parseDicomFile("file.dcm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
