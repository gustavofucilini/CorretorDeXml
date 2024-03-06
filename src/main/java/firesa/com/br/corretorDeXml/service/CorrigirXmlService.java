package firesa.com.br.corretorDeXml.service;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.github.underscore.U;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


@Service
public class CorrigirXmlService {
	
	JProgressBar progressBar = new JProgressBar();
	JFrame f = new JFrame("Progresso XMLs");

	CorretorNfService consutaService = new CorretorNfService();
	
	private List<File> listaAquivosXml;
	
	public List<File> SelecionaXml() {
		FileChooser escolherArquivos = new FileChooser();
		escolherArquivos.setTitle("Selecione as XMLs");
		listaAquivosXml = escolherArquivos.showOpenMultipleDialog(new Stage());
		return listaAquivosXml;
	}
	
	public void CorrigirXml(TextField edtXmlSelecionada) {
		Iterable<String> arquivosIterable = Splitter.on(CharMatcher.anyOf(".,[]")).split(edtXmlSelecionada.getText());
		List<String> arquivosLista = Arrays.asList(arquivosIterable.toString().split(","));

		int qtdArquivosNaoXml = 0;
		int tamanhoParaExtensoes = 2;
		for (int i = 0; i < arquivosLista.size(); i++) {
			if (!arquivosLista.get(tamanhoParaExtensoes).equals(" xml")) {
				qtdArquivosNaoXml++;
			} else {
				continue;
			}
			tamanhoParaExtensoes += 0;
		}
		if (!edtXmlSelecionada.getText().equals("ERRO")) {
			//String diretorioXmls = edtXmlSelecionada.getText().substring(1, edtXmlSelecionada.getText().length() - 1).replaceAll(" ", "");
			//List<String> listaDiretoriosXmls = Arrays.asList(diretorioXmls.split(","));
			
			Reader fileReader;
			try {
				for (int i = 0; i < listaAquivosXml.size(); i++) {
					try {
						f.setVisible(false);
						barraDeProgresso(listaAquivosXml.size(),i+1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					List<String> xmlCorrigida = new ArrayList<String>();
					
					fileReader = new FileReader(listaAquivosXml.get(i));
					BufferedReader bufReader = new BufferedReader(fileReader);
					String xmlFormatada = U.formatXml(bufReader.readLine());
					
					List<String> xmlLinhas = Arrays.asList(xmlFormatada.split("\\r?\\n"));
					int tamanhoXmlLinhas = xmlLinhas.size();
					for (int j = 0; j < tamanhoXmlLinhas; j++) {
						if (j != 1) {
							xmlCorrigida.add(xmlLinhas.get(j));
						}else {
							xmlCorrigida.add("<nfeProc versao=\"4.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
							xmlCorrigida.add(xmlLinhas.get(j));
						}
						
					}
					List<String> protNfe = consutaService.consultaChave(xmlCorrigida.get(3).replaceAll("<infNFe versao=\"4.00\" Id=\"NFe", "").replaceAll("\">", "").replaceAll("\\s+", ""));
					if (protNfe != null) {
						for (int j = 0; j < protNfe.size(); j++) {
							xmlCorrigida.add(protNfe.get(j));
						}
						xmlCorrigida.add("</nfeProc>");
						
						xmlCorrigida.set(xmlCorrigida.size()-30, "      <Reference URI=\"#" + protNfe.get(4).replaceAll("		<chNFe>", "").replaceAll("</chNFe>", "") + "\">");
						xmlCorrigida.set(3, "  <infNFe versao=\"4.00\" Id=\"" + protNfe.get(4).replaceAll("		<chNFe>", "").replaceAll("</chNFe>", "") + "\">");
					}else {
						xmlCorrigida.remove(1);
					}
					
					FileUtils.writeStringToFile(listaAquivosXml.get(i), String.join("\n", xmlCorrigida));
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		} else if (qtdArquivosNaoXml > 0) {
			edtXmlSelecionada.setText("ERRO");
			JFrame jf=new JFrame();
			jf.setAlwaysOnTop(true);
			JOptionPane.showMessageDialog(jf, "Erro:\nUm ou mais alquivos selecionados não são XML");
		} else {
			JFrame jf=new JFrame();
			jf.setAlwaysOnTop(true);
			JOptionPane.showMessageDialog(jf, "Erro:\nSem arquivos selecionados");
		}
		
	}
	
	public boolean checaArquivo(List<File> arquivos) {
		String aquivosString = Arrays.toString(arquivos.toArray());
		Iterable<String> arquivosIterable = Splitter.on(CharMatcher.anyOf(".,[]")).split(aquivosString);
		List<String> arquivosLista = Arrays.asList(arquivosIterable.toString().split(","));

		int qtdArquivosNaoXml = 0;
		int tamanhoParaExtensoes = 2;
		for (int i = 0; i < arquivosLista.size(); i++) {
			if (!arquivosLista.get(tamanhoParaExtensoes).equals(" xml")) {
				qtdArquivosNaoXml++;
			} else {
				continue;
			}
			tamanhoParaExtensoes += 0;
		}
		if (qtdArquivosNaoXml > 0) {
			JFrame jf=new JFrame();
			jf.setAlwaysOnTop(true);
			JOptionPane.showMessageDialog(jf, "Erro:\nUm ou mais alquivos selecionados não são XML");
			return true;
		} else {
			return false;
		}
	}
	
	private void barraDeProgresso(int tamanho,int i) throws InterruptedException{
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.setAlwaysOnTop(true);
		f.toFront();
		Container content = f.getContentPane();
		progressBar.setValue(100/tamanho*i);
		progressBar.setStringPainted(true);
		Border border = BorderFactory.createTitledBorder(i + "/" + tamanho);
		progressBar.setBorder(border);
		content.add(progressBar, BorderLayout.NORTH);
		f.setSize(300, 100);
		f.setVisible(true);
	}

}
