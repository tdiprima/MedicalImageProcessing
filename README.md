# Medical Image Processing 🩺 📷

This Java Maven project demonstrates three essential techniques for working with medical images:

- **Tesseract OCR** - Extract text from an image file.
- **DCM4CHE** - Read and process DICOM medical image files.
- **Bio-Formats** - Analyze bio-imaging formats such as OME-TIFF.

## Getting started 🚀

```sh
mvn clean install
java -jar target/MedicalImageProcessing-1.0-jar-with-dependencies.jar
```

**Note:** If the operating system throws a segmentation fault during execution, it means you didn't set your `TESSDATA_DIR` correctly.

## Prerequisites
- Java 8+ installed.
- Maven installed.
- `tessdata` folder configured for Tesseract (download [here](https://github.com/tesseract-ocr/tessdata)).

<br>
