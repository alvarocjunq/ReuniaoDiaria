<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lista - Resumo dos RCs</title>

	<link rel="stylesheet" href="css/jquery-ui-1.8.4.custom.css" />
	<link rel="stylesheet" href="css/demo_page.css" />
	<link rel="stylesheet" href="css/demo_table_jui.css" />
	
	<link rel="stylesheet" href="css/TableTools_JUI.css" />
	<link rel="stylesheet" href="css/TableTools.css" />

	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/jquery-ui-tabs.js"></script>
	<script type="text/javascript" src="js/jquery.dataTables.js"></script>
	
	<script type="text/javascript" src="js/TableTools.js"></script>
	<script type="text/javascript" src="js/ZeroClipboard.js"></script>

		<script type="text/javascript" charset="utf-8">
			$(document).ready(function() {
				$("#tabs").tabs( {
					"show": function(event, ui) {
						var table = $.fn.dataTable.fnTables(true);
						
						if ( table.length > 0 ) { 
							$(table).dataTable().fnAdjustColumnSizing();
						
 						}
						
							ttInstances = TableTools.fnGetMasters();
							for (i in ttInstances) {
								if (ttInstances[i].fnResizeRequired())
								{
									ttInstances[i].fnResizeButtons();
								}
							}
						
					}
				} );

					
				$('table.display').dataTable({
					"bStateSave" : false,
					"bAutoWidth" : false,
					"bPaginate" : false,
					"bJQueryUI" : true,
					"aaSorting" : [ [ 7, "desc" ] ],
					"aoColumnDefs" : [ {
						"sWidth" : "10%",
						"aTargets" : [ -1 ]
					} ],
					//botões gerar csv/xls/pdf
					//"sDom" : 'T<"clear"><"H"lfr>t<"F"ip>', // deixa os botões acima da tabela
					"sDom" : '<"H"Tfr>t<"F"ip>', // deixa os botões dentro da tabela
					"oTableTools" : 
					{
						"sSwfPath" : "js/swf/copy_csv_xls_pdf.swf"
					}
				});
			});

			$(document).ready(function() {
				$("a[rel=modal]").click(function(ev) {
					ev.preventDefault();

					var id = $(this).attr("href");

					var alturaTela = $(document).height();
					var larguraTela = $(window).width();

					//colocando o fundo preto
					$('#mascara').css({
						'width' : larguraTela,
						'height' : alturaTela
					});
					$('#mascara').fadeIn(1000);
					$('#mascara').fadeTo("slow", 0.8);

					var left = ($(window).width() / 2) - ($(id).width() / 2);
					var top = ($(window).height() / 2) - ($(id).height() / 2);

					$(id).css({
						'top' : top,
						'left' : left
					});
					$(id).show();
				});

				$("#mascara").click(function() {
					$(this).hide();
					$(".window").hide();
					$(".windowGrafico").hide();
				});

				$('.fechar').click(function(ev) {
					ev.preventDefault();
					$("#mascara").hide();
					$(".window").hide();
					$(".windowGrafico").hide();
				});
			});

			// 			jQuery.fn.toggleText = function(a, b) {
			// 				return this.html(this.html().replace(
			// 						new RegExp("(" + a + "|" + b + ")"), function(x) {
			// 							return (x == a) ? b : a;
			// 						}));
			// 			}

			// 			$(document).ready(
			// 					function() {
			// 						$('.tgl').before('<span>Revelar conteúdo</span>');
			// 						$('.tgl').css('display', 'none')
			// 						$('span', '#box-toggle').click(
			// 								function() {
			// 									$(this).next().slideToggle('slow')
			// 											.siblings('.tgl:visible')
			// 											.slideToggle('fast');
			// 									// aqui começa o funcionamento do plugin
			// 									$(this).toggleText('Revelar', 'Esconder')
			// 											.siblings('span').next(
			// 													'.tgl:visible').prev()
			// 											.toggleText('Revelar', 'Esconder')
			// 								});
			// 					});

		</script>

</head>
<body>

	<c:import url="template/cabecalho.jsp" />
