//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Thu Feb  7 00:45:17 2019 by Chris Weaver
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
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.effect.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

//******************************************************************************

public class FXIceSkating extends Application
{
	//**********************************************************************
	// Main
	//**********************************************************************

	public static void	main(String[] args)
	{
		launch(args);
	}

	//**********************************************************************
	// Override Methods (Application)
	//**********************************************************************

	public void	start(Stage stage)
	{
		Group	root = new Group();
		Scene	scene = new Scene(root, 800, 600, Color.BLACK);

		stage.setScene(scene);

		Group	circles = new Group();

		for (int i=0; i<5; i++)
		{
			int	wh = i * 20;
			Circle	circle = new Circle(wh, wh, 100, Color.web("white", 0.25));

			circle.setStrokeType(StrokeType.OUTSIDE);
			circle.setStroke(Color.web("white", 0.50));
			circle.setStrokeWidth(4);
			circle.setEffect(new Reflection(10.0, 0.33, 0.50, 0.0));
			circles.getChildren().add(circle);
		}

		Rectangle	rcolors = new Rectangle(scene.getWidth(), scene.getHeight(),
				new LinearGradient(0f, 0f, 0f, 1f, true, CycleMethod.NO_CYCLE,
								   new Stop[]
					{
						new Stop(0.00, Color.web("#ffffff", 1.00)),
						new Stop(1.00, Color.web("#8080ff", 1.00)),
					}));

		rcolors.setEffect(new Glow(0.3));
		rcolors.widthProperty().bind(scene.widthProperty());
		rcolors.heightProperty().bind(scene.heightProperty());

		Rectangle	box = new Rectangle(scene.getWidth(), scene.getHeight(),
										Color.BLACK);
		Group		blendModeGroup = new Group(rcolors, circles);
		//Group		blendModeGroup = new Group(new Group(box, circles), rcolors);

		rcolors.setBlendMode(null);//BlendMode.OVERLAY);

		root.getChildren().add(blendModeGroup);
		//root.setEffect(new Lighting(new Light.Distant(45.0, 45.0, Color.WHITE)));

		//circles.setEffect(new BoxBlur(10, 10, 1));
		//circles.setEffect(new Bloom(0.3));
		//circles.setEffect(new Glow(0.3));
		//circles.setEffect(new MotionBlur(45.0, 10.0));
		//circles.setEffect(new SepiaTone(0.7));
		//circles.setEffect(new Shadow(10.0, Color.RED));

		Timeline	timeline = new Timeline();
		MotionBlur	blur = new MotionBlur(45.0, 0.0);
		DropShadow	shadow =
			new DropShadow(10.0, 5.0, 5.0, Color.web("#000080"));

		for (Node circle : circles.getChildren())
		{
			timeline.getKeyFrames().addAll(
					new KeyFrame(Duration.ZERO,
					new KeyValue(circle.translateXProperty(),
								 Math.random() * 800),
					new KeyValue(circle.translateYProperty(),
								 Math.random() * 600)),

					new KeyFrame(new Duration(1000),
						new KeyValue(circles.effectProperty(), blur),
						new KeyValue(blur.radiusProperty(), 0.0)),

					new KeyFrame(new Duration(6000),
						new KeyValue(circles.effectProperty(), shadow),
						new KeyValue(blur.radiusProperty(), 20.0),
						new KeyValue(shadow.radiusProperty(), 10.0)),

					new KeyFrame(new Duration(11000),
						new KeyValue(circles.effectProperty(), null),
						new KeyValue(shadow.radiusProperty(), 0.0)),

					new KeyFrame(new Duration(40000), // end at 40s
					new KeyValue(circle.translateXProperty(),
								 Math.random() * 800),
					new KeyValue(circle.translateYProperty(),
								 Math.random() * 600)));
		}

		// play 40s of animation
		timeline.play();

		stage.show();
	}
}

//******************************************************************************
