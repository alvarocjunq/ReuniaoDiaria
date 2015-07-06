package br.com.consinco.reuniaodiaria.modelos;

import java.util.Calendar;

public class ProximaVersao {

	private int rc;
	private String projeto;
	private String etapa;
	private String responsavel;
	private int prioridade;
	private Calendar dataRequisicao;
	private String sistema;
	//private Strin
	
	private Calendar prazodesenv;
	private Calendar prazohomolog;

	
	private int diasatrasado;

	private String aplicacao;
	private int totalTabela;
	private String nrochamado;

	
	
	public int getPrioridade() {
		return prioridade;
	}
	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}
	public int getRc() {
		return rc;
	}
	public void setRc(int rc) {
		this.rc = rc;
	}
	public String getProjeto() {
		return projeto;
	}
	public void setProjeto(String projeto) {
		this.projeto = projeto.replaceAll("\n", "<br/>");
	}
	public String getResponsavel() {
		return responsavel;
	}
	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel.replaceAll("\n", "<br/>");
	}
	public Calendar getPrazodesenv() {
		return prazodesenv;
	}
	public void setPrazodesenv(Calendar prazodesenv) {
		this.prazodesenv = prazodesenv;
	}
	public Calendar getPrazohomolog() {
		return prazohomolog;
	}
	public void setPrazohomolog(Calendar prazohomolog) {
		this.prazohomolog = prazohomolog;
	}
	public String getEtapa() {
		return etapa;
	}
	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}
	public int getDiasatrasado() {
		return diasatrasado;
	}
	public void setDiasatrasado(int diasatrasado) {
		this.diasatrasado = diasatrasado;
	}
	public Calendar getDataRequisicao() {
		return dataRequisicao;
	}
	public void setDataRequisicao(Calendar dataRequisicao) {
		this.dataRequisicao = dataRequisicao;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	public String getAplicacao() {
		return aplicacao;
	}
	public void setAplicacao(String aplicacao) {
		this.aplicacao = aplicacao;
	}
	public int getTotalTabela() {
		return totalTabela;
	}
	public void setTotalTabela(int totalTabela) {
		this.totalTabela = totalTabela;
	}
	public String getNrochamado() {
		return nrochamado;
	}
	public void setNrochamado(String nrochamado) {
		this.nrochamado = nrochamado;
	}
	
}
