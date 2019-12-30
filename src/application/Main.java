package application;

import gui.MainWindow;
import hook.HookController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import translator.TranslateController;
import javafx.scene.Scene;

// Translator
// Ahmert
// 10.05.2014

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			Platform.setImplicitExit(false);
			MainWindow root = new MainWindow(primaryStage);
			Scene scene = new Scene(root, 500, 300);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

			TranslateController translateController = new TranslateController();
			root.setTranslator(translateController);

			HookController hookController = new HookController();
			hookController.setGui(root);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