<br>
	<table style="border-width: 0 0 1px 1px;">
		<tr>
			<td width="20%" valign="top" align="left">
			<!-- -------------------------Tabela de Resumo dos RCs--------------------------------- -->
			<c:set var="totalresumorc" value="0" />
				<table border="1" cellpadding="10px" class="tabela">
					
					<tr class="tituloTabela">
						<th><a href="#glResumoRC" rel="modal"><img src="<c:url value="imagens/graph.png" />" /></a></th>
						<th>Resumo dos RCs</th>
					</tr>
					
					<c:forEach items="${lista}" var="i">
						<c:set var="totalresumorc" value="${totalresumorc + i.quantidade}" /> 
							<tr style="background-color: #F5F5F5"  class="mousehover">
								<td align="center">${i.quantidade}</td>
								<td>${i.situacao}</td>
					</c:forEach>
					
					<tr class="rodapeTabela">
						<th>${totalresumorc}</th>
						<td>&nbsp;</td>
					</tr>
					
				</table>
				
				<div class="windowGrafico" id="glResumoRC">
					<a href="#" class="fechar">Fechar</a>
					<img src="<c:url value="gResumoRC" />" />
				</div>
				
			</td>
			
			<td width="20%" valign="top" >
			<!-- -------------------------Tabela Atrasados por Analista--------------------------------- -->
				<c:set var="totalrcanalista" value="0" />
				<table border="1" cellpadding="5px"	style="width: 200px" class="tabela">

					<tr class="tituloTabela">
						<th colspan="2">RC(S) Atrasados por Analista</th>
					</tr>
					
					<tr class="subtituloTabela">
						<th>Qtd</th>
						<th>Analista</th>
					</tr>
					
					<c:forEach items="${listaAtrasadoAnalista}" var="i">
					
						<c:set var="totalrcanalista" value="${totalrcanalista + i.quantidade}" /> 
					
						<tr style="background-color: #F5F5F5"  class="mousehover">
							<td class="centro">${i.quantidade}</td>
							<td class="esquerda">${i.responsavel}</td>
						</tr>
					</c:forEach>
					
					<tr class="rodapeTabela">
						<th>${totalrcanalista}</th>
						<th>&nbsp;</th>
					</tr>
				</table>
			</td>

			<td width="20%" valign="top" >
			<!-- -------------------------Tabela Resumo por Etapa --------------------------------- -->
				<c:set var="totalresumorceta" 	value="0" />
				<c:set var="totSubDesenv" 		value="0" />
				<c:set var="totSubTestes" 		value="0" />
				<c:set var="totSubHomol" 		value="0" />
				<c:set var="totSubBacklog" 		value="0" />
				<table border="1" cellpadding="5px"	style="width: 200px" class="tabela">

					<tr class="tituloTabela">
						<th><a href="#glResumoRCEtapa" rel="modal"><img src="<c:url value="imagens/graph.png" />" /></a></th>
						<th>RC(S) por Etapa</th>
					</tr>
					
					<!-- SubGrupo Backlog -->
					<tr style="background-color: #F5F5F5"  class="mousehover">
							<th colspan="2" class="tituloTabela" style="color: #000">Backlog</th>
					</tr>
					<c:forEach items="${listaResRCEtapa}" var="i">
						<c:set var="totalresumorceta" value="${totalresumorceta + i.quantidade}" />
						
						<c:choose>
							<c:when test="${i.etapa eq 'SOL.PRODUÇAO'}">
								<c:set var="totSubBacklog" value="${totSubBacklog + i.quantidade}" />  		 
								<tr style="background-color: #F5F5F5"  class="mousehover">
									<td class="centro">${i.quantidade}</td>
									<td class="esquerda">${i.etapa}</td>
								</tr>
							</c:when>
						</c:choose>
					</c:forEach>
					<tr style="background-color: #F5F5F5"  class="mousehover">
							<th colspan="2" class="subtituloTabela esquerda" style="color: #000">
							Total: ${totSubBacklog}
							</th>
					</tr>
					
								
					<!-- SubGrupo Desenvolvimento -->
					<tr style="background-color: #F5F5F5"  class="mousehover">
							<th colspan="2" class="tituloTabela" style="color: #000">Desenvolvimento</th>
					</tr>
					<c:forEach items="${listaResRCEtapa}" var="i">
												
						<c:choose>
							<c:when test="${i.etapa eq 'AGE.PRODUÇAO' 
										 or i.etapa eq 'AND.PRODUÇAO'
										 or i.etapa eq 'REF.PRODUÇAO'
										 or i.etapa eq 'SUSP.PROD'}">
								<c:set var="totSubDesenv" value="${totSubDesenv + i.quantidade}" />  		 
								<tr style="background-color: #F5F5F5"  class="mousehover">
									<td class="centro">${i.quantidade}</td>
									<td class="esquerda">${i.etapa}</td>
								</tr>
							</c:when>
						</c:choose>
					</c:forEach>
					<tr style="background-color: #F5F5F5"  class="mousehover">
							<th colspan="2" class="subtituloTabela esquerda" style="color: #000">
							Total: ${totSubDesenv}
							</th>
					</tr>
					
					<!-- SubGrupo Testes Consinco -->
					<tr style="background-color: #F5F5F5"  class="mousehover">
							<th colspan="2" class="tituloTabela" style="color: #000">Testes Consinco</th>
					</tr>
					<c:forEach items="${listaResRCEtapa}" var="i">
						
						<c:choose>
							<c:when test="${i.etapa eq 'LIB.TESTES' 
										 or i.etapa eq 'TESTES'
										 or i.etapa eq 'SUSP.SUP'
										 or i.etapa eq 'AGE.SUPORTE'
										 or i.etapa eq 'AND.SUPORTE'}">
								<c:set var="totSubTestes" value="${totSubTestes + i.quantidade}" />  
								<tr style="background-color: #F5F5F5"  class="mousehover">
									<td class="centro">${i.quantidade}</td>
									<td class="esquerda">${i.etapa}</td>
								</tr>
							</c:when>
						</c:choose>
					</c:forEach>
					<tr style="background-color: #F5F5F5"  class="mousehover">
							<th colspan="2" class="subtituloTabela esquerda" style="color: #000">
							Total: ${totSubTestes}
							</th>
					</tr>
					
					<!-- SubGrupo Homologação -->
					<tr style="background-color: #F5F5F5"  class="mousehover">
							<th colspan="2" class="tituloTabela" style="color: #000">Homologação Assa&iacute;</th>
					</tr>
					<c:forEach items="${listaResRCEtapa}" var="i">
						
						<c:choose>
							<c:when test="${i.etapa eq 'PEND.CLIENTE'}">
								<c:set var="totSubHomol" value="${totSubHomol + i.quantidade}" />  
								<tr style="background-color: #F5F5F5"  class="mousehover">
									<td class="centro">${i.quantidade}</td>
									<td class="esquerda">${i.etapa}</td>
								</tr>
							</c:when>
						</c:choose>
					</c:forEach>
					<tr style="background-color: #F5F5F5"  class="mousehover">
							<th colspan="2" class="subtituloTabela esquerda" style="color: #000">
							Total: ${totSubHomol}
							</th>
					</tr>
					
					<tr class="rodapeTabela esquerda">
						<th colspan="2">Total Geral: ${totalresumorceta}</th>
						
					</tr>
				</table>
				
				<div class="windowGrafico" id="glResumoRCEtapa">
					<a href="#" class="fechar">Fechar</a>
					<img src="<c:url value="gResumoRCEtapa" />" />
				</div>
				
			</td>

			<td width="40%" valign="top" >
			<!-- -------------------------Tabela RC por Modulo--------------------------------- -->
				<table border="1" cellpadding="5px" style="width: 600px" class="tabela" align="left">

					<tr class="tituloTabela">
						<th colspan="6">Situa&ccedil;&atilde;o dos RC(S) por M&oacute;dulo</th>
					</tr>
					
					<tr class="subtituloTabela">
						<th>Aplica&ccedil;&atilde;o</th>
						<th>Homologados</th>
						<th>Em dia</th>
						<th>Atrasados</th>
						<th>Germud</th>
						<th>Backlog</th>
					</tr>
					
					<c:set var="totalH" value="0" />
					<c:set var="totalA" value="0" />
					<c:set var="totalG" value="0" />
					<c:set var="totalE" value="0" />
					<c:set var="totalB" value="0" />
					<c:forEach items="${listaRCModulo}" var="i" varStatus="vs">
						
						<c:set var="totalH" value="${totalH + i.homologado}" /> 
						<c:set var="totalE" value="${totalE + i.emdia}" /> 
						<c:set var="totalA" value="${totalA + i.atrasados}" /> 
						<c:set var="totalG" value="${totalG + i.germud}" /> 
						<c:set var="totalB" value="${totalB + i.backlog}" /> 
						
						<tr style="background-color: #F5F5F5"  class="mousehover">
							<td class="esquerda">${i.aplicacao}</td>
							<td class="centro">${i.homologado}</td>
							<td class="centro">${i.emdia}</td>
							<td class="centro">${i.atrasados}</td>
							<td class="centro">${i.germud}</td>
							<td class="centro">${i.backlog}</td>
						</tr>
					</c:forEach>
					
					<tr class="rodapeTabela">
						<th align="right">Totais:</th>
						<th>${totalH}</th>
						<th>${totalE}</th>
						<th>${totalA}</th>
						<th>${totalG}</th>
						<th>${totalB}</th>
					</tr>
					
				</table>
			</td>
		</tr>
		
		
		<tr>
			<td width="300px" valign="top"  colspan="3">
			<!-- -------------------------Tabela RC por Prioridade --------------------------------- -->
				<table border="1" cellpadding="5px" style="width: 600px" class="tabela">

					<tr class="tituloTabela">
						<th colspan="6">Situa&ccedil;&atilde;o dos RC(S) por Prioridade</th>
					</tr>
					
					<tr class="subtituloTabela">
						<th>Prioridade</th>
						<th>Homologados</th>
						<th>Em dia</th>
						<th>Atrasados</th>
						<th>Germud</th>
						<th>Backlog</th>
					</tr>
					
					<c:set var="totalH" value="0" />
					<c:set var="totalA" value="0" />
					<c:set var="totalG" value="0" />
					<c:set var="totalE" value="0" />
					<c:set var="totalB" value="0" />
					<c:forEach items="${listaRCPrioridade}" var="i" varStatus="vs">
						
						<c:set var="totalH" value="${totalH + i.homologado}" /> 
						<c:set var="totalE" value="${totalE + i.emdia}" /> 
						<c:set var="totalA" value="${totalA + i.atrasados}" /> 
						<c:set var="totalG" value="${totalG + i.germud}" /> 
						<c:set var="totalB" value="${totalB + i.backlog}" /> 
						
						<tr style="background-color: #F5F5F5" >
							<td class="esquerda">${i.prioridade}</td>
							<td class="centro">${i.homologado}</td>
							<td class="centro">${i.emdia}</td>
							<td class="centro">${i.atrasados}</td>
							<td class="centro">${i.germud}</td>
							<td class="centro">${i.backlog}</td>
						</tr>
					</c:forEach>
					
					<tr class="rodapeTabela">
						<th align="right">Totais:</th>
						<th>${totalH}</th>
						<th>${totalE}</th>
						<th>${totalA}</th>
						<th>${totalG}</th>
						<th>${totalB}</th>
					</tr>
					
				</table>
			</td>
			
			<td>
			<!-- -------------------------Tabela RC por Categoria --------------------------------- -->
				<table  align="left" border="1" cellpadding="5px" style="width: 600px" class="tabela">

					<tr class="tituloTabela">
						<th colspan="6">Situa&ccedil;&atilde;o dos RC(S) por Categoria</th>
					</tr>
					
					<tr class="subtituloTabela">
						<th>Categoria</th>
						<th>Homologados</th>
						<th>Em dia</th>
						<th>Atrasados</th>
						<th>Germud</th>
						<th>Backlog</th>
					</tr>
					
					<c:set var="totalH" value="0" />
					<c:set var="totalA" value="0" />
					<c:set var="totalG" value="0" />
					<c:set var="totalE" value="0" />
					<c:set var="totalB" value="0" />
					<c:forEach items="${listaRCCategoria}" var="i" varStatus="vs">
						
						<c:set var="totalH" value="${totalH + i.homologado}" /> 
						<c:set var="totalE" value="${totalE + i.emdia}" /> 
						<c:set var="totalA" value="${totalA + i.atrasados}" /> 
						<c:set var="totalG" value="${totalG + i.germud}" /> 
						<c:set var="totalB" value="${totalB + i.backlog}" /> 
						
						<tr style="background-color: #F5F5F5" >
							<td class="esquerda">${i.categoria}</td>
							<td class="centro">${i.homologado}</td>
							<td class="centro">${i.emdia}</td>
							<td class="centro">${i.atrasados}</td>
							<td class="centro">${i.germud}</td>
							<td class="centro">${i.backlog}</td>
						</tr>
					</c:forEach>
					
					<tr class="rodapeTabela">
						<th align="right">Totais:</th>
						<th>${totalH}</th>
						<th>${totalE}</th>
						<th>${totalA}</th>
						<th>${totalG}</th>
						<th>${totalB}</th>
					</tr>
					
				</table>
			</td>
		</tr>
	
		
		<tr>
			<td colspan="23">
