package br.com.consinco.reuniaodiaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.consinco.reuniaodiaria.modelos.RCPorModulo;

public class RCPorModuloDAO {
	
	private final Connection connection;

	public RCPorModuloDAO(Connection connection) {
		this.connection = connection;
	}


	public List<RCPorModulo> getLista() {
		try {
			List<RCPorModulo> lstResumoRC = new ArrayList<RCPorModulo>();
			
			PreparedStatement stmt = this.connection.prepareStatement(
							" SELECT X.APLICACAO as Aplicacao,"
							+"        SUM(X.HOMOLOGADO) as Homologado,"
							+"        SUM(X.GERMUD) as Germud,"
							+"        SUM(X.BACKLOG) as Backlog,"
							+"        SUM(X.Atrasado) as Atrasados,"
							+"        SUM(X.EmDia) as EmDia"
							+" FROM ("
							+" "
							+" Select J.MODULO || ' - ' || J.DESCRICAO as Aplicacao,"
							+"        COUNT(CASE WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN 'H' END ) AS homologado,"
							+"        COUNT(CASE WHEN RA_ANALISTA.CODINOME = 'GERMUD' THEN  'G' END ) AS germud,"
							+"        0 as Backlog,"
							+"        0 as Atrasado,"
							+"        0 as EmDia"
							+"   From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA, ge_aplicacao c, ge_modulo j"
							+"  Where RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA"
							+"    AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO"
							+"    AND C.MODULO = J.MODULO" +
							"	  And c.Sistema = j.Sistema "
							+"    and ra_requisicao.seqaplicacao = c.seqaplicacao"
							+"   AND RA_APONTAMENTO.DTATERMINO IS NULL"
							+"    AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR"
							+"        (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND"
							+"        RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))"
							+"    AND RA_REQUISICAO.CODETAPA IN"
							+"        ('AGE.PRODUÇAO', 'AGE.SUPORTE', 'AND.PRODUÇAO', 'AND.SUPORTE',"
							+"         'REF.PRODUÇAO', 'TESTES', 'SUSP.PROD', 'SUSP.SUP', 'SUSPENSO',"
							+"         'LIB.TESTES', 'PEND.CLIENTE') "
							+"    AND TO_NUMBER(REPLACE(SUBSTR(J.VERSAO,1,2), '.',NULL)) > 10 "
							+" GROUP BY J.MODULO || ' - ' || J.DESCRICAO"
							+" HAVING COUNT(CASE WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN 'H' END ) > 0"
							+" OR    COUNT(CASE WHEN RA_ANALISTA.CODINOME = 'GERMUD' THEN  'G' END ) > 0"
							+" "
							+" UNION"
							+" "
							+" SELECT Z.APLICACAO ,"
							+"        0,"
							+"        0,"
							+"        0,"
							+"        0,"
							+"        COUNT(*) as EmDia"
							+" FROM ("
							+" Select CASE"
							+"          WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN"
							+"           'H'"
							+"          WHEN RA_ANALISTA.CODINOME = 'GERMUD' THEN"
							+"           'G'"
							+"          WHEN RA_REQUISICAO.DTAPROMETIDA < TRUNC(SYSDATE) AND"
							+"               SUBSTR(RA_ANALISTA.CODINOME, 1, 3) != 'ASS' THEN"
							+"           'A'"
							+"          WHEN RA_REQUISICAO.DTAPREVLIBERACAO < TRUNC(SYSDATE) AND"
							+"               SUBSTR(RA_ANALISTA.CODINOME, 1, 3) = 'ASS' THEN"
							+"           'A'"
							+"          ELSE"
							+"           'D'"
							+"        END STATUS,"
							+"        MAX(DECODE(C.MODULO,'OPERLIGHT','OPERADOR',C.MODULO) || ' - ' || J.DESCRICAO) as Aplicacao"
							+" "
							+"   From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA, GE_APLICACAO C, GE_MODULO J"
							+"  Where RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA"
							+"    AND C.MODULO = J.MODULO" +
							"	  And c.Sistema = j.Sistema "
							+"    AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO"
							+"    AND RA_REQUISICAO.SEQAPLICACAO = C.SEQAPLICACAO"
							+"    AND RA_APONTAMENTO.DTATERMINO IS NULL"
							+"    AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR"
							+"        (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND"
							+"        RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))"
							+"    AND RA_REQUISICAO.CODETAPA IN"
							+"        ('AGE.PRODUÇAO', 'AGE.SUPORTE', 'AND.PRODUÇAO', 'AND.SUPORTE',"
							+"         'REF.PRODUÇAO', 'TESTES', 'SUSP.PROD', 'SUSP.SUP', 'SUSPENSO',"
							+"         'LIB.TESTES', 'PEND.CLIENTE')"
							+"  GROUP BY RA_ANALISTA.CODINOME,"
							+"           NVL(DECODE(RA_REQUISICAO.TIPOREQUISICAO,"
							+"                      'R',"
							+"                      RA_REQUISICAO.SEQREQUISITOCLIENTE,"
							+"                      RA_REQUISICAO.SEQREQUISICAO),"
							+"               RA_REQUISICAO.SEQREQUISICAO),"
							+"           RA_REQUISICAO.JUSTIFICATIVA,"
							+"           RA_REQUISICAO.DTAPROMETIDA,"
							+"           ra_requisicao.sistema,"
							+"           RA_REQUISICAO.DTAPREVLIBERACAO,"
							+"           RA_REQUISICAO.CODETAPA ) Z"
							+" WHERE Z.STATUS = 'D'"
							+" GROUP BY Z.APLICACAO"
							+" "
							+" UNION"
							+" "
							+" SELECT J.MODULO || ' - ' || J.DESCRICAO as Aplicacao,"
							+"        0,"
							+"        0,"
							+"        COUNT(*) as Backlog,"
							+"        0,"
							+"        0"
							+"   FROM RA_REQUISICAO C, GE_APLICACAO G, GE_MODULO J"
							+"  WHERE C.CODETAPA != 'CANCELADO'"
							+"    AND G.MODULO = J.MODULO" +
							"	  And g.Sistema = j.Sistema"
							+"    AND C.CODETAPA = 'SOL.PRODUÇAO'"
							+"    AND C.DTAREQUISICAO > '01/JAN/2011'"
							+"    and c.seqrequisicao > 900000"
							+"    and c.seqaplicacao = g.seqaplicacao"
							+"    AND C.SEQREQUISITOCLIENTE IS NULL"
							+"	  AND C.STATUSCOMERCIAL = 'L' "
							+" GROUP BY J.MODULO || ' - ' || J.DESCRICAO"
							+" "
							+" UNION"
							+" "
							+" SELECT Y.APLICACAO ,"
							+"        0,"
							+"        0,"
							+"        0,"
							+"        COUNT(*) as Atrasados,"
							+"        0"
							+" FROM ("
							+" Select CASE"
							+"          WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN 'H'"
							+"          WHEN RA_ANALISTA.CODINOME = 'GERMUD' THEN 'G'"
							+"          WHEN RA_REQUISICAO.DTAPROMETIDA < TRUNC(SYSDATE) AND"
							+"               SUBSTR(RA_ANALISTA.CODINOME, 1, 3) != 'ASS'"
							+"          THEN 'A'"
							+"          WHEN RA_REQUISICAO.DTAPREVLIBERACAO < TRUNC(SYSDATE) AND"
							+"               SUBSTR(RA_ANALISTA.CODINOME, 1, 3) = 'ASS'"
							+"          THEN 'A'"
							+"       ELSE"
							+"         'D'"
							+"       END STATUS,"
							+"       MAX(J.MODULO || ' - ' || J.DESCRICAO) as Aplicacao"
							+" "
							+"   From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA, ge_aplicacao c, GE_MODULO J"
							+"  Where C.MODULO = J.MODULO" +
							"     And c.Sistema = j.Sistema "
							+"    AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA"
							+"    AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO"
							+"    and ra_requisicao.seqaplicacao = c.seqaplicacao"
							+"    AND RA_APONTAMENTO.DTATERMINO IS NULL"
							+"    AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR"
							+"        (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND"
							+"        RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))"
							+"    AND RA_REQUISICAO.CODETAPA IN ('AGE.PRODUÇAO',"
							+"                                   'AGE.SUPORTE',"
							+"                                   'AND.PRODUÇAO',"
							+"                                   'AND.SUPORTE',"
							+"                                   'REF.PRODUÇAO',"
							+"                                   'TESTES',"
							+"                                   'SUSP.PROD',"
							+"                                   'SUSP.SUP',"
							+"                                   'SUSPENSO',"
							+"                                   'LIB.TESTES',"
							+"                                   'PEND.CLIENTE')"
							+"  GROUP BY RA_ANALISTA.CODINOME,"
							+"           NVL(DECODE(RA_REQUISICAO.TIPOREQUISICAO,"
							+"                      'R',"
							+"                      RA_REQUISICAO.SEQREQUISITOCLIENTE,"
							+"                      RA_REQUISICAO.SEQREQUISICAO),"
							+"               RA_REQUISICAO.SEQREQUISICAO),"
							+"           RA_REQUISICAO.JUSTIFICATIVA,"
							+"           RA_REQUISICAO.DTAPROMETIDA,"
							+"           ra_requisicao.sistema,"
							+"           RA_REQUISICAO.DTAPREVLIBERACAO, "
							+"           RA_REQUISICAO.CODETAPA) Y"
							+" WHERE Y.STATUS = 'A'"
							+" GROUP BY Y.APLICACAO"
							+" "
							+" ) X"
							+" "
							+" "
							+" GROUP BY X.APLICACAO"
							+" ORDER BY 1");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {	
				RCPorModulo resumoRCModulo = new RCPorModulo();
				resumoRCModulo.setAplicacao	(rs.getString("Aplicacao"));
				resumoRCModulo.setHomologado(rs.getInt	("Homologado"));
				resumoRCModulo.setAtrasados	(rs.getInt	("Atrasados"));
				resumoRCModulo.setGermud	(rs.getInt	("Germud"));
				resumoRCModulo.setEmdia		(rs.getInt	("EmDia"));
				resumoRCModulo.setBacklog	(rs.getInt	("Backlog"));
	
				lstResumoRC.add(resumoRCModulo);
			}

			rs.close();
			stmt.close();

			return lstResumoRC;
		} catch (SQLException e) {
			System.out.println("RC por modulo ----> " + e.getErrorCode() + " - " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
