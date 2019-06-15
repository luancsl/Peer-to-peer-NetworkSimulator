package projeto_sd;

import peersim.core.*;

import java.util.ArrayList;

import peersim.config.*;
import peersim.edsim.*;
import peersim.transport.*;

public class SDProtocolo implements EDProtocol{

	
	public static final String TRANSPORTE = "transporte"; 
	
	private static final int EV_RESULT = 1;
	private static final int EV_SOMA = 2;
	private static final int EV_DIVISAO = 3;
	private static final int EV_OK = 4;
	
	
	ArrayList<Integer> result;
	
	int tid;
	
	public SDProtocolo (String prefixo) {
		this.tid = Configuration.getPid(prefixo+"."+TRANSPORTE);
		
	}
	
	public void processEvent(Node node, int pid, Object evento) {
		
		Object ev;
		long latencia;
		
		switch(((Evento)evento).getTipo()) {
			
			case EV_OK:{
				
				int resultado =  ((IntMsg)evento).getValor();
				
				if(resultado == -1) {
					System.err.println("Não foi possivel realizar a operacao!");
				}else
					System.out.println("Nó "+node.getIndex()+" com "+this.result.toString()+"");
					System.out.println("Operacao "+resultado+" realizada com sucesso!");
				
			};break;
		
			case EV_RESULT:{
				Node remetente = ((IntMsg)evento).getRemetente();
				int resultado =  ((IntMsg)evento).getValor();
				result.add(resultado);
				
				ev = new IntMsg(EV_OK,node,EV_RESULT);
				latencia = ((Transport)node.getProtocol(tid)).getLatency(node, remetente);
				EDSimulator.add(latencia,ev,remetente,pid);
				
			};break;
		
			case EV_SOMA:{
				

				Node remetente = ((IntMsg)evento).getRemetente();
				
				int soma = 0;
				for(int item: result) {
					soma += item;
				}
				
				ev = new IntMsg(EV_OK,node,EV_SOMA);
				latencia = ((Transport)node.getProtocol(tid)).getLatency(node, remetente);
				EDSimulator.add(latencia,ev,remetente,pid);
				
				ev = new IntMsg(EV_RESULT,node,soma);
				latencia = ((Transport)node.getProtocol(tid)).getLatency(node, remetente);
				EDSimulator.add(latencia,ev,remetente,pid);
				
				
				
			};break;
			
			case EV_DIVISAO:{
				
			};break;
		
		}
		
	}
	
	public Object clone(){
		Object prot = null;
		try{
			prot = (SDProtocolo)super.clone();
		}
		catch(CloneNotSupportedException e){};
		
		((SDProtocolo)prot).result = new ArrayList();
		
		return prot;
	}
		

}
