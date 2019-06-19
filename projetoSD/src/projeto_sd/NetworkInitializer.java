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
	
	private static final int LOOP_COMSUMO = 5;
	
	private final int pid;
	private final int tid;
	
	public NetworkInitializer(String prefixo) {
		this.pid = Configuration.getPid(prefixo+"."+PROT);
		this.tid = Configuration.getPid(prefixo+"."+TRANSPORTE);
	}
	
	public void enviarMsg(long latencia, Node remetente, Node destinatario, int tipo, int valor ) {
		Object ev;
		ev = new Capsula(tipo,remetente, -1, valor);
		EDSimulator.add(latencia,ev,destinatario,this.pid);
		
		System.out.println("DYN: Nó "+remetente.getIndex()+" operacao "+tipo+" para "+destinatario.getIndex()+"");
	}


	public boolean execute() {
		
		
		for(int i=1; i<Network.size(); i++){
			Node no = Network.get(i);
			SDProtocolo prot = (SDProtocolo)no.getProtocol(pid);
			
			prot.result.add(CommonState.r.nextInt(100));
			prot.result.add(CommonState.r.nextInt(100));
			
			prot.setLatencia(CommonState.r.nextInt(100));
			
			int recurso= CommonState.r.nextInt(900)+100;
			
			prot.setRecursos_proprio(recurso);
			prot.setRecursos_disponivel(recurso);
			
			System.err.println("Nó "+no.getIndex()+" inicializado com "+prot.result.toString()+"");
			
		}
		
		for(int i = 1; i<Network.size(); i++) {
			Node no = Network.get(i);
			SDProtocolo prot = (SDProtocolo)no.getProtocol(pid);
			
			enviarMsg(20000, no, no, this.LOOP_COMSUMO, -1);
			
		}
		
		return true;
	}

}