<!-- ---------------------------------------------------------------------------------------------------------------------- -->
<!-- ------------------------------------------------ Inicio Abas --------------------------------------------------------- -->
<!-- ---------------------------------------------------------------------------------------------------------------------- -->
					<div id="tabs">
						<ul>
							<li><a href="#tabs-1">Em Atraso</a></li>
							<li><a href="#tabs-2">No Prazo</a></li>
							<li><a href="#tabs-3">Homologados</a></li>
							<li><a href="#tabs-4">Germud</a></li>
							<li><a href="#tabs-5">Prox. Vers&atilde;o</a></li>
							<li><a href="#tabs-6">BackLog</a></li>
							<li><a href="#tabs-7">An&aacute;lise</a></li>
						</ul>

<!-- -------------------------Tabela de Atrasado--------------------------------- -->
				    <div id="tabs-1">
				    	<c:set var="totaltblAtrasado" value="0" />
				    	
					<table border="1" class="display" id="example1" width="100%" style="width: 100%"> 
								
								<!-- Cabeçalho -->
								<thead>
								<tr class="tituloTabela">
									
									<th>RP's</th>
									<th>RC</th>
									<th>Projeto</th>
									<th>Respons&aacute;vel</th>
									<th>Prazo Desenv</th>
									<th>Prazo Homolog</th>
									<th>Etapa</th>
									<th>Dias Atras.</th>
									<th>Nro Chamado</th>
									<th>Aplica&ccedil;&atilde;o</th>
									<th>Pri.</th>
									<th>Categoria</th>
								</tr>
								</thead>
								
								<tbody>
								<c:forEach items="${listaAtrasado}" var="i" varStatus="id">
								
								<c:set var="totaltblAtrasado" value="${id.count}" /> 
								
							<c:choose>
								<c:when test="${i.diasatrasado < 30}">
									<tr bgcolor="#${id.count % 2 == 0 ? 'E2E4FF' : 'FFF' }" class="mousehover">
								</c:when>
								<c:otherwise>
									<tr bgcolor="#FFDAB9" class="mousehover">
								</c:otherwise>
							</c:choose>
										
							<c:choose>
								<c:when test="${i.possuirp == 0}">
									<td>&nbsp;</td>
								</c:when>
								<c:otherwise>
									<td align="center">
										<a href="rp?pRC=${i.rc}">
											<img src="<c:url value="imagens/bot-mais.png" />" />
										</a>
									</td>
								</c:otherwise>
							</c:choose>										
										
										<td><a href="#${i.rc}" rel="modal">${i.rc}</a></td>
										<td>${i.projeto}</td>
										<td>${i.responsavel}</td>
										<td><fmt:formatDate value="${i.prazodesenv.time}" pattern="dd/MM/yyyy" /></td>
										<td><fmt:formatDate value="${i.prazohomolog.time}" pattern="dd/MM/yyyy" /></td>
										<td>${i.etapa}</td>
										<td align="center">${i.diasatrasado}</td>
										<td align="center">${i.nrochamado}</td>
										<td>${i.aplicacao}</td>
										<td align="center">${i.prioridade}</td>
										<td>${i.categoria}</td>
									</tr>
									
								<div class="window" id="${i.rc}">
								    <a href="#" class="fechar">Fechar</a>
									    <label class="requisitomodal">Requisito: ${i.rc}</label> <br><br>
									    <label class="textomodal"><b>Analista de Neg&oacute;cio:</b> ${i.analistaNegocio}</label> <br>
									    <label class="textomodal"><b>Analista de Sistema:</b> ${i.analistaSistema}</label> <br>
									    <label class="textomodal"><b>Analista Anotou:</b> ${i.analistaAnotou}</label> <br><br>
									    	<label class="textomodal">
													<c:if test="${not empty i.dtaLibHomolog}">
														<br><br>
														<b>Data de Libera&ccedil;&atilde;o:</b>
														<fmt:formatDate value="${i.dtaLibHomolog.time}" pattern="dd/MM/yyyy" />
													</c:if>
									    	</label> <br>
									    ${i.anotacao}
								</div>
										
								</c:forEach>
								</tbody>
								
								<tfoot>
									<tr class="rodapeTabela">
										<td colspan="12" align="left"><b>Total de RC's: ${totaltblAtrasado}</b></td>
									</tr>
								</tfoot>
							</table>
						</div>
						
