package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.FilaTriagemResponseDTO;
import br.com.hackaton.priorizasus.entities.FilaTriagem;
import br.com.hackaton.priorizasus.mapper.FilaTriagemMapper;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.FilaTriagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuscarPacienteFilaTriagemUseCase  {

    private final FilaTriagemRepository filaTriagemRepository;

    public FilaTriagemResponseDTO buscar(String termo) {
        Optional<FilaTriagem> fila;

        if (termo.length() == 11 && termo.chars().allMatch(Character::isDigit)) {
            fila = filaTriagemRepository.findByPacienteCpf(termo);
        } else {
            try {
                Long id = Long.parseLong(termo);
                fila = filaTriagemRepository.findById(id);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Termo de busca inválido: deve ser CPF ou ID.");
            }
        }

        return fila.map(FilaTriagemMapper::mapearParaReponseDTO)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Paciente na fila de triagem não encontrado."));
    }


}


