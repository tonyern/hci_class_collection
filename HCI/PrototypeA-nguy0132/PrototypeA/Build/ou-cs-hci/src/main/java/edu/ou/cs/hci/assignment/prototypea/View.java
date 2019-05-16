//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Mon Feb  4 23:32:30 2019 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20190203 [weaver]:	Original file.
//
//******************************************************************************
//
//******************************************************************************

package edu.ou.cs.hci.assignment.prototypea;

//import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;

import edu.ou.cs.hci.application.swingmvc.View.MyCellRenderer;
import edu.ou.cs.hci.resources.Resources;

//******************************************************************************

/**
 * The <CODE>View</CODE> class.
 *
 * @author  Layout by Chris Weaver, changes made by Tony Nguyen
 * @version %I%, %G%
 */
public final class View implements ActionListener, CaretListener, ChangeListener, ListSelectionListener
{
    //**********************************************************************
    // Private Class Members
    //**********************************************************************

    private static final Dimension  SPACER = new Dimension(0, 10);
    
	//**********************************************************************
	// Private Members
	//**********************************************************************

	// Master of the program, manager of the data, mediator of all updates
	private final Controller controller;

	// The stuff on the right
	private JLabel          mediaImage;
	private JLabel          dropMediaHereText;
	private JButton         openMediaButton;

	// The list of media on the left
	private JList<String>	list;

	// The buttons, volume, and progress bar on the bottom
	private JButton         reverseButton;
	private JButton         playPauseButton;
	private JButton         forwardButton;
	private JButton         stackButton;
	private JSlider         volume;
	private JProgressBar    timerBar;
	private JButton         expandButton;
	private JLabel          volumeLabel;

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	// Construct the UI.
	public View(Controller controller)
	{
	    // Set the controller
		this.controller = controller;
		
		// Create a vertical panel of various widget panes to show on the left
        Box box = Box.createVerticalBox();
        box.add(createDropMedia());
        box.add(Box.createRigidArea(SPACER));

		JPanel scrollPane = new JPanel(new GridBagLayout());

		// Create a panel with the list of musics, videos, pod casts, etc.
		JPanel listPanel = createList();

		// Create a split pane to allow adjustment of left versus right...
		JSplitPane	splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);

		splitPanel.setResizeWeight(0.30);	// ...with default 30% for the right

		// Have one panel to the left and one to the right.
		splitPanel.setLeftComponent(listPanel);
        splitPanel.setRightComponent(scrollPane);
        
        JPanel      dropMediaPanel = createDropMedia();
        scrollPane.add(dropMediaPanel);

		// Create a pane with a few buttons for the bottom of the screen
		JPanel		buttonPanel = createButtons();

		// Create the topmost panel to contain the split and button panels
		JPanel		panel = new JPanel();

		panel.setPreferredSize(new Dimension(840, 600));
		panel.setLayout(new BorderLayout());

		panel.add(splitPanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);


		// Create a frame to hold everything
		JFrame		frame = new JFrame("VLC media player");

