package ua.onix.server;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import ua.onix.server.template.FileTemplateSource;
import ua.onix.server.template.TemplateParseException;
import ua.onix.server.template.TemplateParser;

import javax.servlet.http.HttpServletRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestTemplateParser {

    private TemplateParser initParserForTemplate(String name) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(name);
        return new TemplateParser(new FileTemplateSource("src/test/resources/"), request);
    }

    @Test
    public void testConditional() throws TemplateParseException {
        String result = initParserForTemplate("/conditional.html").parse();

        Assert.assertTrue(result.contains("Should be displayed"));
        Assert.assertFalse(result.contains("Shouldn't be displayed"));
    }

    @Test
    public void testConditionalNesting() throws TemplateParseException {
        String result = initParserForTemplate("/conditional-nesting.html").parse();

        Assert.assertTrue(result.contains("inside1"));
        Assert.assertTrue(result.contains("inside2"));
        Assert.assertFalse(result.contains("inside3"));
    }

    @Test
    public void testScripts() throws TemplateParseException {
        String result = initParserForTemplate("/script.html").parse();

        Assert.assertTrue(result.contains("<h1>2</h1>"));
        Assert.assertTrue(result.contains("<h2>3</h2>"));
        Assert.assertTrue(result.contains("<h3>5.0</h3>"));
    }

    @Test
    public void testLoop() throws TemplateParseException {
        String result = initParserForTemplate("/loop.html").parse();

        Assert.assertTrue(result.contains("<li>Name: Max</li>"));
        Assert.assertTrue(result.contains("<li>Name: Denis</li>"));
    }

    @Test
    public void testLoopNested() throws TemplateParseException {
        String result = initParserForTemplate("/loop-nested.html").parse();

        Assert.assertTrue(result.contains("<li> Name: Max <p>Rate: 7</p><p>Rate: 4</p> </li>"));
        Assert.assertTrue(result.contains("<li> Name: Denis <p>Rate: 3</p><p>Rate: 2</p><p>Rate: 1</p> </li>"));
    }

        @Test
    public void testAttrExpressions() throws TemplateParseException {
        String result = initParserForTemplate("/attrs.html").parse();

        Assert.assertTrue(result.contains("<h1 title=\"hello\">hello</h1>"));
        Assert.assertTrue(result.contains("<h2 title=\"world\">world</h2>"));
        Assert.assertTrue(result.contains("<h3 title=\"3.141592653589793\">world</h3>"));
    }

    @Test
    public void testComplete() throws TemplateParseException {
        String result = initParserForTemplate("/index.html").parse();

        Assert.assertTrue(result.contains("<h1 title=\"Kerstin\">Kerstin</h1>"));
        Assert.assertTrue(result.contains("Child: Child 0"));
    }

    @Test
    public void testCode() throws TemplateParseException {
        String result = initParserForTemplate("/code.html").parse();

        Assert.assertTrue(result.contains("<p>Person [name=Kerstin, married=false, spouse=Jose, children=[Child 0]]</p>"));
        Assert.assertTrue(result.contains("<p>Kerstin</p>"));
    }

    @Test
    public void testInclude() throws TemplateParseException {
        String result = initParserForTemplate("/parent.html").parse();
        Assert.assertTrue(result.contains("ChildA"));
        Assert.assertTrue(result.contains("ChildB"));
    }

}

