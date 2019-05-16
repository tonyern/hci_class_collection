//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Tue Dec  4 18:48:43 2001 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20190206 [weaver]:	Original file.
//
//******************************************************************************
//
// Not yet functional...experimenting with using the text field to pick a file..
//
//******************************************************************************

package edu.ou.cs.hci.example;

//import java.lang.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.stage.Stage;

//******************************************************************************

/**
 * The <CODE>FXMediaView</CODE> class.
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public class FXMediaView extends Application
{
	//**********************************************************************
	// Main
	//**********************************************************************

	// arg[0] = file:/Users/chris/some/thing.m4v
	public static void	main(String[] args)
	{
		launch(args);
	}

	//**********************************************************************
	// Private Members
	//**********************************************************************

	// State (internal) variables
	private static TextField	field;
	private static MediaView	mediaView;

	//**********************************************************************
	// Override Methods (Application)
	//**********************************************************************

	public void	start(Stage stage)
	{
		MediaView		mediaView = new MediaView();
		Label			label = new Label("File:");

		field = new TextField();
		mediaView = new MediaView();

		GridPane		grid = new GridPane();

		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		grid.add(label, 0, 0);
		grid.add(field, 1, 0);
		grid.add(mediaView, 0, 1, 2, 1);

		Scene	scene = new Scene(grid, 600, 400);

		stage.setTitle("Media View");
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
			String			source = field.getText();
			Media			media = new Media(source);
			MediaPlayer	mediaPlayer = new MediaPlayer(media);

			mediaPlayer.setAutoPlay(true);
			mediaView.setMediaPlayer(mediaPlayer);
		}
	}
}

//******************************************************************************
