package projeto_sd;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class NetworkInitializer implements Control {
	
	private static final String PROT="protocolo";
	
	private static final String TRANSPORTE ="transporte";
	
	private final int pid;
	private final int tid;
	
	public NetworkInitializer(String prefixo) {
		this.pid = Configuration.getPid(prefixo+"."+PROT);
		this.tid = Configuration.getPid(prefixo+"."+TRANSPORTE);
	}


	public boolean execute() {
		
		
		for(int i=1; i<Network.size(); i++){
			Node no = Network.get(i);
			SDProtocolo prot = (SDProtocolo)no.getProtocol(pid);
			
			prot.result.add(CommonState.r.nextInt(100));
			prot.result.add(CommonState.r.nextInt(100));
			
			System.err.println("Nó "+no.getIndex()+" inicializado com "+prot.result.toString()+"");
			
		}
		
		return true;
	}

}
