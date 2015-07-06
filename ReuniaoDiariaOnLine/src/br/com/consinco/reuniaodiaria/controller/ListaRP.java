package br.com.consinco.reuniaodiaria.controller;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.consinco.reuniaodiaria.dao.TabelaRPdeRCDAO;

@Controller
public class ListaRP {
	

//	@RequestMapping(value = "rp={rc}", method = RequestMethod.GET)  
//	public String lista(String rc, Model model, HttpServletRequest req) { 	
//		
//		Connection con = (Connection)req.getAttribute("conexao");
//		TabelaRPdeRCDAO daotabelarp = new TabelaRPdeRCDAO(con);
//		
//		model.addAttribute("listaRP", 		daotabelarp.getListaRP(rc));
//		model.addAttribute("rc", 			daotabelarp.getListaRP(rc).get(0).getRc());
//		model.addAttribute("anotacaorc", 	daotabelarp.getListaRP(rc).get(0).getAnotacaorc());
//
//		return "rps";
//	}
	
	
	
	@RequestMapping(value="rp", method=RequestMethod.GET)  
	public String lista(String pRC, Model model, HttpServletRequest req) {
		
		Connection con = (Connection)req.getAttribute("conexao");
		TabelaRPdeRCDAO daotabelarp = new TabelaRPdeRCDAO(con);
		
		model.addAttribute("listaRP", 		daotabelarp.getListaRP(pRC));
		model.addAttribute("rc", 			daotabelarp.getListaRP(pRC).get(0).getRc());
		model.addAttribute("anotacaorc", 	daotabelarp.getListaRP(pRC).get(0).getAnotacaorc());
		//model.addAttribute("anotacaorc", 	"2");

		return "rps";
	}
	


		
}
