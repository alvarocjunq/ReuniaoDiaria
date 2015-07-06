package br.com.consinco.reuniaodiaria.filtros;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import br.com.consinco.reuniaodiaria.ConnectionFactory;

public class FiltroConexao implements Filter {

	//@Override
	public void destroy() {}

	//@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		//Nota, os filtros sao executados na ordem que estao no XML
		try {
			Connection con = new ConnectionFactory().getConnection();
			req.setAttribute("conexao", con);
			
			chain.doFilter(req, res);
			
			con.close();
			
		}catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	//@Override
	public void init(FilterConfig arg0) throws ServletException {}

}
