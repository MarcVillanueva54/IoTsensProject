package com.example.demo.DocumentDefinition;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document
public class Script {
    @Id
    private String id;
    private String ScriptName;
    private String scriptBody;
    private Map<String, String> variables;

    private String result;

    public Script(String ScriptName, String scriptBody, Map<String, String> variables, String result) {
        this.ScriptName = ScriptName;
        this.scriptBody = scriptBody;
        this.variables = variables;
        this.result = result;
    }
}
