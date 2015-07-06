package br.com.consinco.reuniaodiaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.consinco.reuniaodiaria.modelos.ResumoRC;

public class ResumoRCDAO {
	
	private final Connection connection;
	

	public ResumoRCDAO(Connection connection) {
		this.connection = connection;
	}

	
	public List<ResumoRC> getLista() {
		try {
			List<ResumoRC> lstResumoRC = new ArrayList<ResumoRC>();
			PreparedStatement stmt = this.connection.prepareStatement(
							 " Select COUNT(1)as Qtd, 'GERMUD' as Situacao"
							+"   From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA, ge_aplicacao c"
							+"  Where RA_ANALISTA.CODINOME = 'GERMUD'"
							+"    AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA"
							+"    AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO"
							+"    and ra_requisicao.seqaplicacao = c.seqaplicacao"
							+"    AND RA_APONTAMENTO.DTATERMINO IS NULL"
							+"    AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR"
							+"        (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND"
							+"        RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))"
							+"    AND RA_REQUISICAO.CODETAPA IN"
							+"        ('AGE.PRODUÇAO', 'AGE.SUPORTE', 'AND.PRODUÇAO', 'AND.SUPORTE',"
							+"         'REF.PRODUÇAO', 'TESTES', 'SUSP.PROD', 'SUSP.SUP', 'SUSPENSO',"
							+"         'LIB.TESTES', 'PEND.CLIENTE')"
							+""
							+" UNION ALL"
							+""
							+" Select COUNT(*) as Qtd, 'HOMOLOGADOS' as Situacao"
							+"  From RA_APONTAMENTO, RA_REQUISICAO, RA_ANALISTA"
							+" Where RA_APONTAMENTO.SEQANALISTA in"
							+"       (select a.seqanalista"
							+"          from ra_analista a"
							+"         where A.STATUSREL IS NOT NULL"
							+"           AND A.STATUSREL = 'A')"
							+"   AND RA_ANALISTA.CODINOME = 'HOMOLOGADO'"
							+"   AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA"
							+"   AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO"
							+"   AND RA_APONTAMENTO.DTATERMINO IS NULL"
							+"   AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR"
							+"       (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND"
							+"       RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))"
							+"   AND RA_REQUISICAO.CODETAPA IN"
							+"       ('AGE.PRODUÇAO', 'AGE.SUPORTE', 'AND.PRODUÇAO', 'AND.SUPORTE',"
							+"        'REF.PRODUÇAO', 'TESTES', 'SUSP.PROD', 'SUSP.SUP', 'SUSPENSO',"
							+"        'LIB.TESTES', 'PEND.CLIENTE')"
							+"        "
							+" UNION ALL"
							+""
							+" SELECT COUNT(*), 'EM DIA'"
							+"  FROM (Select CASE"
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
							+"         Where RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA"
							+"           AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO"
							+"           AND RA_APONTAMENTO.DTATERMINO IS NULL"
							+"           AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR"
							+"               (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND"
							+"               RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))"
							+"           AND RA_REQUISICAO.CODETAPA IN"
							+"               ('AGE.PRODUÇAO', 'AGE.SUPORTE', 'AND.PRODUÇAO', 'AND.SUPORTE',"
							+"                'REF.PRODUÇAO', 'TESTES', 'SUSP.PROD', 'SUSP.SUP', 'SUSPENSO',"
							+"                'LIB.TESTES', 'PEND.CLIENTE')) Atrasado"
							+" WHERE Atrasado.STATUS = 'D'"
							+" "
							+" UNION ALL"
							+""
							+" SELECT COUNT(*), 'ATRASADOS'"
							+"  FROM (Select CASE"
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
							+"         Where RA_ANALISTA.CODINOME != 'GERMUD'"
							+"           AND RA_APONTAMENTO.SEQANALISTA = RA_ANALISTA.SEQANALISTA"
							+"           AND RA_REQUISICAO.SEQREQUISICAO = RA_APONTAMENTO.SEQREQUISICAO"
							+"           AND RA_APONTAMENTO.DTATERMINO IS NULL"
							+"           AND (RA_REQUISICAO.TIPOREQUISICAO = 'Q' OR"
							+"               (RA_REQUISICAO.TIPOREQUISICAO = 'R' AND"
							+"               RA_REQUISICAO.SEQREQUISITOCLIENTE IS NULL))"
							+"           AND RA_REQUISICAO.CODETAPA IN"
							+"               ('AGE.PRODUÇAO', 'AGE.SUPORTE', 'AND.PRODUÇAO', 'AND.SUPORTE',"
							+"                'REF.PRODUÇAO', 'TESTES', 'SUSP.PROD', 'SUSP.SUP', 'SUSPENSO',"
							+"                'LIB.TESTES', 'PEND.CLIENTE')) Atrasado"
							+" WHERE Atrasado.STATUS = 'A'"
							+" "
							+" UNION ALL"
							+""
							+" SELECT COUNT(*), 'BACKLOG'"
							+"  FROM RA_REQUISICAO C, ge_aplicacao g"
							+" WHERE C.CODETAPA != 'CANCELADO'"
							+"   AND C.CODETAPA = 'SOL.PRODUÇAO'"
							+"   AND C.DTAREQUISICAO > '01/JAN/2011'"
							+"   and c.seqrequisicao > 900000"
							+"   and c.seqaplicacao = g.seqaplicacao"
							+"   AND C.SEQREQUISITOCLIENTE IS NULL"
							+"   AND C.STATUSCOMERCIAL = 'L' ");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				ResumoRC resumoRC = new ResumoRC();
				//popula o objeto ResumoRC
				resumoRC.setQuantidade(rs.getInt("Qtd"));
				resumoRC.setSituacao(rs.getString("Situacao"));

				lstResumoRC.add(resumoRC);
			}

			rs.close();
			stmt.close();

			return lstResumoRC;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	


}
