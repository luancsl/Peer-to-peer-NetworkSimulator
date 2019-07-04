package projeto_sd;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;

public class NetworkInitializer implements Control {
	
	private static final String PROT="protocolo";
	
	private static final String TRANSPORTE ="transporte";
	
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

	
	private final int pid;
	private final int tid;
	private Inicializar init;
	
	public NetworkInitializer(String prefixo) {
		this.pid = Configuration.getPid(prefixo+"."+PROT);
		this.tid = Configuration.getPid(prefixo+"."+TRANSPORTE);
		this.init = new Inicializar(prefixo);
	}
	
	public void enviarMsg(long latencia, Node remetente, Node destinatario, int tipo, int tipoResposta, Object valor, int pid) {
		Object ev;
		ev = new Capsula(tipo, remetente, tipoResposta, valor);
		EDSimulator.add(latencia, ev, destinatario, pid);

		System.out.println(
				"DYN: Node " + remetente.getIndex() + " operacao " + tipo + " para " + destinatario.getIndex() + "");
	}


	public boolean execute() {
		
		Node noInicial = Network.get(0);
		this.init.inicializar(noInicial);
		/*
		for(int i=0; i<Network.size(); i++){
			Node no = Network.get(i);
			this.init.inicializar(no);
			
		}*/
		//Node no = Network.get(0);
		//this.init.inicializar(no);
		
		// Adiciona 5 nós na rede
		for(int i=0; i<5; i++){
			
			
			Node novoNo = (Node)noInicial.clone();
			
			this.init.inicializar(novoNo);
			
			Network.add(novoNo);
			
			enviarMsg(1,novoNo, noInicial, this.EV_ESCOLHER_MASTER, -1, null, pid);
			enviarMsg(100,novoNo, novoNo, this.LOOP_PRINCIPAL, -1, null, pid);
			
		}
		
		
		/*
		 * for(int i = 1; i<Network.size(); i++) { Node no = Network.get(i); SDProtocolo
		 * prot = (SDProtocolo)no.getProtocol(pid);
		 * 
		 * }
		 */
		
		/*
		 * Node remetente = Network.get(3); Node destinatario = Network.get(2);
		 * 
		 * enviarMsg(1000, remetente, destinatario, this.EV_ESCOLHER_MASTER, -1, null,
		 * pid);;
		 * 
		 * remetente = Network.get(1); destinatario = Network.get(2);
		 * 
		 * enviarMsg(2000, remetente, destinatario, this.EV_ESCOLHER_MASTER, -1, null,
		 * pid);;
		 * 
		 * remetente = Network.get(2); destinatario = Network.get(3);
		 * 
		 * 
		 * enviarMsg(3000, remetente, destinatario, this.EV_TORNE_SE_2MASTER, -1, null,
		 * pid);;
		 */
		
		return true;
	}

}
