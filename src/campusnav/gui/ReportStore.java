package campusnav.gui;

import java.util.ArrayList;

// Stores submitted reports so the admin can read them (Collections: ArrayList)
public class ReportStore {
    private static ArrayList<String> reports = new ArrayList<String>();

    public static void add(String report) { reports.add(report); }
    public static ArrayList<String> getAll() { return reports; }
}
