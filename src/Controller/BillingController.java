package Controller;

import dao.AppointmentDAO;
import Model.Appointment;

public class BillingController {
    private final AppointmentDAO appointmentDAO;
    private static final double SERVICE_TAX_RATE = 0.05;

    public BillingController() {
        this.appointmentDAO = new AppointmentDAO();
    }

    public double getTreatmentFee(String treatmentName) {
        return appointmentDAO.getTreatmentFee(treatmentName);
    }

    public boolean updateFee(int appointmentId, double fee) {
        return appointmentDAO.updateFee(appointmentId, fee);
    }

    public double getBaseFee(String treatmentName) {
        if (treatmentName == null) return 0.00;
        double fee = appointmentDAO.getTreatmentFee(treatmentName);
        return (fee < 0) ? 0.00 : fee;
    }

    public boolean isGeneralVisit(String treatmentName) {
        return treatmentName != null && treatmentName.toLowerCase().contains("general");
    }

    public String buildBill(String patientName, String visitType, String treatment, double baseFee) {
        double tax;
        double total;
        if (isGeneralVisit(treatment)) {
            baseFee = 0.00;
            tax = 0.00;
            total = 0.00;
        } else {
            tax = baseFee * SERVICE_TAX_RATE;
            total = baseFee + tax;
        }
        return "=== SUNRISE DENTAL CLINIC BILL ===" +
               "\nPatient Name: " + patientName +
               "\nVisit Type: " + visitType +
               "\nTreatment: " + (treatment == null ? "N/A" : treatment) +
               "\nBase Treatment Fee: LKR " + String.format("%.2f", baseFee) +
               "\nService Tax (5%): LKR " + String.format("%.2f", tax) +
               "\n----------------------------------------" +
               "\nTotal Payable Amount: LKR " + String.format("%.2f", total);
    }
}
