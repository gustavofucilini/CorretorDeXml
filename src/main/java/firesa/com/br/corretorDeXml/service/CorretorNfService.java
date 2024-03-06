package firesa.com.br.corretorDeXml.service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.underscore.U;

import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.AmbienteEnum;
import br.com.swconsultoria.nfe.dom.enuns.DocumentoEnum;
import br.com.swconsultoria.nfe.dom.enuns.EstadosEnum;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.schema_4.retConsSitNFe.TRetConsSitNFe;

@SpringBootApplication
public class CorretorNfService {

	public List<String> consultaChave(String chave){
		try {
			List<String> consultaLinhaProtNfe = new ArrayList<String>();
			ConfiguracoesNfe config = criaConfiguracoes();
			config.setEstado(checarEstadoNF(chave));

			TRetConsSitNFe retornoTRTCSNfe = Nfe.consultaXmlRetornoTRetConsSitNFe(config, chave, checarTipoNF(chave));
			String retornoXML = Nfe.consultaXmlRetornoString(config, chave, checarTipoNF(chave));
			String XMLFormatada = U.formatXml(retornoXML);
			
			if (!retornoTRTCSNfe.getXMotivo().equals("Autorizado o uso da NF-e")) {
				String chaveCorreta[] = retornoTRTCSNfe.getXMotivo().split("\\[|\\]");
				retornoTRTCSNfe = Nfe.consultaXmlRetornoTRetConsSitNFe(config, chaveCorreta[1].replaceAll("chNFe:", ""), checarTipoNF(chaveCorreta[1].replaceAll("chNFe:", "")));
				retornoXML = Nfe.consultaXmlRetornoString(config, chaveCorreta[1].replaceAll("chNFe:", ""), checarTipoNF(chaveCorreta[1].replaceAll("chNFe:", "")));
				XMLFormatada = U.formatXml(retornoXML);
				
				List<String> xmlConsultaLinhas = Arrays.asList(XMLFormatada.split("\\s*\n\\s*"));
				String protNfe = (xmlConsultaLinhas.get(8) + "\n	" + xmlConsultaLinhas.get(9) + "\n		" + xmlConsultaLinhas.get(10) + "\n		"
						+ xmlConsultaLinhas.get(11) + "\n		" + xmlConsultaLinhas.get(12) + "\n		" + xmlConsultaLinhas.get(13) + "\n		"
						+ xmlConsultaLinhas.get(14) + "\n		" + xmlConsultaLinhas.get(15) + "\n		" + xmlConsultaLinhas.get(16) + "\n		"
						+ xmlConsultaLinhas.get(17) + "\n	" + xmlConsultaLinhas.get(18) + "\n" + xmlConsultaLinhas.get(19));
				consultaLinhaProtNfe = Arrays.asList(protNfe.split("\n"));
				return consultaLinhaProtNfe;
			}else {
				return null;
			}

		} catch (IndexOutOfBoundsException | NfeException | FileNotFoundException
				| CertificadoException ioobeEneEfnfeEce) {
			System.err.println();
			System.err.println("Erro: " + ioobeEneEfnfeEce.getMessage());
		}
		return null;
	}

	public static ConfiguracoesNfe criaConfiguracoes() throws CertificadoException, FileNotFoundException {
		Certificado certificado = CertificadoService.certificadoPfx("/certivicado.pfx",
				"senha");
		return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.SC, AmbienteEnum.PRODUCAO, certificado,
				"/schemas");
	}//src/main/resources/schemas

	private static DocumentoEnum checarTipoNF(String chave) {
		List<String> chaveList = Arrays.asList(chave.split("(?!^)"));
		int estadoChave = Integer.parseInt(chaveList.get(20) + chaveList.get(21));
		try {
			if (estadoChave == 55) {
				return DocumentoEnum.NFE;
			} else if (estadoChave == 65) {
				return DocumentoEnum.NFCE;
			} else {
				return null;
			}
		} catch (NullPointerException npe) {
			JFrame jf=new JFrame();
			jf.setAlwaysOnTop(true);
			JOptionPane.showMessageDialog(jf, "Erro: Nota fiscal invalida\n" + npe.getMessage());
		}
		return null;
	}

	private static EstadosEnum checarEstadoNF(String chave) {
		List<String> chaveList = Arrays.asList(chave.split("(?!^)"));
		System.out.println(chave);
		int estadoChave = Integer.parseInt(chaveList.get(0) + chaveList.get(1));

		if (estadoChave == 11) {
			return EstadosEnum.RO;
		} else if (estadoChave == 12) {
			return EstadosEnum.AC;
		} else if (estadoChave == 13) {
			return EstadosEnum.AM;
		} else if (estadoChave == 14) {
			return EstadosEnum.RR;
		} else if (estadoChave == 15) {
			return EstadosEnum.PA;
		} else if (estadoChave == 16) {
			return EstadosEnum.AP;
		} else if (estadoChave == 17) {
			return EstadosEnum.TO;
		} else if (estadoChave == 21) {
			return EstadosEnum.MA;
		} else if (estadoChave == 22) {
			return EstadosEnum.PI;
		} else if (estadoChave == 23) {
			return EstadosEnum.CE;
		} else if (estadoChave == 24) {
			return EstadosEnum.RN;
		} else if (estadoChave == 25) {
			return EstadosEnum.PB;
		} else if (estadoChave == 26) {
			return EstadosEnum.PE;
		} else if (estadoChave == 27) {
			return EstadosEnum.AL;
		} else if (estadoChave == 28) {
			return EstadosEnum.SE;
		} else if (estadoChave == 29) {
			return EstadosEnum.BA;
		} else if (estadoChave == 31) {
			return EstadosEnum.MG;
		} else if (estadoChave == 32) {
			return EstadosEnum.ES;
		} else if (estadoChave == 33) {
			return EstadosEnum.RJ;
		} else if (estadoChave == 35) {
			return EstadosEnum.SP;
		} else if (estadoChave == 41) {
			return EstadosEnum.PR;
		} else if (estadoChave == 42) {
			return EstadosEnum.SC;
		} else if (estadoChave == 43) {
			return EstadosEnum.RS;
		} else if (estadoChave == 50) {
			return EstadosEnum.MS;
		} else if (estadoChave == 51) {
			return EstadosEnum.MT;
		} else if (estadoChave == 52) {
			return EstadosEnum.GO;
		} else if (estadoChave == 53) {
			return EstadosEnum.DF;
		} else {
			JFrame jf=new JFrame();
			jf.setAlwaysOnTop(true);
			JOptionPane.showMessageDialog(jf, "Erro: Estado invalido\nEncerrando aplicação...");
			System.exit(1);
			return null;
		}

	}

}
