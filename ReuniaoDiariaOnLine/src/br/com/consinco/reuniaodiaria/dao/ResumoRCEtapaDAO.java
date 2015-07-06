package br.com.consinco.reuniaodiaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.consinco.reuniaodiaria.modelos.ResumoRCEtapa;

public class ResumoRCEtapaDAO {
	
	private final Connection connection;

	public ResumoRCEtapaDAO(Connection connection) {
		this.connection = connection;
	}

	
	public List<ResumoRCEtapa> getLista() {
		try {
			List<ResumoRCEtapa> lstResumoRCEtapa = new ArrayList<ResumoRCEtapa>();
			PreparedStatement stmt = this.connection.prepareStatement(
							"SELECT SUM(X.QTD) as Qtd, X.CODETAPA\n" +
							"  FROM (Select COUNT(1) as Qtd,\n" + 
							"               'GERMUD' as Situacao,\n" + 
							"               RA_REQUISICAO.CODETAPA\n" + 
							"          From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA\n" + 
							"         Where RA_APONTAMENTO.SEQANALISTA in\n" + 
							"               (select a.seqanalista\n" + 
							"                  from ra_analista a\n" + 
							"                 where A.STATUSREL IS NOT NULL\n" + 
							"                   AND A.STATUSREL = 'A')\n" + 
							"           AND RA_ANALISTA.CODINOME = 'GERMUD'\n" + 
							"           AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
							"           AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO\n" + 
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
							"         GROUP BY RA_REQUISICAO.CODETAPA\n" + 
							"\n" + 
							"        UNION ALL\n" + 
							"\n" + 
							"        Select COUNT(*) as Qtd,\n" + 
							"               'HOMOLOGADOS' as Situacao,\n" + 
							"               RA_REQUISICAO.CODETAPA\n" + 
							"          From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA\n" + 
							"         Where RA_ANALISTA.CODINOME = 'HOMOLOGADO'\n" + 
							"           AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
							"           AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO\n" + 
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
							"         GROUP BY RA_REQUISICAO.CODETAPA\n" + 
							"\n" + 
							"        UNION ALL\n" + 
							"\n" + 
							"        SELECT COUNT(*), 'EM DIA', EMDIA.CODETAPA\n" + 
							"          FROM (Select CASE\n" + 
							"                         WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN\n" + 
							"                          'H'\n" + 
							"                         WHEN RA_REQUISICAO.DTAPROMETIDA < TRUNC(SYSDATE) AND\n" + 
							"                              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) != 'ASS' THEN\n" + 
							"                          'A'\n" + 
							"                         WHEN RA_REQUISICAO.DTAPREVLIBERACAO < TRUNC(SYSDATE) AND\n" + 
							"                              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) = 'ASS' THEN\n" + 
							"                          'A'\n" + 
							"                         ELSE\n" + 
							"                          'D'\n" + 
							"                       END STATUS,\n" + 
							"                       RA_REQUISICAO.CODETAPA\n" + 
							"                  From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA\n" + 
							"                 Where RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
							"                   AND RA_REQUISICAO.SEQREQUISICAO =\n" + 
							"                       RA_APONTAMENTO.SEQREQUISICAO\n" + 
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
							"                        'PEND.CLIENTE')) EMDIA\n" + 
							"         WHERE emdia.STATUS = 'D'\n" + 
							"         GROUP BY EMDIA.CODETAPA\n" + 
							"\n" + 
							"        UNION ALL\n" + 
							"\n" + 
							"        SELECT COUNT(*), 'ATRASADOS', Atrasado.CODETAPA\n" + 
							"          FROM (Select CASE\n" + 
							"                         WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN\n" + 
							"                          'H'\n" + 
							"                         WHEN RA_REQUISICAO.DTAPROMETIDA < TRUNC(SYSDATE) AND\n" + 
							"                              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) != 'ASS' THEN\n" + 
							"                          'A'\n" + 
							"                         WHEN RA_REQUISICAO.DTAPREVLIBERACAO < TRUNC(SYSDATE) AND\n" + 
							"                              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) = 'ASS' THEN\n" + 
							"                          'A'\n" + 
							"                         ELSE\n" + 
							"                          'D'\n" + 
							"                       END STATUS,\n" + 
							"                       RA_REQUISICAO.CODETAPA\n" + 
							"                  From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA\n" + 
							"                 Where RA_ANALISTA.CODINOME != 'GERMUD'\n" + 
							"                   AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
							"                   AND RA_REQUISICAO.SEQREQUISICAO =\n" + 
							"                       RA_APONTAMENTO.SEQREQUISICAO\n" + 
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
							"                        'PEND.CLIENTE')) Atrasado\n" + 
							"         WHERE Atrasado.STATUS = 'A'\n" + 
							"         GROUP BY Atrasado.CODETAPA\n" + 
							"\n" + 
							"        UNION ALL\n" + 
							"\n" + 
							"        SELECT COUNT(*), 'BACKLOG', C.CODETAPA\n" + 
							"          FROM RA_REQUISICAO C, ge_aplicacao g\n" + 
							"         WHERE C.CODETAPA != 'CANCELADO'\n" + 
							"           AND C.CODETAPA = 'SOL.PRODUÇAO'\n" + 
							"           AND C.DTAREQUISICAO > '01/JAN/2011'\n" + 
							"           and c.seqrequisicao > 900000\n" + 
							"           and c.seqaplicacao = g.seqaplicacao\n" + 
							"           AND C.SEQREQUISITOCLIENTE IS NULL\n" +
							"			AND C.STATUSCOMERCIAL = 'L' " + 
							"         GROUP BY C.CODETAPA) X\n" + 
							" GROUP BY X.CODETAPA\n" + 
							" ORDER BY X.CODETAPA");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				ResumoRCEtapa resumoRCEtapa = new ResumoRCEtapa();
				//popula o objeto ResumoRC
				resumoRCEtapa.setQuantidade( rs.getInt("Qtd"));
				resumoRCEtapa.setEtapa(rs.getString("CodEtapa"));

				lstResumoRCEtapa.add(resumoRCEtapa);
			}

			rs.close();
			stmt.close();

			return lstResumoRCEtapa;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<ResumoRCEtapa> getListaGrafico() {
		try {
			List<ResumoRCEtapa> lstResumoRCEtapa = new ArrayList<ResumoRCEtapa>();
			PreparedStatement stmt = this.connection.prepareStatement(
							"SELECT SUM(X.QTD) as Qtd,\n" +
							"       CASE WHEN X.CODETAPA = 'SOL.PRODUÇAO' THEN\n" + 
							"            'Backlog'\n" + 
							"       WHEN X.CODETAPA = 'PEND.CLIENTE' THEN\n" + 
							"            'Homologação Assaí'\n" + 
							"       WHEN X.CODETAPA IN ('LIB.TESTES', 'TESTES', 'SUSP.SUP') THEN\n" + 
							"            'Testes Consinco'\n" + 
							"       ELSE\n" + 
							"            'Desenvolvimento'\n" + 
							"       END as CodEtapa\n" + 
							"  FROM (Select COUNT(1) as Qtd,\n" + 
							"               'GERMUD' as Situacao,\n" + 
							"               RA_REQUISICAO.CODETAPA\n" + 
							"          From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA\n" + 
							"         Where RA_APONTAMENTO.SEQANALISTA in\n" + 
							"               (select a.seqanalista\n" + 
							"                  from ra_analista a\n" + 
							"                 where A.STATUSREL IS NOT NULL\n" + 
							"                   AND A.STATUSREL = 'A')\n" + 
							"           AND RA_ANALISTA.CODINOME = 'GERMUD'\n" + 
							"           AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
							"           AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO\n" + 
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
							"         GROUP BY RA_REQUISICAO.CODETAPA\n" + 
							"\n" + 
							"        UNION ALL\n" + 
							"\n" + 
							"        Select COUNT(*) as Qtd,\n" + 
							"               'HOMOLOGADOS' as Situacao,\n" + 
							"               RA_REQUISICAO.CODETAPA\n" + 
							"          From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA\n" + 
							"         Where RA_APONTAMENTO.SEQANALISTA in\n" + 
							"               (select a.seqanalista\n" + 
							"                  from ra_analista a\n" + 
							"                 where A.STATUSREL IS NOT NULL\n" + 
							"                   AND A.STATUSREL = 'A')\n" + 
							"           AND RA_ANALISTA.CODINOME = 'HOMOLOGADO'\n" + 
							"           AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
							"           AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO\n" + 
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
							"         GROUP BY RA_REQUISICAO.CODETAPA\n" + 
							"\n" + 
							"        UNION ALL\n" + 
							"\n" + 
							"        SELECT COUNT(*), 'EM DIA', EMDIA.CODETAPA\n" + 
							"          FROM (Select CASE\n" + 
							"                         WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN\n" + 
							"                          'H'\n" + 
							"                         WHEN RA_REQUISICAO.DTAPROMETIDA < TRUNC(SYSDATE) AND\n" + 
							"                              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) != 'ASS' THEN\n" + 
							"                          'A'\n" + 
							"                         WHEN RA_REQUISICAO.DTAPREVLIBERACAO < TRUNC(SYSDATE) AND\n" + 
							"                              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) = 'ASS' THEN\n" + 
							"                          'A'\n" + 
							"                         ELSE\n" + 
							"                          'D'\n" + 
							"                       END STATUS,\n" + 
							"                       RA_REQUISICAO.CODETAPA\n" + 
							"                  From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA\n" + 
							"                 Where RA_APONTAMENTO.SEQANALISTA in\n" + 
							"                       (select a.seqanalista\n" + 
							"                          from ra_analista a\n" + 
							"                         where A.STATUSREL IS NOT NULL\n" + 
							"                           AND A.STATUSREL = 'A')\n" + 
							"                   AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
							"                   AND RA_REQUISICAO.SEQREQUISICAO =\n" + 
							"                       RA_APONTAMENTO.SEQREQUISICAO\n" + 
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
							"                        'PEND.CLIENTE')) EMDIA\n" + 
							"         WHERE emdia.STATUS = 'D'\n" + 
							"         GROUP BY EMDIA.CODETAPA\n" + 
							"\n" + 
							"        UNION ALL\n" + 
							"\n" + 
							"        SELECT COUNT(*), 'ATRASADOS', Atrasado.CODETAPA\n" + 
							"          FROM (Select CASE\n" + 
							"                         WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN\n" + 
							"                          'H'\n" + 
							"                         WHEN RA_REQUISICAO.DTAPROMETIDA < TRUNC(SYSDATE) AND\n" + 
							"                              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) != 'ASS' THEN\n" + 
							"                          'A'\n" + 
							"                         WHEN RA_REQUISICAO.DTAPREVLIBERACAO < TRUNC(SYSDATE) AND\n" + 
							"                              SUBSTR(RA_ANALISTA.CODINOME, 1, 3) = 'ASS' THEN\n" + 
							"                          'A'\n" + 
							"                         ELSE\n" + 
							"                          'D'\n" + 
							"                       END STATUS,\n" + 
							"                       RA_REQUISICAO.CODETAPA\n" + 
							"                  From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA\n" + 
							"                 Where RA_APONTAMENTO.SEQANALISTA in\n" + 
							"                       (select a.seqanalista\n" + 
							"                          from ra_analista a\n" + 
							"                         where A.STATUSREL IS NOT NULL\n" + 
							"                           AND A.STATUSREL = 'A')\n" + 
							"                   AND RA_ANALISTA.CODINOME != 'GERMUD'\n" + 
							"                   AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA\n" + 
							"                   AND RA_REQUISICAO.SEQREQUISICAO =\n" + 
							"                       RA_APONTAMENTO.SEQREQUISICAO\n" + 
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
							"                        'PEND.CLIENTE')) Atrasado\n" + 
							"         WHERE Atrasado.STATUS = 'A'\n" + 
							"         GROUP BY Atrasado.CODETAPA\n" + 
							"\n" + 
							"        UNION ALL\n" + 
							"\n" + 
							"        SELECT COUNT(*), 'BACKLOG', C.CODETAPA\n" + 
							"          FROM RA_REQUISICAO C, ge_aplicacao g\n" + 
							"         WHERE C.CODETAPA != 'CANCELADO'\n" + 
							"           AND C.CODETAPA = 'SOL.PRODUÇAO'\n" + 
							"           AND C.DTAREQUISICAO > '01/JAN/2011'\n" + 
							"           and c.seqrequisicao > 900000\n" + 
							"           and c.seqaplicacao = g.seqaplicacao\n" + 
							"           AND C.SEQREQUISITOCLIENTE IS NULL\n" + 
							"         GROUP BY C.CODETAPA) X\n" + 
							" GROUP BY\n" + 
							"       CASE WHEN X.CODETAPA = 'SOL.PRODUÇAO' THEN\n" + 
							"            'Backlog'\n" + 
							"       WHEN X.CODETAPA = 'PEND.CLIENTE' THEN\n" + 
							"            'Homologação Assaí'\n" + 
							"       WHEN X.CODETAPA IN ('LIB.TESTES', 'TESTES', 'SUSP.SUP') THEN\n" + 
							"            'Testes Consinco'\n" + 
							"       ELSE\n" + 
							"           'Desenvolvimento'\n" + 
							"       END\n" + 
							" ORDER BY 1 ");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				ResumoRCEtapa resumoRCEtapa = new ResumoRCEtapa();
				//popula o objeto ResumoRC
				resumoRCEtapa.setQuantidade( rs.getInt("Qtd"));
				resumoRCEtapa.setEtapa(rs.getString("CodEtapa"));

				lstResumoRCEtapa.add(resumoRCEtapa);
			}

			rs.close();
			stmt.close();

			return lstResumoRCEtapa;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}	
	
}
