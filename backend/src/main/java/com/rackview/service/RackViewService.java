package com.rackview.service;

import com.rackview.config.RackViewProperties;
import com.rackview.domain.Bastidor;
import com.rackview.domain.DataCenter;
import com.rackview.domain.Equipamento;
import com.rackview.domain.EquipamentoFactory;
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

    public DataCenter encontrarDataCenter(String nomeDataCenter) {
        return dataCenters.stream()
                .filter(dataCenter -> dataCenter.getNome().equalsIgnoreCase(nomeDataCenter))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Data center não encontrado: " + nomeDataCenter));
    }

    public List<Bastidor> listarBastidores(String nomeDataCenter) {
        return encontrarDataCenter(nomeDataCenter).getBastidores();
    }

    public Bastidor encontrarBastidor(String nomeDataCenter, String nomeBastidor) {
        DataCenter dataCenter = encontrarDataCenter(nomeDataCenter);

        return dataCenter.getBastidores().stream()
                .filter(bastidor -> bastidor.getNome().equalsIgnoreCase(nomeBastidor))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Bastidor não encontrado: " + nomeBastidor));
    }

    public List<Equipamento> listarEquipamentos(String nomeDataCenter, String nomeBastidor) {
        return encontrarBastidor(nomeDataCenter, nomeBastidor).getEquipamentos();
    }

    public Equipamento adicionarEquipamento(String nomeDataCenter, String nomeBastidor, String nomeEquipamento) {
        Bastidor bastidor = encontrarBastidor(nomeDataCenter, nomeBastidor);
        Equipamento equipamento = EquipamentoFactory.criar(nomeEquipamento, null);
        bastidor.adicionarEquipamento(equipamento);
        return equipamento;
    }

    public void removerEquipamento(String nomeDataCenter, String nomeBastidor, String nomeEquipamento) {
        Bastidor bastidor = encontrarBastidor(nomeDataCenter, nomeBastidor);
        Equipamento equipamento = bastidor.buscarEquipamentoPorNome(nomeEquipamento)
                .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado no bastidor " + nomeBastidor + ": " + nomeEquipamento));
        bastidor.removerEquipamento(equipamento);
    }
}
