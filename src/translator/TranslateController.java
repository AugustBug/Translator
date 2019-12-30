package translator;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class TranslateController {

	// Google API KEY
	// AIza************************P8a4

	private String soundSourceUs;
	private String soundSourceUk;

	public TranslateController() {
		soundSourceUk = "";
		soundSourceUs = "";
	}

	// translates the query string and returns the result strings
	public ArrayList<String> translate(String qry) {
		ArrayList<String> means = new ArrayList<>();

		String url = generateUrlFor(qry);
		try {
			Document doc = Jsoup.connect(url).get();
			Elements items = doc.select("td.tr");
			Elements voicesUs = doc.select("#turengVoiceENTRENus");
			Elements voicesUk = doc.select("#turengVoiceENTRENuk");
			Element voiceUs = null;
			Element voiceUk = null;

			for(int i=0; i<items.size(); i++) {
				addToMeanList(means, items.get(i), qry);
			}

			soundSourceUs = "";
			if(voicesUs.size() > 0) {
				voiceUs = voicesUs.get(0);
				if(voiceUs != null) {
					soundSourceUs = getSoundSourceFrom(voicesUs.toString());
					System.out.println(soundSourceUs);
					soundSourceUs = removeLeadingSlashes(soundSourceUs);
					System.out.println(soundSourceUs);
				}
			}
			soundSourceUk = "";
			if(voicesUk.size() > 0) {
				voiceUk = voicesUk.get(0);
				if(voiceUk != null) {
					soundSourceUk = getSoundSourceFrom(voiceUk.toString());
					soundSourceUk = removeLeadingSlashes(soundSourceUk);
				}
			}


		} catch (IOException e) {
			e.printStackTrace();
		}

		return means;
	}

	// plays US english sound of string
	public void speakEnglishUs() {
		if(soundSourceUs.length() > 0) {
			playSound(soundSourceUs);
		}
	}

	// plays UK english sound of string
	public void speakEnglishUk() {
		if(soundSourceUk.length() > 0) {
			playSound(soundSourceUk);
		}
	}

	// generates search query string based on the text
	private String generateUrlFor(String qry) {
		return "http://tureng.com/tr/turkce-ingilizce/" + qry;
	}
	
	private String removeLeadingSlashes(String s) {
		if(s == null) {
			return null;
		}
		
		while(s.charAt(0) == '/') {
			s = s.substring(1, s.length());
		}
		return s;
	}

	// gets the source of desired sound
	private String getSoundSourceFrom(String str) {
		int index = str.indexOf("src");
		if(index >= 0) {
			String mid1 = str.substring(index);
			int quot1 = mid1.indexOf("\"");
			if(quot1 >= 0) {
				String mid2 = mid1.substring(quot1 + 1);
				int quot2 = mid2.indexOf("\"");
				if(quot2 >= 0) {
					String fin = mid2.substring(0, quot2);
					return fin;
				}
			}
		}

		return "";
	}

	private void addToMeanList(ArrayList<String> means, Element el, String qry) {

		if(el.siblingElements().get(2).child(0).text().equals(qry.trim())) {
			means.add(el.child(0).text());
		}
	}

	// plays the sound
	private void playSound(String src) {
		Media hit = new Media("http://" + src);
		MediaPlayer player = new MediaPlayer(hit);
		player.play();
	}
}
