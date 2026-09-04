package Controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BillingControllerTest {

    @Test
    public void testBuildBillWithNormalTreatment() {
        BillingController controller = new BillingController();
        String bill = controller.buildBill("John Doe", "Treatment", "Root Canal", 1000.00);
        assertTrue(bill.contains("Patient Name: John Doe"));
        assertTrue(bill.contains("Treatment: Root Canal"));
        assertTrue(bill.contains("Base Treatment Fee: LKR 1000.00"));
        assertTrue(bill.contains("Service Tax (5%): LKR 50.00"));
        assertTrue(bill.contains("Total Payable Amount: LKR 1050.00"));
    }

    @Test
    public void testBuildBillWithGeneralVisit() {
        BillingController controller = new BillingController();
        String bill = controller.buildBill("Jane Doe", "Consultation", "General Checkup", 2000.00);
        assertTrue(bill.contains("Base Treatment Fee: LKR 0.00"));
        assertTrue(bill.contains("Service Tax (5%): LKR 0.00"));
        assertTrue(bill.contains("Total Payable Amount: LKR 0.00"));
    }

    @Test
    public void testBuildBillWithNullTreatment() {
        BillingController controller = new BillingController();
        String bill = controller.buildBill("John Doe", "Treatment", null, 500.00);
        assertTrue(bill.contains("Treatment: N/A"));
    }

    @Test
    public void testIsGeneralVisitWithGeneralKeyword() {
        BillingController controller = new BillingController();
        assertTrue(controller.isGeneralVisit("General Checkup"));
        assertTrue(controller.isGeneralVisit("general consultation"));
    }

    @Test
    public void testIsGeneralVisitWithoutGeneralKeyword() {
        BillingController controller = new BillingController();
        assertFalse(controller.isGeneralVisit("Root Canal"));
    }

    @Test
    public void testIsGeneralVisitWithNull() {
        BillingController controller = new BillingController();
        assertFalse(controller.isGeneralVisit(null));
    }

    @Test
    public void testGetBaseFeeWithNullTreatment() {
        BillingController controller = new BillingController();
        assertEquals(0.00, controller.getBaseFee(null));
    }
}
