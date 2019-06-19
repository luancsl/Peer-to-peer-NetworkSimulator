package projeto_sd;

import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

import peersim.core.Node;

public class Capsula extends Msg{
	
	private int tipoResposta;
	private Object valor;
	
	public Capsula(int tipo, Node remetente, int tipoResposta, Object valor){
		super.tipo = tipo;
		super.remetente = remetente;
		Object copyValor = SerializationUtils.clone((Serializable) valor);
		this.valor = copyValor;
	}
	

	public int getTipoResposta() {
		return tipoResposta;
	}

	public Object getValor() {
		return valor;
	}
	
}
