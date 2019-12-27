package projeto_sd;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;

public class NetworkDynamicsTeste implements Control {
	
	private static final int EV_RESPOSTA = 0;
	private static final int EV_ESTA_VIVO = 1;
	private static final int EV_PING = 2;
	
	private static final int EV_ESCOLHA_SEU_MASTER = 3;
	private static final int EV_TORNE_SE_MASTER = 4;
	private static final int EV_TORNE_SE_2MASTER = 5;
	private static final int EV_COPIA_MASTER = 6;
	private static final int EV_ESCOLHER_MASTER = 7;

	private static final int EV_RECURSO_PEDIDO = 8;
	private static final int EV_RECURSO_ENVIO = 9;

	private static final int LOOP_PRINCIPAL = 10;
	private static final int LOOP_COMSUMO = 11;
	private static final int LOOP_MASTER = 12;
	private static final int LOOP_2MASTER = 13;

	
	private static final String PROT = "protocolo";
	
	private static final String TRANSPORTE = "transporte";
	
	private final int pid;
	private final int tid;
	private Inicializar init;
	
	public NetworkDynamicsTeste(String prefixo) {
		this.pid = Configuration.getPid(prefixo + "." + PROT);
		this.tid = Configuration.getPid(prefixo + "." + TRANSPORTE);
		
		this.init = new Inicializar("init.net");
	}
	
	
	public void enviarMsg(long latencia, Node remetente, Node destinatario, int tipo, int tipoResposta, Object valor, int pid) {
		Object ev;
		ev = new Capsula(tipo, remetente, tipoResposta, valor);
		EDSimulator.add(latencia, ev, destinatario, pid);

		System.out.println(
				"DYN: Nó " + remetente.getIndex() + " operacao " + tipo + " para " + destinatario.getIndex() + "");
	}
	
	public boolean execute() {
		
		
		
		
		
		
		return false;
	}

}
