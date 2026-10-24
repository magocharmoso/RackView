package com.rackview.service;

import com.rackview.config.RackViewProperties;
import com.rackview.domain.DataCenter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RackViewService {

    private final List<DataCenter> dataCenters;

    public RackViewService(RackViewProperties properties) {
        this.dataCenters = List.copyOf(properties.toDomain());
    }

    public List<DataCenter> listarDataCenters() {
        return dataCenters;
    }
}
