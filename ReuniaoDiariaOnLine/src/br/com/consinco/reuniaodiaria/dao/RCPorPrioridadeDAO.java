package br.com.consinco.reuniaodiaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.consinco.reuniaodiaria.modelos.RCPorPrioridade;

public class RCPorPrioridadeDAO {
	
	private final Connection connection;

	public RCPorPrioridadeDAO(Connection connection) {
		this.connection = connection;
	}


	public List<RCPorPrioridade> getLista() {
		try {
			List<RCPorPrioridade> lstResumoRC = new ArrayList<RCPorPrioridade>();
			
			PreparedStatement stmt = this.connection.prepareStatement(
						"SELECT X.PRIORIDADE || ' - ' || decode(X.PRIORIDADE, 1, 'Urgente', 2, 'Alta', 3, 'Média', 'Baixa') as Prioridade,\n" +
						"\n" + 
						"       SUM(X.HOMOLOGADO) as Homologado,\n" + 
						"       SUM(X.GERMUD) as Germud,\n" + 
						"       SUM(X.BACKLOG) as Backlog,\n" + 
						"       SUM(X.Atrasado) as Atrasados,\n" + 
						"       SUM(X.EmDia) as EmDia\n" + 
						"FROM (\n" + 
						"\n" + 
						"Select RA_REQUISICAO.Prioridade as Prioridade,\n" + 
						"       COUNT(CASE WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN 'H' END ) AS homologado,\n" + 
						"       COUNT(CASE WHEN RA_ANALISTA.CODINOME = 'GERMUD' THEN  'G' END ) AS germud,\n" + 
						"       0 as Backlog,\n" + 
						"       0 as Atrasado,\n" + 
						"       0 as EmDia\n" + 
						"  From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA, ge_aplicacao c, ge_modulo j\n" + 
						" Where RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
						"   AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO\n" + 
						"   AND C.MODULO = J.MODULO\n" +
						"	And c.Sistema = j.Sistema" + 
						"   and ra_requisicao.seqaplicacao = c.seqaplicacao\n" + 
						"   AND RA_APONTAMENTO.DTATERMINO IS NULL\n" + 
						"   AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR\n" + 
						"       (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND\n" + 
						"       RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))\n" + 
						"   AND RA_REQUISICAO.CODETAPA IN\n" + 
						"       ('AGE.PRODUÇAO', 'AGE.SUPORTE', 'AND.PRODUÇAO', 'AND.SUPORTE',\n" + 
						"        'REF.PRODUÇAO', 'TESTES', 'SUSP.PROD', 'SUSP.SUP', 'SUSPENSO',\n" + 
						"        'LIB.TESTES', 'PEND.CLIENTE')\n" + 
						"   AND TO_NUMBER(REPLACE(SUBSTR(J.VERSAO,1,2), '.',NULL)) > 10\n" + 
						"GROUP BY RA_REQUISICAO.Prioridade\n" + 
						"HAVING COUNT(CASE WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN 'H' END ) > 0\n" + 
						"OR    COUNT(CASE WHEN RA_ANALISTA.CODINOME = 'GERMUD' THEN  'G' END ) > 0\n" + 
						"\n" + 
						"UNION\n" + 
						"\n" + 
						"SELECT Z.PRIORIDADE ,\n" + 
						"       0,\n" + 
						"       0,\n" + 
						"       0,\n" + 
						"       0,\n" + 
						"       COUNT(*) as EmDia\n" + 
						"FROM (\n" + 
						"Select CASE\n" + 
						"         WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN\n" + 
						"          'H'\n" + 
						"         WHEN RA_ANALISTA.CODINOME = 'GERMUD' THEN\n" + 
						"          'G'\n" + 
						"         WHEN RA_REQUISICAO.DTAPROMETIDA < TRUNC(SYSDATE) AND\n" + 
						"              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) != 'ASS' THEN\n" + 
						"          'A'\n" + 
						"         WHEN RA_REQUISICAO.DTAPREVLIBERACAO < TRUNC(SYSDATE) AND\n" + 
						"              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) = 'ASS' THEN\n" + 
						"          'A'\n" + 
						"         ELSE\n" + 
						"          'D'\n" + 
						"       END STATUS,\n" + 
						"       RA_REQUISICAO.PRIORIDADE\n" + 
						"\n" + 
						"  From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA, GE_APLICACAO C, GE_MODULO J\n" + 
						" Where RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
						"   AND C.MODULO = J.MODULO\n" +
						"	And c.Sistema = j.Sistema " + 
						"   AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO\n" + 
						"   AND RA_REQUISICAO.SEQAPLICACAO = C.SEQAPLICACAO\n" + 
						"   AND RA_APONTAMENTO.DTATERMINO IS NULL\n" + 
						"   AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR\n" + 
						"       (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND\n" + 
						"       RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))\n" + 
						"   AND RA_REQUISICAO.CODETAPA IN\n" + 
						"       ('AGE.PRODUÇAO', 'AGE.SUPORTE', 'AND.PRODUÇAO', 'AND.SUPORTE',\n" + 
						"        'REF.PRODUÇAO', 'TESTES', 'SUSP.PROD', 'SUSP.SUP', 'SUSPENSO',\n" + 
						"        'LIB.TESTES', 'PEND.CLIENTE')\n" + 
						" GROUP BY RA_ANALISTA.CODINOME,\n" + 
						"          NVL(DECODE(RA_REQUISICAO.TIPOREQUISICAO,\n" + 
						"                     'R',\n" + 
						"                     RA_REQUISICAO.SEQREQUISITOCLIENTE,\n" + 
						"                     RA_REQUISICAO.SEQREQUISICAO),\n" + 
						"              RA_REQUISICAO.SEQREQUISICAO),\n" + 
						"          RA_REQUISICAO.JUSTIFICATIVA,\n" + 
						"          RA_REQUISICAO.DTAPROMETIDA,\n" + 
						"          ra_requisicao.sistema,\n" + 
						"          RA_REQUISICAO.DTAPREVLIBERACAO,\n" + 
						"          RA_REQUISICAO.CODETAPA,\n" + 
						"          RA_REQUISICAO.PRIORIDADE ) Z\n" + 
						"WHERE Z.STATUS = 'D'\n" + 
						"GROUP BY Z.PRIORIDADE\n" + 
						"\n" + 
						"UNION\n" + 
						"\n" + 
						"SELECT c.PRIORIDADE,\n" + 
						"       0,\n" + 
						"       0,\n" + 
						"       COUNT(*) as Backlog,\n" + 
						"       0,\n" + 
						"       0\n" + 
						"  FROM RA_REQUISICAO C, GE_APLICACAO G, GE_MODULO J\n" + 
						" WHERE C.CODETAPA != 'CANCELADO'\n" + 
						"   AND G.MODULO = J.MODULO\n" +
						"	And g.Sistema = j.Sistema " + 
						"   AND C.CODETAPA = 'SOL.PRODUÇAO'\n" + 
						"   AND C.DTAREQUISICAO > '01/JAN/2011'\n" + 
						"   and c.seqrequisicao > 900000\n" + 
						"   and c.seqaplicacao = g.seqaplicacao\n" + 
						"   AND C.SEQREQUISITOCLIENTE IS NULL\n" +
						"	AND C.STATUSCOMERCIAL = 'L' " + 
						"GROUP BY C.PRIORIDADE\n" + 
						"\n" + 
						"UNION\n" + 
						"\n" + 
						"SELECT Y.PRIORIDADE ,\n" + 
						"       0,\n" + 
						"       0,\n" + 
						"       0,\n" + 
						"       COUNT(*) as Atrasados,\n" + 
						"       0\n" + 
						"FROM (\n" + 
						"Select CASE\n" + 
						"         WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN 'H'\n" + 
						"         WHEN RA_ANALISTA.CODINOME = 'GERMUD' THEN 'G'\n" + 
						"         WHEN RA_REQUISICAO.DTAPROMETIDA < TRUNC(SYSDATE) AND\n" + 
						"              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) != 'ASS'\n" + 
						"         THEN 'A'\n" + 
						"         WHEN RA_REQUISICAO.DTAPREVLIBERACAO < TRUNC(SYSDATE) AND\n" + 
						"              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) = 'ASS'\n" + 
						"         THEN 'A'\n" + 
						"      ELSE\n" + 
						"        'D'\n" + 
						"      END STATUS,\n" + 
						"      RA_REQUISICAO.PRIORIDADE\n" + 
						"\n" + 
						"  From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA, ge_aplicacao c, GE_MODULO J\n" + 
						" Where C.MODULO = J.MODULO\n" +
						"	And c.Sistema = j.Sistema " + 
						"   AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
						"   AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO\n" + 
						"   and ra_requisicao.seqaplicacao = c.seqaplicacao\n" + 
						"   AND RA_APONTAMENTO.DTATERMINO IS NULL\n" + 
						"   AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR\n" + 
						"       (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND\n" + 
						"       RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))\n" + 
						"   AND RA_REQUISICAO.CODETAPA IN ('AGE.PRODUÇAO',\n" + 
						"                                  'AGE.SUPORTE',\n" + 
						"                                  'AND.PRODUÇAO',\n" + 
						"                                  'AND.SUPORTE',\n" + 
						"                                  'REF.PRODUÇAO',\n" + 
						"                                  'TESTES',\n" + 
						"                                  'SUSP.PROD',\n" + 
						"                                  'SUSP.SUP',\n" + 
						"                                  'SUSPENSO',\n" + 
						"                                  'LIB.TESTES',\n" + 
						"                                  'PEND.CLIENTE')\n" + 
						" GROUP BY RA_ANALISTA.CODINOME,\n" + 
						"          NVL(DECODE(RA_REQUISICAO.TIPOREQUISICAO,\n" + 
						"                     'R',\n" + 
						"                     RA_REQUISICAO.SEQREQUISITOCLIENTE,\n" + 
						"                     RA_REQUISICAO.SEQREQUISICAO),\n" + 
						"              RA_REQUISICAO.SEQREQUISICAO),\n" + 
						"          RA_REQUISICAO.JUSTIFICATIVA,\n" + 
						"          RA_REQUISICAO.DTAPROMETIDA,\n" + 
						"          ra_requisicao.sistema,\n" + 
						"          RA_REQUISICAO.DTAPREVLIBERACAO,\n" + 
						"          RA_REQUISICAO.CODETAPA,\n" + 
						"          RA_REQUISICAO.PRIORIDADE) Y\n" + 
						"WHERE Y.STATUS = 'A'\n" + 
						"GROUP BY Y.PRIORIDADE\n" + 
						"\n" + 
						") X\n" + 
						"\n" + 
						"\n" + 
						"GROUP BY X.PRIORIDADE\n" + 
						"ORDER BY 1");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {	
				RCPorPrioridade resumoRCPrioridade = new RCPorPrioridade();
				resumoRCPrioridade.setPrioridade(rs.getString("Prioridade"));
				resumoRCPrioridade.setHomologado(rs.getInt	("Homologado"));
				resumoRCPrioridade.setAtrasados	(rs.getInt	("Atrasados"));
				resumoRCPrioridade.setGermud	(rs.getInt	("Germud"));
				resumoRCPrioridade.setEmdia		(rs.getInt	("EmDia"));
				resumoRCPrioridade.setBacklog	(rs.getInt	("Backlog"));
	
				lstResumoRC.add(resumoRCPrioridade);
			}

			rs.close();
			stmt.close();

			return lstResumoRC;
		} catch (SQLException e) {
			System.out.println("RCPorPrioridade --->" + e.getErrorCode() + " - " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

}
