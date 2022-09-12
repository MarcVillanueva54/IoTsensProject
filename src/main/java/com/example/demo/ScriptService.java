package com.example.demo;

import com.example.demo.DocumentDefinition.Script;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    public String insertScript(Script script){
        scriptRepository.insert(script);
        return "Script introducido con éxito";
    }
}
