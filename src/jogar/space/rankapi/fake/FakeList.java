package jogar.space.rankapi.fake;

import java.util.ArrayList;
import java.util.Random;

public class FakeList {
 public static ArrayList<String> nicks = new ArrayList<>();
 public static void populate() {
	 nicks.add("SashaHoot");
	 nicks.add("Hootsan");
	 nicks.add("Mayegg");
	 nicks.add("Kivasan");
	 nicks.add("Kivakun");
	 nicks.add("VanXenial");
	 nicks.add("DasXenial");
	 nicks.add("Stumpy");
 }
 public static String random() {
	 Random r = new Random();
	 int ri = r.nextInt(nicks.size());
	 return nicks.get(ri);
 }
}
