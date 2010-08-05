package com.sugestio.client.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kris
 */
public class Analytics {

    private List<Report> reports;

    public Analytics(String raw) throws Exception {
        this.reports = new ArrayList<Report>();
        parse(raw);
    }

    public List<Report> getReports() {
        return this.reports;
    }

    private void parse(String raw) throws Exception {

        String[] lines = raw.split("\n");

        if (lines.length > 1) {

            String[] keys = lines[0].split(",");

            for (int i=1; i<lines.length; i++) {

                Report report = new Report();

                String[] values = lines[i].split(",");

                for (int j=0; j<keys.length; j++) {
                    report.put(keys[j], values[j]);
                }

                reports.add(report);
            }
        }

    }

}
