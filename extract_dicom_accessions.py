"""
Read a directory of DICOM files, extract the "Accession Number" (tag "0008,0050"),
and write it to a text file called accession_list.txt
"""
import os

import pydicom


def extract_accession_numbers(dicom_dir, output_file):
    """
    Extracts accession numbers from DICOM files in a directory and saves them to a text file.
    
    Parameters:
        dicom_dir (str): Path to the directory containing DICOM files.
        output_file (str): Path to the output text file.
    """
    accession_numbers = []

    # Iterate through files in the directory
    for root, _, files in os.walk(dicom_dir):
        for file in files:
            file_path = os.path.join(root, file)
            try:
                # Read DICOM file
                ds = pydicom.dcmread(file_path)
                # Extract the "Accession Number" (tag "0008,0050")
                if "AccessionNumber" in ds:
                    accession_number = ds.AccessionNumber
                    accession_numbers.append(accession_number)
            except Exception as e:
                print(f"Error reading {file_path}: {e}")

    # Write accession numbers to the output file
    with open(output_file, 'w') as f:
        for number in accession_numbers:
            f.write(f"{number}\n")

    print(f"Accession numbers saved to {output_file}")


# Specify the directory containing DICOM files and the output file name
dicom_directory = "path/to/dicom/directory"  # Replace with your directory path
output_file_name = "accession_list.txt"

# Run the extraction
extract_accession_numbers(dicom_directory, output_file_name)
