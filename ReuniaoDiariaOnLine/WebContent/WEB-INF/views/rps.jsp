<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
	
	<title>Detalhamento do RC</title>
	
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
				
				$('table.display').dataTable( {
					"bStateSave" : false,
					"bAutoWidth" : false,
					"bPaginate": false,
					"bJQueryUI": true,
					"aoColumnDefs": [
						{ "sWidth": "10%", "aTargets": [ -1 ] }
					],
					//botões gerar csv/xls/pdf
					//"sDom" : 'T<"clear"><"H"lfr>t<"F"ip>', // deixa os botões acima da tabela
					"sDom" : '<"H"Tfr>t<"F"ip>', // deixa os botões dentro da tabela
					"oTableTools" : 
					{
						"sSwfPath" : "js/swf/copy_csv_xls_pdf.swf"
					}
				} );
			} );
		</script>
</head>
<body>

	<c:import url="template/cabecalho.jsp" />
	
	
		<h2>&nbsp;&nbsp;&nbsp;&nbsp;Detalhamento do RC: ${rc}</h2> 
		<h3>&nbsp;&nbsp;&nbsp;&nbsp;Anota&ccedil;&atilde;o: ${anotacaorc}</h3>
	<br><br>
	
			
<!-- --------------------------------------------------------------------------- -->
<!-- -------------------------------- Tabelas ---------------------------------- -->
<!-- --------------------------------------------------------------------------- -->
			<div id="tabs">
				<ul>
					<li><a href="#tabs-1">Listagem de RP's</a></li>
				</ul>


		<div id="tabs-1">
			<c:set var="totaltblAtrasado" value="0" />

			<table border="1" class="display" id="example1" >

				<!-- Cabeçalho -->
				<thead>
					<tr class="tituloTabela">

						<th>RP</th>
						<th>Aplica&ccedil;&atilde;o</th>
						<th>Etapa</th>
						<th>Analista Anotou</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${listaRP}" var="i" varStatus="id">

						<c:set var="totaltblrp" value="${id.count}" />

						<tr bgcolor="#${id.count % 2 == 0 ? 'E2E4FF' : 'FFF' }"	class="mousehover">

<%-- 						<td align="center"><a href="/ReuniaoDiariaOnLine/rps#${i.rp}" rel="modal">${i.rp}</a></td> --%>
						<td align="center">${i.rp}</td>
						<td>${i.aplicacao}</td>
						<td>${i.codetapa}</td>
						<td>${i.analistaAnotou}</td>
						</tr>

						<div class="window" id="${i.rp}">
							<a href="#" class="fechar">Fechar</a> 
							<label class="requisitomodal">Requisito: ${i.rp}</label> 
							<br> <br> <br> ${i.anotacao}
 						</div>

					</c:forEach>
				</tbody>

				<tfoot>
					<tr class="rodapeTabela">
						<td colspan="5" align="left"><b>Total de RP's:
								${totaltblrp}</b></td>
					</tr>
				</tfoot>
			</table>
		</div>

		<!-- -------------------------Tabela 2--------------------------------- -->
		<!-- 				    <div id="tabs-2"> -->

		<!-- 							<table cellpadding="0" cellspacing="0" border="0" class="display" id="example2">  -->


		<!-- 								<thead> -->
		<!-- 								<tr class="tituloTabela"> -->
		<!-- 									<th width="10px">Dias Atras.</th> -->
		<!-- 									<th>RC</th> -->
		<!-- 									<th>Projeto</th> -->
		<!-- 									<th>Respons&aacute;vel</th> -->
		<!-- 									<th>Prazo Desenv</th> -->
		<!-- 									<th>Prazo Homolog</th> -->
		<!-- 									<th width="50px">Etapa</th> -->

		<!-- 									<th>Nro Chamado</th> -->
		<!-- 									<th>Sistema</th> -->
		<!-- 									<th>Aplica&ccedil;&atilde;o</th> -->
		<!-- 									<th>Pri.</th> -->
		<!-- 								</tr> -->
		<!-- 								</thead> -->

		<!-- 								<tbody> -->
		<!-- 									<tr bgcolor="#${id.count % 2 == 0 ? 'E2E4FF' : 'FFF' }"> -->
		<%-- 										<td width="10px">${i.diasatrasado}</td> --%>
		<!-- 										<td>3</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 										<td>teste3</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 										<td width="50px">teste</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 									</tr> -->
		<!-- 									<tr bgcolor="#${id.count % 2 == 0 ? 'E2E4FF' : 'FFF' }" > -->
		<%-- 										<td width="10px">${i.diasatrasado}</td> --%>
		<!-- 										<td>4</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 										<td>teste4</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 										<td width="50px">teste</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 										<td>teste</td> -->
		<!-- 									</tr> -->
		<!-- 								</tbody> -->

		<!-- 								<tfoot> -->
		<!-- 									<tr class="rodapeTabela"> -->
		<!-- 										<td colspan="11" align="left"><b>Total de RC's: 0</b></td> -->
		<!-- 									</tr> -->
		<!-- 								</tfoot> -->
		<!-- 							</table> -->
		<!-- 						</div>	 -->
	</div>
	<c:import url="template/rodape.jsp" />	
</body>
</html>