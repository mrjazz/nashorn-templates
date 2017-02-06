package ua.onix.server.template;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by denis on 1/20/2017.
 */
public class TemplateParseException extends Throwable {

    private static final long serialVersionUID = 1L;

    final private HttpServletRequest request;

    public TemplateParseException(HttpServletRequest request) {
        super(String.format("Exception in parsing request: %s", request.toString()));
        this.request = request;
    }

    public TemplateParseException(HttpServletRequest request, String message) {
        super(message);
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}