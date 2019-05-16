//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Thu Feb  7 00:23:59 2019 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20190206 [weaver]:	Original file.
//
//******************************************************************************
//
//******************************************************************************

package edu.ou.cs.hci.example;

//import java.lang.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

//******************************************************************************

/**
 * The <CODE>FXHelloWorld</CODE> class.
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public class FXHelloWorld extends Application
{
	//**********************************************************************
	// Main
	//**********************************************************************

	public static void	main(String[] args)
	{
		launch(args);
	}

	//**********************************************************************
	// Private Members
	//**********************************************************************

	// State (internal) variables
	private static Circle	circle;

	//**********************************************************************
	// Override Methods (Application)
	//**********************************************************************

	public void	start(Stage stage)
	{
		Button			button = new Button();

		button.setText("Hello");

		circle = new Circle(0, 0, 50, Color.TRANSPARENT);
		circle.setStroke(Color.TRANSPARENT);
		circle.setStrokeType(StrokeType.OUTSIDE);
		circle.setStrokeWidth(2);

		ActionHandler	handler = new ActionHandler();

		button.setOnAction(handler);

		StackPane		root = new StackPane();

		root.getChildren().add(circle);
		root.getChildren().add(button);

		Scene			scene = new Scene(root, 160, 120);

		stage.setTitle("Hello World...");
		stage.setScene(scene);
		stage.show();
	}

	//**********************************************************************
	// Inner Classes (EventHandlers)
	//**********************************************************************

	public static final class ActionHandler
		implements EventHandler<ActionEvent>
	{
		public void	handle(ActionEvent e)
		{
			Button	button = (Button)e.getTarget();

			if ("Hello".equals(button.getText()))
			{
				button.setText("World");
				circle.setFill(Color.DEEPSKYBLUE);
				circle.setStroke(Color.FORESTGREEN);
			}
			else
			{
				button.setText("Hello");
				circle.setFill(Color.TRANSPARENT);
				circle.setStroke(Color.TRANSPARENT);
			}
		}
	}
}

//******************************************************************************
