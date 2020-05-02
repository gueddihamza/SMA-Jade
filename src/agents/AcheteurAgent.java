package agents;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AcheteurAgent extends GuiAgent {
	protected AcheteurGUI gui;
	protected AID[]vendeurs;
 @Override
 	protected void setup() {
	 		if(getArguments().length==1) {
	 			gui=(AcheteurGUI) getArguments()[0];
	 			gui.acheteurAgent=this;
	 		}
ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
addBehaviour(parallelBehaviour);
parallelBehaviour.addSubBehaviour(new TickerBehaviour(this,5000) {
	
	@Override
	protected void onTick() {
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setName("vente-livres");
			sd.setType("transaction");
			template.addServices(sd);
			try {
			DFAgentDescription[] results =	DFService.search(myAgent, template);
			vendeurs = new AID[results.length];
			for(int i=0 ; i<vendeurs.length;i++) {
				vendeurs[i] = results[i].getName();
				
				
			}
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
});
parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
	private int counter = 0;
	private List<ACLMessage> aclMessages = new ArrayList<ACLMessage>();
	@Override
	public void action() {
		MessageTemplate messageTemplate = MessageTemplate.or(MessageTemplate
				.or(MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
				MessageTemplate.MatchPerformative(ACLMessage.AGREE)), 
				MessageTemplate.MatchPerformative(ACLMessage.REFUSE)), 
				MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
		ACLMessage aclMessage=receive(messageTemplate);
		
		if(aclMessage!=null) {
			switch(aclMessage.getPerformative()) {
			
			case ACLMessage.REQUEST: 
				String livre = aclMessage.getContent();
				ACLMessage aclMessage2 = new ACLMessage(ACLMessage.CFP);
				aclMessage2.setContent(livre);
				for(AID aid:vendeurs) {
					aclMessage2.addReceiver(aid);
					}
				send(aclMessage2);
				break;
				
				
			case ACLMessage.PROPOSE:
				aclMessages.add(aclMessage);
				
				if(vendeurs.length==aclMessages.size()) {
					ACLMessage bestoffer =aclMessages.get(0);
					double min = Double.parseDouble(bestoffer.getContent());
					for(ACLMessage message:aclMessages) {
						if(Double.parseDouble(message.getContent())<min) {
							bestoffer = message;
						   min=Double.parseDouble(bestoffer.getContent());
					}
					}
				   
					ACLMessage acceptproposal = bestoffer.createReply();
					acceptproposal.setContent(bestoffer.getContent());
					acceptproposal.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
					send(acceptproposal);
					acceptproposal=null;
					aclMessages = new ArrayList<ACLMessage>();
				}
					
				
				break;
			case ACLMessage.AGREE: 
				ACLMessage confirm = new ACLMessage(ACLMessage.CONFIRM);
				confirm.addReceiver(new AID("Consumer",AID.ISLOCALNAME));
				confirm.setContent(aclMessage.getContent());
				send(confirm);
				
				break;
			case ACLMessage.REFUSE: 
				
				break;
			
			default:
				break;
				}
			gui.logMessage(aclMessage);
		}
		else block();
	}
});
 }
	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		}
}
