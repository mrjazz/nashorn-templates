package ua.onix.server.template;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by denis on 1/20/2017.
 */
interface TemplateSource {

    InputStream getTemplate(String name) throws IOException;

}
