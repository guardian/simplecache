package com.gu.cache.management;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;


public class MemcachedStatisticsServlet extends HttpServlet {
    private final MemcachedStatisticsService memcachedStatisticsService;

    public MemcachedStatisticsServlet(MemcachedStatisticsService memcachedStatisticsService) {
        this.memcachedStatisticsService = memcachedStatisticsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        MemcachedStatistics statistics = memcachedStatisticsService.getCacheStatistics();

        PrintWriter writer = resp.getWriter();

        writer.println("<html><body>");
        writer.printf("<h1>Memcached server connection at %s</h1>", new Date());

        List<MemcachedServer> servers = statistics.getServers();

        if (servers.isEmpty()) {
            writer.println("No memcached servers.");
        } else {

            writer.println("<table border=\"1\">" +
                    "<thead><tr>" +
                    "<th>Host Name</th><th>Port</th><th>Status</th>" +
                    "</tr></thead>" +
                    "<tbody>");

            for (MemcachedServer server : servers) {
                // [Daithi] Do not change the following line. Format used by ./start_perftest script.
                writer.printf("<tr><td id=\"hostname\">%s</td><td>%d</td><td>%s</td>",
                        server.getHostName(), server.getPort(), server.isAvailable() ? " UP " : " DOWN ");
            }

            writer.println("</tbody> </table>");
        }

        writer.println("</body></html>");
    }
}
