package scenes;

import managers.EnemyManager;
import managers.TowerManager;
import managers.WaveManager;
import objects.Tower;

public interface Playable {
    boolean isGamePaused();
    void setGamePaused(boolean paused);
    WaveManager getWaveManager();
    EnemyManager getEnemyManger();
    TowerManager getTowerManager();
    void removeTower(Tower tower);
    void upgradeTower(Tower tower);
    void setSelectedTower(Tower tower);
    void update();
    void render(java.awt.Graphics g);
    void mouseClicked(int x, int y);
    void mouseMoved(int x, int y);
    void mousePressed(int x, int y);
    void mouseReleased(int x, int y);
    void mouseDragged(int x, int y);
    void keyPressed(java.awt.event.KeyEvent e);
    void togglePause();
    void resetEverything();
}
