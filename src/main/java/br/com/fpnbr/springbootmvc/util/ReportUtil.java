package br.com.fpnbr.springbootmvc.util;

import jakarta.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Component
public class ReportUtil implements Serializable {
    public byte[] gerarRelatorio(List dados, String relatorio, ServletContext servletContext) throws Exception { // Retorna o PDF em Byte para download no navegador
        //  Cria uma fonte de dados do JasperReports a partir de uma coleção de objetos Java, que será usada para preencher um relatório com dados
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(dados);

        // Carrega o caminho do arquivo jasper compilado
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("report/" + relatorio + ".jasper");

        // Carrega o arquivo Jasper passando os dados
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, new HashMap<>(), jrBeanCollectionDataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
