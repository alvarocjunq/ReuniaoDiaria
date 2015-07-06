package br.com.consinco.reuniaodiaria.modelos;

public class RCPorPrioridade {

	private String prioridade;
	private int homologado;
	private int germud;
	private int backlog;
	private int atrasados;
	private int emdia;

	
	public String getPrioridade() {
		return prioridade;
	}
	public void setPrioridade(String prioridade) {
		this.prioridade = prioridade;
	}
	public int getHomologado() {
		return homologado;
	}
	public void setHomologado(int homologado) {
		this.homologado = homologado;
	}
	public int getGermud() {
		return germud;
	}
	public void setGermud(int germud) {
		this.germud = germud;
	}
	public int getBacklog() {
		return backlog;
	}
	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}
	public int getAtrasados() {
		return atrasados;
	}
	public void setAtrasados(int atrasados) {
		this.atrasados = atrasados;
	}
	public int getEmdia() {
		return emdia;
	}
	public void setEmdia(int emdia) {
		this.emdia = emdia;
	}
	
	

}
