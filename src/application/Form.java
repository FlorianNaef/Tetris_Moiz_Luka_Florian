package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Form {
	
	
	Rectangle a;
	Rectangle b;
	Rectangle c;
	Rectangle d;
	Color color;
	public String name;
	public int form = 1;
	
	
	public Form(Rectangle a, Rectangle b, Rectangle c, Rectangle d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	public Form(Rectangle a, Rectangle b, Rectangle c, Rectangle d, String name) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.name = name;
	
	
	//color of pieces
	//letters corespond with shape of Piece
	switch(name) {
		case "j":
			color = Color.SALMON;
			break;
		case "l":
			color = Color.GOLD;
			break;
		case "o":
			color = Color.GREY;
			break;
		case "s":
			color = Color.DARKGREEN;
			break;
		case "t":
			color = Color.TAN;
			break;
		case "z":
			color = Color.LIGHTPINK;
			break;
		case "i":
			color = Color.SADDLEBROWN;
			break;
	}
	
	this.a.setFill(color);
	this.b.setFill(color);
	this.c.setFill(color);
	this.d.setFill(color);
	}
	
	public String getName() {
		return name;
	}
	
	public void rotatePiece() {
		if (form != 4) {
			form++;
		}else {
			form = 1;
		}
	}
	
	//6:45
	
	
}
