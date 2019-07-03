package projeto_sd;

import peersim.core.Node;

public class IntMsg extends Msg{
	
	private int valor;
	
	public IntMsg(int tipo, Node remetente, int valor){
		super.tipo = tipo;
		super.remetente = remetente;
		this.valor = valor;
	}

	public int getValor() {
		return valor;
	}
	
}
