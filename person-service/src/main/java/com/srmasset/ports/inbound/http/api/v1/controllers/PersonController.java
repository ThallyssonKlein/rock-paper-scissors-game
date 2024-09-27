package com.srmasset.ports.inbound.http.api.v1.controllers;

import com.srmasset.adapters.inbound.http.InboundPersonAdapter;
import com.srmasset.ports.inbound.http.api.v1.dto.InboundPersonDTO;
import com.srmasset.ports.outbound.database.person.dao.PersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import datadog.trace.api.Trace;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private InboundPersonAdapter inboundPersonAdapter;

    @PostMapping
    @Trace(operationName = "PersonController.createPerson")
    public ResponseEntity<PersonDAO> createPerson(@RequestBody InboundPersonDTO inboundPersonDTO) throws Exception {
        return ResponseEntity.ok(this.inboundPersonAdapter.createPerson(inboundPersonDTO));
    }

    @GetMapping("/{id}")
    @Trace(operationName = "PersonController.getPerson")
    public ResponseEntity<PersonDAO> getPerson(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(this.inboundPersonAdapter.getPerson(id));
    }
}