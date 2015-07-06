package br.com.consinco.reuniaodiaria.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.consinco.reuniaodiaria.modelos.TabelaRC;
import br.com.consinco.reuniaodiaria.utilitarios.ManipularString;

public class TabelaRCDAO {
	
	private final Connection connection;

	public TabelaRCDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<TabelaRC> getListaTabela(String status) {
		try {
			List<TabelaRC> lstTabelaRC = new ArrayList<TabelaRC>();
			PreparedStatement stmt = this.connection.prepareStatement(
							 "SELECT X.RC,"
							+"       NVL( X.Projeto, ' ') as Projeto ,"
							+"       X.Responsavel,"
							+"       X.Prazo_Desenv,"
							+"       X.Prazo_Homol,"
							+"       X.Etapa,"
							+"       X.Dias,"
							+"       X.NroChamado,"
							+"       X.Sistema,"
							+"       X.Aplicacao,"
							+"       X.Prioridade,"
							+"       X.DtaHoraInclusao,"
							+"       X.UsuarioInclusao,"
							+"       X.Anotacao,"
							+"       X.Categoria,"
							+"       X.AnalistaSistema,"
							+"       X.AnalistaNegocio,"
							+"       X.AnalistaAnotou,"
							+"       X.DtaLibHomologa,"
							+"       X.Possuirp"
							+" FROM ("
							+" Select NVL(DECODE(RA_REQUISICAO.TIPOREQUISICAO,"
							+"                  'R',"
							+"                  RA_REQUISICAO.SEQREQUISITOCLIENTE,"
							+"                  RA_REQUISICAO.SEQREQUISICAO),"
							+"        RA_REQUISICAO.SEQREQUISICAO) RC,"
							+"       InitCap( RA_REQUISICAO.JUSTIFICATIVA) PROJETO,"
							+""
							+"       RA_ANALISTA.CODINOME || CHR(13)|| CHR(10) ||"
							+"       DECODE(SUBSTR(RA_ANALISTA.CODINOME, 1, 3),"
							+"              'ASS',"
							+"              'TI-ASSAÍ',"
							+"              'CONSINCO') RESPONSAVEL,"
							+""
							+"       RA_REQUISICAO.DTAPROMETIDA PRAZO_DESENV,"
							+"       RA_REQUISICAO.DTAPREVLIBERACAO PRAZO_HOMOL,"
							+"       DECODE( RA_REQUISICAO.CODETAPA,"
							+"               'PEND.CLIENTE', 'HOMOLOGAÇÃO',"
							+"               'LIB.TESTES', 'TESTES CONS.',"
							+"               'DESENVOLVIMENTO' ) ETAPA,"
							+""
							+"     CASE WHEN"
							+"         TRUNC(SYSDATE ) > ( DECODE ( SUBSTR(RA_ANALISTA.CODINOME, 1, 3),"
							+"               'ASS', RA_REQUISICAO.DTAPREVLIBERACAO,"
							+"               RA_REQUISICAO.DTAPROMETIDA )) THEN"
							+""
							+"           (TRUNC(SYSDATE ) -"
							+"           DECODE ( SUBSTR(RA_ANALISTA.CODINOME, 1, 3),"
							+"                     'ASS', RA_REQUISICAO.DTAPREVLIBERACAO,"
							+"                     RA_REQUISICAO.DTAPROMETIDA ))"
							+"      ELSE 0 END DIAS,"
							+""
							+"   MAX((SELECT MAX(RA_REQCLIENTE.NROCHAMADO) "
							+"      FROM RA_REQCLIENTE "
							+"      WHERE RA_REQCLIENTE.SEQREQUISICAO = RA_REQUISICAO.SEQREQUISICAO)) as NroChamado,"
							+""
							+"      ra_requisicao.sistema,"
							+""
							+"      c.modulo||': '|| c.codaplicacao||'-'||c.descricao aplicacao,"
							+""
							+"      CASE"
							+"         WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN 'H'"
							+"         WHEN RA_ANALISTA.CODINOME = 'GERMUD' THEN 'G'"
							+"         WHEN RA_REQUISICAO.DTAPROMETIDA < TRUNC(SYSDATE) AND"
							+"              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) != 'ASS'"
							+"         THEN 'A'"
							+"         WHEN RA_REQUISICAO.DTAPREVLIBERACAO < TRUNC(SYSDATE) AND"
							+"              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) = 'ASS'"
							+"         THEN 'A'"
							+"      ELSE"
							+"        'D'"
							+"      END STATUS," 
							+"		RA_REQUISICAO.Prioridade as Prioridade,"
							+"        "
							+"        max((select l.dtainclusao"
							+"               from ra_requisicaolog l"
							+"              where l.seqrequisicao = RA_REQUISICAO.Seqrequisicao"
							+"                and l.item ="
							+"                   (select max(x.item)"
							+"                      from ra_requisicaolog x"
							+"                     where x.seqrequisicao = l.Seqrequisicao))) as DtaHoraInclusao,"
							+"        "
							+"        max((select l.codusuario"
							+"               from ra_requisicaolog l"
							+"              where l.seqrequisicao = RA_REQUISICAO.Seqrequisicao"
							+"                and l.item ="
							+"                   (select max(x.item)"
							+"                      from ra_requisicaolog x"
							+"                     where x.seqrequisicao = l.Seqrequisicao))) as UsuarioInclusao, "
							+"       DBMS_LOB.SUBSTR( RA_HISTORICO.ANOTACAO, 4000, 1 ) as Anotacao, "
							+" 		 DECODE(RA_REQUISICAO.Categoria,'C','Customização','E','Erro','Fiscal/Legal') as Categoria, "
							
							+"NVL((SELECT Ana_Sis.Codinome\n" +
							"   FROM RA_ANALISTA Ana_Sis\n" + 
							"  WHERE Ana_Sis.Seqanalista = RA_REQUISICAO.Anasisresp ),'Não informado') as AnalistaSistema,\n" + 
							"\n" + 
							"NVL((SELECT Ana_Neg.Codinome\n" + 
							"   FROM RA_ANALISTA Ana_Neg\n" + 
							"  WHERE Ana_Neg.Seqanalista = RA_REQUISICAO.Ananegresp ),'Não informado') as AnalistaNegocio,\n" + 
							"\n" + 
							"NVL((SELECT Ana_Anot.Codinome\n" + 
							"   FROM RA_ANALISTA Ana_Anot\n" + 
							"  WHERE Ana_Anot.Seqanalista = RA_REQUISICAO.ANALISTAANOT ),'Não informado') as AnalistaAnotou, "
							
							+"   TRUNC(RA_REQUISICAO.DtaLibHomologa) AS DtaLibHomologa, "
							+"	 Case "
							+"         When (Select Count(*) "
							+"                 From Ra_Requisicao Rareq "
							+"                Where Rareq.Seqrequisitocliente = Ra_Requisicao.Seqrequisicao "
							+"                  And Rownum = 1) > 0 Then "
							+"          '1' "
							+"         Else "
							+"          '0' "
							+"       End Possuirp" 
							+"  From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA, ge_aplicacao c, RA_HISTORICO"
							+" Where RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA"
							+"   AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO"
							+"   and ra_requisicao.seqaplicacao = c.seqaplicacao"
							+"   AND RA_APONTAMENTO.DTATERMINO IS NULL"
							+"   AND RA_HISTORICO.SEQREQUISICAO = RA_REQUISICAO.SEQREQUISICAO"
							+"   AND RA_HISTORICO.FASE = 0 "
							+"   AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR"
							+"       (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND"
							+"       RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))"
							+"   AND RA_REQUISICAO.CODETAPA IN ('AGE.PRODUÇAO',"
							+"                                  'AGE.SUPORTE',"
							+"                                  'AND.PRODUÇAO',"
							+"                                  'AND.SUPORTE',"
							+"                                  'REF.PRODUÇAO',"
							+"                                  'TESTES',"
							+"                                  'SUSP.PROD',"
							+"                                  'SUSP.SUP',"
							+"                                  'SUSPENSO',"
							+"                                  'LIB.TESTES',"
							+"                                  'PEND.CLIENTE')"
							+" GROUP BY Ra_Requisicao.Seqrequisicao, " 
							+" 			DBMS_LOB.SUBSTR( RA_HISTORICO.ANOTACAO, 4000, 1 ), " 
							+"			RA_ANALISTA.CODINOME,"
							+"          NVL(DECODE(RA_REQUISICAO.TIPOREQUISICAO,"
							+"                     'R',"
							+"                     RA_REQUISICAO.SEQREQUISITOCLIENTE,"
							+"                     RA_REQUISICAO.SEQREQUISICAO),"
							+"              RA_REQUISICAO.SEQREQUISICAO),"
							+"          RA_REQUISICAO.JUSTIFICATIVA,"
							+"          RA_REQUISICAO.DTAPROMETIDA,"
							+"          ra_requisicao.sistema,"
							+"          RA_REQUISICAO.DTAPREVLIBERACAO, RA_REQUISICAO.CODETAPA,"
							+"          c.modulo||': '|| c.codaplicacao||'-'||c.descricao,"
							+"			RA_REQUISICAO.Prioridade, " 
							+" 			RA_REQUISICAO.Categoria,"
							+"			RA_REQUISICAO.ANALISTAANOT,"
							+"			RA_REQUISICAO.Anasisresp,"
							+"			RA_REQUISICAO.Ananegresp," 
							+" 			RA_REQUISICAO.DtaLibHomologa ) X "
							+" WHERE X.STATUS = '" + status +  "'");
			
			
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
				switch (status) {
				case "A":
					lstTabelaRC.add(popularCampos(rs, "Atrasado"));
					break;
				case "H":
					lstTabelaRC.add(popularCampos(rs, "Homologado"));
					break;
				case "D":
					lstTabelaRC.add(popularCampos(rs, "EmDia"));
					break;
				default: //G = Germud
					lstTabelaRC.add(popularCampos(rs, "Germud"));
					break;
				}
			}

			rs.close();
			stmt.close();


			return lstTabelaRC;
		} catch (SQLException e) {
			System.out.println("Tabelas ----> " + e.getErrorCode() + " - " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public List<TabelaRC> getListaBacklog() {
		try {
			List<TabelaRC> lstTabelaRC = new ArrayList<TabelaRC>();
			PreparedStatement stmt = this.connection.prepareStatement(
							 "SELECT C.SEQREQUISICAO as RC, "
							+"       Nvl(c.Justificativa, 'Não informado') as Projeto,"
							+" (SELECT MAX(RA_REQCLIENTE.NROCHAMADO) "
							+"    FROM RA_REQCLIENTE "
							+"   WHERE RA_REQCLIENTE.SEQREQUISICAO = C.SEQREQUISICAO) as NroChamado,"
							+"       c.sistema as Sistema, "
							+" 		(g.modulo||': '|| g.codaplicacao||'-'||g.descricao) as Aplicacao, "
							+"       c.dtarequisicao as DataRequisicao, "
							+"       C.PRIORIDADE, "
							+"       DBMS_LOB.SUBSTR( RA_HISTORICO.ANOTACAO, 4000, 1 ) as Anotacao, "
							+"       DECODE(C.Categoria, 'C', 'Customização', 'E', 'Erro', 'Fiscal/Legal') as Categoria, "
							+"   NVL((SELECT Ana_Sis.Codinome "
							+"           FROM RA_ANALISTA Ana_Sis "
						    +"          WHERE Ana_Sis.Seqanalista = C.Anasisresp), "
						    +"         'Não informado') as AnalistaSistema, "
						    +"     "
						    +"     NVL((SELECT Ana_Neg.Codinome "
						    +"           FROM RA_ANALISTA Ana_Neg "
						    +"          WHERE Ana_Neg.Seqanalista = C.Ananegresp), "
						    +"         'Não informado') as AnalistaNegocio, "
						    +"      "
						    +"     NVL((SELECT Ana_Anot.Codinome "
						    +"           FROM RA_ANALISTA Ana_Anot "
						    +"          WHERE Ana_Anot.Seqanalista = C.ANALISTAANOT), "
						    +"         'Não informado') as AnalistaAnotou, "
						    +"         C.DTAPROMETIDA as DtaPrometida," +
							"		Case\n" +
							"         When (Select Count(*)\n" + 
							"                 From Ra_Requisicao Rareq\n" + 
							"                Where Rareq.Seqrequisitocliente = c.Seqrequisicao\n" + 
							"                  And Rownum = 1) > 0 Then\n" + 
							"          '1'\n" + 
							"         Else\n" + 
							"          '0'\n" + 
							"       End Possuirp" 
							+"  FROM RA_REQUISICAO C, ge_aplicacao g, RA_HISTORICO "
							+" WHERE C.CODETAPA != 'CANCELADO' "
							+"   AND RA_HISTORICO.SEQREQUISICAO = C.SEQREQUISICAO"
							+"   AND RA_HISTORICO.FASE = 0"
							+"   AND C.CODETAPA = 'SOL.PRODUÇAO' "
							+"   AND C.DTAREQUISICAO > '01/JAN/2011' "
							+"   and c.seqrequisicao > 900000 "
							+"   and c.seqaplicacao = g.seqaplicacao  "
							+"   AND C.SEQREQUISITOCLIENTE IS NULL "
							+"   AND C.STATUSCOMERCIAL = 'L' ");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
				lstTabelaRC.add(popularCampos(rs, "Backlog"));
			}

			rs.close();
			stmt.close();

			return lstTabelaRC;
		} catch (SQLException e) {
			System.out.println("Backlog----> " + e.getErrorCode() + " - " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public List<TabelaRC> getListaAnalise() {
		try {
			List<TabelaRC> lstTabelaRC = new ArrayList<TabelaRC>();
			PreparedStatement stmt = this.connection.prepareStatement(
							 "SELECT C.SEQREQUISICAO as RC, "
							+"       Nvl(c.Justificativa, 'Não informado') As Projeto,"
							+" (SELECT MAX(RA_REQCLIENTE.NROCHAMADO) "
							+"    FROM RA_REQCLIENTE "
							+"   WHERE RA_REQCLIENTE.SEQREQUISICAO = C.SEQREQUISICAO) as NroChamado,"
							+"       c.sistema as Sistema, "
							+" 		(g.modulo||': '|| g.codaplicacao||'-'||g.descricao) as Aplicacao, "
							+"       c.dtarequisicao as DataRequisicao, "
							+"       C.PRIORIDADE, "
							+"       DBMS_LOB.SUBSTR( RA_HISTORICO.ANOTACAO, 4000, 1 ) as Anotacao, "
							+"       DECODE(C.Categoria, 'C', 'Customização', 'E', 'Erro', 'Fiscal/Legal') as Categoria, "
							+"   NVL((SELECT Ana_Sis.Codinome "
							+"           FROM RA_ANALISTA Ana_Sis "
						    +"          WHERE Ana_Sis.Seqanalista = C.Anasisresp), "
						    +"         'Não informado') as AnalistaSistema, "
						    +"     "
						    +"     NVL((SELECT Ana_Neg.Codinome "
						    +"           FROM RA_ANALISTA Ana_Neg "
						    +"          WHERE Ana_Neg.Seqanalista = C.Ananegresp), "
						    +"         'Não informado') as AnalistaNegocio, "
						    +"      "
						    +"     NVL((SELECT Ana_Anot.Codinome "
						    +"           FROM RA_ANALISTA Ana_Anot "
						    +"          WHERE Ana_Anot.Seqanalista = C.ANALISTAANOT), "
						    +"         'Não informado') as AnalistaAnotou, "
						    +"         C.DTAPROMETIDA as DtaPrometida," +
							"Case\n" +
							"  When (Select Count(*)\n" + 
							"          From Ra_Requisicao Rareq\n" + 
							"         Where Rareq.Seqrequisitocliente = c.Seqrequisicao\n" + 
							"           And Rownum = 1) > 0 Then\n" + 
							"   '1'\n" + 
							"  Else\n" + 
							"   '0'\n" + 
							"End Possuirp"
							+"  FROM RA_REQUISICAO C, ge_aplicacao g, RA_HISTORICO "
							+" WHERE C.CODETAPA != 'CANCELADO' "
							+"   AND RA_HISTORICO.SEQREQUISICAO = C.SEQREQUISICAO"
							+"   AND RA_HISTORICO.FASE = 0"
							+"   AND C.CODETAPA = 'SOL.PRODUÇAO' "
							+"   AND C.DTAREQUISICAO > '01/JAN/2011' "
							+"   and c.seqrequisicao > 900000 "
							+"   and c.seqaplicacao = g.seqaplicacao  "
							+"   AND C.SEQREQUISITOCLIENTE IS NULL "
							+"   AND ( C.STATUSCOMERCIAL != 'L' OR C.STATUSCOMERCIAL IS NULL ) ");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
				lstTabelaRC.add(popularCampos(rs, "Analise"));
			}

			rs.close();
			stmt.close();

			return lstTabelaRC;
		} catch (SQLException e) {
			System.out.println("Análise----> " + e.getErrorCode() + " - " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public List<TabelaRC> getListaProxVersao() {
		try {
			List<TabelaRC> lstTabelaRC = new ArrayList<TabelaRC>();
			PreparedStatement stmt = this.connection.prepareStatement(
							"select r.seqrequisicao as RC,\n" +
							"       NVL(r.justificativa,'Não informado') as Projeto,\n" + 
							"       R.CODETAPA as Etapa,\n" + 
							"       nvl((select a.codinome\n" + 
							"          from ra_analista a\n" + 
							"         where a.seqanalista = r.anasisresp), ' ') as Responsavel,\n" + 
							"       r.prioridade  as Prioridade,\n" + 
							"       r.dtarequisicao  as DataRequisicao,\n" + 
							"       r.sistema,\n" + 
							"       decode(rp.ismerge, 'S', 'SIM', 'NAO') as Merge,\n" + 
							"       DECODE((SELECT LENGTH(H.ANOTACAO)\n" + 
							"                FROM RA_HISTORICO H\n" + 
							"               WHERE H.SEQREQUISICAO = R.SEQREQUISICAO\n" + 
							"                 AND H.FASE = 2),\n" + 
							"              326,\n" + 
							"              'NAO',\n" + 
							"              'SIM') as SOLUCAO,\n" + 
							"       DECODE(R.CODETAPA, 'GERAR EXEC', 25, 0) +\n" + 
							"       decode(rp.ismerge, 'S', 50, 0) +\n" + 
							"       DECODE((SELECT LENGTH(H.ANOTACAO)\n" + 
							"                FROM RA_HISTORICO H\n" + 
							"               WHERE H.SEQREQUISICAO = R.SEQREQUISICAO\n" + 
							"                 AND H.FASE = 2),\n" + 
							"              326,\n" + 
							"              0,\n" + 
							"              25) as PercConcluido,\n" + 
							"                (SELECT MAX(RA_REQCLIENTE.NROCHAMADO)\n" + 
							"					FROM RA_REQCLIENTE\n" + 
							"				   WHERE RA_REQCLIENTE.SEQREQUISICAO = r.SEQREQUISICAO) as NroChamado,\n" +
							"		DBMS_LOB.SUBSTR( RA_HISTORICO.ANOTACAO, 4000, 1 ) as Anotacao," +
							"		DECODE(R.Categoria, 'C', 'Customização', 'E', 'Erro', 'Fiscal/Legal') as Categoria, " + 
							
							"NVL((SELECT Ana_Sis.Codinome\n" +
							"      FROM RA_ANALISTA Ana_Sis\n" + 
							"     WHERE Ana_Sis.Seqanalista = r.Anasisresp),\n" + 
							"    'Não informado') as AnalistaSistema,\n" + 
							"\n" + 
							"NVL((SELECT Ana_Neg.Codinome\n" + 
							"      FROM RA_ANALISTA Ana_Neg\n" + 
							"     WHERE Ana_Neg.Seqanalista = r.Ananegresp),\n" + 
							"    'Não informado') as AnalistaNegocio,\n" + 
							"\n" + 
							"NVL((SELECT Ana_Anot.Codinome\n" + 
							"      FROM RA_ANALISTA Ana_Anot\n" + 
							"     WHERE Ana_Anot.Seqanalista = r.ANALISTAANOT),\n" + 
							"    'Não informado') as AnalistaAnotou,"+

							"Case\n" +
							"  When (Select Count(*)\n" + 
							"          From Ra_Requisicao Rareq\n" + 
							"         Where Rareq.Seqrequisitocliente = r.Seqrequisicao\n" + 
							"           And Rownum = 1) > 0 Then\n" + 
							"   '1'\n" + 
							"  Else\n" + 
							"   '0'\n" + 
							"End Possuirp" +
							
							"  from ra_requisicao r, ra_reqprojeto rp, RA_HISTORICO \n" + 
							" where r.seqrequisicao = rp.seqrequisicao\n" +
							"   AND RA_HISTORICO.SEQREQUISICAO = r.SEQREQUISICAO\n" +
							"   AND RA_HISTORICO.FASE = 0\n" + 
							"   and (rp.ismerge = 'S' or r.codetapa = 'GERAR EXEC')\n" + 
							"   and r.codetapa != 'CANCELADO'\n" + 
							"   and r.codetapa != 'LIB.CLIENTE'\n" + 
							"   AND rp.versao = '12.01'\n" + 
							"   and rp.release is null\n" + 
							"   and r.seqrequisitocliente is null");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
				lstTabelaRC.add(popularCampos(rs, "ProxVersao"));
			}

			rs.close();
			stmt.close();

			return lstTabelaRC;
		} catch (SQLException e) {
			System.out.println("ProxVersao----> " + e.getErrorCode() + " - " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	private TabelaRC popularCampos (ResultSet rs, String tabela) throws SQLException 
	{
		TabelaRC objTabelaRC = new TabelaRC();
		
		Date dprazodesenv;
		Date dprazohomolog;
		Date dDtaRequisicao;
		Date dDtaPrometida;
		Date dDtaLibHomolog;
		Timestamp tDtaHoraInclusao;
	
		
		//Campos que tem em todas tabelas
		objTabelaRC.setRc				(rs.getInt("RC"));
		objTabelaRC.setProjeto			(rs.getString("Projeto"));
		objTabelaRC.setSistema			(rs.getString("Sistema"));
		objTabelaRC.setNrochamado		(rs.getString("NroChamado"));
		objTabelaRC.setPrioridade		(rs.getInt("Prioridade"));
		objTabelaRC.setAnalistaAnotou	(rs.getString("AnalistaAnotou"));
		objTabelaRC.setAnalistaNegocio	(rs.getString("AnalistaNegocio"));
		objTabelaRC.setAnalistaSistema	(rs.getString("AnalistaSistema"));
		objTabelaRC.setPossuirp			(rs.getInt("Possuirp"));
		
		objTabelaRC.setAnotacao(ManipularString.convertTextRTF2HTML(rs.getString("Anotacao")));
		
		objTabelaRC.setCategoria(rs.getString("Categoria"));
		
		switch (tabela) 
		{
		case "Backlog":
			objTabelaRC.setAplicacao(rs.getString("Aplicacao"));
			
			dDtaRequisicao = rs.getDate("DataRequisicao");
			if ( dDtaRequisicao != null )
			{
				Calendar cDtaRequisicao = Calendar.getInstance();
				cDtaRequisicao.setTime(dDtaRequisicao);
				objTabelaRC.setDataRequisicao(cDtaRequisicao);
			}
			
			dDtaPrometida = rs.getDate("DtaPrometida");
			if ( dDtaPrometida != null )
			{
				Calendar cDtaPrometida = Calendar.getInstance();
				cDtaPrometida.setTime(dDtaPrometida);
				objTabelaRC.setDtaPrometida(cDtaPrometida);
			}
			break;
			
		case "Analise":
			objTabelaRC.setAplicacao(rs.getString("Aplicacao"));
			
			dDtaRequisicao = rs.getDate("DataRequisicao");
			if ( dDtaRequisicao != null )
			{
				Calendar cDtaRequisicao = Calendar.getInstance();
				cDtaRequisicao.setTime(dDtaRequisicao);
				objTabelaRC.setDataRequisicao(cDtaRequisicao);
			}
			
			dDtaPrometida = rs.getDate("DtaPrometida");
			if ( dDtaPrometida != null )
			{
				Calendar cDtaPrometida = Calendar.getInstance();
				cDtaPrometida.setTime(dDtaPrometida);
				objTabelaRC.setDtaPrometida(cDtaPrometida);
			}
			break;

		case "Atrasado":
			objTabelaRC.setResponsavel(rs.getString("Responsavel"));
			dprazodesenv = rs.getDate("Prazo_Desenv");
			dprazohomolog = rs.getDate("Prazo_Homol");
			
			if ( dprazodesenv != null )
			{
				Calendar cprazodesen = Calendar.getInstance();
				cprazodesen.setTime(dprazodesenv);
				objTabelaRC.setPrazodesenv(cprazodesen);
			}

			if ( dprazohomolog != null )
			{
				Calendar cprazohomolog = Calendar.getInstance();
				cprazohomolog.setTime(dprazohomolog);
				objTabelaRC.setPrazohomolog(cprazohomolog);
			}

			objTabelaRC.setEtapa(rs.getString("Etapa"));
			objTabelaRC.setDiasatrasado(rs.getInt("Dias"));
			objTabelaRC.setAplicacao(rs.getString("Aplicacao"));

			break;
		
		case "Homologado":
			objTabelaRC.setAplicacao(rs.getString("Aplicacao"));
			objTabelaRC.setUsuInclusao(rs.getString("UsuarioInclusao"));

			tDtaHoraInclusao = rs.getTimestamp("DtaHoraInclusao");
			if ( tDtaHoraInclusao != null ) 
			{
				Calendar cDtaHoraInclusao = Calendar.getInstance();
				cDtaHoraInclusao.setTime(tDtaHoraInclusao);
				objTabelaRC.setDtaHoraInclusao(cDtaHoraInclusao);
			}

			dDtaLibHomolog = rs.getDate("DtaLibHomologa");
			if ( dDtaLibHomolog != null )
			{
				Calendar cLibHomolog = Calendar.getInstance();
				cLibHomolog.setTime(dDtaLibHomolog);
				objTabelaRC.setDtaLibHomolog(cLibHomolog);
			}
			
			break;
		
		case "EmDia":
			objTabelaRC.setResponsavel(rs.getString("Responsavel"));
			dprazodesenv = rs.getDate("Prazo_Desenv");
			dprazohomolog = rs.getDate("Prazo_Homol");
			
			if ( dprazodesenv != null )
			{
				Calendar cprazodesen = Calendar.getInstance();
				cprazodesen.setTime(dprazodesenv);
				objTabelaRC.setPrazodesenv(cprazodesen);
			}

			if ( dprazohomolog != null )
			{
				Calendar cprazohomolog = Calendar.getInstance();
				cprazohomolog.setTime(dprazohomolog);
				objTabelaRC.setPrazohomolog(cprazohomolog);
			}
			
			dDtaLibHomolog = rs.getDate("DtaLibHomologa");
			if ( dDtaLibHomolog != null )
			{
				Calendar cLibHomolog = Calendar.getInstance();
				cLibHomolog.setTime(dDtaLibHomolog);
				objTabelaRC.setDtaLibHomolog(cLibHomolog);
			}
			
			objTabelaRC.setEtapa(rs.getString("Etapa"));
			objTabelaRC.setAplicacao(rs.getString("Aplicacao"));
			
			break;
		
		case "ProxVersao":
			objTabelaRC.setEtapa				(rs.getString("Etapa"));
			objTabelaRC.setResponsavel			(rs.getString("Responsavel"));
			objTabelaRC.setMerge				(rs.getString("Merge"));
			objTabelaRC.setSolucao				(rs.getString("Solucao"));
			objTabelaRC.setPercentualConcluido	(rs.getInt("PercConcluido"));

			dDtaRequisicao = rs.getDate("DataRequisicao");
			if ( dDtaRequisicao != null )
			{
				Calendar cDtaRequisicao = Calendar.getInstance();
				cDtaRequisicao.setTime(dDtaRequisicao);
				objTabelaRC.setDataRequisicao(cDtaRequisicao);
			}
			
			break;
		
		default: //Germud
			objTabelaRC.setAplicacao(rs.getString("Aplicacao"));
			objTabelaRC.setUsuInclusao(rs.getString("UsuarioInclusao"));
			
			tDtaHoraInclusao = rs.getTimestamp("DtaHoraInclusao");
			if ( tDtaHoraInclusao != null ) 
			{
				Calendar cDtaHoraInclusao = Calendar.getInstance();
				cDtaHoraInclusao.setTime(tDtaHoraInclusao);
				objTabelaRC.setDtaHoraInclusao(cDtaHoraInclusao);
			}
			
			dDtaLibHomolog = rs.getDate("DtaLibHomologa");
			if ( dDtaLibHomolog != null )
			{
				Calendar cLibHomolog = Calendar.getInstance();
				cLibHomolog.setTime(dDtaLibHomolog);
				objTabelaRC.setDtaLibHomolog(cLibHomolog);
			}
			
			break;
		}


		return objTabelaRC;
	}
	

	
}