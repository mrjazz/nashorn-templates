package ua.onix.server.template;

import org.xml.sax.InputSource;
import ua.onix.server.template.TemplateSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by denis on 1/20/2017.
 */
public class FileTemplateSource implements TemplateSource {

    private final String directory;

    public FileTemplateSource(String directory) {
        this.directory = directory;
    }

//    @Override
//    public String getTemplate(String name) throws IOException {
//        if (name.equals("/")) {
//            name = "/index.html";
//        }
//        byte[] content = Files.readAllBytes(Paths.get(directory + name));
//        return new String(content);
//    }

    public InputStream getTemplate(String name) throws IOException {
        if (name.equals("/")) {
            name = "/index.html";
        }
        return new FileInputStream(directory + name);
    }
}
