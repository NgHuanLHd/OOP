package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static main.GameStates.*;

import main.Game;
import main.GameStates;

public class KeyboardListener implements KeyListener {
	private Game game;

	public KeyboardListener(Game game) {
		this.game = game;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
	    int keyCode = e.getKeyCode();

	    // Kiểm tra phím P để tạm dừng/trở lại
	    if (keyCode == KeyEvent.VK_P && GameStates.gameState == PLAYING) {
	        game.getPlaying().togglePause(); // Gọi phương thức `togglePause`
	    }

	    // Kiểm tra phím ESC để tiếp tục trò chơi nếu đang tạm dừng
	    if (keyCode == KeyEvent.VK_R && game.getPlaying().isGamePaused()) {
	        game.getPlaying().togglePause(); // Tiếp tục trò chơi (thoát khỏi pause)
	    }

	    // Xử lý sự kiện cho các chế độ khác
	    if (GameStates.gameState == EDIT) {
	        game.getEditor().keyPressed(e);
	    } else if (GameStates.gameState == PLAYING) {
	        game.getPlaying().keyPressed(e); // Đảm bảo xử lý các sự kiện khác cho PLAYING nếu cần.
	    }
	}



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
