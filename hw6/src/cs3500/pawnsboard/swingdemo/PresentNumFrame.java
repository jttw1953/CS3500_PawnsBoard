package cs3500.pawnsboard.swingdemo;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.util.List;

public class PresentNumFrame extends JFrame {

    public PresentNumFrame(List<Integer> numbers) {
	super();
	this.setSize(500, 300);

	//This is also the default behavior of the close button
	this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	
	JLabel numberLabel = new JLabel("Numbers are " + numbers);
	this.add(numberLabel);

    }
    
}
