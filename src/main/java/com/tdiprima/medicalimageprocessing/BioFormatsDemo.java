package com.tdiprima.medicalimageprocessing;

import loci.formats.ImageReader;
import loci.formats.MetadataTools;
import loci.formats.meta.IMetadata;

public class BioFormatsDemo {
    public static void main(String[] args) {
        String filePath = "src/main/resources/test_image_bioformats.ome.tiff";

        // Create Metadata object
        IMetadata metadata = MetadataTools.createOMEXMLMetadata();

        try (ImageReader reader = new ImageReader()) {
            // Initialize the reader with metadata store
            reader.setMetadataStore(metadata);

            // Set the file path for the reader
            reader.setId(filePath);

            // Print metadata
            System.out.println("Extracted Metadata:");
            System.out.println("Image Count: " + reader.getImageCount());
            System.out.println("Image Dimensions: " + reader.getSizeX() + "x" + reader.getSizeY());
            System.out.println("Pixel Type: " + reader.getPixelType());
            System.out.println("Format: " + reader.getFormat());
            System.out.println("Core Metadata: " + reader.getCoreMetadataList());
            System.out.println("Global Metadata: " + reader.getGlobalMetadata());

            // You can iterate over more metadata using reader and metadata APIs
        } catch (Exception e) {
            System.err.println("Error reading file with Bio-Formats: " + e.getMessage());
        }
    }
}
