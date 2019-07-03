package projeto_sd;


/**
 * Classe de tipo de evento que ser� tratado
 * @author Luan Lins
 *
 */
public class Evento {
	
	/**
	 * O identificador do evento
	 * <p>
	 * Os identificadores s�o do tipo:<br/>
	 * <ul>
	 *   <li> 1 soma </li>
	 *   <li> 2 divisao </li>
	 * </ul></p>
	 */
	protected int tipo;
	
	public Evento() {
	}
	
	public Evento(int tipo) {
		this.tipo = tipo;
	}

	public int getTipo() {
		return tipo;
	}
	
	

}
