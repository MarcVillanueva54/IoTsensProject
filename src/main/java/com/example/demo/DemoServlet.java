package com.example.demo;

import com.example.demo.DocumentDefinition.Script;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/")
@WebServlet("/DemoServlet")
public class DemoServlet extends HttpServlet {
    private final ScriptService scriptService;

    @GetMapping
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        List<Script> listScripts = scriptService.getAllScriptsName();
        String json = new Gson().toJson(listScripts);
        response.setContentType("application/json");
        response.getWriter().write(json);

    }

    @PostMapping
    protected void doPost(HttpServletRequest request, HttpServletRequest response) throws ServletException, IOException {

        String scriptName = "Script" + (scriptService.getAllScriptsNum() + 1);

        List<String> keys = new ArrayList<>(List.of(request.getParameterValues("Clave[]")));
        List<String> values = new ArrayList<>(List.of(request.getParameterValues("Valor[]")));
        String scriptBody = request.getParameter("Script")
                .replace("\r", "")
                .replace("\n", " ");

        String sRootPath = new File(scriptName).getAbsolutePath();
        System.out.println("Path: " + sRootPath);

        createWriteScriptDocument(scriptName, scriptBody);


        String[] splitScriptBody = scriptBody.split("[ ;]");
        for (int i = 0; i< splitScriptBody.length; i++ ){
            if (splitScriptBody[i].equals("var") || splitScriptBody[i].equals("let")){
                keys.add(splitScriptBody[i + 1]);
                values.add(splitScriptBody[i + 3]);
            }
        }

        Map<String, String> mapa = new HashMap<String, String>();
        for(int i = 0; i < keys.size(); i++){
            mapa.put(keys.get(i), values.get(i));
        }

        Script script = new Script(
                scriptName,
                sRootPath,
                mapa,
                ""
        );

        scriptService.insertScript(script);
    }

    public void createWriteScriptDocument(String scriptName, String scriptBody) throws IOException {
        File myObj = new File(scriptName + ".txt");
        if (myObj.createNewFile()){
            System.out.println("File created with name " + myObj.getName());
        }else{
            System.out.println("Error al crear el fichero. Ya existe o no va.");
        }

        try {
            FileWriter myWriter = new FileWriter(myObj.getName());
            myWriter.write(scriptBody);
            myWriter.close();
            System.out.println("Escrito bien");
        }catch (IOException e) {
            System.out.println("Escrito mal");
            e.printStackTrace();
        }
    }
}
