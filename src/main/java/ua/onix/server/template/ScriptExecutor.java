package ua.onix.server.template;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


/**
 * Created by denis on 1/20/2017.
 */
public class ScriptExecutor {

    private class LocalScriptExecutor extends ScriptExecutor {

        final protected Bindings bindings;

        public LocalScriptExecutor(HttpServletRequest request) {
            super(request);
            this.bindings = super.bindings;
        }
    }

    final private HttpServletRequest request;
    final private ScriptEngine engine;
    final protected Bindings bindings;

    public ScriptExecutor(HttpServletRequest request) {
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
        this.request = request;

        // inject request in global scope
        bindings = new SimpleBindings();
        if (request != null) {
            bindings.put("request", request);
        }

//        bindings.put("importClass", (Consumer<StaticClass>) (n) -> {
//            System.out.println(n); // TODO : implement import in global scope
//        });

        engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);

        eval("load(\"nashorn:mozilla_compat.js\");");
    }

    public ScriptExecutor clone() {
        return new LocalScriptExecutor(request);
    }

    public void bindVariable(String name, Object value) {
        bindings.put(name, value);
        engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
    }

    public Object eval(String code) {
        try {
            return engine.eval(code);
        } catch (ScriptException e) {
//            e.printStackTrace();
            System.out.println(String.format("Code: %s\nError: %s", code, e.getMessage()));
            return String.format("Error: %s", e.getMessage());
        }
    }

    public Iterator<Object> evalArray(String variable) {
        Object result = eval(variable);
        if (result instanceof ScriptObjectMirror) {
            ScriptObjectMirror r = (ScriptObjectMirror) result;
            if (r.getClassName().equals("Array")) {
                return r.values().iterator();
            }
        } else if (result instanceof ArrayList) {
            return ((ArrayList) result).iterator();
        }
        return Collections.emptyIterator();
    }

}
