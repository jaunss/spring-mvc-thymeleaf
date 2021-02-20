package com.joaogcm.projeto.spring.mvc.resources;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class RelatorioUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Retorna nosso PDF em Byte para Download no Navegador */
	public byte[] gerarRelatorio(List<?> listDados, String relatorio, ServletContext servletContext)
			throws JRException {

		/*
		 * Cria a lista de dados para o relatório com nossa lista de objetos para
		 * imprimir
		 */
		JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(listDados);

		/* Carrega o caminho do arquivo Jasper compilado */
		String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator + relatorio + ".jasper";

		/* Carrega o arquivo Jasper passando os dados */
		JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, new HashMap<>(),
				jrBeanCollectionDataSource);

		/* Exporta para byte[] para fazer o download do PDF */
		return JasperExportManager.exportReportToPdf(impressoraJasper);
	}
}