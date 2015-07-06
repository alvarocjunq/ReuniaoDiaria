package br.com.consinco.reuniaodiaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TratarErro {
	
	@RequestMapping("404")
	public String execute() {
		System.out.println( "Página não encontrada - 404 ");
		return "err/404"; 
	}
}