<!-- -------------------------Tabela de No Prazo--------------------------------- -->
				    <div id="tabs-2">
				    	<c:set var="totaltblPrazo" value="0" />
				<table border="1" class="display" id="example2" >

								<!-- Cabeçalho -->
								<thead>
								<tr class="tituloTabela">
									<th>RP's</th>
									<th>RC</th>
									<th>Projeto</th>
									<th>Respons&aacute;vel</th>
									<th>Prazo Desenv</th>
									<th>Prazo Homolog</th>
									<th>Etapa</th>
									<th>Nro Chamado</th>
									<th>Aplica&ccedil;&atilde;o</th>
									<th>Pri.</th>
									<th>Categoria</th>
								</tr>
								</thead>
								
								
								<tbody>
								<c:forEach items="${listaEmDia}" var="i" varStatus="id">
								
									<c:set var="totaltblPrazo" value="${id.count}" /> 	
							
									<tr bgcolor="#${id.count % 2 == 0 ? 'E2E4FF' : 'FFF' }" class="mousehover">
									
										<c:choose>
											<c:when test="${i.possuirp == 0}">
												<td>&nbsp;</td>
											</c:when>
											<c:otherwise>
												<td align="center">
													<a href="rp?pRC=${i.rc}">
														<img src="<c:url value="imagens/bot-mais.png" />" />
													</a>
												</td>
											</c:otherwise>
										</c:choose>
									
										<td><a href="#${i.rc}" rel="modal">${i.rc}</a></td>
										<td>${i.projeto}</td>
										<td>${i.responsavel}</td>
										<td><fmt:formatDate value="${i.prazodesenv.time}"  pattern="dd/MM/yyyy" /></td>
										<td><fmt:formatDate value="${i.prazohomolog.time}" pattern="dd/MM/yyyy" /></td>
										<td>${i.etapa}</td>
										<td align="center">${i.nrochamado}</td>
										<td>${i.aplicacao}</td>
										<td align="center">${i.prioridade}</td>
										<td>${i.categoria}</td>
									</tr>
									
										<div class="window" id="${i.rc}">
										    <a href="#" class="fechar">Fechar</a>
										    <label class="requisitomodal">Requisito: ${i.rc}</label> <br><br>
										    <label class="textomodal"><b>Analista de Neg&oacute;cio:</b> ${i.analistaNegocio}</label> <br>
										    <label class="textomodal"><b>Analista de Sistema:</b> ${i.analistaSistema}</label> <br>
										    <label class="textomodal"><b>Analista Anotou:</b> ${i.analistaAnotou}</label> 
									    	<label class="textomodal">
													<c:if test="${not empty i.dtaLibHomolog}">
														<br><br>
														<b>Data de Libera&ccedil;&atilde;o:</b>
														<fmt:formatDate value="${i.dtaLibHomolog.time}" pattern="dd/MM/yyyy" />
													</c:if>
									    	</label> <br>
										    ${i.anotacao}
										</div>
										
								</c:forEach>
								</tbody>
								
								<tfoot>
									<tr class="rodapeTabela">
										<td colspan="11" align="left"><b>Total de RC's: ${totaltblPrazo}</b></td>
									</tr>
								</tfoot>
							</table>
				</div>
						
