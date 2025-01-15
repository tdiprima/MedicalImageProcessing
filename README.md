# Medical Image Processing 🩺 📷

This Java Maven project demonstrates three essential techniques for working with medical images:

1. **Tesseract OCR** - Extract text from an image file.
2. **DCM4CHE** - Read and process DICOM medical image files.
3. **Bio-Formats** - Analyze bio-imaging formats such as OME-TIFF.

## Prerequisites
- Java 8+ installed.
- Maven installed.
- `tessdata` folder configured for Tesseract (download [here](https://github.com/tesseract-ocr/tessdata)).

## Getting Started
1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/medical-image-processing.git
   cd medical-image-processing
   ```

2. Build the project:

   ```bash
   mvn clean install
   ```

3. Run the demos:

   ```bash
   java -cp target/MedicalImageProcessing-1.0.jar com.tdiprima.medicalimageprocessing.TesseractDemo
   java -cp target/MedicalImageProcessing-1.0.jar com.tdiprima.medicalimageprocessing.DCM4CHE_Demo
   java -cp target/MedicalImageProcessing-1.0.jar com.tdiprima.medicalimageprocessing.BioFormatsDemo
   ```

<br>
