package br.com.consinco.reuniaodiaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.consinco.reuniaodiaria.modelos.RCPorCategoria;

public class RCPorCategoriaDAO {
	
	private final Connection connection;

	public RCPorCategoriaDAO(Connection connection) {
		this.connection = connection;
	}


	public List<RCPorCategoria> getLista() {
		try {
			List<RCPorCategoria> lstResumoRC = new ArrayList<RCPorCategoria>();
			
			PreparedStatement stmt = this.connection.prepareStatement(
					"SELECT DECODE(X.Categoria,'C','Customização','E','Erro','Fiscal/Legal') as Categoria,\n" + 
					"       SUM(X.HOMOLOGADO) as Homologado,\n" + 
					"       SUM(X.GERMUD) as Germud,\n" + 
					"       SUM(X.BACKLOG) as Backlog,\n" + 
					"       SUM(X.Atrasado) as Atrasados,\n" + 
					"       SUM(X.EmDia) as EmDia\n" + 
					"  FROM (\n" + 
					"\n" + 
					"        Select RA_REQUISICAO.Categoria as Categoria,\n" + 
					"                COUNT(CASE\n" + 
					"                        WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN\n" + 
					"                         'H'\n" + 
					"                      END) AS homologado,\n" + 
					"                COUNT(CASE\n" + 
					"                        WHEN RA_ANALISTA.CODINOME = 'GERMUD' THEN\n" + 
					"                         'G'\n" + 
					"                      END) AS germud,\n" + 
					"                0 as Backlog,\n" + 
					"                0 as Atrasado,\n" + 
					"                0 as EmDia\n" + 
					"          From RA_APONTAMENTO,\n" + 
					"                RA_REQUISICAO,\n" + 
					"                RA_ANALISTA,\n" + 
					"                ge_aplicacao   c,\n" + 
					"                ge_modulo      j\n" + 
					"         Where RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
					"           AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO\n" + 
					"           AND C.MODULO = J.MODULO\n" +
					"			And c.Sistema = j.Sistema " + 
					"           and ra_requisicao.seqaplicacao = c.seqaplicacao\n" + 
					"           AND RA_APONTAMENTO.DTATERMINO IS NULL\n" + 
					"           AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR\n" + 
					"               (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND\n" + 
					"               RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))\n" + 
					"           AND RA_REQUISICAO.CODETAPA IN\n" + 
					"               ('AGE.PRODUÇAO',\n" + 
					"                'AGE.SUPORTE',\n" + 
					"                'AND.PRODUÇAO',\n" + 
					"                'AND.SUPORTE',\n" + 
					"                'REF.PRODUÇAO',\n" + 
					"                'TESTES',\n" + 
					"                'SUSP.PROD',\n" + 
					"                'SUSP.SUP',\n" + 
					"                'SUSPENSO',\n" + 
					"                'LIB.TESTES',\n" + 
					"                'PEND.CLIENTE')\n" + 
					"           AND TO_NUMBER(REPLACE(SUBSTR(J.VERSAO, 1, 2), '.', NULL)) > 10\n" + 
					"         GROUP BY RA_REQUISICAO.Categoria\n" + 
					"        HAVING COUNT(CASE\n" + 
					"          WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN\n" + 
					"           'H'\n" + 
					"        END) > 0 OR COUNT(CASE\n" + 
					"          WHEN RA_ANALISTA.CODINOME = 'GERMUD' THEN\n" + 
					"           'G'\n" + 
					"        END) > 0\n" + 
					"\n" + 
					"        UNION\n" + 
					"\n" + 
					"        SELECT Z.Categoria, 0, 0, 0, 0, COUNT(*) as EmDia\n" + 
					"          FROM (Select CASE\n" + 
					"                         WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN\n" + 
					"                          'H'\n" + 
					"                         WHEN RA_ANALISTA.CODINOME = 'GERMUD' THEN\n" + 
					"                          'G'\n" + 
					"                         WHEN RA_REQUISICAO.DTAPROMETIDA < TRUNC(SYSDATE) AND\n" + 
					"                              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) != 'ASS' THEN\n" + 
					"                          'A'\n" + 
					"                         WHEN RA_REQUISICAO.DTAPREVLIBERACAO < TRUNC(SYSDATE) AND\n" + 
					"                              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) = 'ASS' THEN\n" + 
					"                          'A'\n" + 
					"                         ELSE\n" + 
					"                          'D'\n" + 
					"                       END STATUS,\n" + 
					"                       RA_REQUISICAO.Categoria\n" + 
					"\n" + 
					"                  From RA_APONTAMENTO,\n" + 
					"                       RA_REQUISICAO,\n" + 
					"                       RA_ANALISTA,\n" + 
					"                       GE_APLICACAO   C,\n" + 
					"                       GE_MODULO      J\n" + 
					"                 Where RA_APONTAMENTO.SEQANALISTA in\n" + 
					"                       (select a.seqanalista\n" + 
					"                          from ra_analista a\n" + 
					"                         where A.STATUSREL IS NOT NULL\n" + 
					"                           AND A.STATUSREL = 'A')\n" + 
					"                   AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
					"                   AND C.MODULO = J.MODULO\n" +
					"					And c.Sistema = j.Sistema " + 
					"                   AND RA_REQUISICAO.SEQREQUISICAO =\n" + 
					"                       RA_APONTAMENTO.SEQREQUISICAO\n" + 
					"                   AND RA_REQUISICAO.SEQAPLICACAO = C.SEQAPLICACAO\n" + 
					"                   AND RA_APONTAMENTO.DTATERMINO IS NULL\n" + 
					"                   AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR\n" + 
					"                       (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND\n" + 
					"                       RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))\n" + 
					"                   AND RA_REQUISICAO.CODETAPA IN\n" + 
					"                       ('AGE.PRODUÇAO',\n" + 
					"                        'AGE.SUPORTE',\n" + 
					"                        'AND.PRODUÇAO',\n" + 
					"                        'AND.SUPORTE',\n" + 
					"                        'REF.PRODUÇAO',\n" + 
					"                        'TESTES',\n" + 
					"                        'SUSP.PROD',\n" + 
					"                        'SUSP.SUP',\n" + 
					"                        'SUSPENSO',\n" + 
					"                        'LIB.TESTES',\n" + 
					"                        'PEND.CLIENTE')\n" + 
					"                 GROUP BY RA_ANALISTA.CODINOME,\n" + 
					"                          NVL(DECODE(RA_REQUISICAO.TIPOREQUISICAO,\n" + 
					"                                     'R',\n" + 
					"                                     RA_REQUISICAO.SEQREQUISITOCLIENTE,\n" + 
					"                                     RA_REQUISICAO.SEQREQUISICAO),\n" + 
					"                              RA_REQUISICAO.SEQREQUISICAO),\n" + 
					"                          RA_REQUISICAO.JUSTIFICATIVA,\n" + 
					"                          RA_REQUISICAO.DTAPROMETIDA,\n" + 
					"                          ra_requisicao.sistema,\n" + 
					"                          RA_REQUISICAO.DTAPREVLIBERACAO,\n" + 
					"                          RA_REQUISICAO.CODETAPA,\n" + 
					"                          RA_REQUISICAO.Categoria) Z\n" + 
					"         WHERE Z.STATUS = 'D'\n" + 
					"         GROUP BY Z.Categoria\n" + 
					"\n" + 
					"        UNION\n" + 
					"\n" + 
					"        SELECT c.Categoria, 0, 0, COUNT(*) as Backlog, 0, 0\n" + 
					"          FROM RA_REQUISICAO C, GE_APLICACAO G, GE_MODULO J\n" + 
					"         WHERE C.CODETAPA != 'CANCELADO'\n" + 
					"           AND G.MODULO = J.MODULO\n" +
					"			And g.Sistema = j.Sistema " + 
					"           AND C.CODETAPA = 'SOL.PRODUÇAO'\n" + 
					"           AND C.DTAREQUISICAO > '01/JAN/2011'\n" + 
					"           and c.seqrequisicao > 900000\n" + 
					"           and c.seqaplicacao = g.seqaplicacao\n" + 
					"           AND C.SEQREQUISITOCLIENTE IS NULL" +
					"			AND C.STATUSCOMERCIAL = 'L'\n" + 
					"         GROUP BY C.Categoria\n" + 
					"\n" + 
					"        UNION\n" + 
					"\n" + 
					"        SELECT Y.Categoria, 0, 0, 0, COUNT(*) as Atrasados, 0\n" + 
					"          FROM (Select CASE\n" + 
					"                         WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN\n" + 
					"                          'H'\n" + 
					"                         WHEN RA_ANALISTA.CODINOME = 'GERMUD' THEN\n" + 
					"                          'G'\n" + 
					"                         WHEN RA_REQUISICAO.DTAPROMETIDA < TRUNC(SYSDATE) AND\n" + 
					"                              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) != 'ASS' THEN\n" + 
					"                          'A'\n" + 
					"                         WHEN RA_REQUISICAO.DTAPREVLIBERACAO < TRUNC(SYSDATE) AND\n" + 
					"                              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) = 'ASS' THEN\n" + 
					"                          'A'\n" + 
					"                         ELSE\n" + 
					"                          'D'\n" + 
					"                       END STATUS,\n" + 
					"                       RA_REQUISICAO.Categoria\n" + 
					"\n" + 
					"                  From RA_APONTAMENTO,\n" + 
					"                       RA_REQUISICAO,\n" + 
					"                       RA_ANALISTA,\n" + 
					"                       ge_aplicacao   c,\n" + 
					"                       GE_MODULO      J\n" + 
					"                 Where RA_APONTAMENTO.SEQANALISTA in\n" + 
					"                       (select a.seqanalista\n" + 
					"                          from ra_analista a\n" + 
					"                         where A.STATUSREL IS NOT NULL\n" + 
					"                           AND A.STATUSREL = 'A')\n" + 
					"                   AND C.MODULO = J.MODULO\n" + 
					"                   AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
					"                   AND RA_REQUISICAO.SEQREQUISICAO =\n" + 
					"                       RA_APONTAMENTO.SEQREQUISICAO\n" + 
					"                   and ra_requisicao.seqaplicacao = c.seqaplicacao\n" +
					"					And c.Sistema = j.Sistema" + 
					"                   AND RA_APONTAMENTO.DTATERMINO IS NULL\n" + 
					"                   AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR\n" + 
					"                       (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND\n" + 
					"                       RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))\n" + 
					"                   AND RA_REQUISICAO.CODETAPA IN\n" + 
					"                       ('AGE.PRODUÇAO',\n" + 
					"                        'AGE.SUPORTE',\n" + 
					"                        'AND.PRODUÇAO',\n" + 
					"                        'AND.SUPORTE',\n" + 
					"                        'REF.PRODUÇAO',\n" + 
					"                        'TESTES',\n" + 
					"                        'SUSP.PROD',\n" + 
					"                        'SUSP.SUP',\n" + 
					"                        'SUSPENSO',\n" + 
					"                        'LIB.TESTES',\n" + 
					"                        'PEND.CLIENTE')\n" + 
					"                 GROUP BY RA_ANALISTA.CODINOME,\n" + 
					"                          NVL(DECODE(RA_REQUISICAO.TIPOREQUISICAO,\n" + 
					"                                     'R',\n" + 
					"                                     RA_REQUISICAO.SEQREQUISITOCLIENTE,\n" + 
					"                                     RA_REQUISICAO.SEQREQUISICAO),\n" + 
					"                              RA_REQUISICAO.SEQREQUISICAO),\n" + 
					"                          RA_REQUISICAO.JUSTIFICATIVA,\n" + 
					"                          RA_REQUISICAO.DTAPROMETIDA,\n" + 
					"                          ra_requisicao.sistema,\n" + 
					"                          RA_REQUISICAO.DTAPREVLIBERACAO,\n" + 
					"                          RA_REQUISICAO.CODETAPA,\n" + 
					"                          RA_REQUISICAO.Categoria) Y\n" + 
					"         WHERE Y.STATUS = 'A'\n" + 
					"         GROUP BY Y.Categoria\n" + 
					"\n" + 
					"        ) X\n" + 
					"\n" + 
					" GROUP BY X.Categoria\n" + 
					" ORDER BY 1");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {	
				RCPorCategoria resumoRCPrioridade = new RCPorCategoria();
				resumoRCPrioridade.setCategoria(rs.getString("Categoria"));
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
			System.out.println("RCPorCategoria --->" + e.getErrorCode() + " - " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

}