<!-- -------------------------Tabela de Homologados--------------------------------- -->
					<div id="tabs-3">
						<c:set var="totaltblHomologados" value="0" />
			<table border="1" class="display" id="example3">

								<!-- Cabeçalho -->
								<thead>
								<tr class="tituloTabela">
									<th>RP's</th>
									<th>RC</th>
									<th>Projeto</th>
									<th>Nro Chamado</th>
									<th>Aplica&ccedil;&atilde;o</th>
									<th>Pri.</th>
									<th>Data/Hora Inclus&atilde;o</th>
									<th>Usu. Inclus&atilde;o</th>
									<th>Categoria</th>
								</tr>
								</thead>
								
								<tbody>
								<c:forEach items="${listaHomologado}" var="i" varStatus="id">
								
									<c:set var="totaltblHomologados" value="${id.count}" /> 
									
									<tr bgcolor="#${id.count % 2 == 0 ? 'E2E4FF' : 'FFF' }" class="mousehover">
									
										<c:choose>
											<c:when test="${i.possuirp == 0}">
												<td>&nbsp;</td>
											</c:when>
											<c:otherwise>
												<td align="center">
													<a href="rp?pRC=${i.rc}">
														<img src="<c:url value="imagens/bot-mais.png" />" />
													</a>
												</td>
											</c:otherwise>
										</c:choose>
										
										<td><a href="#${i.rc}" rel="modal">${i.rc}</a></td>
										<td>${i.projeto}</td>
										<td align="center">${i.nrochamado}</td>
										<td>${i.aplicacao}</td>
										<td align="center">${i.prioridade}</td>
										<td><fmt:formatDate value="${i.dtaHoraInclusao.time}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
										<td>${i.usuInclusao}</td>
										<td>${i.categoria}</td>										
									</tr>
									
										<div class="window" id="${i.rc}">
										    <a href="#" class="fechar">Fechar</a>
										    <label class="requisitomodal">Requisito: ${i.rc}</label> <br><br>
										    <label class="textomodal"><b>Analista de Neg&oacute;cio:</b> ${i.analistaNegocio}</label> <br>
										    <label class="textomodal"><b>Analista de Sistema:</b> ${i.analistaSistema}</label> <br>
										    <label class="textomodal"><b>Analista Anotou:</b> ${i.analistaAnotou}</label> <br><br>
									    	<label class="textomodal">
													<c:if test="${not empty i.dtaLibHomolog}">
														<br><br>
														<b>Data de Libera&ccedil;&atilde;o:</b>
														<fmt:formatDate value="${i.dtaLibHomolog.time}" pattern="dd/MM/yyyy" />
													</c:if>
									    	</label> <br>
										    ${i.anotacao}
										</div>
										
								</c:forEach>
								</tbody>
								
								<tfoot>
									<tr class="rodapeTabela">
										<td colspan="9" align="left"><b>Total de RC's: ${totaltblHomologados}</b></td>
									</tr>
								</tfoot>
							</table>
						
						</div>
								
