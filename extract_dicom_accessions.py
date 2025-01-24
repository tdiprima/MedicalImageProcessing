"""
Read a directory of DICOM files, extract the "Accession Number" (tag "0008,0050"),
and write it to a text file called accession_list.txt
"""
import os

import pydicom


def extract_accession_numbers(dicom_dir, output_file):
    """
    Extracts accession numbers from DICOM files and writes them directly to the output file.
        Parameters:
        dicom_dir (str): Path to the directory containing DICOM files.
        output_file (str): Path to the output text file.
    """
    with open(output_file, 'w') as f:
        for root, _, files in os.walk(dicom_dir):
            for file in files:
                file_path = os.path.join(root, file)
                try:
                    ds = pydicom.dcmread(file_path)
                    if "AccessionNumber" in ds:
                        f.write(f"{ds.AccessionNumber}\n")
                except Exception as e:
                    print(f"Error reading {file_path}: {e}")


# Example usage
extract_accession_numbers("path/to/dicom/directory", "accession_list.txt")
