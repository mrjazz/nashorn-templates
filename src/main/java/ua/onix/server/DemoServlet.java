package ua.onix.server;

import ua.onix.server.template.FileTemplateSource;
import ua.onix.server.template.TemplateParseException;
import ua.onix.server.template.TemplateParser;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by denis on 1/20/2017.
 */
public class DemoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private String templateDir;

    public void init(ServletConfig servletConfig) throws ServletException{
        this.templateDir = servletConfig.getInitParameter("templateDir");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        FileTemplateSource templateSource = new FileTemplateSource(templateDir);
        TemplateParser parser = new TemplateParser(templateSource, request);

        try {
            response.getWriter().println(parser.parse());
        } catch (TemplateParseException e) {
            response.getWriter().println(e.getMessage());
            response.getWriter().println("<pre>");
            e.printStackTrace(response.getWriter());
            response.getWriter().println("</pre>");
        }
    }
}