		// Populate and show the frame
		frame.setBounds(50 + (int)(50 * Math.random()), 50 + (int)(50 * Math.random()), 200, 200);
		
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Exit when the user clicks the frame's close button
		frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					controller.removeView(View.this);
				}
			});
	}

    //**********************************************************************
	// Public Methods (Controller)
	//**********************************************************************

	// Populate the UI with data, accessing it through the controller.
	public void	initialize()
	{
		list.setSelectedIndex((Integer)controller.get("index"));
		timerBar.setValue((Integer)controller.get("number"));
		volume.setValue((Integer)controller.get("number"));
	}

	public void	terminate()
	{
	    reverseButton.removeActionListener(this);
	    playPauseButton.removeActionListener(this);
	    forwardButton.removeActionListener(this);
	    stackButton.removeActionListener(this);
	    timerBar.removeChangeListener(this);
	    expandButton.removeActionListener(this);
	    
	    volume.removeChangeListener(this);
	    timerBar.removeChangeListener(this);
	    
	    openMediaButton.removeActionListener(this);
	}

	// Update the UI when the controller says something changed in the model.
	public void	update(String key, Object value)
	{
		System.out.println("update " + key + " to " + value);
		
		if ("number".equals(key))
        {
            volume.setValue((Integer)value);
        }
	}

	//**********************************************************************
	// Override Methods (ActionListener)
	//**********************************************************************

	// Send changes to the controller from widgets that trigger actions.
	public void	actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();	// Component source of event
		
		if (source == reverseButton)
		{
            controller.trigger("Reversing");
		}
        if (source == forwardButton)
        {
            controller.trigger("Forwarding");
        }
        else if (source == playPauseButton)
        {
            controller.set("Playing", 1);
        }
        else if (source == playPauseButton)
        {
            controller.set("Pausing", 0);
        }
        else if (source == expandButton)
        {
            controller.set("Expanding Screen", 1);
        }
        else if (source == expandButton)
        {
            controller.set("Disexpanding Screen", 0);
        }
        else if (source == openMediaButton)
        {
            controller.trigger("Opening media");
        }
	}

	//**********************************************************************
	// Override Methods (CaretListener)
	//**********************************************************************

	// Send changes to the controller from widgets that move a caret.
	public void	caretUpdate(CaretEvent e)
	{
	    // Empty function
	}

	//**********************************************************************
	// Override Methods (ChangeListener)
	//**********************************************************************

	// Send changes to the controller from widgets that change somehow.
	public void	stateChanged(ChangeEvent e)
	{
		Object source = e.getSource();	// Component source of event
		
		if (source == volume)
		{
            controller.set("number", volume.getValue());
		}
		if (source == timerBar)
		{
            controller.set("number", timerBar.getValue());
		}
	}

	//**********************************************************************
	// Override Methods (ListSelectionListener)
	//**********************************************************************

	// Send changes to the controller from widgets that select items.
	public void	valueChanged(ListSelectionEvent e)
	{
		Object source = e.getSource();	// Component source of event

		if (source == list)
		{
			controller.set("index", list.getSelectedIndex());
		}
	}

	//**********************************************************************
	// Override Methods (WindowListener)
	//**********************************************************************

	// Let the controller know that this window is going away.
	public void	windowClosing(WindowEvent e)
	{
		controller.removeView(this);
	}

	//**********************************************************************
	// Private Methods (Widget Pane Creators)
	//**********************************************************************

	// Bottom buttons.
	// Create a pane with the bottom buttons.
	private JPanel createButtons()
	{
	    // Make the panel with its flow and size
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		// Add stuff to the buttons
		reverseButton = new JButton("Reverse");
		playPauseButton = new JButton("Play");
		forwardButton = new JButton("Forward");
		stackButton = new JButton("More");
		
		// Volume of media player
		volumeLabel = new JLabel("Volume:");
		volume = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		volume.setMajorTickSpacing(10);
		volume.setPaintTicks(true);
		
		// Time progress bar
		DefaultBoundedRangeModel  model = new DefaultBoundedRangeModel(0, 0, 0, 100);
		timerBar = new JProgressBar(model);
	    timerBar.setPreferredSize(new Dimension(150, 15));
		
		expandButton = new JButton("Expand");
		
		reverseButton.addActionListener(this);
		playPauseButton.addActionListener(this);
		forwardButton.addActionListener(this);
		stackButton.addActionListener(this);
		volume.addChangeListener(this);
		timerBar.addChangeListener(this);
        expandButton.addActionListener(this);

        // Add the buttons to the panel
		panel.add(reverseButton);
		panel.add(playPauseButton);
		panel.add(forwardButton);
		panel.add(stackButton);
		panel.add(volumeLabel);
		panel.add(volume);
		panel.add(timerBar);
		panel.add(expandButton);
		
		return panel;
	}
	
	// Create the drop media items on the top right panel on the screen.
	private JPanel createDropMedia()
	{
	    // <div>Icons made by <a href="https://www.flaticon.com/authors/google" title="Google">Google</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/"title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
	    mediaImage = new JLabel();
	    mediaImage.setIcon(new ImageIcon("/example/swing/icon/dropMedia.png"));
	    
	    dropMediaHereText = new JLabel("Drop media here");
	    openMediaButton = new JButton("Open media...");
	    
	    openMediaButton.addActionListener(this);
	    
	    Box box = Box.createVerticalBox();
	    box.add(mediaImage);
	    box.add(dropMediaHereText);
	    box.add(Box.createRigidArea(SPACER));
	    box.add(openMediaButton);
	    
	    return createTitledPanel(box, "");
	}
	
	// This is a utility method to wrap components in a nicely labeled frame.
    // See the BorderFactory class API for many more panel decoration options.
    private JPanel createTitledPanel(JComponent c, String title)
    {
        JPanel  p = new JPanel();

        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(c);

        return p;
    }
    
    // Create a pane with a list for the gallery. The list uses a custom
    // CellRenderer class to draw its items. The combobox and the list share
    // the same selection.
    private JPanel  createList()
    {
        ArrayList<String>   data =
            Resources.getLines("example/swing/text/list-data.txt");

        DefaultListModel<String>    model =
            new DefaultListModel<String>();

        for (String item : data)
            model.addElement(item);

        list = new JList<String>(model);

        list.setPrototypeCellValue("##################,empty.png");
        list.setCellRenderer(new MyCellRenderer(true));
        list.setVisibleRowCount(6);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);

        JScrollPane     sp = new JScrollPane(list,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        return createTitledPanel(sp, "Media");
    }
    
  //**********************************************************************
    // Inner Classes
    //**********************************************************************

    public static final class MyCellRenderer extends JLabel
        implements ListCellRenderer<Object>
    {
        private static final long serialVersionUID = 1L;
        private final boolean   showIcon;   // For combobox, or list?

        public MyCellRenderer(boolean showIcon)
        {
            this.showIcon = showIcon;

            setOpaque(true);
        }

        // Lists, comboboxes, tables, and trees render their cells by setting
        // the style characteristics of the same hidden JLabel over and over.
        public Component    getListCellRendererComponent(JList<?> list,
            Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            String      item = value.toString();
            String[]    text = item.split(",");
            String      label = text[0];
            String      name = text[1];

            setText(label);

            if (showIcon)
            {
                Icon    icon = Resources.getImage("example/swing/icon/" + name);

                setIcon(icon);
                setIconTextGap(2);
            }

            setHorizontalAlignment(SwingConstants.LEFT);
            setHorizontalTextPosition(SwingConstants.TRAILING);

            setVerticalAlignment(SwingConstants.CENTER);
            setVerticalTextPosition(SwingConstants.CENTER);

            if (isSelected)
                setBackground(Color.LIGHT_GRAY);    // Highlight selected cells
            else
                setBackground(Color.WHITE);

            setForeground(Color.BLACK);

            // Make the text in the list larger than in the combobox
            int size = (showIcon ? 24 : 16);

            setFont(new Font(Font.SERIF, Font.ITALIC, size));

            return this;    // <-- the same JLabel over and over!
        }
    }
	
}

//******************************************************************************