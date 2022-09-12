package com.example.demo.DocumentDefinition;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class ScriptBody {
    @Id
    private String id;

    private String scriptBody;

    public ScriptBody(String scriptBody) {
        this.scriptBody = scriptBody;
    }
}
