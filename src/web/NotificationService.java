package web;

public class NotificationService {

    private static String lastEmailContent;

    public static String sendBookingConfirmation(String toEmail, String patientName, String doctorName, String date, String treatment) {
        String subject = "Appointment Confirmed - Sunrise Dental Clinic";
        String body = "<div style='font-family:Segoe UI,sans-serif;max-width:600px;margin:0 auto;border:1px solid #ddd;border-radius:8px;overflow:hidden;'>"
            + "<div style='background:#000099;color:#fff;padding:20px;text-align:center;'>"
            + "<h2 style='margin:0;font-size:22px;'>SUNRISE DENTAL CLINIC</h2>"
            + "</div>"
            + "<div style='padding:24px;'>"
            + "<h3 style='color:#006666;margin-top:0;'>Appointment Confirmation</h3>"
            + "<p>Dear <strong>" + escapeHtml(patientName) + "</strong>,</p>"
            + "<p>Your appointment has been successfully confirmed. Here are the details:</p>"
            + "<table style='width:100%;border-collapse:collapse;margin:16px 0;'>"
            + "<tr><td style='padding:8px;color:#666;border-bottom:1px solid #eee;'>Doctor</td><td style='padding:8px;border-bottom:1px solid #eee;'><strong>" + escapeHtml(doctorName) + "</strong></td></tr>"
            + "<tr><td style='padding:8px;color:#666;border-bottom:1px solid #eee;'>Date</td><td style='padding:8px;border-bottom:1px solid #eee;'><strong>" + escapeHtml(date) + "</strong></td></tr>"
            + "<tr><td style='padding:8px;color:#666;border-bottom:1px solid #eee;'>Treatment</td><td style='padding:8px;border-bottom:1px solid #eee;'><strong>" + escapeHtml(treatment) + "</strong></td></tr>"
            + "</table>"
            + "<p>Please arrive 15 minutes before your scheduled time. If you need to reschedule or cancel, contact us as soon as possible.</p>"
            + "<p style='color:#666;margin-top:24px;'>Thank you for choosing Sunrise Dental Clinic.</p>"
            + "</div>"
            + "<div style='background:#f4f4f4;padding:16px;text-align:center;color:#888;font-size:12px;'>"
            + "Sunrise Dental Clinic | Contact: +94 11 234 5678 | Email: info@sunrisedental.lk"
            + "</div>"
            + "</div>";

        String emailContent = formatEmail(toEmail, subject, body);
        lastEmailContent = emailContent;
        System.out.println(emailContent);
        return emailContent;
    }

    public static String sendAppointmentDeleted(String toEmail, String patientName, String date) {
        String subject = "Appointment Cancelled - Sunrise Dental Clinic";
        String body = "<div style='font-family:Segoe UI,sans-serif;max-width:600px;margin:0 auto;border:1px solid #ddd;border-radius:8px;overflow:hidden;'>"
            + "<div style='background:#000099;color:#fff;padding:20px;text-align:center;'>"
            + "<h2 style='margin:0;font-size:22px;'>SUNRISE DENTAL CLINIC</h2>"
            + "</div>"
            + "<div style='padding:24px;'>"
            + "<h3 style='color:#dc3545;margin-top:0;'>Appointment Cancelled</h3>"
            + "<p>Dear <strong>" + escapeHtml(patientName) + "</strong>,</p>"
            + "<p>We regret to inform you that your appointment scheduled for <strong>" + escapeHtml(date) + "</strong> has been cancelled.</p>"
            + "<p>If you believe this was done in error or would like to rebook, please contact us immediately.</p>"
            + "<p style='color:#666;margin-top:24px;'>We apologize for any inconvenience caused.</p>"
            + "</div>"
            + "<div style='background:#f4f4f4;padding:16px;text-align:center;color:#888;font-size:12px;'>"
            + "Sunrise Dental Clinic | Contact: +94 11 234 5678 | Email: info@sunrisedental.lk"
            + "</div>"
            + "</div>";

        String emailContent = formatEmail(toEmail, subject, body);
        lastEmailContent = emailContent;
        System.out.println(emailContent);
        return emailContent;
    }

