package com.tdiprima.medicalimageprocessing;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReader;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReaderSpi;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;
import org.dcm4che3.data.Tag;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.dcm4che3.imageio.codec.TransferSyntaxType;
import org.dcm4che3.imageio.codec.Decompressor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Extract text from DICOM file.
 *
 * @author tdiprima
 */
public class DicomTextExtractor {
    public static final String TESSDATA_DIR = "/usr/local/Cellar/tesseract/5.5.0/share/tessdata"; // macOS
//    public static final String TESSDATA_DIR = "/usr/share/tesseract-ocr/5/tessdata"; // Ubuntu

    public static void main(String[] args) {
        String dicomFilePath = "src/main/resources/test_image.dcm";

        try {
            // Check if the DICOM file contains an image
            if (!isDicomImage(dicomFilePath)) {
                System.out.println("\nThe DICOM file does not contain an image.");
                return;
            }

            // Print pixel data information for debugging
            printPixelDataInfo(dicomFilePath);

            // Read DICOM image
            BufferedImage dicomImage = readDicomImage(dicomFilePath);

            if (dicomImage != null) {
                // Check if the image is blank
                if (isImageBlank(dicomImage)) {
                    System.out.println("\nThe DICOM image is blank. Skipping processing.");
                    return;
                }

                // Perform OCR using Tesseract
                String extractedText = performOCR(dicomImage);

                // Output the extracted text
                System.out.println("\nExtracted Text: \n" + extractedText);
            } else {
                System.out.println("\nFailed to process DICOM file: " + dicomFilePath);
            }

            // Print metadata for debugging
            printMetadata(dicomFilePath);

        } catch (Exception e) {
            System.err.println("\nError in main(): " + e.getMessage());
        }
    }

    /**
     * TODO: Need better test for burned-in text
     *
     * @param dicomFilePath
     * @return
     */
    private static boolean isDicomImage(String dicomFilePath) {
        try (DicomInputStream dicomInputStream = new DicomInputStream(new File(dicomFilePath))) {
            Attributes metadata = dicomInputStream.readDataset(-1, -1);

            // Check if pixel data exists in the file
            if (!metadata.contains(Tag.PixelData)) {
                System.out.println("\nNo pixel data found in the DICOM file.");
                return false; // No image data in this DICOM file
            }

            // Optionally, check SOP Class UID for known image types (e.g., CT, MR, Ultrasound)
            String sopClassUID = metadata.getString(Tag.SOPClassUID, "");
            System.out.println("\nSOP Class UID: " + sopClassUID);

            return true; // The file contains image data
        } catch (Exception e) {
            System.err.println("\nError checking DICOM image: " + e.getMessage());
            return false;
        }
    }

