package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.FilaAtendimentoDTO;
import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.mapper.AtendimentoMapper;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarFilaAtendimentoPorIdOuCpfUseCase {

    private final FilaAtendimentoRepository filaAtendimentoRepository;

    public FilaAtendimentoDTO executar(String idOuCpf) {
        FilaAtendimento atendimento;

        if (idOuCpf.length() == 11 && idOuCpf.matches("\\d+")) {
            // É um CPF
            atendimento = filaAtendimentoRepository.findByTriagem_Paciente_Cpf(idOuCpf)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Fila não encontrada por CPF"));
        } else {
            // Tenta converter para ID
            try {
                Long id = Long.parseLong(idOuCpf);
                atendimento = filaAtendimentoRepository.findById(id)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Fila não encontrada por ID"));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Identificador inválido. Deve ser um CPF (11 dígitos) ou um ID numérico.");
            }
        }

        return AtendimentoMapper.toDTO(atendimento);
    }

}

