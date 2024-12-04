package scenes;

import java.awt.Color;
import java.awt.Graphics;
import main.Game;
import ui.Logo;
import ui.MyButton;
import ui.drawTileScreen;

import static main.GameStates.*;

public class Menu extends GameScene implements SceneMethods {

	private MyButton bPlaying, bEdit, bSettings, bQuit, bLevelSelection, bLevelEditor, bTutorial;
    private drawTileScreen TileScreen, TileScreen2;
    private AudioPlayer audioPlayer;
    
    private OptionsWindow optionsWindow; // Thêm biến cho cửa sổ Options
    private boolean isOptionsWindowOpen = false; // Biến kiểm tra nếu cửa sổ options đang mở

	
	public Menu(Game game) {
		super(game);
		game.getAudioPlayer().setBackgroundMusicVolume(game.getVolumeLevel());
		game.getAudioPlayer().setEffectVolume(game.getEffectVolumeLevel()); 

		game.getAudioPlayer().playBackgroundMusic(0);
		initButtons();
		
	}

	private void initButtons() {

		int w = 150;
		int h = w / 3;
		int x = 640 / 2 - w / 2;  
		int y = 400;
		int yOffset = 100;

		bPlaying = new MyButton("Play", x, y-50, w, h);
		bEdit = new MyButton("Edit", x, y + yOffset-50, w, h);
		bSettings = new MyButton("Settings", x, y + yOffset * 2-50, w, h);
		bQuit = new MyButton("Quit", x, y + yOffset * 3, w, h);
		bLevelSelection = new MyButton("Selection Level", x, y + yOffset * 4-50, w, h);
		bLevelEditor = new MyButton(" Level", x, y + yOffset * 4-50, w, h);
		TileScreen = new drawTileScreen("TOWER DEFENSE", x, 200-yOffset*3/2-50, w, h);
		TileScreen2 = new drawTileScreen("TOWER DEFENSE", x+5, 200-yOffset*3/2+5-50, w, h);
		bTutorial = new MyButton("Tutorial", x, y + yOffset * 3-75, w, h);
		//logo = new Logo("res/khien.png", x, 300);//

	}

	@Override
	public void render(Graphics g) {
	    if (!game.getAudioPlayer().isBackgroundMusicPlaying()) {
	     	game.getAudioPlayer().playBackgroundMusic(0);
	    }
	    g.setColor(new Color(70, 120, 70));
	    g.fillRect(0, 0, 640, 800);
	    drawButtons(g);
	    
	    if (isOptionsWindowOpen) {
            optionsWindow.render(g);
        }
	}


	private void drawButtons(Graphics g) {
		bPlaying.draw(g);
		bEdit.draw(g);
		bSettings.draw(g);
		bQuit.draw(g);
		TileScreen2.draw2(g);
		TileScreen.draw(g);
		bTutorial.draw(g);
		//logo.draw(g);//
	}

	@Override
	public void mouseClicked(int x, int y) {
		
		
		if (bPlaying.getBounds().contains(x, y)) {
			game.getAudioPlayer().playEffect(1);
			SetGameState(LEVEL_SELECTION); 
		}else if (bEdit.getBounds().contains(x, y)) {
			SetGameState(EDIT);
		}else if (bSettings.getBounds().contains(x, y)) {
			game.getAudioPlayer().playEffect(1);
			SetGameState(SETTINGS);
	    }else if (bLevelSelection.getBounds().contains(x, y)) {
			SetGameState(LEVEL_SELECTION);
        }else if (bTutorial.getBounds().contains(x, y)) {
			SetGameState(TUTORIAL);
        }else if (bQuit.getBounds().contains(x, y)) {
			System.exit(0);
        }
		
	}

	@Override
	public void mouseMoved(int x, int y) {
		bPlaying.setMouseOver(false);
		bEdit.setMouseOver(false);
		bSettings.setMouseOver(false);
		bQuit.setMouseOver(false);
		bLevelSelection.setMouseOver(false);
		bTutorial.setMouseOver(false);

		if (bPlaying.getBounds().contains(x, y))
			bPlaying.setMouseOver(true);
		else if (bEdit.getBounds().contains(x, y))
			bEdit.setMouseOver(true);
		else if (bSettings.getBounds().contains(x, y))
			bSettings.setMouseOver(true);
		else if (bQuit.getBounds().contains(x, y))
			bQuit.setMouseOver(true);
		else if (bTutorial.getBounds().contains(x, y))
			bTutorial.setMouseOver(true);
		else if (bLevelSelection.getBounds().contains(x, y))
			bLevelSelection.setMouseOver(true); 

	}

	@Override
	public void mousePressed(int x, int y) {  

		if (bPlaying.getBounds().contains(x, y))
			bPlaying.setMousePressed(true);
		else if (bEdit.getBounds().contains(x, y))
			bEdit.setMousePressed(true);
		else if (bSettings.getBounds().contains(x, y)) 
			bSettings.setMousePressed(true);
		else if (bQuit.getBounds().contains(x, y))
			bQuit.setMousePressed(true);
		else if (bLevelSelection.getBounds().contains(x, y))
			bLevelSelection.setMousePressed(true);
		else if (bTutorial.getBounds().contains(x, y)) 
			bTutorial.setMousePressed(true);

	}

	@Override 
	public void mouseReleased(int x, int y) {
		resetButtons();
	}

	private void resetButtons() {
		bPlaying.resetBooleans();
		bEdit.resetBooleans();
		bSettings.resetBooleans();
		bQuit.resetBooleans();
		bLevelSelection.resetBooleans();
		bTutorial.resetBooleans();
		
	}

	@Override
	public void mouseDragged(int x, int y) {
		// TODO Auto-generated method stub

	}

}