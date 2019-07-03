package projeto_sd;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Node;

public class Inicializar {
	
	private static final String PROT = "protocolo";
	
	private final int pid;
	
	
	public Inicializar(String prefixo){
		pid = Configuration.getPid(prefixo+"."+PROT);
		
	}
	
	public void inicializar(Node no) {
		SDProtocolo prot = (SDProtocolo)no.getProtocol(pid);
		
		
		prot.setLatencia(CommonState.r.nextInt(100));
		
		int recurso= CommonState.r.nextInt(900)+100;
		
		prot.setRecursos_proprio(recurso);
		prot.setRecursos_disponivel(recurso);
		
		System.err.println("Nó "+no.getIndex()+" inicializado com "+prot.result.toString()+"");
		
	}
}
