package lib.processing;

import files.Game;

public class Main
{	

	public void startUI()
	{
		String[] a = {"MAIN"};
        processing.core.PApplet.runSketch( a, new Game());		
	}

	public static void main(String[] args)
	{
		Main main = new Main();
		main.startUI();			
	}
}