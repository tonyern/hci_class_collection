//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Sun Feb 24 18:17:00 2019 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20190203 [weaver]:	Original file.
// 20190220 [weaver]:	Adapted from swingmvc to fxmvc.
//
//******************************************************************************
//
//******************************************************************************

package edu.ou.cs.hci.assignment.prototypeb.pane;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
//import java.lang.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.scene.paint.Color;

import javax.swing.event.ChangeEvent;

import edu.ou.cs.hci.assignment.prototypeb.Controller;

//******************************************************************************

/**
 * The <CODE>MediaPane</CODE> class.
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public final class MediaPane extends AbstractPane
{
	//**********************************************************************
	// Private Class Members
	//**********************************************************************

	private static final String	NAME = "Media";
	private static final String	HINT = "Basic Player for Movie Clips";

	//**********************************************************************
	// Private Class Members (Data)
	//**********************************************************************

	private static final String	URL =
		"http://ampliation.org/hci/HPSS.mp4";

	//**********************************************************************
	// Private Members
	//**********************************************************************

	// Layout
	private BorderPane				base;
	private BorderPane              progressView; // TODO
	private MediaView				mediaView;
	private Button					playButton;
	private Button					stopButton;
	private Button                  pauseButton;
	private Text                    volumeText;
	private Slider                  volumeSlider;
	private Button                  muteButton;
	private Text                    timeText;
	private ProgressBar             videoProgressBar;

	// Support
	private MediaPlayer			mediaPlayer;

	// Handlers
	private final ActionHandler	actionHandler;

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public MediaPane(Controller controller)
	{
		super(controller, NAME, HINT);

		actionHandler = new ActionHandler();

		setBase(buildPane());
	}

	//**********************************************************************
	// Public Methods (Controller)
	//**********************************************************************

	// The controller calls this method when it adds a view.
	// Set up the nodes in the view with data accessed through the controller.
	public void	initialize()
	{
		// Keep the media view the same width as the entire scene
		mediaView.fitWidthProperty().bind(mediaView.getScene().widthProperty());

		Media		media = new Media(URL);

		media.setOnError(new Runnable() {
				public void run() {
					System.out.println(media.getError());
				}
			});

		mediaPlayer = new MediaPlayer(media);

		mediaPlayer.setAutoPlay(false);

		mediaPlayer.setOnError(new Runnable() {
				public void run() {
					System.out.println(mediaPlayer.getError());
				}
			});

		mediaView.setMediaPlayer(mediaPlayer);
		
		// Adjust volume of media player.
		volumeSlider.setValue(mediaPlayer.getVolume() * 100);
		volumeSlider.valueProperty().addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable)
            {
                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
            }
        });
		
	}

	// The controller calls this method when it removes a view.
	// Unregister event and property listeners for the nodes in the view.
	public void	terminate()
	{
		mediaView.setMediaPlayer(null);
		mediaPlayer.dispose();

		playButton.setOnAction(null);
		stopButton.setOnAction(null);
		pauseButton.setOnAction(null);
		muteButton.setOnAction(null);
	}

	// The controller calls this method whenever something changes in the model.
	// Update the nodes in the view to reflect the change.
	public void	update(String key, Object value)
	{
	}

	//**********************************************************************
	// Private Methods (Layout)
	//**********************************************************************

	private Pane	buildPane()
	{
		base = new BorderPane();

		base.setId("media-pane");			// See #media-pane in View.css

		base.setCenter(createScreen());
		base.setBottom(createPlayer());

		return base;
	}

	// Create a node with the media view.
	private Pane	createScreen()
	{
		mediaView = new MediaView();

		mediaView.setOnError(new MediaErrorHandler());

		return new StackPane(mediaView);
	}
	
	// Create a node with the progress bar.
	private Pane    createProgressBar()
	{
	    progressView = new BorderPane();
	    
	    return null;
	}

	// Create a node with various playback controls.
	private Pane	createPlayer()
	{
		playButton = new Button("Play");
		pauseButton = new Button("Pause");
		stopButton = new Button("Stop");
		volumeText = new Text("Volume");
		volumeText.setFill(Color.WHITE);
		volumeSlider = new Slider();
		
		muteButton = new Button("Mute");
		timeText = new Text("00:00 / 00:00");
		timeText.setFill(Color.WHITE);

		playButton.setOnAction(actionHandler);
		pauseButton.setOnAction(actionHandler);
		stopButton.setOnAction(actionHandler);
		muteButton.setOnAction(actionHandler);

		FlowPane	pane = new FlowPane(8.0, 8.0);
		
		pane.setAlignment(Pos.BOTTOM_CENTER);

		pane.getChildren().add(playButton);
		pane.getChildren().add(pauseButton);
		pane.getChildren().add(stopButton);
		pane.getChildren().add(volumeText);
		pane.getChildren().add(volumeSlider);
		pane.getChildren().add(muteButton);
		pane.getChildren().add(timeText);

		return pane;
	}

	//**********************************************************************
	// Inner Classes (Event Handlers)
	//**********************************************************************

	private final class ActionHandler
		implements EventHandler<ActionEvent>
	{
		public void	handle(ActionEvent e)
		{
			Object	source = e.getSource();

			if (source == playButton) {
				mediaPlayer.play();
			}
			else if (e.getSource() == stopButton) {
				mediaPlayer.stop();
			}
			else if (e.getSource() == pauseButton) {
			    mediaPlayer.pause();
			}
			// If user clicks the mute button and volume is not muted then mute it.
			else if (e.getSource() == muteButton && !(mediaPlayer.isMute())) {
			    mediaPlayer.setMute(true);
			    muteButton.setText("Unmute");
            }
			// If user clicks the mute button and volume is muted then unmute it.
			else if (e.getSource() == muteButton && mediaPlayer.isMute()) {
                mediaPlayer.setMute(false);
                muteButton.setText("Mute");
            }
			
		}
	}

	private final class MediaErrorHandler
		implements EventHandler<MediaErrorEvent>
	{
		public void	handle(MediaErrorEvent e)
		{
			System.err.println(e);
		}
	}
}

//******************************************************************************
