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

public class VendeurGUI extends Application {
	protected VendeurAgent vendeurAgent;
	protected ObservableList<String> observableList=FXCollections.observableArrayList();
	protected AgentContainer agentContainer;
	
 public static void main(String[] args) {
	launch(args);
}
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Vendeur");
		startContainer();
		HBox hBox=new HBox(); 
		hBox.setPadding(new Insets(10));
		hBox.setSpacing(10);
		Label label = new Label();
		TextField textFieldAgent = new TextField();
		Button buttonDeploy = new Button("Deploy");
		buttonDeploy.setOnAction((evt)->{
		try {
			String name=textFieldAgent.getText();
			AgentController agentController = agentContainer.createNewAgent(name, "agents.VendeurAgent", new Object[] {this});
			agentController.start();
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
		});
		hBox.getChildren().addAll(label,textFieldAgent,buttonDeploy);
		BorderPane borderPane = new BorderPane();
		VBox vBox = new VBox();
		observableList=FXCollections.observableArrayList();
		ListView<String> listView=new ListView<String>(observableList);
		vBox.getChildren().add(listView);
		borderPane.setTop(hBox);
		borderPane.setCenter(vBox);
		Scene scene=new Scene(borderPane,480,480);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	private void startContainer() throws Exception {
		Runtime runtime=Runtime.instance();
		ProfileImpl profileImpl = new ProfileImpl();
		profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		agentContainer = runtime.createAgentContainer(profileImpl);
		agentContainer.start();
	}
	
	public void logMessage(ACLMessage aclMessage) {
		Platform.runLater(()->{
			observableList.add(aclMessage.getContent()+" "+aclMessage.getSender().getName());
			
					
		});

	}

}
