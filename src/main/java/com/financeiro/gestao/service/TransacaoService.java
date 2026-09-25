package com.financeiro.gestao.service;

import com.financeiro.gestao.model.Transacao;
import com.financeiro.gestao.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    public List<Transacao> listarTodas() {
        return transacaoRepository.findAll();
    }

    // Agrupa as transações por data para exibir organizadas no extrato
    public Map<LocalDate, List<Transacao>> listarAgrupadasPorDia() {
        List<Transacao> transacoes = transacaoRepository.findAll();
        return transacoes.stream()
                .collect(Collectors.groupingBy(Transacao::getData));
    }

    public Transacao salvar(Transacao transacao) {
        if (transacao.getData() == null) {
            transacao.setData(LocalDate.now());
        }
        if (transacao.getTipo() == null || transacao.getTipo().isEmpty()) {
            transacao.setTipo("DESPESA"); // Define padrão como despesa se não vier preenchido
        }
        return transacaoRepository.save(transacao);
    }

    public void deletar(Long id) {
        transacaoRepository.deleteById(id);
    }
}