package com.tdiprima.medicalimageprocessing;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.io.File;

/**
 * Extract text from an image file.
 * 
 * @author tdiprima
 */
public class TesseractDemo {
    public static void main(String[] args) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/usr/local/Cellar/tesseract/5.5.0/share/tessdata/");
        tesseract.setLanguage("eng");
        try {
            File imageFile = new File("src/main/resources/test_image_tesseract.png");
            String text = tesseract.doOCR(imageFile);
            System.out.println("Extracted text: \n" + text);
        } catch (TesseractException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
