package br.com.hackaton.priorizasus.casosdeuso;

import br.com.hackaton.priorizasus.entities.FilaAtendimento;
import br.com.hackaton.priorizasus.entities.FilaTriagem;
import br.com.hackaton.priorizasus.entities.Paciente;
import br.com.hackaton.priorizasus.enums.StatusAtendimentoEnum;
import br.com.hackaton.priorizasus.enums.StatusTriagemEnum;
import br.com.hackaton.priorizasus.exception.EntidadeNaoEncontradaException;
import br.com.hackaton.priorizasus.repository.FilaAtendimentoRepository;
import br.com.hackaton.priorizasus.repository.FilaTriagemRepository;
import br.com.hackaton.priorizasus.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class RegistrarChegadaUseCase {

    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private FilaTriagemRepository filaTriagemRepository;
    @Autowired
    private FilaAtendimentoRepository filaAtendimentoRepository;

    @Transactional
    public void registrarChegada(Long pacienteId, Boolean atendimentoAdministrativo) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Paciente não encontrado"));

        // Se já existir o cpf na fila de triagem ou de atendimento não deixa
        if(!filaAtendimentoRepository.findByTriagem_Paciente_CpfAndStatusAtendimentoEnum(paciente.getCpf(),StatusAtendimentoEnum.PENDENTE).isEmpty()||
        !filaTriagemRepository.findByPacienteCpfAndStatusTriagem(paciente.getCpf(),StatusTriagemEnum.AGUARDANDO).isEmpty()){
            throw new IllegalArgumentException("Já existe uma chegada com este paciente! Finalize ou cancele para começar uma nova!");
        }

        if (Boolean.TRUE.equals(atendimentoAdministrativo)) {
            FilaAtendimento fila = new FilaAtendimento();
            fila.setAtendimentoAdministrativo(true);
            fila.setStatusAtendimentoEnum(StatusAtendimentoEnum.PENDENTE);
            fila.setHorarioEntradaFila(LocalDateTime.now());
            filaAtendimentoRepository.save(fila);
        } else {
            FilaTriagem filaTriagem = new FilaTriagem();
            filaTriagem.setPaciente(paciente);
            filaTriagem.setStatusTriagem(StatusTriagemEnum.AGUARDANDO);
            filaTriagem.setHorarioEntrada(LocalDateTime.now());
            filaTriagemRepository.save(filaTriagem);
        }
    }
}

