package projeto_sd;

import javax.xml.crypto.NodeSetData;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;

public class Observer implements Control{

	private static final int EV_RESULT = 1;
	private static final int EV_SOMA = 2;
	private static final int EV_DIVISAO = 3;
	private static final int EV_OK = 4;
	
	private static final String PROT = "protocolo";
	
	private static final String TRANSPORTE = "transporte";
	
	private final int pid;
	private final int tid;
	
	public Observer(String prefixo) {
		this.pid = Configuration.getPid(prefixo + "." + PROT);
		this.tid = Configuration.getPid(prefixo + "." + TRANSPORTE);
	}
	
	public void enviarMsg(Node remetente, Node destinatario, int tipo, int valor ) {
		Object ev;
		long latencia;
		
		ev = new Capsula(tipo,remetente, -1, valor);
		latencia = ((Transport)remetente.getProtocol(tid)).getLatency(remetente, destinatario);
		EDSimulator.add(latencia,ev,destinatario,pid);
		
		System.out.println("DYN: Nó "+remetente.getIndex()+" operacao Somar para "+destinatario.getIndex()+"");
	}
	

	public boolean execute() {
		
		Node n = Network.get(1);
		Node n2 = Network.get(2);
		
		//n.setFailState(n.DEAD);
		
		for(int i = 0; i < Network.size(); i++) {
			Node no = Network.get(i);
			SDProtocolo prot = (SDProtocolo)no.getProtocol(pid);
			
			System.out.println(String.format("Id do no: %d, isUp: %b, DOWN: %d, DEAD: %d, OK: %d", no.getIndex(), no.isUp(), no.DOWN, no.DEAD, no.OK));
			
			
			
			if(prot.getMaster() == null) {
				System.out.println(String.format("Master: %b, 2Master: %b, Latencia: %d\n"
						+ "Recursos_proprio: %d, Recursos_disponiveis: %d, Recursos_ocupado: %d, Pedidos_Recursos: %d \n"
						+ "Disponibilidade: %d, Indisponibilidade: %d ",
						prot.isMaster(), prot.is2Master(), prot.getLatencia(), prot.getRecursos_proprio(),
						prot.getRecursos_disponivel(), prot.getRecursos_alocado(), prot.getPedidos_recursos(),
						prot.getDisponibilidade(), prot.getIndisponibilidade()));
			}else {
				System.out.println(String.format("Nó Master: %d, Master: %b, 2Master: %b, Latencia: %d\n"
						+ "Recursos_proprio: %d, Recursos_disponiveis: %d, Recursos_ocupado: %d, Pedidos_Recursos: %d \n"
						+ "Disponibilidade: %d, Indisponibilidade: %d ",
						prot.getMaster().getIndex(), prot.isMaster(), prot.is2Master(), prot.getLatencia(), prot.getRecursos_proprio(),
						prot.getRecursos_disponivel(), prot.getRecursos_alocado(), prot.getPedidos_recursos(),
						prot.getDisponibilidade(), prot.getIndisponibilidade()));
			}
		
			
			
		}
		
		//System.out.println(String.format("Id do no: %d, isUp: %b, DOWN: %d, DEAD: %d, OK: %d", n.getID(), n.isUp(), n.DOWN, n.DEAD, n.OK));
		//n.setFailState(n.DOWN);
		//System.out.println(String.format("Id do no: %d, isUp: %b, DOWN: %d, DEAD: %d, OK: %d", n.getID(), n.isUp(), n.DOWN, n.DEAD, n.OK));
		//n.setFailState(n.OK);
		//System.out.println(String.format("Id do no: %d, isUp: %b, DOWN: %d, DEAD: %d, OK: %d", n.getID(), n.isUp(), n.DOWN, n.DEAD, n.OK));
		
		enviarMsg(n2, n, this.EV_SOMA, 0);
		
		return false;
	}

}
