package campusnav.gui;

import javax.swing.JFrame;

public class Navigation {

    public static void showWelcome() {
        openFrame(new WelcomeFrame());
    }

    public static void showLogin() {
        openFrame(new LoginFrame());
    }

    public static void showHome() {
        openFrame(new HomeFrame());
    }

    public static void showSearch() {
        openFrame(new SearchFrame());
    }

    public static void showDestination() {
        openFrame(new DestinationFrame());
    }

    public static void showRoute() {
        openFrame(new RouteFrame());
    }

    public static void showReportIssue() {
        openFrame(new ReportIssueFrame());
    }

    public static void showAdmin() {
        openFrame(new AdminFrame());
    }

    private static void openFrame(JFrame frame) {
        frame.setVisible(true);
    }
}
