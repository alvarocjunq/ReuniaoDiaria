package br.com.consinco.reuniaodiaria.modelos;

public class RCAtrasadoPorAnalista {

	private int quantidade;
	private String responsavel;
	private int totalRC;
	
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		
		this.quantidade = quantidade;
	}
	public String getResponsavel() {
		return responsavel;
	}
	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel.replaceAll("\n", "<br/>");
	}
	public int getTotalRC() {
		return totalRC;
	}
	public void setTotalRC(int totalRC) {
		this.totalRC = totalRC;
	}

}
