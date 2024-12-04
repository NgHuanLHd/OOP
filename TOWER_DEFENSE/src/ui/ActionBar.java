package ui;

import static main.GameStates.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import helpz.Constants.Towers;
import helpz.LoadSave;
import objects.Tower;
import scenes.OptionsWindow;
import scenes.Playable;
import scenes.Playing;
import scenes.Settings;

public class ActionBar extends Bar {
	private Playable playing;
	//private Playing playing;
	private MyButton bMenu, bPause;

	private MyButton[] towerButtons;
	private Tower selectedTower;
	private Tower displayedTower;
	private MyButton sellTower, upgradeTower;

	private DecimalFormat formatter;

	private int gold = 200;
	private boolean showTowerCost;
	private int towerCostType;

	private int lives = 1;
	
	private OptionsWindow optionsWindow;
	private boolean isOptionsWindowVisible = false;

	private BufferedImage pauseImage ,resumeImage;

	public ActionBar(int x, int y, int width, int height, Playable playing) {
	//public ActionBar(int x, int y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		//this.playable = playable;
		formatter = new DecimalFormat("0.0");
		
		pauseImage = LoadSave.getImageFromFile("pause.png");
		resumeImage = LoadSave.getImageFromFile("resume.png");

		initButtons();
	}

	public void resetEverything() {
		lives = 1;
		towerCostType = 0;
		showTowerCost = false;
		gold = 200;
		selectedTower = null; 
		displayedTower = null;
	}

	private void initButtons() {

		bMenu = new MyButton("Menu", 2, 642, 100, 30);
		bPause = new MyButton("Pause", 2, 642, 28, 28);

		towerButtons = new MyButton[4];

		int w = 50;
		int h = 50;
		int xStart = 50;
		int yStart = 650;
		int xOffset = (int) (w * 1.1f);

		for (int i = 0; i < towerButtons.length; i++)
			towerButtons[i] = new MyButton("", xStart + xOffset * i, yStart, w, h, i);


		sellTower = new MyButton("Sell", 420, 702, 80, 25);
		upgradeTower = new MyButton("Upgrade", 545, 702, 80, 25);

	}

	public void removeOneLife() {
		lives--;
		if (lives <= 0)
			SetGameState(GAME_OVER);
	}

	private void drawButtons(Graphics g) {
		//bMenu.draw(g);
		
		
		if (playing.isGamePaused() && resumeImage != null) {
		    g.drawImage(resumeImage, bPause.x, bPause.y, bPause.width, bPause.height, null); // Vẽ ảnh Resume khi game tạm dừng
		} else if (!playing.isGamePaused() && pauseImage != null) {
		    g.drawImage(pauseImage, bPause.x, bPause.y, bPause.width, bPause.height, null); // Vẽ ảnh Pause khi game đang chạy
		} else {
		    bPause.draw(g); // Nếu không tải được ảnh, hiển thị nút như bình thường
		}


		for (MyButton b : towerButtons) {
			g.setColor(Color.gray);
			g.fillRect(b.x, b.y, b.width, b.height);
			int towerImgIndex = b.getId() * 3;
			g.drawImage(playing.getTowerManager().getTowerImgs()[towerImgIndex], b.x, b.y, b.width, b.height, null);
			drawButtonFeedback(g, b);
		}
	}

	public void draw(Graphics g) {

		// Background
		g.setColor(new Color(220, 123, 15));
		g.fillRect(x, y, width, height);

		// Buttons
		drawButtons(g);

		// DisplayedTower
		drawDisplayedTower(g);

		// Wave info
		drawWaveInfo(g);

		// Gold info
		drawGoldAmount(g);

		// Draw Tower Cost
		if (showTowerCost)
			drawTowerCost(g);

		// Game paused text
		/*if (playing.isGamePaused()) {
			g.setColor(Color.black);
			g.drawString("Game is Paused!", 110, 500);
			
			g.drawRect(140, 100, 400, 600);
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(140, 100, 400, 600);
		}*/

		// Lives
		g.setColor(Color.black); 
		g.drawString("Lives: " + lives, 50, 735);
		 
		/*//note
		g.setColor(Color.MAGENTA);
	    g.drawString("Preparation time for ", 110, 755 );
	    g.drawString("  each wave is 10s", 110, 770);  */ 
	    
	    if (isOptionsWindowVisible) {
	        optionsWindow.render(g);  // Gọi phương thức draw() của OptionsWindow để vẽ nó lên màn hình
	    }

	}
	
	
	

