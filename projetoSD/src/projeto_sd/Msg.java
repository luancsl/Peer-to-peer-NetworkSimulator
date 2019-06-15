package projeto_sd;

import peersim.core.*;

public class Msg extends Evento {
	
	
	protected Node remetente;
	
	public Msg() {
		
	}
	
	public Msg(int tipo, Node remetente) {
		super.tipo = tipo;
		this.remetente = remetente;
	}

	public Node getRemetente() {
		return remetente;
	}
	

}
