package com.example.demo;

import com.example.demo.ServicesDefinition.AuditService;
import com.example.demo.DocumentDefinition.Audit;
import com.example.demo.DocumentDefinition.Script;
import com.example.demo.ScriptFunction.ScriptExecution;
import com.example.demo.ServicesDefinition.ScriptService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/")
@WebServlet("/DemoServlet")
public class DemoServlet extends HttpServlet {
    private final ScriptService scriptService;
    private final AuditService auditService;

    @GetMapping()
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        List<Script> listScripts = scriptService.getAllScriptsName();

        StringBuilder stringBuilder = new StringBuilder();

        for (Script script : listScripts) {
            stringBuilder.append(script.getScriptName()).append(";").append(script.getResult()).append(";");
        }

        try {
            response.setContentType("text/plain");
            response.getWriter().write(stringBuilder.toString());
        }catch (IOException e){
            registerAudit("Found IOException when showing scripts");
        }

    }

    @PostMapping
    protected void doPost(HttpServletRequest request, HttpServletRequest response) {

        String scriptName = "Script" + (scriptService.getAllScriptsNum() + 1);

        List<String> keys = new ArrayList<>(List.of(request.getParameterValues("Clave[]")));
        List<String> values = new ArrayList<>(List.of(request.getParameterValues("Valor[]")));
        String scriptBody = request.getParameter("Script")
                .replace("\r", "")
                .replace("\n", " ");

        createWriteScriptDocument(scriptName, scriptBody); //creamos documento de script

        //=======Add variables defined in script to lists of keys and values=======//
        String[] splitScriptBody = scriptBody.split("[ ;]");
        for (int i = 0; i< splitScriptBody.length; i++ ){
            if (splitScriptBody[i].equals("var") || splitScriptBody[i].equals("let")){
                keys.add(splitScriptBody[i + 1]);
                values.add(splitScriptBody[i + 3]);
            }
        }

        //=======Build map for variable document inside script document======//
        Map<String, String> mapa = new HashMap<String, String>();
        for(int i = 0; i < keys.size(); i++){
            mapa.put(keys.get(i), values.get(i));
        }

        //=======Insert script in database IoTsensDB collection Script=======//
        scriptService.insertScript(new Script(
                scriptName,
                new File(scriptName).getAbsolutePath(), //cambiar por scriptBody para guardar input usuario
                mapa,
                "Not Evaluated"
        ));

        registerAudit("Registered new script: " + scriptName);
    }

    @PostMapping("executeScripts")
    public void executeScripts() {

        try {

            List<Script> scriptList = scriptService.getAllScriptPaths();

            List<Object> executedScripts = ScriptExecution.executeScript(scriptList, auditService);

            for (int i = 0; i < scriptList.size(); i++) {
                scriptList.get(i).setResult(executedScripts.get(i) != null ? executedScripts.get(i).toString() : "");
                //scriptList.get(i).setResult("Evaluated");  //uncomment this for test evaluation
            }
            updateScriptsResults(scriptList);

        }catch (IndexOutOfBoundsException e){
            registerAudit("Found IndexOutOfBoundsException when executing scripts");
        }
    }

    //===========================Aux Functions=========================//
    public void registerAudit(String action){   //registrar audit en bd
        auditService.insertAudit(new Audit(action));
    }

    public void updateScriptsResults(List<Script> scriptList){  //update all scripts when evaluated
        scriptService.updateScriptsResults(scriptList);
    }

    public void createWriteScriptDocument(String scriptName, String scriptBody) {   //crear y escribir doc. script
        File myObj = new File(scriptName + ".txt");
        try {
            if (myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter(myObj.getName());
                myWriter.write(scriptBody);
                myWriter.close();
            }
        } catch (IOException e) {
            registerAudit("Found IOException when storing " + myObj.getName());
        } catch (SecurityException e) {
            registerAudit("Found SecurityException when storing " + myObj.getName());
        }
    }
}
