import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Audio {

	public static Map<String, Sound> soundMap = new HashMap<String, Sound>();
	public static Map<String, Music> musicMap = new HashMap<String, Music>();

	public static void loadSounds() {
		try {
			soundMap.put("menu_sound", new Sound("resources/Menu_Click_Sound.ogg"));
			musicMap.put("music", new Music("resources/Background.ogg"));
			soundMap.put("gameOver", new Sound("resources/you_are_an_idiot.ogg"));
			soundMap.put("levelUp", new Sound("resources/LevelUp.ogg"));
			soundMap.put("Winning", new Sound("resources/Winning.ogg"));
			soundMap.put("takeItem", new Sound("resources/Take_Item.ogg"));
			soundMap.put("addRandomItem", new Sound("resources/Click1.ogg"));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public static Music getMusic(String key) {
		return musicMap.get(key);
	}

	public static Sound getSound(String key) {
		return soundMap.get(key);
	}

}
