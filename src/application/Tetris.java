package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class Tetris extends Application {
	// The variables
	public static final int MOVE = 25;
	public static final int SIZE = 25;
	public static int XMAX = SIZE * 12;
	public static int YMAX = SIZE * 24;
	public static int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE];
	private static Pane group = new Pane();
	private static Form object;
	private static Scene scene1 = new Scene(group, XMAX + 150, YMAX);
	public static int score = 0;
	private static int top = 0;
	private static boolean game = false;
	private static Form nextObj = Controller.makeRect();
	private static int linesNo = 0;
	private String[] scoreboardNames = new String[5];
	private int[] scoreboardScores = new int[5];
	private File scoreboardFile = new File("scoreboard.txt");
	
	
	Stage menuStage = new Stage(); 
	Stage gameStage = new Stage();
	Stage scoreboardStage = new Stage();
	
	StackPane paneMenu = new StackPane();   
    Scene sceneMenu = new Scene(paneMenu,450,600);
    
    StackPane paneScoreboard = new StackPane();
    Scene sceneScoreboard = new Scene(paneScoreboard,450,600);
    

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		for (int[] a : MESH) {
			Arrays.fill(a, 0);
		}
		
		gameStage = stage;
		
		
		//Scoreboard file erstellen oder Werte einlesen
		if (scoreboardFile.createNewFile()) {
	        System.out.println("File created: " + scoreboardFile.getName());
	      } else {
	    	  Scanner myReader = new Scanner(scoreboardFile);
	    	  int i = 0;
	    	  while (myReader.hasNextLine()) {
	    	        String data = myReader.nextLine();
	    	        String[] arrOfStr = data.split(":");
	    	        scoreboardNames[i] = arrOfStr[0];
	    	        scoreboardScores[i] = Integer.parseInt(arrOfStr[1]);
	    	        i++;
	    	  }
	    	  myReader.close();
	      }
        
		
		//Main Menu
		
		Text tetrisText = new Text("TETRIS");
	    Text playText = new Text("Play - press ENTER");
	    Text scoreboardText = new Text("Scoreboard - press S");
	    Text exitText = new Text("Exit - press E");
		
        playText.setTranslateY(-75);
        exitText.setTranslateY(75);
        tetrisText.setTranslateY(-200);
        
        actionMenuKeyPress(menuStage);
        tetrisText.setStyle("-fx-fill: blue; -fx-font: 40 arial");
        playText.setStyle("-fx-font: 20 arial;");
        scoreboardText.setStyle("-fx-font: 20 arial;");
        exitText.setStyle("-fx-font: 20 arial;");
        
        paneMenu.getChildren().add(playText); 
        paneMenu.getChildren().add(scoreboardText);
        paneMenu.getChildren().add(exitText);
        paneMenu.getChildren().add(tetrisText);
        menuStage.setScene(sceneMenu);  
        menuStage.setTitle("T E T R I S - S T A R T");  
        menuStage.show();
        
        
        //Scoreboard Screen
        
        Text scoreboardTitle = new Text("SCOREBOARD");
        scoreboardTitle.setTranslateY(-200);
        scoreboardTitle.setStyle("-fx-fill: blue; -fx-font: 40 arial");
        actionScoreboardKeyPress(scoreboardStage);
        
        Text score1 = new Text("No Entries Jet");
        score1.setTranslateY(-100);
        score1.setStyle("-fx-font: 20 arial;");
        
        Text score2 = new Text("No Entries Jet");
        score2.setTranslateY(-50);
        score2.setStyle("-fx-font: 20 arial;");
        
        Text score3 = new Text("No Entries Jet");
        score3.setTranslateY(0);
        score3.setStyle("-fx-font: 20 arial;");
        
        Text score4 = new Text("No Entries Jet");
        score4.setTranslateY(50);
        score4.setStyle("-fx-font: 20 arial;");
        
        Text score5 = new Text("No Entries Jet");
        score5.setTranslateY(100);
        score5.setStyle("-fx-font: 20 arial;");
        
        Text gameTextScoreboard = new Text("Game - ENTER");
        gameTextScoreboard.setStyle("-fx-font: 20 arial;");
        gameTextScoreboard.setTranslateY(200);
        
        Text menuTextScoreboard = new Text("Menu - M");
        menuTextScoreboard.setStyle("-fx-font: 20 arial;");
        menuTextScoreboard.setTranslateY(225);
        
		Text exitTextScoreboard = new Text("Exit - E");
		exitTextScoreboard.setStyle("-fx-font: 20 arial;");
		exitTextScoreboard.setTranslateY(250);
        
        paneScoreboard.getChildren().addAll(scoreboardTitle,score1,score2,score3,score4,score5,gameTextScoreboard,menuTextScoreboard,exitTextScoreboard);
        scoreboardStage.setScene(sceneScoreboard);
        scoreboardStage.setTitle("T E T R I S - S C O R E B O A R D");
        
        
        //Game Screen
		
		//Text der Linken Spalte plus Trennstrich
		Line line = new Line(XMAX, 0, XMAX, YMAX);
		Text scoretext = new Text("Score: ");
		scoretext.setStyle("-fx-font: 20 arial;");
		scoretext.setY(50);
		scoretext.setX(XMAX + 10);
		Text leveltext = new Text("Lines: ");
		leveltext.setStyle("-fx-font: 20 arial;");
		leveltext.setY(100);
		leveltext.setX(XMAX + 10);
		leveltext.setFill(Color.GREEN);
		Text menutext = new Text("Menu - M");
		menutext.setStyle("-fx-font: 20 arial;");
		menutext.setY(400);
		menutext.setX(XMAX + 10);
		Text scoreboardtext = new Text("Scoreboard - S");
		scoreboardtext.setStyle("-fx-font: 20 arial;");
		scoreboardtext.setY(450);
		scoreboardtext.setX(XMAX + 10);
		Text exittext = new Text("Exit - E");
		exittext.setStyle("-fx-font: 20 arial;");
		exittext.setY(500);
		exittext.setX(XMAX + 10);
		group.getChildren().addAll(scoretext, line, leveltext, menutext, scoreboardtext, exittext);
		
		gameStage.setScene(scene1);
		gameStage.setTitle("T E T R I S - G A M E");
		
		Form tempObj = nextObj;
		group.getChildren().addAll(tempObj.a, tempObj.b, tempObj.c, tempObj.d);
		actionKeyPress(tempObj);
		object = tempObj;
		nextObj = Controller.makeRect();

		Timer fall = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						if (object.a.getY() == 0 || object.b.getY() == 0 || object.c.getY() == 0 || object.d.getY() == 0) {
							top++;
						}else {
							top = 0;
							}

						if (top == 2 && game == true) {
							// GAME OVER
							Text over = new Text("GAME OVER\n     press R");
							over.setFill(Color.RED);
							over.setStyle("-fx-font: 70 arial;");
							over.setY(250);
							over.setX(10);
							group.getChildren().add(over);
							game = false;
						}
						// Exit
						if (game) {
							MoveDown(object);
							scoretext.setText("Score: " + Integer.toString(score));
							leveltext.setText("Lines: " + Integer.toString(linesNo));
						}
					}
				});
			}
		};
		fall.schedule(task, 0, 300); 
	}
	
	private void actionMenuKeyPress(Stage stage) {
		sceneMenu.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case S:
					scoreboardStage.show();
					game = false;
					menuStage.close();
					break;
				case E:
					System.exit(0);
					break;
				case ENTER:
		        		gameStage.show();
		        		game = true;
		        		menuStage.close();
					break;
				default:
					break;
				}
			}
		});
	}
	
	private void actionScoreboardKeyPress(Stage stage) {
		sceneScoreboard.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case M:
					menuStage.show();
					game = false;
					scoreboardStage.close();
					break;
				case E:
					System.exit(0);
					break;
				case ENTER:
					gameStage.show();
					game = true;
					scoreboardStage.close();
				default:
					break;
				}
			}
		});
	}

	private void actionKeyPress(Form form) {
		scene1.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case RIGHT:
					if(game) {
					Controller.MoveRight(form);
					}
					break;
				case DOWN:
					if(game) {
					MoveDown(form);
					}
					score++;
					break;
				case LEFT:
					if(game) {
					Controller.MoveLeft(form);
					}
					break;
				case UP:
					if(game) {
					MoveTurn(form);
					}
					break;
				case M:
					if(game) {
					menuStage.show();
					game = false;
					gameStage.close();
					}
					break;
				case S:
			        scoreboardStage.show();
					game = false;
					gameStage.close();
					break;
				case E:
					System.exit(0);
					break;
				case R:
					menuStage.show();
					game = false;
					gameStage.close();
					break;
				default:
					break;
				}
			}
		});
	}

	private void MoveTurn(Form form) {
		int f = form.form;
		Rectangle a = form.a;
		Rectangle b = form.b;
		Rectangle c = form.c;
		Rectangle d = form.d;
		switch (form.getName()) {
		case "j":
			if (f == 1 && cB(a, 1, -1) && cB(c, -1, -1) && cB(d, -2, -2)) {
				MoveRight(form.a);
				MoveDown(form.a);
				MoveDown(form.c);
				MoveLeft(form.c);
				MoveDown(form.d);
				MoveDown(form.d);
				MoveLeft(form.d);
				MoveLeft(form.d);
				form.rotatePiece();
				break;
			}
			if (f == 2 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, -2, 2)) {
				MoveDown(form.a);
				MoveLeft(form.a);
				MoveLeft(form.c);
				MoveUp(form.c);
				MoveLeft(form.d);
				MoveLeft(form.d);
				MoveUp(form.d);
				MoveUp(form.d);
				form.rotatePiece();
				break;
			}
			if (f == 3 && cB(a, -1, 1) && cB(c, 1, 1) && cB(d, 2, 2)) {
				MoveLeft(form.a);
				MoveUp(form.a);
				MoveUp(form.c);
				MoveRight(form.c);
				MoveUp(form.d);
				MoveUp(form.d);
				MoveRight(form.d);
				MoveRight(form.d);
				form.rotatePiece();
				break;
			}
			if (f == 4 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 2, -2)) {
				MoveUp(form.a);
				MoveRight(form.a);
				MoveRight(form.c);
				MoveDown(form.c);
				MoveRight(form.d);
				MoveRight(form.d);
				MoveDown(form.d);
				MoveDown(form.d);
				form.rotatePiece();
				break;
			}
			break;
		case "l":
			if (f == 1 && cB(a, 1, -1) && cB(c, 1, 1) && cB(b, 2, 2)) {
				MoveRight(form.a);
				MoveDown(form.a);
				MoveUp(form.c);
				MoveRight(form.c);
				MoveUp(form.b);
				MoveUp(form.b);
				MoveRight(form.b);
				MoveRight(form.b);
				form.rotatePiece();
				break;
			}
			if (f == 2 && cB(a, -1, -1) && cB(b, 2, -2) && cB(c, 1, -1)) {
				MoveDown(form.a);
				MoveLeft(form.a);
				MoveRight(form.b);
				MoveRight(form.b);
				MoveDown(form.b);
				MoveDown(form.b);
				MoveRight(form.c);
				MoveDown(form.c);
				form.rotatePiece();
				break;
			}
			if (f == 3 && cB(a, -1, 1) && cB(c, -1, -1) && cB(b, -2, -2)) {
				MoveLeft(form.a);
				MoveUp(form.a);
				MoveDown(form.c);
				MoveLeft(form.c);
				MoveDown(form.b);
				MoveDown(form.b);
				MoveLeft(form.b);
				MoveLeft(form.b);
				form.rotatePiece();
				break;
			}
			if (f == 4 && cB(a, 1, 1) && cB(b, -2, 2) && cB(c, -1, 1)) {
				MoveUp(form.a);
				MoveRight(form.a);
				MoveLeft(form.b);
				MoveLeft(form.b);
				MoveUp(form.b);
				MoveUp(form.b);
				MoveLeft(form.c);
				MoveUp(form.c);
				form.rotatePiece();
				break;
			}
			break;
		case "o":
			break;
		case "s":
			if (f == 1 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
				MoveDown(form.a);
				MoveLeft(form.a);
				MoveLeft(form.c);
				MoveUp(form.c);
				MoveUp(form.d);
				MoveUp(form.d);
				form.rotatePiece();
				break;
			}
			if (f == 2 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
				MoveUp(form.a);
				MoveRight(form.a);
				MoveRight(form.c);
				MoveDown(form.c);
				MoveDown(form.d);
				MoveDown(form.d);
				form.rotatePiece();
				break;
			}
			if (f == 3 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
				MoveDown(form.a);
				MoveLeft(form.a);
				MoveLeft(form.c);
				MoveUp(form.c);
				MoveUp(form.d);
				MoveUp(form.d);
				form.rotatePiece();
				break;
			}
			if (f == 4 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
				MoveUp(form.a);
				MoveRight(form.a);
				MoveRight(form.c);
				MoveDown(form.c);
				MoveDown(form.d);
				MoveDown(form.d);
				form.rotatePiece();
				break;
			}
			break;
		case "t":
			if (f == 1 && cB(a, 1, 1) && cB(d, -1, -1) && cB(c, -1, 1)) {
				MoveUp(form.a);
				MoveRight(form.a);
				MoveDown(form.d);
				MoveLeft(form.d);
				MoveLeft(form.c);
				MoveUp(form.c);
				form.rotatePiece();
				break;
			}
			if (f == 2 && cB(a, 1, -1) && cB(d, -1, 1) && cB(c, 1, 1)) {
				MoveRight(form.a);
				MoveDown(form.a); 
				MoveLeft(form.d);
				MoveUp(form.d);
				MoveUp(form.c);
				MoveRight(form.c);
				form.rotatePiece();
				break;
			}
			if (f == 3 && cB(a, -1, -1) && cB(d, 1, 1) && cB(c, 1, -1)) {
				MoveDown(form.a);
				MoveLeft(form.a);
				MoveUp(form.d);
				MoveRight(form.d);
				MoveRight(form.c);
				MoveDown(form.c);
				form.rotatePiece();
				break;
			}
			if (f == 4 && cB(a, -1, 1) && cB(d, 1, -1) && cB(c, -1, -1)) {
				MoveLeft(form.a);
				MoveUp(form.a);
				MoveRight(form.d);
				MoveDown(form.d);
				MoveDown(form.c);
				MoveLeft(form.c);
				form.rotatePiece();
				break;
			}
			break;
		case "z":
			if (f == 1 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
				MoveUp(form.b);
				MoveRight(form.b);
				MoveLeft(form.c);
				MoveUp(form.c);
				MoveLeft(form.d);
				MoveLeft(form.d);
				form.rotatePiece();
				break;
			}
			if (f == 2 && cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
				MoveDown(form.b);
				MoveLeft(form.b);
				MoveRight(form.c);
				MoveDown(form.c);
				MoveRight(form.d);
				MoveRight(form.d);
				form.rotatePiece();
				break;
			}
			if (f == 3 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
				MoveUp(form.b);
				MoveRight(form.b);
				MoveLeft(form.c);
				MoveUp(form.c);
				MoveLeft(form.d);
				MoveLeft(form.d);
				form.rotatePiece();
				break;
			}
			if (f == 4 && cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
				MoveDown(form.b);
				MoveLeft(form.b);
				MoveRight(form.c);
				MoveDown(form.c);
				MoveRight(form.d);
				MoveRight(form.d);
				form.rotatePiece();
				break;
			}
			break;
		case "i":
			if (f == 1 && cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -1, -1)) {
				MoveUp(form.a);
				MoveUp(form.a);
				MoveRight(form.a);
				MoveRight(form.a);
				MoveUp(form.b);
				MoveRight(form.b);
				MoveDown(form.d);
				MoveLeft(form.d);
				form.rotatePiece();
				break;
			}
			if (f == 2 && cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
				MoveDown(form.a);
				MoveDown(form.a);
				MoveLeft(form.a);
				MoveLeft(form.a);
				MoveDown(form.b);
				MoveLeft(form.b);
				MoveUp(form.d);
				MoveRight(form.d);
				form.rotatePiece();
				break;
			}
			if (f == 3 && cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -1, -1)) {
				MoveUp(form.a);
				MoveUp(form.a);
				MoveRight(form.a);
				MoveRight(form.a);
				MoveUp(form.b);
				MoveRight(form.b);
				MoveDown(form.d);
				MoveLeft(form.d);
				form.rotatePiece();
				break;
			}
			if (f == 4 && cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
				MoveDown(form.a);
				MoveDown(form.a);
				MoveLeft(form.a);
				MoveLeft(form.a);
				MoveDown(form.b);
				MoveLeft(form.b);
				MoveUp(form.d);
				MoveRight(form.d);
				form.rotatePiece();
				break;
			}
			break;
		}
	}

	private void RemoveRows(Pane pane) {
		ArrayList<Node> rects = new ArrayList<Node>();
		ArrayList<Integer> lines = new ArrayList<Integer>();
		ArrayList<Node> newrects = new ArrayList<Node>();
		int full = 0;
		for (int i = 0; i < MESH[0].length; i++) {
			for (int j = 0; j < MESH.length; j++) {
				if (MESH[j][i] == 1)
					full++;
			}
			if (full == MESH.length)
			lines.add(i);
			//lines.add(i + lines.size());
			full = 0;
		}
		if (lines.size() > 0)
			do {
				for (Node node : pane.getChildren()) {
					if (node instanceof Rectangle)
						rects.add(node);
				}
				score += 50;
				linesNo++;

				for (Node node : rects) {
					Rectangle a = (Rectangle) node;
					if (a.getY() == lines.get(0) * SIZE) {
						MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
						pane.getChildren().remove(node);
					} else
						newrects.add(node);
				}

				for (Node node : newrects) {
					Rectangle a = (Rectangle) node;
					if (a.getY() < lines.get(0) * SIZE) {
						MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
						a.setY(a.getY() + SIZE);
					}
				}
				lines.remove(0);
				rects.clear();
				newrects.clear();
				for (Node node : pane.getChildren()) {
					if (node instanceof Rectangle)
						rects.add(node);
				}
				for (Node node : rects) {
					Rectangle a = (Rectangle) node;
					try {
						MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1;
					} catch (ArrayIndexOutOfBoundsException e) {
					}
				}
				rects.clear();
			} while (lines.size() > 0);
	}

	private void MoveDown(Rectangle rect) {
		if (rect.getY() + MOVE < YMAX)
			rect.setY(rect.getY() + MOVE);

	}

	private void MoveRight(Rectangle rect) {
		if (rect.getX() + MOVE <= XMAX - SIZE)
			rect.setX(rect.getX() + MOVE);
	}

	private void MoveLeft(Rectangle rect) {
		if (rect.getX() - MOVE >= 0)
			rect.setX(rect.getX() - MOVE);
	}

	private void MoveUp(Rectangle rect) {
		if (rect.getY() - MOVE > 0)
			rect.setY(rect.getY() - MOVE);
	}

	private void MoveDown(Form form) {
		if (form.a.getY() == YMAX - SIZE || form.b.getY() == YMAX - SIZE || form.c.getY() == YMAX - SIZE
				|| form.d.getY() == YMAX - SIZE || moveA(form) || moveB(form) || moveC(form) || moveD(form)) {
			MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
			MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
			MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
			MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
			RemoveRows(group);

			Form a = nextObj;
			nextObj = Controller.makeRect();
			object = a;
			group.getChildren().addAll(a.a, a.b, a.c, a.d);
			actionKeyPress(a);
		}

		if (form.a.getY() + MOVE < YMAX && form.b.getY() + MOVE < YMAX && form.c.getY() + MOVE < YMAX
				&& form.d.getY() + MOVE < YMAX) {
			int movea = MESH[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) + 1];
			int moveb = MESH[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) + 1];
			int movec = MESH[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) + 1];
			int moved = MESH[(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE) + 1];
			if (movea == 0 && movea == moveb && moveb == movec && movec == moved) {
				form.a.setY(form.a.getY() + MOVE);
				form.b.setY(form.b.getY() + MOVE);
				form.c.setY(form.c.getY() + MOVE);
				form.d.setY(form.d.getY() + MOVE);
			}
		}
	}

	private boolean moveA(Form form) {
		return (MESH[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) + 1] == 1);
	}

	private boolean moveB(Form form) {
		return (MESH[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) + 1] == 1);
	}

	private boolean moveC(Form form) {
		return (MESH[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) + 1] == 1);
	}

	private boolean moveD(Form form) {
		return (MESH[(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE) + 1] == 1);
	}

	private boolean cB(Rectangle rect, int x, int y) {
		boolean xb = false;
		boolean yb = false;
		if (x >= 0)
			xb = rect.getX() + x * MOVE <= XMAX - SIZE;
		if (x < 0)
			xb = rect.getX() + x * MOVE >= 0;
		if (y >= 0)
			yb = rect.getY() - y * MOVE > 0;
		if (y < 0)
			yb = rect.getY() + y * MOVE < YMAX;
		return xb && yb && MESH[((int) rect.getX() / SIZE) + x][((int) rect.getY() / SIZE) - y] == 0;
	}

}