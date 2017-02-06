package ua.onix.server;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.Assert;
import org.junit.Test;
import ua.onix.server.template.ScriptExecutor;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.Iterator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestScriptExecutor {

    @Test
    public void testRequest() throws FileNotFoundException, ScriptException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("test")).thenReturn("passed");

        ScriptExecutor script = new ScriptExecutor(request);
        script.eval("print(request)");
        script.eval("print(request.getParameter('test'))");
    }

    @Test
    public void testVariables() throws FileNotFoundException, ScriptException {
        ScriptExecutor script = new ScriptExecutor(null);
        script.eval("var a = 1");
        script.eval("b = 2");

        script.eval("print(a)");
        script.eval("print(b)");
    }

    @Test
    public void testImport() throws FileNotFoundException, ScriptException {
        ScriptExecutor script = new ScriptExecutor(null);

//        script.eval("load(\"nashorn:mozilla_compat.js\");");
        script.eval("importClass(Packages.biz.test.Person)");
//        script.eval("(function() {Person = Java.type('biz.test.Person')})()");
//        System.out.println(script.eval("Person"));
        script.eval("print(new Person())");
//        script.eval("var a = new JavaImporter(Packages.biz.test.Person)");

//        script.eval("a");
//        script.eval("print(Person);");
//        script.eval("with(a) { print(Person) }");
//        script.eval("with(new JavaImporter(Packages.biz.test.Person)) {print(new Person())}");
//        script.eval("var imports = new JavaImporter(Packages.biz.test)");
//        script.eval("with(imports) {new Person()}");
    }

    @Test
    public void testEvalueate() {
        ScriptExecutor script = new ScriptExecutor(null);
        script.eval("var a = 4");
        Assert.assertEquals(8D, script.eval("a * 2"));
    }

    @Test
    public void testEvalArray() {
        ScriptExecutor script = new ScriptExecutor(null);
        script.eval("var a = [1,2,3]");
        ScriptObjectMirror result = (ScriptObjectMirror) script.eval("a");
        Assert.assertNotNull(result);
        Assert.assertEquals("Array", result.getClassName());
        Assert.assertEquals(3, result.getOwnKeys(false).length);
    }

    @Test
    public void testEvalArrayOfObjects() {
        ScriptExecutor script = new ScriptExecutor(null);
        script.eval("var a = [{name:'Denis'},{name:'Alex'}]");
        ScriptObjectMirror result = (ScriptObjectMirror) script.eval("a");
        Assert.assertNotNull(result);
        Assert.assertEquals("Array", result.getClassName());
        Assert.assertEquals(2, result.getOwnKeys(false).length);
    }

    @Test
    public void testEvalArrayMethod() {
        ScriptExecutor script = new ScriptExecutor(null);
        script.eval("var a = [{name:'Denis'},{name:'Alex'}]");
        Iterator<Object> result = script.evalArray("a");
        ScriptObjectMirror o1 = (ScriptObjectMirror) result.next();
        Assert.assertEquals("Denis", o1.get("name"));
    }

    @Test
    public void testEvalBool() {
        ScriptExecutor script = new ScriptExecutor(null);
        Assert.assertFalse((Boolean) script.eval("1 == 2"));
        Assert.assertTrue((Boolean) script.eval("1 == 1"));
    }

}