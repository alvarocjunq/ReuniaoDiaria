package br.com.consinco.reuniaodiaria.modelos;

import java.util.Calendar;

public class TabelaRC {

	private int rc;
	private String projeto;
	private String responsavel;
	private Calendar prazodesenv;
	private Calendar prazohomolog;
	private Calendar dataRequisicao;
	private String etapa;
	private int diasatrasado;
	private String sistema;
	private String aplicacao;
	private int totalTabela;
	private String nrochamado;
	private int prioridade;
	private String merge;
	private String solucao;
	private int percentualConcluido;
	private Calendar dtaHoraInclusao;
	private String usuInclusao;	
	private String anotacao;
	private String categoria;
	private String analistaSistema;
	private String analistaNegocio;
	private String analistaAnotou;
	private Calendar dtaPrometida;
	private Calendar dtaLibHomolog;
	private int possuirp;
				
	public int getPossuirp() {
		return possuirp;
	}
	public void setPossuirp(int possuirp) {
		this.possuirp = possuirp;
	}
	public Calendar getDtaLibHomolog() {
		return dtaLibHomolog;
	}
	public void setDtaLibHomolog(Calendar dtaLibHomolog) {
		this.dtaLibHomolog = dtaLibHomolog;
	}
	public Calendar getDtaPrometida() {
		return dtaPrometida;
	}
	public void setDtaPrometida(Calendar dtaPrometida) {
		this.dtaPrometida = dtaPrometida;
	}
	public String getAnalistaSistema() {
		return analistaSistema;
	}
	public void setAnalistaSistema(String analistaSistema) {
		this.analistaSistema = analistaSistema;
	}
	public String getAnalistaNegocio() {
		return analistaNegocio;
	}
	public void setAnalistaNegocio(String analistaNegocio) {
		this.analistaNegocio = analistaNegocio;
	}
	public String getAnalistaAnotou() {
		return analistaAnotou;
	}
	public void setAnalistaAnotou(String analistaAnotou) {
		this.analistaAnotou = analistaAnotou;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getAnotacao() {
		return anotacao;
	}
	public void setAnotacao(String anotacao) {
		this.anotacao =  anotacao;
	}
	public String getUsuInclusao() {
		return usuInclusao;
	}
	public void setUsuInclusao(String usuInclusao) {
		this.usuInclusao = usuInclusao;
	}
	public Calendar getDtaHoraInclusao() {
		return dtaHoraInclusao;
	}
	public void setDtaHoraInclusao(Calendar dtaHoraInclusao) {
		this.dtaHoraInclusao = dtaHoraInclusao;
	}
	public String getMerge() {
		return merge;
	}
	public void setMerge(String merge) {
		this.merge = merge;
	}
	public String getSolucao() {
		return solucao;
	}
	public void setSolucao(String solucao) {
		this.solucao = solucao;
	}
	public int getPercentualConcluido() {
		return percentualConcluido;
	}
	public void setPercentualConcluido(int percentualConcluido) {
		this.percentualConcluido = percentualConcluido;
	}
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
		String projetoT = projeto.replaceAll("\n", "<br/>");
		this.projeto = projetoT;
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
