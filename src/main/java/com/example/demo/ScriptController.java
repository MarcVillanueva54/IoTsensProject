package com.example.demo;

import com.example.demo.DocumentDefinition.Script;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scriptVariables")
@AllArgsConstructor
public class ScriptController {

    private final ScriptService scriptService;

    @GetMapping
    public List<Script> fetchAllScripts(){return scriptService.getAllScriptsName();}

    @PostMapping
    public String insertScript(@RequestBody Script script){
        return scriptService.insertScript(script);
    }

}
