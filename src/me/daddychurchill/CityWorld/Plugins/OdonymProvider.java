package me.daddychurchill.CityWorld.Plugins;

import java.util.Random;

import me.daddychurchill.CityWorld.WorldGenerator;
import me.daddychurchill.CityWorld.Support.Odds;

public abstract class OdonymProvider extends Provider {

	public abstract String[] generateNorthSouthOdonym(WorldGenerator generator, int x, int z);
	public abstract String[] generateWestEastOdonym(WorldGenerator generator, int x, int z);
	
	// yep it is a little one... we will make it bigger in a moment
	int baseSeed;
	
	public OdonymProvider(int baseSeed) {
		super();
		this.baseSeed = baseSeed;
	}
	
	protected Random getRandomFor(int i) {
		return new Random((long) i * (long) Integer.MAX_VALUE + (long) baseSeed);
	}
	
	public void decaySign(Odds odds, String[] text) {
		for (int i = 0; i < text.length; i++) {
			text[i] = decayLine(odds, text[i]);
		}
	}
	
	private final static double oddsOfDecay = Odds.oddsExtremelyLikely;
	
	public String decayLine(Odds odds, String line) {
		String result = "";
		for (int i = 0; i < line.length(); i++) {
			if (odds.playOdds(oddsOfDecay))
				result = result + line.charAt(i);
			else
				result = result + ' ';
		}
		return result;
	}
	
	// Based on work contributed by drew-bahrue (https://github.com/echurchill/CityWorld/pull/2)
	public static OdonymProvider loadProvider(WorldGenerator generator, Odds odds) {

		OdonymProvider provider = null;
		
//		// need something like PhatLoot but for Odonym
//		provider = OdonymProvider_PhatOdonym.loadPhatOdonym();
		
		// default to stock OreProvider
		if (provider == null) {
			
//			if (generator.settings.environment == Environment.NETHER)
//				provider = new NameProvider_Nether(random);
//			else if (generator.settings.environment == Environment.THE_END)
//				provider = new NameProvider_TheEnd(random);
//			else
				provider = new OdonymProvider_Normal(odds.getRandomInt());
		}
	
		return provider;
	}
	
}
