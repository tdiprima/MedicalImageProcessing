# Medical Image Processing 🩺 📷

This Java Maven project demonstrates three essential techniques for working with medical images:

### Java:

- **Tesseract OCR** - Extract text from an image file.
- **DCM4CHE** - Read and process DICOM medical image files.
- **Bio-Formats** - Analyze bio-imaging formats such as OME-TIFF.
- **DicomBufferedReader.java**: Reads a DICOM file using a buffered stream to validate its header and manage basic DICOM metadata.
- **DicomParser.java**: Parses DICOM files in Java to read and validate tags, value representations (VR), and metadata structures.
- **DicomReader.java**: Reads DICOM files in Java using the dcm4che3 library for detailed and reliable metadata parsing.
- **ProductionDicomParser.java**: Implements a production-ready approach to parse and log detailed DICOM metadata using the dcm4che3 library in Java.

### Python:

- **dicom_robust_reader.py**: Reads and validates DICOM files using Python, ensuring the file format is correct and handling errors robustly.
- **extract_dicom_accessions.py**: Extracts specific accession numbers or metadata from DICOM files in Python.

## Prerequisites
- Java 8+ installed.
- Maven installed.
- `tessdata` folder configured for Tesseract (download [here](https://github.com/tesseract-ocr/tessdata)).

<br>
