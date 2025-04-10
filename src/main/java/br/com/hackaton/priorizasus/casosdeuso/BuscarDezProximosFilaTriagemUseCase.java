package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.FilaTriagemResponseDTO;
import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import br.com.hackaton.priorizasus.mapper.FilaTriagemMapper;
import br.com.hackaton.priorizasus.repository.FilaTriagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarDezProximosFilaTriagemUseCase {

    private final FilaTriagemRepository filaTriagemRepository;

    public List<FilaTriagemResponseDTO> buscarProximos() {
        return filaTriagemRepository
                .findTop10ByStatusTriagemOrderByHorarioEntradaAsc(StatusTriagemEnum.AGUARDANDO)
                .stream()
                .map(FilaTriagemMapper::mapearParaReponseDTO)
                .toList();
    }
}


