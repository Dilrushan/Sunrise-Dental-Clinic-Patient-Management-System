package Model;

public class Patient {
    private int patientId;
    private String fullName;
    private String address;
    private String contactNumber;
    private String email;
    private String treatmentHistory;

    public Patient() {}

    public Patient(int patientId, String fullName, String address, String contactNumber, String email, String treatmentHistory) {
        this.patientId = patientId;
        this.fullName = fullName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
        this.treatmentHistory = treatmentHistory;
    }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTreatmentHistory() { return treatmentHistory; }
    public void setTreatmentHistory(String treatmentHistory) { this.treatmentHistory = treatmentHistory; }
}
