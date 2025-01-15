package com.tdiprima.medicalimageprocessing;

import loci.formats.ImageReader;
import loci.formats.MetadataTools;
import loci.formats.meta.IMetadata;
import java.io.File;

public class BioFormatsDemo {
    public static void main(String[] args) {
        File bioImageFile = new File("src/main/resources/test_image_bioformats.ome.tiff");
        IMetadata metadata = MetadataTools.createOMEXMLMetadata();
        try (ImageReader reader = new ImageReader()) {
            reader.setMetadataStore(metadata);
            reader.setId(bioImageFile.getAbsolutePath());
            System.out.println("Metadata from Bio-Formats file: ");
            System.out.println(metadata.dumpXML());
        } catch (Exception e) {
            System.err.println("Error reading bio-imaging file: " + e.getMessage());
        }
    }
}