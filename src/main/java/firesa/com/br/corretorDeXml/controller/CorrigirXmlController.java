package firesa.com.br.corretorDeXml.controller;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import firesa.com.br.corretorDeXml.service.CorrigirXmlService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

@Controller
public class CorrigirXmlController {

	@Autowired
	private CorrigirXmlService service = new CorrigirXmlService();

	private String textoNoCampo;

	private List<File> listaAquivosXml;

	@FXML
	private Button btnCorrigiXml;

	@FXML
	private Button btnSelecionaXml;

	@FXML
	private MenuItem btnSobre;

	@FXML
	private Button btnSobreFechar;

	@FXML
	private TextField edtXmlSelecionada;

	@FXML
	private Pane panelSobre;

	@FXML
	void btnSelecionarXmls(ActionEvent event) {
		try {
			listaAquivosXml = service.SelecionaXml();
			boolean checaErro = service.checaArquivo(listaAquivosXml);
			if (checaErro) {
				edtXmlSelecionada.setText("ERRO");
				edtXmlSelecionada.setEditable(false);
			} else {
				textoNoCampo = Arrays.toString(listaAquivosXml.toArray());
				edtXmlSelecionada.setText(textoNoCampo);
				edtXmlSelecionada.setEditable(true);
			}

		} catch (NullPointerException npe) {
			JFrame jf=new JFrame();
			jf.setAlwaysOnTop(true);
			JOptionPane.showMessageDialog(jf, "Erro:\nSeleção não pode ser nula");
		} catch (Exception e) {
			JFrame jf=new JFrame();
			jf.setAlwaysOnTop(true);
			JOptionPane.showMessageDialog(jf, e.getMessage());
		}

	}

	@FXML
	void btnCorrigirXmls(ActionEvent event) {
		service.CorrigirXml(edtXmlSelecionada);
		
	}

	@FXML
	void abrirTelaSobre(ActionEvent event) {
		panelSobre.setVisible(true);

	}

	@FXML
	void fecharTelaSobre(ActionEvent event) {
		panelSobre.setVisible(false);
	}
	
}
