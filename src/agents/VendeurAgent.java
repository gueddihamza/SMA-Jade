package agents;

import java.util.Random;

import jade.core.AgentDescriptor;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class VendeurAgent  extends GuiAgent{
	protected VendeurGUI gui;
@Override
protected void setup() {
	if(getArguments().length==1) {
			gui=(VendeurGUI) getArguments()[0];
			gui.vendeurAgent=this;
		}
ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
addBehaviour(parallelBehaviour);
parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
	 
	@Override
	public void action() {
		 DFAgentDescription dfAgentDescription= new DFAgentDescription();
		 dfAgentDescription.setName(getAID());
		 ServiceDescription serviceDescription=new ServiceDescription();
		 serviceDescription.setType("transaction");
		 serviceDescription.setName("vente-livres");		 
		 dfAgentDescription.addServices(serviceDescription);
		 try {
			DFService.register(myAgent, dfAgentDescription);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
});
parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {

@Override
public void action() {
ACLMessage aclMessage=receive();
if(aclMessage!=null) {
	String livre=aclMessage.getContent();
	gui.logMessage(aclMessage);
	switch(aclMessage.getPerformative()) {
	
	case ACLMessage.CFP:
		
		ACLMessage reply=aclMessage.createReply();
		reply.setContent(String.valueOf(500 + new Random().nextInt(1000)));
		reply.setPerformative(ACLMessage.PROPOSE);
		send(reply);
		break;
		
	case ACLMessage.ACCEPT_PROPOSAL:
		ACLMessage replyproposal = aclMessage.createReply();
		replyproposal.setPerformative(ACLMessage.AGREE);
		replyproposal.setContent(aclMessage.getContent());
		send(replyproposal);
		break;
		
		default:
			break;
	
	}

}
else block();
}
});

}
	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}

}
