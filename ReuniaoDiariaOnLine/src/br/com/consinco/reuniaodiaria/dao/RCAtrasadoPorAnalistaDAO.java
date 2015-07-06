package br.com.consinco.reuniaodiaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.consinco.reuniaodiaria.modelos.RCAtrasadoPorAnalista;

public class RCAtrasadoPorAnalistaDAO {
	
	private final Connection connection;

	public RCAtrasadoPorAnalistaDAO(Connection connection) {
		this.connection = connection;
	}

	public List<RCAtrasadoPorAnalista> getLista() {
		try {
			List<RCAtrasadoPorAnalista> lstResumoRC = new ArrayList<RCAtrasadoPorAnalista>();
			PreparedStatement stmt = this.connection.prepareStatement(
							 "SELECT COUNT(*) as Qtd, "
							+"       X.RESPONSAVEL as Responsavel"
							+"  FROM (Select RA_ANALISTA.CODINOME || CHR(13) || CHR(10) ||"
							+"               DECODE(SUBSTR(RA_ANALISTA.CODINOME, 1, 3),"
							+"                      'ASS',"
							+"                      'TI-ASSAÍ',"
							+"                      'CONSINCO') RESPONSAVEL,"
							+"               CASE"
							+"                 WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN"
							+"                  'H'"
							+"                 WHEN RA_REQUISICAO.DTAPROMETIDA < TRUNC(SYSDATE) AND"
							+"                      SUBSTR(RA_ANALISTA.CODINOME, 1, 3) != 'ASS' THEN"
							+"                  'A'"
							+"                 WHEN RA_REQUISICAO.DTAPREVLIBERACAO < TRUNC(SYSDATE) AND"
							+"                      SUBSTR(RA_ANALISTA.CODINOME, 1, 3) = 'ASS' THEN"
							+"                  'A'"
							+"                 ELSE"
							+"                  'D'"
							+"               END STATUS"
							+"          From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA"
							+"         Where RA_APONTAMENTO.SEQANALISTA in"
							+"               (select a.seqanalista"
							+"                  from ra_analista a"
							+"                 where A.STATUSREL IS NOT NULL"
							+"                   AND A.STATUSREL = 'A')"
							+"           AND RA_ANALISTA.CODINOME != 'GERMUD'"
							+"           AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA"
							+"           AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO"
							+"           AND RA_APONTAMENTO.DTATERMINO IS NULL"
							+"           AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR"
							+"               (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND"
							+"               RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))"
							+"           AND RA_REQUISICAO.CODETAPA IN"
							+"               ('AGE.PRODUÇAO', 'AGE.SUPORTE', 'AND.PRODUÇAO', 'AND.SUPORTE',"
							+"                'REF.PRODUÇAO', 'TESTES', 'SUSP.PROD', 'SUSP.SUP', 'SUSPENSO',"
							+"                'LIB.TESTES', 'PEND.CLIENTE')"
							+"         GROUP BY RA_ANALISTA.CODINOME,"
							+"                  RA_REQUISICAO.JUSTIFICATIVA,"
							+"                  RA_REQUISICAO.DTAPROMETIDA,"
							+"                  RA_REQUISICAO.DTAPREVLIBERACAO,"
							+"                  RA_REQUISICAO.CODETAPA) X"
							+" WHERE X.STATUS = 'A'"
							+" GROUP BY X.RESPONSAVEL"
							+" ORDER BY 1 ");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				RCAtrasadoPorAnalista resumoRCAnalista = new RCAtrasadoPorAnalista();
								
				resumoRCAnalista.setQuantidade(rs.getInt("Qtd"));
				resumoRCAnalista.setResponsavel(rs.getString("Responsavel"));

				lstResumoRC.add(resumoRCAnalista);
			}

			rs.close();
			stmt.close();

			return lstResumoRC;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

//	public int getTotalRC() {
//		try {
//			PreparedStatement stmt = this.connection.prepareStatement(
//					"SELECT COUNT(*) as TotalReq"
//							+"  FROM (Select RA_ANALISTA.CODINOME || CHR(13) || CHR(10) ||"
//							+"               DECODE(SUBSTR(RA_ANALISTA.CODINOME, 1, 3),"
//							+"                      'ASS',"
//							+"                      'TI-ASSAÍ',"
//							+"                      'CONSINCO') RESPONSAVEL,"
//							+"               CASE"
//							+"                 WHEN RA_ANALISTA.CODINOME = 'HOMOLOGADO' THEN"
//							+"                  'H'"
//							+"                 WHEN RA_REQUISICAO.DTAPROMETIDA < TRUNC(SYSDATE) AND"
//							+"                      SUBSTR(RA_ANALISTA.CODINOME, 1, 3) != 'ASS' THEN"
//							+"                  'A'"
//							+"                 WHEN RA_REQUISICAO.DTAPREVLIBERACAO < TRUNC(SYSDATE) AND"
//							+"                      SUBSTR(RA_ANALISTA.CODINOME, 1, 3) = 'ASS' THEN"
//							+"                  'A'"
//							+"                 ELSE"
//							+"                  'D'"
//							+"               END STATUS"
//							+"          From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA"
//							+"         Where RA_APONTAMENTO.SEQANALISTA in"
//							+"               (select a.seqanalista"
//							+"                  from ra_analista a"
//							+"                 where A.STATUSREL IS NOT NULL"
//							+"                   AND A.STATUSREL = 'A')"
//							+"           AND RA_ANALISTA.CODINOME != 'GERMUD'"
//							+"           AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA"
//							+"           AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO"
//							+"           AND RA_APONTAMENTO.DTATERMINO IS NULL"
//							+"           AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR"
//							+"               (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND"
//							+"               RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))"
//							+"           AND RA_REQUISICAO.CODETAPA IN"
//							+"               ('AGE.PRODUÇAO', 'AGE.SUPORTE', 'AND.PRODUÇAO', 'AND.SUPORTE',"
//							+"                'REF.PRODUÇAO', 'TESTES', 'SUSP.PROD', 'SUSP.SUP', 'SUSPENSO',"
//							+"                'LIB.TESTES', 'PEND.CLIENTE')"
//							+"         GROUP BY RA_ANALISTA.CODINOME,"
//							+"                  RA_REQUISICAO.JUSTIFICATIVA,"
//							+"                  RA_REQUISICAO.DTAPROMETIDA,"
//							+"                  RA_REQUISICAO.DTAPREVLIBERACAO,"
//							+"                  RA_REQUISICAO.CODETAPA) X"
//							+" WHERE X.STATUS = 'A'" );
//
//			ResultSet rs = stmt.executeQuery();
//			
//			rs.next();
//			int totalReq = rs.getInt("TotalReq");
//			
//			rs.close();
//			stmt.close();
//
//			return totalReq;
//		} catch (SQLException e) {
//			System.out.println(e.getErrorCode() + " - " + e.getMessage());
//			throw new RuntimeException(e);
//		}
//	}
}
