package br.com.consinco.reuniaodiaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.consinco.reuniaodiaria.modelos.TabelaRPdeRC;
import br.com.consinco.reuniaodiaria.utilitarios.ManipularString;

public class TabelaRPdeRCDAO {
	
	private final Connection connection;

	public TabelaRPdeRCDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<TabelaRPdeRC> getListaRP( String pRC ) {
		try {
			List<TabelaRPdeRC> lstTabelaRP = new ArrayList<TabelaRPdeRC>();
			PreparedStatement stmt = this.connection.prepareStatement(
					"Select Rareq.Seqrequisicao As Rp,\n" +
					"       (App.Modulo || ': ' || App.Codaplicacao || '-' || App.Descricao) As Aplicacao,\n" + 
					"       To_Number(Rareq.Hrsprevista) As HrsPrevista,\n" + 
					"       Rareq.Codetapa,\n" + 
					"       Ana.Codinome As AnalistaAnotou,\n" +
					"		DBMS_LOB.SUBSTR( Hist.ANOTACAO, 4000, 1 ) as Anotacao," +
					"		Rareq.Seqrequisitocliente as RC, " +
					"(Select Dbms_Lob.Substr(Ra_Historico.Anotacao, 4000, 1) as AnotacaoRC \n" +
					"   From Ra_Requisicao,\n" + 
					"        Ra_Historico\n" + 
					"  Where Ra_Historico.Seqrequisicao = Ra_Requisicao.Seqrequisicao\n" + 
					"    And Ra_Requisicao.Seqrequisicao = Rareq.Seqrequisicao\n" + 
					"    And Ra_Historico.Fase = 0 ) as AnotacaoRC" +
					"  From Ra_Requisicao Rareq,\n" + 
					"       Ge_Aplicacao  App,\n" + 
					"       Ra_Analista   Ana,\n" +
					"		Ra_Historico  Hist" + 
					" Where Rareq.Seqrequisitocliente = "+ pRC + "\n" + 
					"   And Rareq.Seqaplicacao = App.Seqaplicacao\n" + 
					"   And Rareq.Analistaanot = Ana.Seqanalista " +
					"   And Rareq.Seqrequisicao = Hist.Seqrequisicao " +
					"   And Hist.Fase = 0 \n" + 
					" Order By 1");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
				TabelaRPdeRC objRP = new TabelaRPdeRC();
				objRP.setRp				(rs.getInt("Rp"));
				objRP.setAplicacao		(rs.getString("Aplicacao"));
				objRP.setHrsprevista	(rs.getInt("HrsPrevista"));
				objRP.setCodetapa		(rs.getString("Codetapa"));
				objRP.setAnalistaAnotou	(rs.getString("AnalistaAnotou"));
				objRP.setAnotacao		(rs.getString("Anotacao"));
				objRP.setRc				(rs.getInt("RC"));
//				objRP.setAnotacao		(rs.getString("AnotacaoRC"));
				objRP.setAnotacao		(ManipularString.convertTextRTF2HTML(rs.getString("AnotacaoRC")));
				
				if  (objRP.getAnotacao().equals(""))
						objRP.setAnotacao ("1");
					
				lstTabelaRP.add(objRP);
			}

			rs.close();
			stmt.close();

			return lstTabelaRP;
		} catch (SQLException e) {
			System.out.println("RP de RC ----> " + e.getErrorCode() + " - " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
}