<!-- -------------------------Tabela de Germud--------------------------------- -->
					<div  id="tabs-4">
						<c:set var="totaltblGermud" value="0" />
					<table border="1" class="display" id="example4">

								<!-- Cabeçalho -->
								<thead>
								<tr class="tituloTabela">
									<th>RP's</th>
									<th>RC</th>
									<th>Projeto</th>
									<th>Nro Chamado</th>
									<th>Ana.Neg&oacute;cio</th>
									<th>Ana.Sistema</th>
									<th>Aplica&ccedil;&atilde;o</th>
									<th>Pri.</th>
									<th>Data/Hora Inclus&atilde;o</th>
									<th>Usu. Inclus&atilde;o</th>
									<th>Categoria</th>
								</tr>
								</thead>

								<tbody>
								<c:forEach items="${listaGermud}" var="i" varStatus="id">
									
									<c:set var="totaltblGermud" value="${id.count}" /> 
									
									<tr bgcolor="#${id.count % 2 == 0 ? 'E2E4FF' : 'FFF' }" class="mousehover">

										<c:choose>
											<c:when test="${i.possuirp == 0}">
												<td>&nbsp;</td>
											</c:when>
											<c:otherwise>
												<td align="center">
													<a href="rp?pRC=${i.rc}">
														<img src="<c:url value="imagens/bot-mais.png" />" />
													</a>
												</td>
											</c:otherwise>
										</c:choose>
										
										<td><a href="#g${i.rc}" rel="modal">${i.rc}</a></td>
										<td>${i.projeto}</td>
										<td align="center">${i.nrochamado}</td>
										<td>${i.analistaNegocio}</td>
										<td>${i.analistaSistema}</td>
										<td>${i.aplicacao}</td>
										<td align="center">${i.prioridade}</td>
										<td><fmt:formatDate value="${i.dtaHoraInclusao.time}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
										<td>${i.usuInclusao}</td>
										<td>${i.categoria}</td>
									</tr>
										
										<div class="window" id="g${i.rc}">
										    <a href="#" class="fechar">Fechar</a>
										    <label class="requisitomodal">Requisito: ${i.rc}</label> <br><br>
										    <label class="textomodal"><b>Analista de Neg&oacute;cio:</b> ${i.analistaNegocio}</label> <br>
										    <label class="textomodal"><b>Analista de Sistema:</b> ${i.analistaSistema}</label> <br>
										    <label class="textomodal"><b>Analista Anotou:</b> ${i.analistaAnotou}</label> <br><br>
									    	<label class="textomodal">
													<c:if test="${not empty i.dtaLibHomolog}">
														<br><br>
														<b>Data de Libera&ccedil;&atilde;o:</b>
														<fmt:formatDate value="${i.dtaLibHomolog.time}" pattern="dd/MM/yyyy" />
													</c:if>
									    	</label> <br>
										    ${i.anotacao}
										</div>
										
								</c:forEach>
								</tbody>
								
								<tfoot>
									<tr class="rodapeTabela">
										<td colspan="11" align="left"><b>Total de RC's: ${totaltblGermud}</b></td>
									</tr>
								</tfoot>
							</table>
					</div>

						