    private static BufferedImage readDicomImage(String dicomFilePath) throws IOException {
        if (!hasPixelData(dicomFilePath)) {
            System.out.println("\nThe DICOM file does not contain Pixel Data. No image to process.");
            return null;
        }

        DicomImageReader reader = (DicomImageReader) new DicomImageReaderSpi().createReaderInstance();

        try (DicomInputStream dicomInputStream = new DicomInputStream(new File(dicomFilePath))) {
            reader.setInput(dicomInputStream);

            // Attempt to read the image
            return reader.read(0);
        } catch (UnsatisfiedLinkError e) {
            System.err.println("\nError: OpenCV native library not loaded.\nPlease ensure OpenCV is correctly installed and the native library is available.");
            System.err.println(e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("\nError reading DICOM image: " + e.getMessage());
            System.out.println("Attempting to decompress the file...");

            // Decompress the file and retry
            File decompressedFile = decompressDicom(dicomFilePath);
            if (decompressedFile != null) {
                try (DicomInputStream decompressedInputStream = new DicomInputStream(decompressedFile)) {
                    reader.setInput(decompressedInputStream);
                    return reader.read(0); // Retry reading with the decompressed file
                } catch (Exception ex) {
                    System.err.println("Error reading decompressed DICOM image: " + ex.getMessage());
                }
            }

            return null; // Return null if decompression or reading fails

        } finally {
            reader.dispose();
        }
    }

    private static String performOCR(BufferedImage image) {
        // Configure Tesseract instance
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(TESSDATA_DIR); // Set the tessdata folder path
        tesseract.setLanguage("eng"); // Set the language for OCR

        try {
            // Perform OCR on the BufferedImage
            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            System.err.println("\nError during OCR: " + e.getMessage());
            return null;
        }
    }

    private static File decompressDicom(String dicomFilePath) {
        File decompressedFile = new File("decompressed_" + new File(dicomFilePath).getName());
        try (DicomInputStream dicomInputStream = new DicomInputStream(new File(dicomFilePath)); DicomOutputStream dicomOutputStream = new DicomOutputStream(decompressedFile)) {

            Attributes metadata = dicomInputStream.readDataset(-1, Tag.PixelData);
            Attributes fileMeta = dicomInputStream.readFileMetaInformation();

            // Decompress if required
            if (TransferSyntaxType.forUID(fileMeta.getString(Tag.TransferSyntaxUID)) == TransferSyntaxType.NATIVE) {
                System.out.println("File is already uncompressed.");
            } else {
                Decompressor decompressor = new Decompressor(metadata, fileMeta.getString(Tag.TransferSyntaxUID));
                decompressor.decompress();
            }

            // Write decompressed output
            dicomOutputStream.writeFileMetaInformation(fileMeta);
            metadata.writeTo(dicomOutputStream);

            System.out.println("Decompressed DICOM file saved to: " + decompressedFile.getAbsolutePath());
            return decompressedFile;

        } catch (IOException e) {
            System.err.println("Error decompressing DICOM file: " + e.getMessage());
            return null;
        }
    }

    private static boolean hasPixelData(String dicomFilePath) {
        try (DicomInputStream dicomInputStream = new DicomInputStream(new File(dicomFilePath))) {
            Attributes metadata = dicomInputStream.readDataset(-1, -1);
            // Check if Pixel Data tag exists
            return metadata.contains(Tag.PixelData);
        } catch (IOException e) {
            System.err.println("Error checking Pixel Data: " + e.getMessage());
            return false;
        }
    }

    private static void printMetadata(String dicomFilePath) {
        try (DicomInputStream dicomInputStream = new DicomInputStream(new File(dicomFilePath))) {
            Attributes metadata = dicomInputStream.readDataset(-1, -1);
            System.out.println("\nDICOM Metadata:");
            System.out.println(metadata.toString());
        } catch (IOException e) {
            System.err.println("Error reading metadata: " + e.getMessage());
        }
    }

    private static void printPixelDataInfo(String dicomFilePath) {
        try (DicomInputStream dicomInputStream = new DicomInputStream(new File(dicomFilePath))) {
            Attributes metadata = dicomInputStream.readDataset(-1, -1);
            System.out.println("Transfer Syntax: " + metadata.getString(Tag.TransferSyntaxUID));
            System.out.println("Rows: " + metadata.getInt(Tag.Rows, -1));
            System.out.println("Columns: " + metadata.getInt(Tag.Columns, -1));
            System.out.println("Bits Allocated: " + metadata.getInt(Tag.BitsAllocated, -1));

            if (!metadata.contains(Tag.PixelData)) {
                System.out.println("Pixel Data is missing.");
            } else {
                System.out.println("Pixel Data is present.");
            }
        } catch (IOException e) {
            System.err.println("Error reading DICOM metadata: " + e.getMessage());
        }
    }

    private static boolean isImageBlank(BufferedImage image) {
        // Simple check: ensure the image is not completely white or transparent
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if ((image.getRGB(x, y) & 0xFFFFFF) != 0xFFFFFF) {
                    return false; // Found a non-white pixel
                }
            }
        }
        return true;
    }
}
