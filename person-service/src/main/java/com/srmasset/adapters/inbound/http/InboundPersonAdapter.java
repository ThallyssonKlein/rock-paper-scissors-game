package com.srmasset.adapters.inbound.http;

import com.srmasset.adapters.outbound.database.OutboundPersonAdapter;
import com.srmasset.ports.inbound.http.api.v1.dto.InboundPersonDTO;
import com.srmasset.ports.outbound.database.person.dao.PersonDAO;
import datadog.trace.api.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InboundPersonAdapter {

    @Autowired
    private OutboundPersonAdapter outboundPersonAdapter;

    @Trace(operationName = "InboundPersonAdapter.createPerson")
    public PersonDAO createPerson(InboundPersonDTO inboundPersonDTO) throws Exception {
        return this.outboundPersonAdapter.createPerson(inboundPersonDTO);
    }

    @Trace(operationName = "InboundPersonAdapter.getPerson")
    public PersonDAO getPerson(Long id) throws Exception {
        return this.outboundPersonAdapter.getPerson(id);
    }
}
