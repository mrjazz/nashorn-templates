package ua.onix.server;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import ua.onix.server.template.TemplateParseException;
import ua.onix.server.template.TemplateParser;

import java.io.File;
import java.io.IOException;

public class TestSoup {


    @Test
    public void testSoupIssue() throws TemplateParseException, IOException {
        // the testcase reproduce issue in jSoup DOM parsing
        Document document = Jsoup.parse(new File("src/test/resources/soup-issue.html"), "UTF-8");
        String result = document.html();
        Assert.assertFalse(result.contains("<h1> </h1>"));
    }

}

