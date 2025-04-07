package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.dto.SintomaDTO;
import br.com.hackaton.priorizasus.dto.SintomaRequestDTO;
import br.com.hackaton.priorizasus.entities.Sintoma;
import br.com.hackaton.priorizasus.mapper.SintomaMapper;
import br.com.hackaton.priorizasus.repository.SintomaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CadastrarSintomaUseCase {

    private final SintomaRepository sintomaRepository;

    @Transactional
    public SintomaDTO cadastrarSintoma(SintomaRequestDTO dto) {
        Sintoma sintoma = SintomaMapper.toEntity(dto);
        Sintoma salvo = sintomaRepository.save(sintoma);
        return SintomaMapper.toDTO(salvo);
    }
}


