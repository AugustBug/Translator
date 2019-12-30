package gui;

import java.util.ArrayList;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import translator.TranslateController;

// Main Window and pop up window of the application
public class MainWindow extends VBox{

	private Label lblTest;
	private Stage parentStage;
	private TranslateController translator;
	private Clipboard clipboard;
	private int cursorX, cursorY;

	class Delta { double x, y; }

	public MainWindow(Stage parentStage) {
		this.parentStage = parentStage;
		this.clipboard = Clipboard.getSystemClipboard();

		this.setAlignment(Pos.TOP_CENTER);
		this.setPadding(new Insets(20, 10, 10, 10));

		parentStage.setTitle("EN-TR Çevirici");
		parentStage.setResizable(false);

		Label lblInfo = new Label("İngilizceden çevirmek istediğiniz yazıyı(genellikle kelime) seçtikten sonra program kısayolunu tuşlayarak "
				+ "yazının Türkçesini görüntüleyebilirsiniz. Yine isterseniz çevirisi yapılan yazıyı ilgili kısayolu kullanarak dinleyebilirsiniz."
				+ " Bu pencereyi kapattıktan sonra program arkaplanda çalışmaya devam eder. Programdan çıkmak için ilgili kısayolu kullanabilirsiniz."
				+ " Çeviri hizmeti www.tureng.com sitesi üzerinden sağlanmaktadır. \n\n"
				+ "\tÇeviri : CTRL + MOUSE MID\n\tÇeviri : CTRL + Ç\n\tSeslendirme(İngiliz İngilizcesi) : CTRL + Ş\n\tSeslendirme(Amerikan İngilizcesi) : CTRL + İ\n"
				+ "\tAçılır Pencereyi Kapatma : ESC\n\tProgramdan Çıkış: CTRL + Ü\n");
		lblInfo.setWrapText(true);
		lblInfo.setStyle("-fx-text-fill: #333537; -fx-font-weight: bold;");
		lblInfo.setTextAlignment(TextAlignment.JUSTIFY);

		lblTest = new Label();
		lblTest.setWrapText(true);

		HBox boxManual = new HBox();
		boxManual.setSpacing(10);
		boxManual.setPrefWidth(10000);
		TextField txtQry = new TextField();
		txtQry.setPrefWidth(10000);
		txtQry.setPromptText("Ýngilizce Kelime");
		Button btnRun = new Button(">>");
		btnRun.setMinWidth(40);
		btnRun.setMaxWidth(40);
		btnRun.setStyle("-fx-font-weight: bold;");
		Label lblRes = new Label("");
		lblRes.setPrefWidth(10000);
		lblRes.setStyle("-fx-font-weight: bold;");
		HBox boxDummy = new HBox();
		boxDummy.setPrefWidth(10000);
		boxManual.getChildren().addAll(txtQry, btnRun, lblRes, boxDummy);

		btnRun.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ArrayList<String> res = translator.translate(txtQry.getText());
				if(res.size() > 0) {
					lblRes.setText(res.get(0));
					lblRes.setStyle("-fx-font-style: normal;");
				} else {
					lblRes.setText("BULUNAMADI!");
					lblRes.setStyle("-fx-font-style: italic;");
				}
			}
		});

		this.getChildren().add(lblInfo);
		this.getChildren().add(lblTest);
		this.getChildren().add(boxManual);
	}

	public void setTranslator(TranslateController translator) {
		this.translator = translator;
	}

	public void addTestStr(String str) {
		lblTest.setText(lblTest.getText() + str + " ; ");
	}

	public void exitApp() {
		Platform.exit();
	}

	// Creates a pop up stage for the translation results
	// pop up could be closed by pressing ESC
	private void createPop(String qry, ArrayList<String> rst) {
		final Stage dialog = createTemplateDialog();

		VBox boxMain = createTemplateBox(dialog);

		Label lblSrc = new Label("Şunun için arama yapıldı:   " + qry);
		lblSrc.setStyle("-fx-font-style: italic; -fx-text-fill: #565656; -fx-underline: true;");
		Label lblDst = new Label(rst.get(0));
		lblDst.setStyle("-fx-font-weight: bold; -fx-font-size: 14pt; -fx-font-style: italic;");
		lblDst.setEffect(new DropShadow(2, Color.BLACK));
		ListView<String> lvMeans = new ListView<>();
		lvMeans.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt; -fx-text-alignment: center;");
		for(int i=1; i<rst.size(); i++) {
			lvMeans.getItems().add(rst.get(i));
		}
		lvMeans.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ESCAPE) {
					dialog.close();
				}
			}
		});
		boxMain.getChildren().addAll(lblSrc, lblDst, lvMeans);
		Scene scene = new Scene(boxMain, 300, 200);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ESCAPE) {
					dialog.close();
				}
			}
		});

		dialog.setScene(scene);
		dialog.show();
	}

	private void createPop(String qry, String rst) {
		final Stage dialog = createTemplateDialog();

		VBox boxMain = createTemplateBox(dialog);

		Label lblSrc = new Label(":unun için arama yapıldı:   " + qry);
		lblSrc.setStyle("-fx-font-style: italic; -fx-text-fill: #565656; -fx-underline: true;");
		Label lblDst = new Label(rst);
		lblDst.setStyle("-fx-font-weight: bold; -fx-font-size: 14pt; -fx-font-style: italic;");
		lblDst.setEffect(new DropShadow(2, Color.BLACK));
		boxMain.getChildren().addAll(lblSrc, lblDst);
		Scene scene = new Scene(boxMain, 300, 200);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				System.out.println("pressed");
				if(event.getCode() == KeyCode.ESCAPE) {
					dialog.close();
				}
			}
		});

		dialog.setScene(scene);
		dialog.show();
	}

	public void translateClicked() {

		try {
			Thread.sleep(50);
		} catch (Exception e) {

		}

		if(clipboard.hasString()) {
			ArrayList<String> res = translator.translate(clipboard.getString().toLowerCase());

			if(res.size() > 0) {
				createPop(clipboard.getString(), res);
			} else {
				createPop(clipboard.getString(), "Karşılık Bulunamadı!");
			}

		} else {

			createPop("HATA!", "Kopyalanmış Yazı Yok");
		}
	}

	public void speakQueryUkClicked() {
		translator.speakEnglishUk();
	}

	public void speakQueryUsClicked() {
		translator.speakEnglishUs();
	}

	// application termination animation
	public void createLastPopup() {
		final Stage dialog = createTemplateDialog();

		VBox boxMain = createTemplateBox(dialog);
		boxMain.setStyle("-fx-background-color: #515451;");

		Label lblSrc = new Label("GÜLE GÜLE");
		lblSrc.setStyle("-fx-font-weight: bold; -fx-font-size: 16pt; -fx-text-fill: #DEDEDE;");
		lblSrc.setEffect(new DropShadow(5, Color.MAROON));
		lblSrc.setPrefHeight(10000);
		lblSrc.setTextAlignment(TextAlignment.CENTER);
		boxMain.getChildren().addAll(lblSrc);
		Scene scene = new Scene(boxMain, 300, 200);
		dialog.setScene(scene);
		dialog.show();

		ScaleTransition st = new ScaleTransition(Duration.millis(1000), lblSrc);
		st.setByX(1.8);
		st.setByY(1.8);
		st.setCycleCount(1);
		st.setAutoReverse(false);
		st.play();

		Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    Thread.sleep(1000);
    				Platform.runLater(new Runnable() {

    					@Override
    					public void run() {
    						dialog.close();
    						Platform.exit();
    					}
    				});
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        new Thread(task).start();

	}

	public void setCursorPos(int x, int y) {
		this.cursorX = x;
		this.cursorY = y;
	}

	private Stage createTemplateDialog() {
		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(parentStage);
		dialog.setAlwaysOnTop(true);
		dialog.setResizable(false);
		dialog.setIconified(false);
		dialog.initStyle(StageStyle.TRANSPARENT);
		dialog.setTitle("EN ====> TR");

		if(cursorX >= 100 && cursorY >= 80) {
			dialog.setX(cursorX - 150);
			dialog.setY(cursorY - 100);
		}

		return dialog;
	}

	private VBox createTemplateBox(Stage stage) {
		VBox boxMain = new VBox();
		boxMain.setPadding(new Insets(0, 10, 10, 10));
		boxMain.setSpacing(10);
		boxMain.setAlignment(Pos.TOP_CENTER);
		boxMain.setStyle("-fx-background-color: #BBBBCD; -fx-background-radius: 8;");

		HBox boxTop = new HBox();
		boxTop.setMinHeight(20);
		boxTop.setMaxHeight(20);
		boxTop.setPrefWidth(10000);
		boxTop.setPadding(new Insets(4, -6, 0, -6));
		boxTop.setStyle("-fx-background-color: #a9a9bf;");
		boxTop.setAlignment(Pos.TOP_RIGHT);
		boxTop.setCursor(Cursor.MOVE);

		final Delta dragDelta = new Delta();
		boxTop.setOnMousePressed(new EventHandler<MouseEvent>() {
		  @Override public void handle(MouseEvent mouseEvent) {
		    dragDelta.x = stage.getX() - mouseEvent.getScreenX();
		    dragDelta.y = stage.getY() - mouseEvent.getScreenY();
		  }
		});
		boxTop.setOnMouseDragged(new EventHandler<MouseEvent>() {
		  @Override public void handle(MouseEvent mouseEvent) {
		    stage.setX(mouseEvent.getScreenX() + dragDelta.x);
		    stage.setY(mouseEvent.getScreenY() + dragDelta.y);
		  }
		});

		Button btnExit = new Button("X");
		btnExit.setMinSize(14, 14);
		btnExit.setMaxSize(14, 14);
		btnExit.setStyle("-fx-font-weight: bold; -fx-font-size: 6; -fx-text-fill: white; -fx-background-color: maroon;");
		btnExit.setCursor(Cursor.HAND);
		btnExit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
		});
		boxTop.getChildren().add(btnExit);
		boxMain.getChildren().add(boxTop);

		return boxMain;
	}

}
