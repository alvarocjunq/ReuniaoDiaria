package br.com.consinco.reuniaodiaria.graficos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import br.com.consinco.reuniaodiaria.modelos.ResumoRCEtapa;

public class ResumoRCEtapaGraph {
	

	public JFreeChart getGrafico(String pTitulo, List<ResumoRCEtapa> plista)
	{

		ArrayList<ResumoRCEtapa> lstResumoRC = new ArrayList<ResumoRCEtapa>(plista);
		JFreeChart iGrafico = criarGrafico( carregarDataSet(lstResumoRC) , pTitulo );
		return iGrafico;
	}
	
	private PieDataset carregarDataSet( ArrayList<ResumoRCEtapa> lista)
	{
		DefaultPieDataset dpd = new DefaultPieDataset();
		Iterator<ResumoRCEtapa> it = lista.iterator();
		
		while(it.hasNext())
		{
			ResumoRCEtapa resumoRC = (ResumoRCEtapa)it.next();
			
			String sEtapa =  resumoRC.getEtapa();			
			int iQtd = resumoRC.getQuantidade();
			
			if (resumoRC.getQuantidade() > 0)
				dpd.setValue( iQtd + " - " + sEtapa, iQtd);

		}
		return dpd;
	
	}
	
	private JFreeChart criarGrafico(PieDataset pdSet, String charTitle)
	{
		JFreeChart chart = ChartFactory.createPieChart3D(charTitle, pdSet, true, true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
				
		return chart;
		
	}

}
