package agents;

import agents.ConsumerAgent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
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

public class AcheteurGUI extends Application {
	protected AcheteurAgent acheteurAgent;
	protected ObservableList<String> observableList=FXCollections.observableArrayList();
 public static void main(String[] args) {
	launch(args);
}
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Acheteur");
		startContainer();
		BorderPane borderPane = new BorderPane();
		VBox vBox = new VBox();
		observableList=FXCollections.observableArrayList();
		ListView<String> listView=new ListView<String>(observableList);
		vBox.getChildren().add(listView);
		borderPane.setCenter(vBox);
		Scene scene=new Scene(borderPane,480,480);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	private void startContainer() throws Exception {
		Runtime runtime=Runtime.instance();
		ProfileImpl profileImpl = new ProfileImpl();
		profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		AgentContainer agentContainer = runtime.createAgentContainer(profileImpl);
		AgentController agentController = agentContainer
				.createNewAgent("ACHETEUR", "agents.AcheteurAgent", new Object[] {this});
		agentController.start();
	}
	
	public void logMessage(ACLMessage aclMessage) {
		Platform.runLater(()->{
			observableList.add(aclMessage.getContent()+" "+aclMessage.getSender().getName());
			
					
		});
	}

}
