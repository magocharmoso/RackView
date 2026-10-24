package com.rackview.service;

import com.rackview.domain.EquipamentoEntity;
import com.rackview.dto.EquipamentoRequestDTO;
import com.rackview.dto.EquipamentoResponseDTO;
import com.rackview.repository.EquipamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository) {
        this.equipamentoRepository = equipamentoRepository;
    }

    public List<EquipamentoResponseDTO> listarTodos() {
        return equipamentoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public EquipamentoResponseDTO buscarPorId(Long id) {
        EquipamentoEntity equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado: " + id));
        return converterParaResponse(equipamento);
    }

    public EquipamentoResponseDTO criar(EquipamentoRequestDTO requestDTO) {
        EquipamentoEntity equipamento = new EquipamentoEntity(requestDTO.getNome());

        EquipamentoEntity salvo = equipamentoRepository.save(equipamento);
        return converterParaResponse(salvo);
    }

    public EquipamentoResponseDTO atualizar(Long id, EquipamentoRequestDTO requestDTO) {
        EquipamentoEntity equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado: " + id));

        equipamento.setNome(requestDTO.getNome());

        EquipamentoEntity atualizado = equipamentoRepository.save(equipamento);
        return converterParaResponse(atualizado);
    }

    public void deletar(Long id) {
        if (!equipamentoRepository.existsById(id)) {
            throw new RuntimeException("Equipamento não encontrado: " + id);
        }
        equipamentoRepository.deleteById(id);
    }

    private EquipamentoResponseDTO converterParaResponse(EquipamentoEntity equipamento) {
        return new EquipamentoResponseDTO(
                equipamento.getNome()
        );
    }
}
