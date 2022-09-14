package com.example.demo.ServicesDefinition;

import com.example.demo.DocumentDefinition.Script;
import com.example.demo.RepositoryInterfaces.ScriptRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ScriptService {

    private final ScriptRepository scriptRepository;
    public int getAllScriptsNum(){
        return scriptRepository.findAll().size();
    }


    public List<Script> getAllScriptsName(){
        return scriptRepository.getTableScript();
    }

    //public List<Script> getAllScriptPaths(){ return scriptRepository.getAllScriptsPath();}

    public List<Script> getAllScriptPaths(){ return scriptRepository.findAll();}

    public void insertScript(Script script){
        scriptRepository.insert(script);
    }

    public void updateScriptsResults(List<Script> scriptList){
        scriptRepository.saveAll(scriptList);
    };
}
