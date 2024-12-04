package scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.BasicStroke;
import static main.GameStates.*;
import main.Game;
import ui.MyButton;



public class OptionsWindow {

    private Game game;
    
    private int sliderX, sliderY, sliderWidth, sliderHeight;
    private int knobX, knobY, knobWidth, knobHeight;
    private boolean draggingKnob = false; // Cờ để kiểm tra xem người dùng có kéo nút không
    private float volumeLevel = 0.1f ; // Mức âm lượng mặc định (0.0 - 1.0)
    
    private int effectSliderX, effectSliderY, effectSliderWidth, effectSliderHeight;
    private int effectKnobX, effectKnobY, effectKnobWidth, effectKnobHeight;
    private boolean draggingEffectKnob = false; // Cờ để kiểm tra kéo nút hiệu ứng
    private float effectVolumeLevel = 0.1f; // Mức âm lượng mặc định của Effect

    private MyButton bMenu;
    
    public OptionsWindow(Game game) {
        this.game = game;
        initSlider();
        initButton();
    }

    private void initButton() {
    	bMenu = new MyButton("<Menu", 130, 650, 80, 40);
	}

	private void initSlider() {
		// Thanh trượt nhạc nền
	    sliderX = 340;
	    sliderY = 230;
	    sliderWidth = 150;
	    sliderHeight = 14;

	    knobWidth = 15;
	    knobHeight = 26;
	    knobX = sliderX + (int) (volumeLevel * sliderWidth) - knobWidth / 2;
	    knobY = sliderY - knobHeight / 2;

	    // Thanh trượt Effect
	    effectSliderX = 340;
	    effectSliderY = 300; 
	    effectSliderWidth = 150;
	    effectSliderHeight = 14;

	    effectKnobWidth = 15;
	    effectKnobHeight = 26;
	    effectKnobX = effectSliderX + (int) (effectVolumeLevel * effectSliderWidth) - effectKnobWidth / 2;
	    effectKnobY = effectSliderY - effectKnobHeight / 2;

        // Đặt âm lượng ban đầu
        if (game.getAudioPlayer() != null) {
        	
        	game.getAudioPlayer().playBackgroundMusic(0);
        	game.getAudioPlayer().setBackgroundMusicVolume(volumeLevel);
        	game.getAudioPlayer().setEffectVolume(effectVolumeLevel);
        }
    }
    
    public void setVolumeLevel(float volumeLevel) {
        this.volumeLevel = volumeLevel;
        knobX = sliderX + (int) (volumeLevel * sliderWidth) - knobWidth / 2;
    }
    
    public void setEffectVolumeLevel(float effectVolumeLevel) {
    	this.effectVolumeLevel = effectVolumeLevel;
        effectKnobX = effectSliderX + (int) (effectVolumeLevel * effectSliderWidth) - effectKnobWidth / 2;
    }


    public void render(Graphics g) {
    	
        // Ép kiểu Graphics thành Graphics2D để sử dụng các tính năng vẽ nâng cao
        Graphics2D g2d = (Graphics2D) g;  
        
        g2d.setColor(new Color(0, 0, 0, 150)); 
        g2d.fillRoundRect(120, 100, 400, 600, 35, 35);  
        
        bMenu.draw(g);
        
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(120, 100, 400, 600, 35, 35);  
 
        drawSliderMusic(g);
        drawSliderEffectMusic(g);

        // Hiển thị mức âm lượng (0% - 100%)
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString("Volume: " + (int) (volumeLevel * 100) + "%", sliderX, sliderY - 20);
        g.drawString("Volume: " + (int) (effectVolumeLevel * 100) + "%", sliderX, sliderY + 50);

        
    }
    

    private void drawSliderEffectMusic(Graphics g) {
    	g.setColor(Color.WHITE);
		g.drawString("EFFECT", 200, 310);
		
    	g.setColor(Color.GRAY);
        g.fillRect(effectSliderX, effectSliderY - effectSliderHeight / 2, effectSliderWidth, effectSliderHeight);

        g.setColor(Color.WHITE);
        g.fillRect(effectSliderX, effectSliderY - effectSliderHeight / 2, (int) (effectVolumeLevel * effectSliderWidth), effectSliderHeight);

        g.setColor(Color.WHITE);
        g.fillRect(effectKnobX, effectKnobY, effectKnobWidth, effectKnobHeight);
    }
    
	private void drawSliderMusic(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("MUSIC", 200, 240);
		
        g.setColor(Color.GRAY);
        g.fillRect(sliderX, sliderY - sliderHeight / 2, sliderWidth, sliderHeight);

        g.setColor(Color.WHITE);
        g.fillRect(sliderX, sliderY - sliderHeight / 2, (int) (volumeLevel * sliderWidth), sliderHeight);

        g.setColor(Color.WHITE);
        g.fillRect(knobX, knobY, knobWidth, knobHeight);
    }
	
	public void mousePressed(int x, int y) {
	    if (isKnobClicked(x, y)) {
	        draggingKnob = true;
	    } else if (isEffectKnobClicked(x, y)) {
	        draggingEffectKnob = true;
	    } else if (bMenu.getBounds().contains(x, y)) {
	        bMenu.setMousePressed(true);
	    }
	}



	public void mouseReleased(int x, int y) {
	    if (bMenu.getBounds().contains(x, y) && bMenu.isMousePressed()) {
	        SetGameState(MENU);
	    }
	    bMenu.resetBooleans();
	    draggingKnob = false;
	    draggingEffectKnob = false;
	}


	public void mouseDragged(int x, int y) {
	    if (draggingKnob) {
	        knobX = Math.max(sliderX, Math.min(x - knobWidth / 2, sliderX + sliderWidth - knobWidth));
	        volumeLevel = (float) (knobX - sliderX) / (sliderWidth - knobWidth);

	        if (game.getAudioPlayer() != null) {
	            game.getAudioPlayer().setBackgroundMusicVolume(volumeLevel);
	        }
	        game.setVolumeLevel(volumeLevel);
	    } else if (draggingEffectKnob) {
	        effectKnobX = Math.max(effectSliderX, Math.min(x - effectKnobWidth / 2, effectSliderX + effectSliderWidth - effectKnobWidth));
	        effectVolumeLevel = (float) (effectKnobX - effectSliderX) / (effectSliderWidth - effectKnobWidth);
	        
	        if (game.getAudioPlayer() != null) {
	            game.getAudioPlayer().setEffectVolume(effectVolumeLevel);	        
	        }
	        game.setEffectVolumeLevel(effectVolumeLevel);
	    }
	}


    public void mouseClicked(int x, int y) {
        if (bMenu.getBounds().contains(x, y)) {
            SetGameState(MENU); 
        }
    }

    // Kiểm tra xem nút kéo có được nhấn hay không
    private boolean isKnobClicked(int x, int y) {
        return x >= knobX && x <= knobX + knobWidth && y >= knobY && y <= knobY + knobHeight;
    }
    
    private boolean isEffectKnobClicked(int x, int y) {
        return x >= effectKnobX && x <= effectKnobX + effectKnobWidth && y >= effectKnobY && y <= effectKnobY + effectKnobHeight;
    }

}
