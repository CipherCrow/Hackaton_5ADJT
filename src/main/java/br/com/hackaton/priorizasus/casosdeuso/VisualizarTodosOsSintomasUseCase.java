package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.SintomaDTO;
import br.com.hackaton.priorizasus.mapper.SintomaMapper;
import br.com.hackaton.priorizasus.repository.SintomaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VisualizarTodosOsSintomasUseCase {

    private final SintomaRepository sintomaRepository;

    public List<SintomaDTO> buscarTodosOsSintomas() {
        return sintomaRepository.findAll()
                .stream()
                .map(SintomaMapper::toDTO)
                .toList();
    }

}


