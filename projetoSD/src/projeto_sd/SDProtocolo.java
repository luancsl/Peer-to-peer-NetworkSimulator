package projeto_sd;

import peersim.core.*;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.SerializationUtils;

import peersim.config.*;
import peersim.edsim.*;
import peersim.transport.*;

public class SDProtocolo implements EDProtocol {

	public static final String TRANSPORTE = "transporte";

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

	int tid;

	ArrayList<Integer> result;

	ArrayList<Node> conhecidos;

	private int recursos_proprio = 0; // quantidade total de recurso do nó 
	private int recursos_disponivel = 0; //recurso para ser compartilhado
	private int recursos_alocado = 0; // recurso utilizado pelo nó

	private int pedidos_recursos = 0;

	private int disponibilidade = 0;
	private int indisponibilidade = 0;

	private Node master = null;
	private Node master_do_master = null;
	private int latencia = 0;
	private boolean isMaster = false;
	private boolean is2Master = false;

	// porcentagem do recurso 
	private int percentual_recurso = 10;
	
	public SDProtocolo(String prefixo) {
		this.tid = Configuration.getPid(prefixo + "." + TRANSPORTE);

	}

	public void processEvent(Node node, int pid, Object evento) {

		Object ev;
		long latencia;
		
		
		switch (((Capsula) evento).getTipo()) {

		case EV_RESPOSTA: {

			switch (((Capsula) evento).getTipoResposta()) {

			case EV_ESTA_VIVO: {

			}
				;
				break;

			case EV_PING: {

			}
				;
				break;

			case EV_ESCOLHA_SEU_MASTER: {

			}
				;
				break;

			case EV_TORNE_SE_MASTER: {

			}
				;
				break;

			case EV_TORNE_SE_2MASTER: {

			}
				;
				break;

			case EV_COPIA_MASTER: {

			}
				;
				break;

			case EV_ESCOLHER_MASTER: {
				Node remetente = ((Capsula) evento).getRemetente();

				this.setMaster(remetente);

			}
				;
				break;

			case EV_RECURSO_PEDIDO: {
				
				// Envia recurso para o nó que solicitou
				int recursoSolicitado = (int)((Capsula) evento).getValor();
				if(recursoSolicitado != -1) {
					this.recursos_alocado += recursoSolicitado;
				}
			}
				;
				break;

			case EV_RECURSO_ENVIO: {
					
					
			}
				;
				break;

			case LOOP_PRINCIPAL: {

			}
				;
				break;

			case LOOP_COMSUMO: {

			}
				;
				break;

			case LOOP_MASTER: {

			}
				;
				break;

			case LOOP_2MASTER: {

			}
				;
				break;

			}

		}
			;
			break;

		case EV_ESTA_VIVO: {

		}
			;
			break;

		case EV_PING: {

		}
			;
			break;

		case EV_ESCOLHA_SEU_MASTER: {

		}
			;
			break;

		case EV_TORNE_SE_MASTER: {

			this.setIsMaster(true);
			enviarMsg(10, node, node, this.LOOP_MASTER, -1, null, pid);

		}
			;
			break;

		case EV_TORNE_SE_2MASTER: {
			Node remetente = ((Capsula) evento).getRemetente();

			this.setMaster(remetente);

			this.setIs2Master(true);
			enviarMsg(10, node, node, this.LOOP_2MASTER, -1, null, pid);

		}
			;
			break;

		case EV_COPIA_MASTER: {

		}
			;
			break;

		case EV_ESCOLHER_MASTER: {
			Node remetente = ((Capsula) evento).getRemetente();

			this.getConhecidos().add(remetente);

			this.setIsMaster(true);

			latencia = ((Transport) node.getProtocol(tid)).getLatency(node, remetente);
			enviarMsg(10, node, remetente, this.EV_RESPOSTA, this.EV_ESCOLHER_MASTER, null, pid);

		}
			;
			break;

		case EV_RECURSO_PEDIDO: {
			
			// Cedendo Recurso do nó master para um nó que solicitou o recurso
			Node remetente = ((Capsula) evento).getRemetente(); // Nó que solicitou o recurso
			int recursoSolicitado = (int) ((Capsula) evento).getValor();
			
			System.err.println("recurso disponivel no no" + node.getID() + ": " + this.recursos_disponivel);
			
			if(this.recursos_disponivel >= recursoSolicitado) {
				this.recursos_disponivel -= recursoSolicitado;
				enviarMsg(10, node, remetente, this.EV_RESPOSTA, this.EV_RECURSO_PEDIDO, recursoSolicitado, pid);
				System.out.println("no: " + remetente.getID() + ", solicitou: " + recursoSolicitado + " do no " + node.getID());
			}
			else {
				enviarMsg(10, node, remetente, this.EV_RESPOSTA, this.EV_RECURSO_PEDIDO, -1, pid);
				System.err.println("nao tem recurso");
			}
			
			System.err.println("recurso disponivel no no" + node.getID() + ": " + this.recursos_disponivel);
		}
			;
			break;

		case EV_RECURSO_ENVIO: { // Nós enviam recurso para o master
			Node remetente = ((Capsula) evento).getRemetente();
			int recurso_disponivel = (int)((Capsula) evento).getValor();
			
			if(this.isMaster()) {
				this.setRecursos_disponivel(this.getRecursos_disponivel() + recurso_disponivel);
				System.err.println("# NO "+ remetente.getID() +" CEDEU "+ recurso_disponivel + " AO MASTER #");
				
			}
			//System.err.println("RECURSO DISPONÍVEL => "+ recurso_disponivel);
		}
			;
			break;

		case LOOP_PRINCIPAL: { 
			// Nó envia pedido de recurso 
			int recursoSolicitado = CommonState.r.nextInt(100);
			

			enviarMsg(10, node ,this.getMaster(),this.EV_RECURSO_PEDIDO,-1, recursoSolicitado,pid);//pede recurso ao master
			System.out.println("##PEDE RECURSO AO MASTER!##");
			
			
			
			
			
			// nó disponibiliza recurso para o master
			if(this.recursos_disponivel > 0) {
			
				this.recursos_disponivel = this.recursos_proprio*this.percentual_recurso/100;
				enviarMsg(5, node, this.getMaster(), this.EV_RECURSO_ENVIO, -1, this.recursos_disponivel, pid);
				
			}
			
			
			// chama o LOOP_PRINCIPAL em um tempoAleatorio
			int tempoAleatorio = CommonState.r.nextInt(10000);
			enviarMsg(tempoAleatorio, node, node, this.LOOP_PRINCIPAL, -1, null, pid);
		}
			;
			break;

		case LOOP_COMSUMO: {

		}
			;
			break;

		case LOOP_MASTER: {

			if (this.isMaster()) {

				System.out.println(String.format("Nó %d eu sou master!!!", node.getID()));
				enviarMsg(1000, node, node, this.LOOP_MASTER, -1, null, pid);
			}

		}
			;
			break;

		case LOOP_2MASTER: {

			if (this.is2Master()) {
				if (this.getMaster().isUp()) {

					SDProtocolo prot = (SDProtocolo) this.getMaster().getProtocol(pid);

					this.setConhecidos(prot.getConhecidos());
					this.setMaster_do_master(prot.getMaster());
					this.setRecursos_disponivel(2);

					System.out.println(String.format("Master primario %d copiado", this.getMaster().getID()));

				} else {

					
					  System.err.println(String.format("Master primario %d estó offline",
					  this.getMaster().getID()));
					  
					  System.err.println(String.
					  format("Master primario %d trocado pelo master secundario %d",
					  this.getMaster().getID(), node.getID()));
					  
					  Network.swap(this.getMaster().getIndex(), node.getIndex());
					  
					  
					  this.setMaster(this.getMaster_do_master()); this.setMaster_do_master(null);
					  this.setIsMaster(true);
					  
					  
					  
					  
					  
					  enviarMsg(10, node, node, this.LOOP_MASTER, -1, null, pid);
					 
				}

				enviarMsg(1000, node, node, this.LOOP_2MASTER, -1, null, pid);
			}

		}
			;
			break;

		/*
		 * case EV_OK: {
		 * 
		 * int resultado = ((Capsula) evento).getValor();
		 * 
		 * if (resultado == -1) {
		 * System.err.println("N�o foi possivel realizar a operacao!"); } else
		 * System.out.println("N� " + node.getIndex() + " com " + this.result.toString()
		 * + ""); System.out.println("Operacao " + resultado +
		 * " realizada com sucesso!");
		 * 
		 * };break;
		 * 
		 * 
		 * case EV_RESULT: { Node remetente = ((Capsula) evento).getRemetente(); int
		 * resultado = ((Capsula) evento).getValor(); result.add(resultado);
		 * 
		 * ev = new Capsula(EV_OK, node, EV_RESULT); latencia = ((Transport)
		 * node.getProtocol(tid)).getLatency(node, remetente); EDSimulator.add(latencia,
		 * ev, remetente, pid);
		 * 
		 * };break;
		 * 
		 * case LOOP_COMSUMO: {
		 * 
		 * this.recursos_alocar(CommonState.r.nextInt(100));
		 * 
		 * this.enviarMsg(CommonState.r.nextInt(10000) + 20000, node, node,
		 * LOOP_COMSUMO, 0, pid);
		 * 
		 * };break;
		 */

		}

	}

