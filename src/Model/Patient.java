/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author HP
 */
public class Patient {
    private int patientId;
    private String fullName;
    private String address;
    private String contactNumber;
    private String email;
    private String treatmentHistory;

    public Patient(int patientId, String fullName, String address, String contactNumber, String email, String treatmentHistory) {
        this.patientId = patientId;
        this.fullName = fullName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
        this.treatmentHistory = treatmentHistory;
    }

    public int getPatientId() {
        return patientId; 
    }
    public String getFullName() {
        return fullName; 
    }
    public String getAddress() {
        return address; 
    }
    public String getContactNumber() {
        return contactNumber; 
    }
    public String getEmail() {
        return email; 
    }
    public String getTreatmentHistory() {
        return treatmentHistory; 
    }
}
