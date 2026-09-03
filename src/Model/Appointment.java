package Model;

public class Appointment {
    private int appointmentId;
    private String patientName;
    private String contactNo;
    private int doctorId;
    private String appointmentDate;
    private String visitType;
    private String treatmentPrescribed;
    private double fee;
    private String status;

    public Appointment(int appointmentId, String patientName, String contactNo, int doctorId,
                       String appointmentDate, String visitType, String treatmentPrescribed,
                       double fee, String status) {
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.contactNo = contactNo;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.visitType = visitType;
        this.treatmentPrescribed = treatmentPrescribed;
        this.fee = fee;
        this.status = status;
    }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }
    public String getVisitType() { return visitType; }
    public void setVisitType(String visitType) { this.visitType = visitType; }
    public String getTreatmentPrescribed() { return treatmentPrescribed; }
    public void setTreatmentPrescribed(String treatmentPrescribed) { this.treatmentPrescribed = treatmentPrescribed; }
    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
