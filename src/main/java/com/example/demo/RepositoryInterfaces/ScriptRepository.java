package com.example.demo.RepositoryInterfaces;

import com.example.demo.DocumentDefinition.Audit;
import com.example.demo.DocumentDefinition.Script;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ScriptRepository extends MongoRepository<Script, String> {

    @Query(value = "{}", fields = "{'ScriptName' :  1, 'result' :  1, '_id' : 0}")
    List<Script> getTableScript();

    //@Query(value = "{}", fields = "{'scriptBody' :  1, '_id' :  0}")
    //List<Script> getAllScriptsPath();
}
