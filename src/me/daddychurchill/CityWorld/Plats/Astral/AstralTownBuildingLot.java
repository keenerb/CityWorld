package me.daddychurchill.CityWorld.Plats.Astral;

import me.daddychurchill.CityWorld.WorldGenerator;
import me.daddychurchill.CityWorld.Context.DataContext;
import me.daddychurchill.CityWorld.Plats.Nature.BunkerLot;
import me.daddychurchill.CityWorld.Plats.Nature.BunkerLot.BunkerType;
import me.daddychurchill.CityWorld.Support.Odds;
import me.daddychurchill.CityWorld.Support.PlatMap;
import me.daddychurchill.CityWorld.Support.RealChunk;

public class AstralTownBuildingLot extends AstralTownEmptyLot {

	BunkerType buildingType;
	public AstralTownBuildingLot(PlatMap platmap, int chunkX, int chunkZ, BunkerType bunkerType) {
		super(platmap, chunkX, chunkZ);

		this.buildingType = bunkerType;
	}
	
	// This doesn't return road tunnels, entry or missiles
	public static BunkerType pickBuildingType(Odds odds) {
		switch (odds.getRandomInt(7)) {
		case 1:
			return BunkerType.BALLSY;
		case 2:
			return BunkerType.FLOORED;
		case 3:
			return BunkerType.GROWING;
		case 4:
			return BunkerType.PYRAMID;
		case 5:
			return BunkerType.QUAD;
		case 6:
			return BunkerType.RECALL;
		default:
			return BunkerType.TANK;
		}
	}

	@Override
	protected void generateActualBlocks(WorldGenerator generator,
			PlatMap platmap, RealChunk chunk, DataContext context,
			int platX, int platZ) {
		
		super.generateActualBlocks(generator, platmap, chunk, context, platX, platZ);
		
		int levelY = generator.seaLevel + aboveSeaLevel;
		int segmentY = (context.buildingMaximumY - levelY) / 6;
		int topY = chunkOdds.getRandomInt(segmentY * 2, segmentY * 4) + levelY;
		
		switch (buildingType) {
		case BALLSY:
			BunkerLot.generateBallsyBunker(generator, context, chunk, chunkOdds, levelY, topY);
			break;
		case ENTRY:
//			generateEntryBunker(generator, context, chunk, chunkOdds, levelY, yPlatformTop, yTop2, yPlatformTop);
			break;
		case FLOORED:
			BunkerLot.generateFlooredBunker(generator, context, chunk, chunkOdds, levelY, topY);
			break;
		case GROWING:
			BunkerLot.generateGrowingBunker(generator, context, chunk, chunkOdds, levelY, topY);
			break;
		case PYRAMID:
			BunkerLot.generatePyramidBunker(generator, context, chunk, chunkOdds, levelY, topY);
			break;
		case QUAD:
			BunkerLot.generateQuadBunker(generator, context, chunk, chunkOdds, levelY, topY);
			break;
		case RECALL:
			BunkerLot.generateRecallBunker(generator, context, chunk, chunkOdds, levelY, topY);
			break;
		case TANK:
			BunkerLot.generateTankBunker(generator, context, chunk, chunkOdds, levelY, topY);
			break;
//		case MISSILE:
//			return generateMissileBunker(generator, context, chunk, chunkOdds, yPlatform + 1, yPlatformTop, surfaceY);
		case ROAD:
//			generateRoadTunnel(generator, context, chunk, chunkOdds, yPlatform + 1, yPlatformTop);
			break;
		}
	}

}