<!-- -------------------------Tabela de Proxima Versão--------------------------------- -->
					<div  id="tabs-5">
						<c:set var="totaltblProxVersao" value="0" />
					<table border="1" class="display" id="example5">

								<!-- Cabeçalho -->
								<thead>
								<tr class="tituloTabela">
									<th>RP's</th>
									<th>RC</th>
									<th>Projeto</th>
									<th>Nro Chamado</th>
									<th>Ana.Neg&oacute;cio</th>
									<th>Ana.Sistema</th>
									<th>Etapa</th>
									<th>Respons&aacute;vel</th>
									<th>Merge</th>
									<th>Solucao</th>
									<th>Perc.Concluido</th>
									<th>Data Req.</th>
									<th>Pri.</th>
									<th>Categoria</th>
								</tr>
								</thead>
								
								<tbody>
								<c:forEach items="${listaProxVersao}" var="i" varStatus="id">
								
									<c:set var="totaltblProxVersao" value="${id.count}" /> 
									
									<tr bgcolor="#${id.count % 2 == 0 ? 'E2E4FF' : 'FFF' }" class="mousehover">
									
										<c:choose>
											<c:when test="${i.possuirp == 0}">
												<td>&nbsp;</td>
											</c:when>
											<c:otherwise>
												<td align="center">
													<a href="rp?pRC=${i.rc}">
														<img src="<c:url value="imagens/bot-mais.png" />" />
													</a>
												</td>
											</c:otherwise>
										</c:choose>
										
										<td><a href="#p${i.rc}" rel="modal">${i.rc}</a></td>
										<td>${i.projeto}</td>
										<td align="center">${i.nrochamado}</td>
										<td>${i.analistaNegocio}</td>
										<td>${i.analistaSistema}</td>
										<td>${i.etapa}</td>
										<td>${i.responsavel}</td>
										<td>${i.merge}</td>
										<td>${i.solucao}</td>
										<td align="center">${i.percentualConcluido}%</td>
										<td><fmt:formatDate value="${i.dataRequisicao.time}" pattern="dd/MM/yyyy" /></td>
										<td align="center">${i.prioridade}</td>
										<td>${i.categoria}</td>
									</tr>
										
										<div class="window" id="p${i.rc}">
										    <a href="#" class="fechar">Fechar</a>
										    <label class="requisitomodal">Requisito: ${i.rc}</label> <br><br>
										    <label class="textomodal"><b>Analista de Neg&oacute;cio:</b> ${i.analistaNegocio}</label> <br>
										    <label class="textomodal"><b>Analista de Sistema:</b> ${i.analistaSistema}</label> <br>
										    <label class="textomodal"><b>Analista Anotou:</b> ${i.analistaAnotou}</label> <br>
										    ${i.anotacao}
										</div>

								</c:forEach>
								</tbody>
								
								<tfoot>
									<tr class="rodapeTabela">
										<td colspan="14" align="left"><b>Total de RC's: ${totaltblProxVersao}</b></td>
									</tr>
								</tfoot>
							</table>

						</div>
						