    public static String sendBillReady(String toEmail, String patientName, String treatment, double baseFee, double tax, double total) {
        String subject = "Your Bill is Ready - Sunrise Dental Clinic";
        String feeFormatted = String.format("LKR %.2f", baseFee);
        String taxFormatted = String.format("LKR %.2f", tax);
        String totalFormatted = String.format("LKR %.2f", total);
        String body = "<div style='font-family:Segoe UI,sans-serif;max-width:600px;margin:0 auto;border:1px solid #ddd;border-radius:8px;overflow:hidden;'>"
            + "<div style='background:#000099;color:#fff;padding:20px;text-align:center;'>"
            + "<h2 style='margin:0;font-size:22px;'>SUNRISE DENTAL CLINIC</h2>"
            + "</div>"
            + "<div style='padding:24px;'>"
            + "<h3 style='color:#006666;margin-top:0;'>Billing Summary</h3>"
            + "<p>Dear <strong>" + escapeHtml(patientName) + "</strong>,</p>"
            + "<p>Your bill for the recent treatment has been calculated. Please find the details below:</p>"
            + "<table style='width:100%;border-collapse:collapse;margin:16px 0;border:1px solid #eee;'>"
            + "<tr style='background:#f9f9f9;'><td style='padding:10px;color:#666;border-bottom:1px solid #eee;'>Treatment</td><td style='padding:10px;border-bottom:1px solid #eee;'><strong>" + escapeHtml(treatment) + "</strong></td></tr>"
            + "<tr><td style='padding:10px;color:#666;border-bottom:1px solid #eee;'>Base Fee</td><td style='padding:10px;border-bottom:1px solid #eee;'>" + feeFormatted + "</td></tr>"
            + "<tr style='background:#f9f9f9;'><td style='padding:10px;color:#666;border-bottom:1px solid #eee;'>Service Tax (5%)</td><td style='padding:10px;border-bottom:1px solid #eee;'>" + taxFormatted + "</td></tr>"
            + "<tr><td style='padding:10px;font-weight:bold;'>Total Amount</td><td style='padding:10px;font-weight:bold;color:#006666;font-size:16px;'>" + totalFormatted + "</td></tr>"
            + "</table>"
            + "<p>Please settle your payment at the reception on your next visit.</p>"
            + "<p style='color:#666;margin-top:24px;'>Thank you for choosing Sunrise Dental Clinic.</p>"
            + "</div>"
            + "<div style='background:#f4f4f4;padding:16px;text-align:center;color:#888;font-size:12px;'>"
            + "Sunrise Dental Clinic | Contact: +94 11 234 5678 | Email: info@sunrisedental.lk"
            + "</div>"
            + "</div>";

        String emailContent = formatEmail(toEmail, subject, body);
        lastEmailContent = emailContent;
        System.out.println(emailContent);
        return emailContent;
    }

    public static String getLastEmailContent() {
        return lastEmailContent;
    }

    public static String getLastEmailSummary() {
        if (lastEmailContent == null) return null;
        String to = "", subject = "";
        for (String line : lastEmailContent.split("\n")) {
            if (line.startsWith("To: ")) to = line.substring(4).trim();
            if (line.startsWith("Subject: ")) subject = line.substring(9).trim();
        }
        if (to.isEmpty()) return null;
        return "Email Notification Sent\nTo: " + to + "\nSubject: " + subject;
    }

    private static String formatEmail(String to, String subject, String bodyHtml) {
        return "====== EMAIL NOTIFICATION ======\n"
            + "To: " + to + "\n"
            + "Subject: " + subject + "\n"
            + "================================\n\n"
            + "[HTML Email Content]\n"
            + bodyHtml + "\n\n"
            + "================================";
    }

    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
