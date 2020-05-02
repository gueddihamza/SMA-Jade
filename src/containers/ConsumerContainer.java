package containers;

import agents.ConsumerAgent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ConsumerContainer extends Application{
	protected ObservableList<String> observablelist = FXCollections.observableArrayList();
	private ConsumerAgent consumeragent ;
	public static void main(String[] args) throws Exception {
		launch(args);
	}
	
	public void startContainer() throws Exception {
		Runtime runtime = Runtime.instance();
		ProfileImpl profileImpl = new ProfileImpl();
		profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		AgentContainer container = runtime.createAgentContainer(profileImpl);
		AgentController agentcontroller = container
				.createNewAgent("Consumer","agents.ConsumerAgent", new Object[] {this});
		container.start();
		agentcontroller.start();
	}

	public void setConsumeragent(ConsumerAgent consumeragent) {
		this.consumeragent = consumeragent;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Consumer");
		startContainer();
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(10));
		hbox.setSpacing(10);
		VBox vbox=new VBox(); vbox.setPadding(new Insets(10));
		observablelist = FXCollections.observableArrayList();
		ListView<String> listviewmessages = new ListView<String>(observablelist);
		vbox.getChildren().add(listviewmessages);
		Label label = new Label();
		TextField textfieldLivre = new TextField("Livre:");
		Button buttonAcheter = new Button("Acheter");
		hbox.getChildren().addAll(label , textfieldLivre,buttonAcheter);
		BorderPane borderpane = new BorderPane();
		borderpane.setTop(hbox);
		borderpane.setCenter(vbox);
		Scene scene = new Scene(borderpane,480,480);
		primaryStage.setScene(scene);
		primaryStage.show();
		buttonAcheter.setOnAction(evt->{
			String livre=textfieldLivre.getText();
			//observablelist.add(livre);	
			GuiEvent event = new GuiEvent(this,1);
			event.addParameter(livre);
			consumeragent.onGuiEvent(event);
		});
	}
	
	public void logMessage(ACLMessage aclMessage) {
		Platform.runLater(()->{
			observablelist.add(aclMessage.getContent()+" "+aclMessage.getSender().getName());
			
					
		});
		
		
	}
	
	
}