<!-- -------------------------Tabela de Backlog--------------------------------- -->
					<div  id="tabs-6">
						<c:set var="totaltblBacklog" value="0" />
							<table border="1" class="display" id="example6" >

								<!-- Cabeçalho -->
								<thead>
								<tr class="tituloTabela">
									<th>RP's</th>
									<th>RC</th>
									<th>Descri&ccedil;&atilde;o</th>
									<th>Nro Chamado</th>
									<th>Data Prometida</th>
									<th>Data Req.</th>
									<th>Analista Anot.</th>
									<th>Sistema</th>
									<th>Aplica&ccedil;&atilde;o</th>
									<th>Pri.</th>
									<th>Categoria</th>
								</tr>
								</thead>

								<tbody>
								<c:forEach items="${listaBacklog}" var="i" varStatus="id">
								
									<c:set var="totaltblBacklog" value="${id.count}" /> 
							<c:choose>
								<c:when test="${i.dtaPrometida eq null}">
									<tr bgcolor="#${id.count % 2 == 0 ? 'E2E4FF' : 'FFF' }" class="mousehover">
								</c:when>
								<c:otherwise>
									<tr bgcolor="#FFDAB9" class="mousehover">
								</c:otherwise>
							</c:choose>
							
										<c:choose>
											<c:when test="${i.possuirp == 0}">
												<td>&nbsp;</td>
											</c:when>
											<c:otherwise>
												<td align="center">
													<a href="rp?pRC=${i.rc}">
														<img src="<c:url value="imagens/bot-mais.png" />" />
													</a>
												</td>
											</c:otherwise>
										</c:choose>
										<td><a href="#${i.rc}" rel="modal">${i.rc}</a></td>
										<td>${i.projeto}</td>
										<td align="center">${i.nrochamado}</td>
										<td><fmt:formatDate value="${i.dtaPrometida.time}" pattern="dd/MM/yyyy" /></td>
										<td><fmt:formatDate value="${i.dataRequisicao.time}" pattern="dd/MM/yyyy" /></td>
										<td>${i.analistaAnotou}</td>
										<td>${i.sistema}</td>
										<td>${i.aplicacao}</td>
										<td align="center">${i.prioridade}</td>
										<td>${i.categoria}</td>
									</tr>
										
										<div class="window" id="${i.rc}">
										    <a href="#" class="fechar">Fechar</a>
										    <label class="requisitomodal">Requisito: ${i.rc}</label> <br><br>
										    <label class="textomodal"><b>Analista de Neg&oacute;cio:</b> ${i.analistaNegocio}</label> <br>
										    <label class="textomodal"><b>Analista de Sistema:</b> ${i.analistaSistema}</label> <br>
										    <label class="textomodal"><b>Analista Anotou:</b> ${i.analistaAnotou}</label> <br>
										    ${i.anotacao}
										</div>
										
								</c:forEach>
								</tbody>
								
								<tfoot>
									<tr class="rodapeTabela">
										<td colspan="11" align="left"><b>Total de RC's: ${totaltblBacklog}</b></td>
									</tr>
								</tfoot>	
							</table>

						</div>
						
<!-- -------------------------Tabela de Análise--------------------------------- -->
					<div  id="tabs-7">
						<c:set var="totaltblAnalise" value="0" />
							<table border="1" class="display" id="example7" >

								<!-- Cabeçalho -->
								<thead>
								<tr class="tituloTabela">
									<th>RP's</th>
									<th>RC</th>
									<th>Descri&ccedil;&atilde;o</th>
									<th>Nro Chamado</th>
									<th>Data Prometida</th>
									<th>Data Req.</th>
									<th>Analista Anot.</th>
									<th>Sistema</th>
									<th>Aplica&ccedil;&atilde;o</th>
									<th>Pri.</th>
									<th>Categoria</th>
								</tr>
								</thead>

								<tbody>
								<c:forEach items="${listaAnalise}" var="i" varStatus="id">
								
									<c:set var="totaltblAnalise" value="${id.count}" /> 
							<c:choose>
								<c:when test="${i.dtaPrometida eq null}">
									<tr bgcolor="#${id.count % 2 == 0 ? 'E2E4FF' : 'FFF' }" class="mousehover">
								</c:when>
								<c:otherwise>
									<tr bgcolor="#FFDAB9" class="mousehover">
								</c:otherwise>
							</c:choose>

										<c:choose>
											<c:when test="${i.possuirp == 0}">
												<td>&nbsp;</td>
											</c:when>
											<c:otherwise>
												<td align="center">
													<a href="rp?pRC=${i.rc}">
														<img src="<c:url value="imagens/bot-mais.png" />" />
													</a>
												</td>
											</c:otherwise>
										</c:choose>
										
										<td><a href="#${i.rc}" rel="modal">${i.rc}</a></td>
										<td>${i.projeto}</td>
										<td align="center">${i.nrochamado}</td>
										<td><fmt:formatDate value="${i.dtaPrometida.time}" pattern="dd/MM/yyyy" /></td>
										<td><fmt:formatDate value="${i.dataRequisicao.time}" pattern="dd/MM/yyyy" /></td>
										<td>${i.analistaAnotou}</td>
										<td>${i.sistema}</td>
										<td>${i.aplicacao}</td>
										<td align="center">${i.prioridade}</td>
										<td>${i.categoria}</td>
									</tr>
										
										<div class="window" id="${i.rc}">
										    <a href="#" class="fechar">Fechar</a>
										    <label class="requisitomodal">Requisito: ${i.rc}</label> <br><br>
										    <label class="textomodal"><b>Analista de Neg&oacute;cio:</b> ${i.analistaNegocio}</label> <br>
										    <label class="textomodal"><b>Analista de Sistema:</b> ${i.analistaSistema}</label> <br>
										    <label class="textomodal"><b>Analista Anotou:</b> ${i.analistaAnotou}</label> <br>
										    ${i.anotacao}
										</div>
										
								</c:forEach>
								</tbody>
								
								<tfoot>
									<tr class="rodapeTabela">
										<td colspan="11" align="left"><b>Total de RC's: ${totaltblAnalise}</b></td>
									</tr>
								</tfoot>	
							</table>

						</div>
						
						<!-- fim conteudo -->
					</div>
			</td>

		</tr>
		

	</table>
		
		<!-- mascara para cobrir o site -->	
		<div id="mascara"></div>
		
	<c:import url="template/rodape.jsp" />
</body>
</html>