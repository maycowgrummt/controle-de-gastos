package com.financeiro.gestao.controller;

import com.financeiro.gestao.model.Transacao;
import com.financeiro.gestao.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TransacaoController {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Transacao> transacoes = transacaoRepository.findAll();
        model.addAttribute("transacoes", transacoes);
        model.addAttribute("transacao", new Transacao());

        // Calcula os totais por categoria para alimentar o gráfico de pizza
        Map<String, Double> totaisPorCategoria = new HashMap<>();
        for (Transacao t : transacoes) {
            // Considera apenas despesas ou tudo, dependendo da preferência (aqui somamos tudo o que for lançado)
            if (t.getCategoria() != null && t.getValor() != null) {
                totaisPorCategoria.put(
                    t.getCategoria(), 
                    totaisPorCategoria.getOrDefault(t.getCategoria(), 0.0) + t.getValor()
                );
            }
        }

        model.addAttribute("categoriasLabels", totaisPorCategoria.keySet());
        model.addAttribute("categoriasValores", totaisPorCategoria.values());

        return "index";
    }

    @PostMapping("/salvar")
    public String salvar(Transacao transacao) {
        if (transacao.getTipo() == null || transacao.getTipo().isEmpty()) {
            transacao.setTipo("DESPESA");
        }
        transacaoRepository.save(transacao);
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        Transacao transacao = transacaoRepository.findById(id).orElse(null);
        model.addAttribute("transacao", transacao);
        model.addAttribute("transacoes", transacaoRepository.findAll());
        return "index";
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable("id") Long id) {
        transacaoRepository.deleteById(id);
        return "redirect:/";
    }
}