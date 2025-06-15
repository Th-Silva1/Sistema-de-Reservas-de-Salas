/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.entidade;

import java.time.LocalDateTime;

/**
 *
 * @author Thalles
 */
public class Sala {
    private int idSala;
    private String nomeSala;
    private int capacidade;
    private String descricao;
    private boolean disponivel;
    private String equipamentos;
    private LocalDateTime dataCriacao;
    
    public Sala() {
        this.disponivel = true;
        this.dataCriacao = LocalDateTime.now();
    }
    
    public Sala(String nomeSala, int capacidade, String descricao, String equipamentos) {
        this();
        this.nomeSala = nomeSala;
        this.capacidade = capacidade;
        this.descricao = descricao;
        this.equipamentos = equipamentos;
    }
    
    // Getters e Setters
    public int getIdSala() {
        return idSala;
    }
    
    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }
    
    public String getNomeSala() {
        return nomeSala;
    }
    
    public void setNomeSala(String nomeSala) {
        this.nomeSala = nomeSala;
    }
    
    public int getCapacidade() {
        return capacidade;
    }
    
    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public boolean isDisponivel() {
        return disponivel;
    }
    
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
    
    public String getEquipamentos() {
        return equipamentos;
    }
    
    public void setEquipamentos(String equipamentos) {
        this.equipamentos = equipamentos;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    @Override
    public String toString() {
        return "Sala{" +
                "idSala=" + idSala +
                ", nomeSala='" + nomeSala + '\'' +
                ", capacidade=" + capacidade +
                ", disponivel=" + disponivel +
                '}';
    }
}