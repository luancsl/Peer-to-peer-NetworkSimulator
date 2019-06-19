package projeto_sd;

import peersim.core.*;

import java.util.ArrayList;

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

	private int recursos_proprio = 0;
	private int recursos_disponivel = 0;
	private int recursos_alocado = 0;

	private int pedidos_recursos = 0;

	private int disponibilidade = 0;
	private int indisponibilidade = 0;

	private Node master = null;
	private Node master_do_master = null;
	private int latencia = 0;
	private boolean isMaster = false;
	private boolean is2Master = false;

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

			};break;

			case EV_PING: {

			};break;
			
			case EV_ESCOLHA_SEU_MASTER: {

			};break;
			
			case EV_TORNE_SE_MASTER: {

			};break;
			
			case EV_TORNE_SE_2MASTER: {

			};break;
			
			case EV_COPIA_MASTER: {

			};break;
			
			case EV_ESCOLHER_MASTER: {
				Node remetente = ((Capsula) evento).getRemetente();
				
				this.setMaster(remetente);

			};break;
			
			case EV_RECURSO_PEDIDO: {

			};break;
			
			case EV_RECURSO_ENVIO: {

			};break;
			
			case LOOP_PRINCIPAL: {

			};break;
			
			case LOOP_COMSUMO: {

			};break;
			
			case LOOP_MASTER: {

			};break;
			
			case LOOP_2MASTER: {

			};break;
			
			
			
			}

		};break;
		
		case EV_ESTA_VIVO: {

		};break;

		case EV_PING: {

		};break;
		
		case EV_ESCOLHA_SEU_MASTER: {

		};break;
		
		case EV_TORNE_SE_MASTER: {
			
			this.setIsMaster(true);
			enviarMsg(1000, node, node, this.LOOP_MASTER, -1, null, pid);

		};break;
		
		case EV_TORNE_SE_2MASTER: {
			
			this.setIs2Master(true);
			enviarMsg(1000, node, node, this.LOOP_2MASTER, -1, null, pid);

		};break;
		
		case EV_COPIA_MASTER: {

		};break;
		
		case EV_ESCOLHER_MASTER: {
			Node remetente = ((Capsula) evento).getRemetente();
			
			this.getConhecidos().add(remetente);
			
			latencia = ((Transport)node.getProtocol(tid)).getLatency(node, remetente);
			enviarMsg(1000, node, remetente, this.EV_RESPOSTA, this.EV_ESCOLHER_MASTER, null, pid);
			
		};break;
		
		case EV_RECURSO_PEDIDO: {

		};break;
		
		case EV_RECURSO_ENVIO: {

		};break;
		
		case LOOP_PRINCIPAL: {

		};break;
		
		case LOOP_COMSUMO: {

		};break;
		
		case LOOP_MASTER: {
			
			if(this.isMaster()) {
				
				enviarMsg(1000, node, node, this.LOOP_MASTER, -1, null, pid);
			}

		};break;
		
		case LOOP_2MASTER: {
			
			if(this.is2Master()) {
				if(this.getMaster().isUp()) {
					
					SDProtocolo prot = (SDProtocolo)this.getMaster().getProtocol(pid);
					
					this.setConhecidos(prot.getConhecidos());
					this.setMaster_do_master(prot.getMaster());	
					
					System.out.println(String.format("Master primario %d copiado", this.getMaster().getID()));
					
				}else {
					
					System.err.println(String.format("Master primario %d está offline", this.getMaster().getID()));
					
					Network.swap(this.getMaster().getIndex(), node.getIndex());
					this.setMaster(this.getMaster_do_master());
					this.setMaster_do_master(null);
					this.setIs2Master(true);
					
					System.err.println(String.format("Master primario %d trocado pelo master secundario %d", this.getMaster().getID(), node.getID()));
					
				}
				
				enviarMsg(1000, node, node, this.LOOP_2MASTER, -1, null, pid);
			}
			
			
			
			

		};break;

		/*
		 * case EV_OK: {
		 * 
		 * int resultado = ((Capsula) evento).getValor();
		 * 
		 * if (resultado == -1) {
		 * System.err.println("Não foi possivel realizar a operacao!"); } else
		 * System.out.println("Nó " + node.getIndex() + " com " + this.result.toString()
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

	public void enviarMsg(long latencia, Node remetente, Node destinatario, int tipo, int tipoResposta, Object valor, int pid) {
		Object ev;
		ev = new Capsula(tipo, remetente, tipoResposta, valor);
		EDSimulator.add(latencia, ev, destinatario, pid);

		System.out.println(
				"DYN: Nó " + remetente.getIndex() + " operacao " + tipo + " para " + destinatario.getIndex() + "");
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
		this.conhecidos = conhecidos;
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

		this.isMaster = set;

		if (set == true) {
			this.isMaster = false;
		}
	}

}
