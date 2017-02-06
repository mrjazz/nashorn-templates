package ua.onix.server.template;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import javax.script.Bindings;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by denis on 1/20/2017.
 */
public class TemplateParser {

    private class LocalTemplateParser extends TemplateParser {

        protected ScriptExecutor script;

        public LocalTemplateParser() {
            super(templateSource, request);
            this.script = super.script.clone();
        }
    }


    public static final String ATTR_CONDITION = "data-if";
    public static final String ATTR_LOOP = "data-for-";
    public static final String ATTR_INCLUDE = "include";

    private TemplateSource templateSource;
    private HttpServletRequest request;
    protected ScriptExecutor script;

    final Pattern expressionPattern;

    public TemplateParser(TemplateSource templateSource, HttpServletRequest request) {
        this.templateSource = templateSource;
        this.request = request;
        this.script = new ScriptExecutor(request);
        expressionPattern = Pattern.compile("\\$\\{(.*?)\\}",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    }

    public TemplateParser clone() {
        return new LocalTemplateParser();
    }

    public String parse() throws TemplateParseException {
        try {
            InputStream stream = templateSource.getTemplate(request.getRequestURI());
            return parseHtml(stream);
        } catch (IOException e) {
            throw new TemplateParseException(request, String.format("Template %s not found", request.getRequestURI()));
        }
    }

    private String parseHtml(InputStream source) {
        Document document = Jsoup.parse(readFromStream(source), "", Parser.xmlParser());
//        Document document = Jsoup.parse(readFromStream(source));
        processNodes(document.childNodes());
        return document.html();
    }

    private void processNodes(List<Node> nodes) {
        Node node;
        for (int i = 0; i < nodes.size(); i++) {
            node = nodes.get(i);
            processNode(node);
        }
    }

    private void processNode(Node node) {
        // process attributes

        if (node.hasAttr(ATTR_CONDITION)) {
            if (!(Boolean) script.eval(node.attr(ATTR_CONDITION))) {
                // do not process tags with false conditions
                node.remove();
                return;
            } else {
                // if valid then just remove conditional attribute
                node.removeAttr(ATTR_CONDITION);
            }
        }

        if (node.hasAttr("type") && node.attr("type").equals("server/javascript")) {
            script.eval(((Element) node).html());
            node.remove();
        }

        if (node.hasAttr(ATTR_INCLUDE)) {
            if (node instanceof Element) {
                Element element = (Element)node;
                String template = node.attr(ATTR_INCLUDE);
                try {
                    String html = clone().parseHtml(templateSource.getTemplate(template));
                    element.append(html);
                } catch (IOException e) {
                    element.appendText(String.format("Can't include template %s", template));
                    e.printStackTrace();
                }
            }
            node.removeAttr(ATTR_INCLUDE);
        }

        // evaluate loop expression
        processAttributes(node);

        if (node instanceof TextNode) {
            processTextNode((TextNode) node);
        }

        if (node.childNodes() != null) processNodes(node.childNodes());
    }

    private void processTextNode(TextNode content) {
        if (content.isBlank()) return;

        String result = evalText(content.text());
        if (result != null) {
            content.text(result);
        }
    }

    private String evalText(String content) {
        Matcher matcher = expressionPattern.matcher(content);
        if (matcher.find()) {
            return matcher.replaceAll(script.eval(matcher.group(1)).toString());
        }
        return null;
    }

    private void processAttributes(Node node) {
        for (Attribute attr : node.attributes()) {
            // try evaluate expression in attribute
            String result = evalText(attr.getValue());
            if (result != null) {
                attr.setValue(result);
            }

            if (attr.getKey().startsWith(ATTR_LOOP)) {
                String variable = attr.getKey().substring(9, attr.getKey().length());
                Iterator<Object> items = script.evalArray(attr.getValue());
                while (items.hasNext()) {
                    Node newNode = node.clone();
                    newNode.removeAttr(attr.getKey());

                    // Inject variable with current item value
                    script.bindVariable(variable, items.next());

                    processNode(newNode);
                    node.after(newNode);
                }
                node.remove();
            }
        }
    }

    private String readFromStream(InputStream source) {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = null;

        try {
            in = new InputStreamReader(source, "UTF-8");
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toString();
    }

}