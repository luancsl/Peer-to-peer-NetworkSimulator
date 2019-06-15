package projeto_sd;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;

public class NetworkDynamics implements Control {
	
	private static final int EV_RESULT = 1;
	private static final int EV_SOMA = 2;
	private static final int EV_DIVISAO = 3;
	private static final int EV_OK = 4;
	
	private static final String PROT = "protocolo";
	
	private static final String TRANSPORTE = "transporte";
	
	private final int pid;
	private final int tid;
	
	public NetworkDynamics(String prefixo) {
		this.pid = Configuration.getPid(prefixo + "." + PROT);
		this.tid = Configuration.getPid(prefixo + "." + TRANSPORTE);
	}
	
	
	public void enviarMsg(Node remetente, Node destinatario, int tipo, int valor ) {
		Object ev;
		long latencia;
		
		ev = new IntMsg(tipo,remetente,valor);
		latencia = ((Transport)remetente.getProtocol(tid)).getLatency(remetente, destinatario);
		EDSimulator.add(latencia,ev,destinatario,pid);
		
		System.out.println("DYN: Nó "+remetente.getIndex()+" operacao Somar para "+destinatario.getIndex()+"");
	}

	
	public boolean execute() {
		
		Node remetente = Network.get(1);
		Node destinatario = Network.get(2);
		
		enviarMsg(remetente, destinatario, this.EV_SOMA, 0);
		
		enviarMsg(destinatario, remetente , this.EV_SOMA, 0);
		
		
		
		
		return false;
	}

}
