package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.FilaAtendimentoDTO;
import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.enums.NivelPrioridadeEnum;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.mapper.AtendimentoMapper;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarProximosAtendimentosUseCase {

    private final FilaAtendimentoRepository filaAtendimentoRepository;

    public List<FilaAtendimentoDTO> executar() {
        return filaAtendimentoRepository.findByStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE).stream()
                .sorted(Comparator.comparingInt((FilaAtendimento fila) -> {
                            NivelPrioridadeEnum prioridade;
                            // Se não tem triagem, significa que é um atendimento administrativo
                            if(fila.getTriagem() == null){
                                return 2;
                            } else{
                                prioridade = fila.getTriagem().getNivelPrioridadeEnum();
                            }
                        // Em cenários reais, o vermelho e laranja são casos extremos de vida ou morte
                        // Prioridade absoluta para VERMELHO e altissima para LARANJA
                            if (prioridade == NivelPrioridadeEnum.VERMELHO) {
                                return 0;
                            } else if (prioridade == NivelPrioridadeEnum.LARANJA) {
                                return 1;
                            } else {
                                return 2;
                            }
                        })
                        // E então comparamos os demais que tiverem o mesmo nivel
                        .thenComparingInt(FilaAtendimento::getPesoFila))
                .limit(10)
                .map(AtendimentoMapper::toDTO)
                .toList();
    }
}


