package br.com.consinco.reuniaodiaria.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.consinco.reuniaodiaria.dao.RCAtrasadoPorAnalistaDAO;
import br.com.consinco.reuniaodiaria.dao.RCPorCategoriaDAO;
import br.com.consinco.reuniaodiaria.dao.RCPorModuloDAO;
import br.com.consinco.reuniaodiaria.dao.RCPorPrioridadeDAO;
import br.com.consinco.reuniaodiaria.dao.ResumoRCDAO;
import br.com.consinco.reuniaodiaria.dao.ResumoRCEtapaDAO;
import br.com.consinco.reuniaodiaria.dao.TabelaRCDAO;
import br.com.consinco.reuniaodiaria.graficos.ResumoRCEtapaGraph;
import br.com.consinco.reuniaodiaria.graficos.ResumoRCGraph;

@Controller
public class ListasHome {
	
	@RequestMapping("/")
	public String barra(Model model, HttpServletRequest req) { 
				
		return "redirect:home";
	}
	
	@RequestMapping("home")
	public String lista(Model model, HttpServletRequest req) { 
		
		Connection con = (Connection)req.getAttribute("conexao");
		
		ResumoRCDAO 				dao 		= new ResumoRCDAO(con);
		ResumoRCEtapaDAO 			daoRRE 		= new ResumoRCEtapaDAO(con);
		RCAtrasadoPorAnalistaDAO 	daoAPA 		= new RCAtrasadoPorAnalistaDAO(con);
		RCPorModuloDAO 				daoAPM 		= new RCPorModuloDAO(con);
		RCPorPrioridadeDAO 			daoRPP 		= new RCPorPrioridadeDAO(con);
		RCPorCategoriaDAO 			daoRPC 		= new RCPorCategoriaDAO(con);
		TabelaRCDAO 				daotabelarc = new TabelaRCDAO(con);
		
		
		model.addAttribute("lista", 				dao.getLista());

		model.addAttribute("listaResRCEtapa", 		daoRRE.getLista());
		
		model.addAttribute("listaAtrasadoAnalista", daoAPA.getLista());
		
		model.addAttribute("listaRCModulo", 		daoAPM.getLista());
		model.addAttribute("listaRCPrioridade", 	daoRPP.getLista());
		model.addAttribute("listaRCCategoria", 		daoRPC.getLista());
		
		model.addAttribute("listaBacklog", 			daotabelarc.getListaBacklog());
		model.addAttribute("listaAnalise", 			daotabelarc.getListaAnalise());
		model.addAttribute("listaProxVersao", 		daotabelarc.getListaProxVersao());
		
		model.addAttribute("listaAtrasado", 		daotabelarc.getListaTabela("A"));	
		model.addAttribute("listaHomologado", 		daotabelarc.getListaTabela("H"));
		model.addAttribute("listaEmDia", 			daotabelarc.getListaTabela("D"));
		model.addAttribute("listaGermud", 			daotabelarc.getListaTabela("G"));
		
		return "index";
	}
	

	
	@RequestMapping("gResumoRC")
	public void gerarGrafico (HttpServletResponse res, HttpServletRequest req) 
	{
		Connection con = (Connection)req.getAttribute("conexao");
		ResumoRCDAO daoResumo = new ResumoRCDAO(con);
		
		JFreeChart gResumo = new ResumoRCGraph().getGrafico("Resumo dos RC's", daoResumo.getLista());
	
		res.setContentType("image/png");
		
		try
		{
			ChartUtilities.writeChartAsPNG(res.getOutputStream(), gResumo, 750, 400);
			res.getOutputStream().close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("gResumoRCEtapa")
	public void gerarGraficoResumoRC (HttpServletResponse res, HttpServletRequest req) 
	{
		Connection con = (Connection)req.getAttribute("conexao");
		ResumoRCEtapaDAO daoResumo = new ResumoRCEtapaDAO(con);
		
		JFreeChart gResumo = new ResumoRCEtapaGraph().getGrafico("Resumo dos RC's por Etapa", daoResumo.getListaGrafico());
	
		res.setContentType("image/png");
		
		try
		{
			ChartUtilities.writeChartAsPNG(res.getOutputStream(), gResumo, 750, 400);
			res.getOutputStream().close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
		
}
