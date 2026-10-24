package com.rackview.controller;

import com.rackview.domain.DataCenter;
import com.rackview.service.RackViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RackViewController {

    private final RackViewService rackViewService;

    public RackViewController(RackViewService rackViewService) {
        this.rackViewService = rackViewService;
    }

    @GetMapping("/data-centers")
    public ResponseEntity<List<DataCenter>> listarDataCenters() {
        return ResponseEntity.ok(rackViewService.listarDataCenters());
    }
}
