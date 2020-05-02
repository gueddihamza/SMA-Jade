package agents;

import containers.ConsumerContainer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class ConsumerAgent extends GuiAgent {
	private int count = 0;
	private transient ConsumerContainer gui;
@Override
	protected void setup() {
		if(getArguments().length==1) {
		gui = (ConsumerContainer) getArguments()[0];
		gui.setConsumeragent(this);
		}
		System.out.println("**********************");
		System.out.println("Agent Initialisation"+this.getAID().getName());
		if(this.getArguments().length==1)
		System.out.println("Je vais tenter d'acheter le livre "+getArguments()[0]);
		ParallelBehaviour parallelbehaviour = new ParallelBehaviour();
		addBehaviour(parallelbehaviour);
		parallelbehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				ACLMessage aclmessage = receive();
				if(aclmessage!=null) {
					switch (aclmessage.getPerformative()) {
					case ACLMessage.CONFIRM:
						gui.logMessage(aclmessage);
					
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
		protected void beforeMove() {
				System.out.println("********************");
				System.out.println("Avant migration");
				System.out.println("********************");
		}
	
		@Override
			protected void afterMove() {
			System.out.println("********************");
			System.out.println("Apres migration");
			System.out.println("********************");

			}
		
		@Override
			protected void takeDown() {
			System.out.println("********************");
			System.out.println("I'm going to die");
			System.out.println("********************");

			}
		@Override
		public void onGuiEvent(GuiEvent params) {
	if(params.getType()==1)			{
	String livre = params.getParameter(0).toString();
	ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
	aclMessage.setContent(livre);
	aclMessage.addReceiver(new AID("ACHETEUR",AID.ISLOCALNAME));
	send(aclMessage);
}
		}
}
