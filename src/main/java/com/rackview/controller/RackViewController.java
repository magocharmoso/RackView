package com.rackview.controller;

import com.rackview.domain.Bastidor;
import com.rackview.domain.DataCenter;
import com.rackview.domain.Equipamento;
import com.rackview.service.RackViewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/data-centers/{nomeDataCenter}/bastidores")
    public ResponseEntity<List<Bastidor>> listarBastidores(@PathVariable String nomeDataCenter) {
        return ResponseEntity.ok(rackViewService.listarBastidores(nomeDataCenter));
    }

    @GetMapping("/data-centers/{nomeDataCenter}/bastidores/{nomeBastidor}")
    public ResponseEntity<Bastidor> buscarBastidor(@PathVariable String nomeDataCenter,
                                                 @PathVariable String nomeBastidor) {
        return ResponseEntity.ok(rackViewService.encontrarBastidor(nomeDataCenter, nomeBastidor));
    }

    @GetMapping("/data-centers/{nomeDataCenter}/bastidores/{nomeBastidor}/equipamentos")
    public ResponseEntity<List<Equipamento>> listarEquipamentos(@PathVariable String nomeDataCenter,
                                                               @PathVariable String nomeBastidor) {
        return ResponseEntity.ok(rackViewService.listarEquipamentos(nomeDataCenter, nomeBastidor));
    }

    @PostMapping("/data-centers/{nomeDataCenter}/bastidores/{nomeBastidor}/equipamentos")
    public ResponseEntity<Equipamento> adicionarEquipamento(@PathVariable String nomeDataCenter,
                                                           @PathVariable String nomeBastidor,
                                                           @RequestBody String nomeEquipamento) {
        Equipamento equipamentoCriado = rackViewService.adicionarEquipamento(nomeDataCenter, nomeBastidor, nomeEquipamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamentoCriado);
    }

    @DeleteMapping("/data-centers/{nomeDataCenter}/bastidores/{nomeBastidor}/equipamentos/{nomeEquipamento}")
    public ResponseEntity<Void> removerEquipamento(@PathVariable String nomeDataCenter,
                                                  @PathVariable String nomeBastidor,
                                                  @PathVariable String nomeEquipamento) {
        rackViewService.removerEquipamento(nomeDataCenter, nomeBastidor, nomeEquipamento);
        return ResponseEntity.noContent().build();
    }
}