	private void drawTowerCost(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(280, 650, 120, 50);
		g.setColor(Color.black);
		g.drawRect(280, 650, 120, 50);

		g.drawString("" + getTowerCostName(), 285, 670);
		g.drawString("Cost: " + getTowerCostCost() + "g", 285, 695);

		// Show this if player lacks gold for the selected tower.
		if (isTowerCostMoreThanCurrentGold()) {
			g.setColor(Color.RED);
			g.drawString("Can't Afford", 270, 725);

		}

	}

	private boolean isTowerCostMoreThanCurrentGold() {
		return getTowerCostCost() > gold;
	}

	private String getTowerCostName() {
		return helpz.Constants.Towers.GetName(towerCostType);
	}

	private int getTowerCostCost() {
		return helpz.Constants.Towers.GetTowerCost(towerCostType);
	}

	private void drawGoldAmount(Graphics g) {
		g.drawString("Gold: " + gold + "g", 50, 717);

	}

	private void drawWaveInfo(Graphics g) {
		g.setColor(Color.black);
		g.setFont(new Font("LucidaSans", Font.BOLD, 20));
		drawWaveTimerInfo(g);
		drawEnemiesLeftInfo(g);
		drawWavesLeftInfo(g);

	}

	private void drawWavesLeftInfo(Graphics g) {
		int current = playing.getWaveManager().getWaveIndex();
		int size = playing.getWaveManager().getWaves().size();
		g.drawString("Wave " + (current + 1) + " / " + size, 425, 770);

	}

	private void drawEnemiesLeftInfo(Graphics g) {
		int remaining = playing.getEnemyManger().getAmountOfAliveEnemies();
		g.drawString("Enemies Left: " + remaining, 425, 790);
	}

	private void drawWaveTimerInfo(Graphics g) {
		
		if (playing.getWaveManager().isPreparationActive()) {
	        float preparationTime = playing.getWaveManager().getPreparationTime();
	        String formattedText = formatter.format(preparationTime);
	        g.drawString("Wave starts in: " + formattedText + "s", 425, 750);   
		} else if (playing.getWaveManager().isWaveTimerStarted()) {

			float timeLeft = playing.getWaveManager().getTimeLeft();
			String formattedText = formatter.format(timeLeft);
			g.drawString("Time Left: " + formattedText, 425, 750);
		}
	}

	private void drawDisplayedTower(Graphics g) {
		if (displayedTower != null) {
			g.setColor(Color.gray);
			g.fillRect(410, 645, 220, 85);
			g.setColor(Color.black);
			g.drawRect(410, 645, 220, 85);
			g.drawRect(420, 650, 50, 50);
			g.drawImage(playing.getTowerManager().getTowerImgs()[displayedTower.getTowerType()*3], 420, 650, 50, 50, null);
			g.setFont(new Font("LucidaSans", Font.BOLD, 15));
			g.drawString("" + Towers.GetName(displayedTower.getTowerType()), 480, 660);
			g.drawString("ID: " + displayedTower.getId(), 480, 675);
			g.drawString("Tier: " + displayedTower.getTier(), 560, 660);
			drawDisplayedTowerBorder(g);
			drawDisplayedTowerRange(g);

			// Sell button
			sellTower.draw(g);
			drawButtonFeedback(g, sellTower);

			// Upgrade Button
			if (displayedTower.getTier() < 3 && gold >= getUpgradeAmount(displayedTower)) {
				upgradeTower.draw(g);
				drawButtonFeedback(g, upgradeTower);
			}

			if (sellTower.isMouseOver()) {
				g.setColor(Color.red);
				g.drawString("Sell for: " + getSellAmount(displayedTower) + "g", 480, 695);
			} else if (upgradeTower.isMouseOver() && gold >= getUpgradeAmount(displayedTower)) {
				g.setColor(Color.blue);
				g.drawString("Upgrade for: " + getUpgradeAmount(displayedTower) + "g", 480, 695);
			}

		}

	}

	private int getUpgradeAmount(Tower displayedTower) {
		return (int) (helpz.Constants.Towers.GetTowerCost(displayedTower.getTowerType()) * 0.3f);
	}

	private int getSellAmount(Tower displayedTower) {
		int upgradeCost = (displayedTower.getTier() - 1) * getUpgradeAmount(displayedTower);
		upgradeCost *= 0.5f;

		return helpz.Constants.Towers.GetTowerCost(displayedTower.getTowerType()) / 2 + upgradeCost;
	}

