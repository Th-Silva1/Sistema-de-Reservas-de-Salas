/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.entidade;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Thalles
 */
public class Reserva {
    private int idReserva;
    private int idUsuario;
    private int idSala;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private String finalidade;
    private String statusReserva;
    private LocalDateTime dataCriacao;
    
    // Propriedades auxiliares para exibição
    private String nomeUsuario;
    private String nomeSala;
    
    public Reserva() {
        this.dataCriacao = LocalDateTime.now();
        this.statusReserva = "PENDENTE";
    }
    
    public Reserva(int idUsuario, int idSala, LocalDateTime dataHoraInicio, 
                   LocalDateTime dataHoraFim, String finalidade) {
        this();
        this.idUsuario = idUsuario;
        this.idSala = idSala;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.finalidade = finalidade;
    }
    
    // Getters e Setters
    public int getIdReserva() {
        return idReserva;
    }
    
    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public int getIdSala() {
        return idSala;
    }
    
    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }
    
    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }
    
    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }
    
    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }
    
    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }
    
    public String getFinalidade() {
        return finalidade;
    }
    
    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
    }
    
    public String getStatusReserva() {
        return statusReserva;
    }
    
    public void setStatusReserva(String statusReserva) {
        this.statusReserva = statusReserva;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public String getNomeUsuario() {
        return nomeUsuario;
    }
    
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
    
    public String getNomeSala() {
        return nomeSala;
    }
    
    public void setNomeSala(String nomeSala) {
        this.nomeSala = nomeSala;
    }
    
    // Métodos auxiliares
    public String getDataHoraInicioFormatada() {
        if (dataHoraInicio != null) {
            return dataHoraInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        return "";
    }
    
    public String getDataHoraFimFormatada() {
        if (dataHoraFim != null) {
            return dataHoraFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        return "";
    }
    
    public String getDataFormatada() {
        if (dataHoraInicio != null) {
            return dataHoraInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "";
    }
    
    public String getHoraInicioFormatada() {
        if (dataHoraInicio != null) {
            return dataHoraInicio.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "";
    }
    
    public String getHoraFimFormatada() {
        if (dataHoraFim != null) {
            return dataHoraFim.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "";
    }
    
    public boolean isConflitoCom(Reserva outraReserva) {
        if (this.idSala != outraReserva.getIdSala()) {
            return false;
        }
        
        return this.dataHoraInicio.isBefore(outraReserva.getDataHoraFim()) &&
               this.dataHoraFim.isAfter(outraReserva.getDataHoraInicio());
    }
    
    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", nomeSala='" + nomeSala + '\'' +
                ", nomeUsuario='" + nomeUsuario + '\'' +
                ", dataHoraInicio=" + getDataHoraInicioFormatada() +
                ", dataHoraFim=" + getDataHoraFimFormatada() +
                ", statusReserva='" + statusReserva + '\'' +
                '}';
    }
}