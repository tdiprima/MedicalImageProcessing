from datetime import datetime

from pydicom.dataset import Dataset, FileDataset

# Create a DICOM file
file_meta = Dataset()
file_meta.MediaStorageSOPClassUID = "1.2.840.10008.5.1.4.1.1.2"
file_meta.MediaStorageSOPInstanceUID = "1.2.3"
file_meta.ImplementationClassUID = "1.2.3.4"
ds = FileDataset("test_image_dcm4che.dcm", {}, file_meta=file_meta, preamble=b"\0" * 128)
ds.PatientName = "Test^Patient"
ds.PatientID = "12345"
ds.Modality = "OT"
ds.StudyDate = datetime.now().strftime("%Y%m%d")
ds.StudyTime = datetime.now().strftime("%H%M%S")
ds.save_as("test_image_dcm4che.dcm")