	private void drawDisplayedTowerRange(Graphics g) {
		g.setColor(Color.white);
		g.drawOval(displayedTower.getX() + 16 - (int) (displayedTower.getRange() * 2) / 2, displayedTower.getY() + 16 - (int) (displayedTower.getRange() * 2) / 2, (int) displayedTower.getRange() * 2,
				(int) displayedTower.getRange() * 2);

	}

	private void drawDisplayedTowerBorder(Graphics g) {

		g.setColor(Color.CYAN);
		g.drawRect(displayedTower.getX(), displayedTower.getY(), 32, 32);

	}

	public void displayTower(Tower t) {
		displayedTower = t;
	}

	private void sellTowerClicked() {
		playing.removeTower(displayedTower);
		gold += helpz.Constants.Towers.GetTowerCost(displayedTower.getTowerType()) / 2;

		int upgradeCost = (displayedTower.getTier() - 1) * getUpgradeAmount(displayedTower);
		upgradeCost *= 0.5f;
		gold += upgradeCost;

		displayedTower = null;

	}

	private void upgradeTowerClicked() {
		playing.upgradeTower(displayedTower);
		gold -= getUpgradeAmount(displayedTower);

	}
	
	private void togglePause() {
		playing.setGamePaused(!playing.isGamePaused());

		if (playing.isGamePaused())
			bPause.setText("Continue");
		else
			bPause.setText("Pause");

	}
	


	public void mouseClicked(int x, int y) {
			/*if (bMenu.getBounds().contains(x, y)) {
				SetGameState(MENU);
				}
			else*/
		    if (bPause.getBounds().contains(x, y))
				togglePause();
			else {

				if (displayedTower != null) {
					if (sellTower.getBounds().contains(x, y)) {
						sellTowerClicked();

						return;
					} else if (upgradeTower.getBounds().contains(x, y) && displayedTower.getTier() < 3 && gold >= getUpgradeAmount(displayedTower)) {
						upgradeTowerClicked();
						return;
					}
				}

				for (MyButton b : towerButtons) {
					if (b.getBounds().contains(x, y)) {
						if (!isGoldEnoughForTower(b.getId()))
							return;

						selectedTower = new Tower(0, 0, -1, b.getId());
						playing.setSelectedTower(selectedTower);
						return;
					}
				}
			}
		
	}

	private boolean isGoldEnoughForTower(int towerType) {

		return gold >= helpz.Constants.Towers.GetTowerCost(towerType);
	}

	public void mouseMoved(int x, int y) {
		bMenu.setMouseOver(false);
		bPause.setMouseOver(false);
		showTowerCost = false;
		sellTower.setMouseOver(false);
		upgradeTower.setMouseOver(false);

		for (MyButton b : towerButtons)
			b.setMouseOver(false);

		if (bMenu.getBounds().contains(x, y))
			bMenu.setMouseOver(true);
		else if (bPause.getBounds().contains(x, y))
			bPause.setMouseOver(true);
		else {

			if (displayedTower != null) {
				if (sellTower.getBounds().contains(x, y)) {
					sellTower.setMouseOver(true);
					return;
				} else if (upgradeTower.getBounds().contains(x, y) && displayedTower.getTier() < 3) {
					upgradeTower.setMouseOver(true);
					return;
				}
			}

			for (MyButton b : towerButtons)
				if (b.getBounds().contains(x, y)) {
					b.setMouseOver(true);
					showTowerCost = true;
					towerCostType = b.getId();
					return;
				}
		}
	}

	public void mousePressed(int x, int y) {
		if (bMenu.getBounds().contains(x, y))
			bMenu.setMousePressed(true);
		else if (bPause.getBounds().contains(x, y))
			bPause.setMousePressed(true);
		else {

			if (displayedTower != null) {
				if (sellTower.getBounds().contains(x, y)) {
					sellTower.setMousePressed(true);
					return;
				} else if (upgradeTower.getBounds().contains(x, y) && displayedTower.getTier() < 3) {
					upgradeTower.setMousePressed(true);
					return;
				}
			}

			for (MyButton b : towerButtons)
				if (b.getBounds().contains(x, y)) { 
					b.setMousePressed(true);
					return;
				}
		}

	}

	public void mouseReleased(int x, int y) {
		bMenu.resetBooleans();
		bPause.resetBooleans();
		for (MyButton b : towerButtons)
			b.resetBooleans();
		sellTower.resetBooleans();
		upgradeTower.resetBooleans();

	}

	public void payForTower(int towerType) {
		this.gold -= helpz.Constants.Towers.GetTowerCost(towerType);

	}

	public void addGold(int getReward) {
		this.gold += getReward;
	}

	public int getLives() {
		return lives;
	}
	

}
