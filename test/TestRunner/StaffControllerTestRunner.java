package TestRunner;

import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import Controller.StaffControllerTest;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class StaffControllerTestRunner {
    public static void main(String[] args) {
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        var request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(StaffControllerTest.class))
                .build();
        LauncherFactory.create().execute(request, listener);
        TestExecutionSummary summary = listener.getSummary();
        System.out.println();
        System.out.println("============================================");
        System.out.println("  StaffControllerTest Results");
        System.out.println("============================================");
        System.out.println("Tests found:    " + summary.getTestsFoundCount());
        System.out.println("Tests started:  " + summary.getTestsStartedCount());
        System.out.println("Tests succeeded:" + summary.getTestsSucceededCount());
        System.out.println("Tests failed:   " + summary.getTestsFailedCount());
        System.out.println("Tests skipped:  " + summary.getTestsSkippedCount());
        System.out.println("Time:           " + summary.getTimeFinished() + " ms");
        summary.printFailuresTo(new java.io.PrintWriter(System.out, true));
        if (summary.getFailures().size() > 0) {
            System.exit(1);
        }
    }
}
