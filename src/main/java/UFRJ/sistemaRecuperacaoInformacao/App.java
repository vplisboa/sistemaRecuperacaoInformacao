package UFRJ.sistemaRecuperacaoInformacao;

import UFRJ.sistemaRecuperacaoInformacao.utils.FormataTexto;

public class App
{
	public static void main(String[] args)
	{
		String texto = "<DOCNO>FSP940101-001</DOCNO>\n" + 
				"<DOCID>FSP940101-001</DOCID>\n" + 
				"<DATE>940101</DATE>\n" + 
				"<TEXT>\n" + 
				"Pesquisa Datafolha feita nas dez principais capitais do país, após um ano de mandato, aponta o prefeito de Recife, Jarbas Vasconcelos (PMDB), como o mais popular (63% de ótimo e bom), seguido de Tarso Genro (PT), de Porto Alegre, com 55% de aprovação. Os prefeitos César Maia (PMDB), do Rio, com 50% de ruim e péssimo; Lídice da Mata (PSDB), de Salvador, com 48%, e Paulo Maluf (PPR), de São Paulo, com 41%, tiveram a pior avaliação. A PF vai investigar pagamentos da Prefeitura de São Paulo a construtoras citadas no caso Paubrasil. Brasil e Cotidiano\n" + 
				"</TEXT>\n" + 
				"</DOC>\n" + 
				"<DOC>\n" + 
				"<DOCNO>FSP940101-002</DOCNO>\n" + 
				"<DOCID>FSP940101-002</DOCID>\n" + 
				"<DATE>940101</DATE>\n" + 
				"<TEXT>\n" + 
				"Os quenianos dominaram a corrida de São Silvestre ontem. Simon Chemwoyo, 25, venceu a prova masculina pelo segundo ano consecutivo, num final confuso. Outros dois quenianos chegaram em terceiro e sexto lugares. A também queniana Hellen Kimaiyo, 25, venceu a prova feminina, disputada mais cedo. Fita Bayesa, 22, da Etiópia, liderava os últimos metros perseguido de perto por Chemwoyo. Na chegada, seguiram as motos dos batedores e entraram pelo corredor exclusivo para os veículos que acompanhavam a prova. Chemwoyo foi mais rápido ao voltar para o corredor de chegada e completou a prova com o tempo de 43min20. Chemwoyo, Bayesa e William Sigei (3.º) se revezaram na liderança o tempo todo. O mexicano Arturo Barrios, vencedor da prova em 90 e 91, chegou em quarto lugar. O brasileiro melhor colocado foi Ronaldo Costa, na sétimo posição. No feminino a brasileira Carmem Oliveira chegou a cinco segundos da vencedora. A portuguesa Albertina Dias foi a terceira. Pág. 5-1\n" + 
				"</TEXT>\n";
		
		
		
		System.out.println(FormataTexto.montarJson(texto).toString(2));
	}
}
