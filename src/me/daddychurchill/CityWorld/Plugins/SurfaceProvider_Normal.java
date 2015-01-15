package me.daddychurchill.CityWorld.Plugins;

import org.bukkit.Material;
import org.bukkit.util.noise.NoiseGenerator;

import me.daddychurchill.CityWorld.WorldGenerator;
import me.daddychurchill.CityWorld.Plats.PlatLot;
import me.daddychurchill.CityWorld.Plugins.CoverProvider.CoverageType;
import me.daddychurchill.CityWorld.Support.Odds;
import me.daddychurchill.CityWorld.Support.SupportChunk;

public class SurfaceProvider_Normal extends SurfaceProvider {

	public SurfaceProvider_Normal(Odds odds) {
		super(odds);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generateSurfacePoint(WorldGenerator generator, PlatLot lot, SupportChunk chunk, CoverProvider foliage, 
			int x, double perciseY, int z, boolean includeTrees) {
		OreProvider ores = generator.oreProvider;
		int y = NoiseGenerator.floor(perciseY);
		
		// roll the dice
		double primary = odds.getRandomDouble();
		double secondary = odds.getRandomDouble();
		
		// top of the world?
		if (y >= generator.snowLevel) {
			ores.dropSnow(generator, chunk, x, y, z, (byte) NoiseGenerator.floor((perciseY - Math.floor(perciseY)) * 8.0));
		
		// are on a plantable spot?
		} else if (foliage.isPlantable(generator, chunk, x, y, z)) {
			
			// below sea level and plantable.. then cactus?
			if (y <= generator.seaLevel) {
				
				// trees? but only if we are not too close to the edge
				if (includeTrees) {
					if (generator.settings.includeAbovegroundFluids) {

					} else {
						if (primary < cactusOdds && x % 2 == 0 && z % 2 != 0) {
							if (chunk.isSurroundedByEmpty(x, y + 1, z))
								foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.CACTUS);
						}
					}
				}
				
			// regular trees, grass and flowers only
			} else if (y < generator.treeLevel) {

				// trees? but only if we are not too close to the edge of the chunk
				if (includeTrees && primary < treeOdds && x > 0 && x < 15 && z > 0 && z < 15 && x % 2 == 0 && z % 2 != 0) {
					if (secondary < treeAltTallOdds && x > 5 && x < 11 && z > 5 && z < 11)
						foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.TALL_OAK_TREE);
					else if (secondary < treeAltOdds)
						foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.BIRCH_TREE);
					else 
						foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.OAK_TREE);
				
				// foliage?
				} else if (primary < foliageOdds) {
					
					// what to pepper about
					if (secondary < flowerRedOdds)
						foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.POPPY);
					else if (secondary < flowerYellowOdds)
						foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.DANDELION);
					else 
						foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.GRASS);
				}
				
			// regular trees, grass and some evergreen trees... no flowers
			} else if (y < generator.evergreenLevel) {

				// trees? 
				if (includeTrees && primary < treeOdds && x % 2 == 0 && z % 2 != 0) {
					
					// range change?
					if (secondary > ((double) (y - generator.treeLevel) / (double) generator.deciduousRange))
						foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.OAK_TREE);
					else
						foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.PINE_TREE);
				
				// foliage?
				} else if (primary < foliageOdds) {

					// range change?
					if (secondary > ((double) (y - generator.treeLevel) / (double) generator.deciduousRange))
						foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.GRASS);
					else
						foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.FERN);
				}
				
			// evergreen and some grass and fallen snow, no regular trees or flowers
			} else if (y < generator.snowLevel) {
				
				// trees? 
				if (includeTrees && primary < treeOdds && x % 2 == 0 && z % 2 != 0) {
					if (secondary < treeTallOdds)
						foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.PINE_TREE);
					else
						foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.TALL_PINE_TREE);
				
				// foliage?
				} else if (primary < foliageOdds) {
					
					// range change?
					if (secondary > ((double) (y - generator.evergreenLevel) / (double) generator.evergreenRange)) {
						if (odds.playOdds(flowerFernOdds))
							foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.FERN);
					} else {
						generator.oreProvider.dropSnow(generator, chunk, x, y + 5, z);
					}
				}
			}
		
		// can't plant, maybe there is something else I can do
		} else {
			
			// below sea level?
			if (y < generator.seaLevel) {
				
			// at sea level?	
			} else if (y == generator.seaLevel) {
				
				// trees? but only if we are not too close to the edge
				if (includeTrees) {
					if (generator.settings.includeAbovegroundFluids) {
						if (primary < reedOdds)
							if (chunk.isType(x, y, z, Material.SAND))
								if (chunk.isSurroundedByWater(x, y, z))
									foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.REED);
					} else {
						
					}
				}
				
			// regular trees, grass and flowers only
			} else if (y < generator.treeLevel) {

			// regular trees, grass and some evergreen trees... no flowers
			} else if (y < generator.evergreenLevel) {

			// evergreen and some grass and fallen snow, no regular trees or flowers
			} else if (y < generator.snowLevel) {
				
				if (primary < foliageOdds) {
					
					// range change?
					if (secondary > ((double) (y - generator.evergreenLevel) / (double) generator.evergreenRange)) {
						if (odds.playOdds(0.10) && foliage.isPlantable(generator, chunk, x, y, z))
							foliage.generateCoverage(generator, chunk, x, y + 1, z, CoverageType.FERN);
					} else {
						ores.dropSnow(generator, chunk, x, y, z);
					}
				}
			}
		}
	}
}