	public Object clone() {
		Object prot = null;
		try {
			prot = (SDProtocolo) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		;

		((SDProtocolo) prot).result = new ArrayList();
		((SDProtocolo) prot).conhecidos = new ArrayList();

		return prot;
	}

	public void enviarMsg(long latencia, Node remetente, Node destinatario, int tipo, int tipoResposta, Object valor,
			int pid) {
		Object ev;
		ev = new Capsula(tipo, remetente, tipoResposta, valor);
		EDSimulator.add(latencia, ev, destinatario, pid);

		//System.out.println(
		//		"DYN: Nó " + remetente.getIndex() + " operacao " + tipo + " para " + destinatario.getIndex() + "");
	}

	public Node getMaster_do_master() {
		return master_do_master;
	}

	public void setMaster_do_master(Node master_do_master) {
		this.master_do_master = master_do_master;
	}

	public ArrayList<Node> getConhecidos() {

		return conhecidos;
	}

	public void setConhecidos(ArrayList<Node> conhecidos) {

		ArrayList<Node> clone = new ArrayList<Node>();

		for (Node item : conhecidos) {
			clone.add(item);
		}

		this.conhecidos = clone;
	}

	public int getRecursos_proprio() {
		return recursos_proprio;
	}

	public void setRecursos_proprio(int recursos_proprio) {
		this.recursos_proprio = recursos_proprio;
	}

	public int getRecursos_disponivel() {
		return recursos_disponivel;
	}

	public void setRecursos_disponivel(int recursos_disponivel) {
		this.recursos_disponivel = recursos_disponivel;
	}

	public int getRecursos_alocado() {
		return recursos_alocado;
	}

	public boolean setRecursos_alocado(int recursos_alocado) {
		if ((this.recursos_disponivel - recursos_alocado) > 0) {

			this.recursos_alocado = recursos_alocado;
			this.recursos_disponivel -= recursos_alocado;
			return true;
		}

		return false;

	}

	public void recursos_desalocar(int recurso) {
		if ((this.recursos_alocado - recurso) < 0) {
			this.recursos_disponivel += this.recursos_alocado;
			this.recursos_alocado = 0;
		}
		this.recursos_alocado -= recurso;
		this.recursos_disponivel += recurso;
	}

	public boolean recursos_alocar(int recurso) {
		if ((this.recursos_disponivel - recurso) > 0) {
			this.recursos_disponivel -= recurso;
			this.recursos_alocado += recurso;
			return true;
		}

		return false;
	}

	public int getPedidos_recursos() {
		return pedidos_recursos;
	}

	public void setPedidos_recursos(int pedidos_recursos) {
		this.pedidos_recursos = pedidos_recursos;
	}

	public int getDisponibilidade() {
		return disponibilidade;
	}

	public void setDisponibilidade(int disponibilidade) {
		this.disponibilidade = disponibilidade;
	}

	public int getIndisponibilidade() {
		return indisponibilidade;
	}

	public void setIndisponibilidade(int indisponibilidade) {
		this.indisponibilidade = indisponibilidade;
	}

	public int getLatencia() {
		return latencia;
	}

	public void setLatencia(int latencia) {
		this.latencia = latencia;
	}

	public Node getMaster() {
		return this.master;
	}

	public void setMaster(Node set) {
		this.master = set;
	}

	public boolean isMaster() {
		return this.isMaster;
	}

	public boolean is2Master() {
		return this.is2Master;
	}

	public void setIsMaster(boolean set) {

		this.isMaster = set;

		if (set == true) {
			this.is2Master = false;
		}
	}

	public void setIs2Master(boolean set) {

		this.is2Master = set;

		if (set == true) {
			this.isMaster = false;
		}
	}
	
	

}
