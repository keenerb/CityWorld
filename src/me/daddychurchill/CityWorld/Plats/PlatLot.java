package me.daddychurchill.CityWorld.Plats;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;

import me.daddychurchill.CityWorld.CityWorldGenerator;
import me.daddychurchill.CityWorld.Context.DataContext;
import me.daddychurchill.CityWorld.Plugins.LootProvider.LootLocation;
import me.daddychurchill.CityWorld.Plugins.OreProvider.OreLocation;
import me.daddychurchill.CityWorld.Plugins.SpawnProvider.SpawnerLocation;
import me.daddychurchill.CityWorld.Support.ShortChunk;
import me.daddychurchill.CityWorld.Support.CachedYs;
import me.daddychurchill.CityWorld.Support.Direction;
import me.daddychurchill.CityWorld.Support.PlatMap;
import me.daddychurchill.CityWorld.Support.Direction.Stair;
import me.daddychurchill.CityWorld.Support.Odds;
import me.daddychurchill.CityWorld.Support.RealChunk;
import me.daddychurchill.CityWorld.Support.SupportChunk;

public abstract class PlatLot {
	
	// extremes
	protected int chunkX;
	protected int chunkZ;
	protected CachedYs blockYs;
	
	protected Odds platmapOdds;
	protected Odds chunkOdds;
	
	// styling!
	public enum LotStyle {NATURE, STRUCTURE, ROAD, ROUNDABOUT};
	public LotStyle style;
	public boolean trulyIsolated;
	
	public PlatLot(PlatMap platmap, int chunkX, int chunkZ) {
		super();
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.style = LotStyle.NATURE;
		this.trulyIsolated = false;
		
		initializeDice(platmap, chunkX, chunkZ);

		// precalc the Ys
		blockYs = platmap.generator.shapeProvider.getCachedYs(platmap.generator, chunkX, chunkZ);
	}

	public abstract long getConnectedKey();
	public abstract boolean makeConnected(PlatLot relative);
	public abstract boolean isConnectable(PlatLot relative);
	public abstract boolean isConnected(PlatLot relative);
	
	public abstract PlatLot newLike(PlatMap platmap, int chunkX, int chunkZ);

	protected abstract void generateActualChunk(CityWorldGenerator generator, PlatMap platmap, ShortChunk chunk, BiomeGrid biomes, DataContext context, int platX, int platZ);
	protected abstract void generateActualBlocks(CityWorldGenerator generator, PlatMap platmap, RealChunk chunk, DataContext context, int platX, int platZ);

	protected void reportLocation(CityWorldGenerator generator, String title, int originX, int originZ) {
		if (generator.settings.broadcastSpecialPlaces)
			generator.reportMessage(title + " placed at " + originX + ", " + originZ);
	}
	
	public Biome getChunkBiome() {
		return Biome.PLAINS;
	}
	
	public boolean isPlaceableAt(CityWorldGenerator generator, int chunkX, int chunkZ) {
		return generator.settings.inCityRange(chunkX, chunkZ);
	}
	
	public PlatLot validateLot(PlatMap platmap, int platX, int platZ) {
		return null; // assume that we don't do anything
	}
	
	public RoadLot repaveLot(CityWorldGenerator generator, PlatMap platmap) {
		return null; // same here
	}
	
	private void initializeDice(PlatMap platmap, int chunkX, int chunkZ) {
		
		// reset and pick up the dice
		platmapOdds = platmap.getOddsGenerator();
		chunkOdds = platmap.getChunkOddsGenerator(chunkX, chunkZ);
	}
	
	protected int getSidewalkLevel(CityWorldGenerator generator) {
		return generator.streetLevel + 1;
	}
	
	protected Material getSidewalkMaterial() {
		return Material.STEP;
	}
	
	protected int getBlockY(int x, int z) {
		return blockYs == null ? 0 : blockYs.getBlockY(x, z);
	}
	
	protected double getPerciseY(int x, int z) {
		return blockYs == null ? 0 : blockYs.getPerciseY(x, z);
	}
	
	protected int getSurfaceAtY(int x, int z) {
		return getSurfaceAtY(x, 15 - x, z, 15 - z);
	}
	
	protected int getSurfaceAtY(int x1, int x2, int z1, int z2) {
		int surfaceY = Math.min(getBlockY(x1, z1), getBlockY(x2, z1));
		surfaceY = Math.min(surfaceY, getBlockY(x1, z2));
		surfaceY = Math.min(surfaceY, getBlockY(x2, z2));
		return surfaceY;
	}
	
	public abstract int getBottomY(CityWorldGenerator generator);
	public abstract int getTopY(CityWorldGenerator generator);
	
	public void generateChunk(CityWorldGenerator generator, PlatMap platmap, ShortChunk chunk, BiomeGrid biomes, DataContext context, int platX, int platZ) {
		initializeDice(platmap, chunk.chunkX, chunk.chunkZ);
		
		// what do we need to first?
		generator.shapeProvider.preGenerateChunk(generator, this, chunk, biomes, blockYs);
		
		// let the specialized platlot do it's thing
		generateActualChunk(generator, platmap, chunk, biomes, context, platX, platZ);
		
		// polish things off
		generator.shapeProvider.postGenerateChunk(generator, this, chunk, blockYs);
	}
		
	public void generateBlocks(CityWorldGenerator generator, PlatMap platmap, RealChunk chunk, DataContext context, int platX, int platZ) {
		initializeDice(platmap, chunk.chunkX, chunk.chunkZ);
		
		// what do we need to first?
		generator.shapeProvider.preGenerateBlocks(generator, this, chunk, blockYs);
		
		// let the specialized platlot do it's thing
		generateActualBlocks(generator, platmap, chunk, context, platX, platZ);
		
		// polish things off
		generator.shapeProvider.postGenerateBlocks(generator, this, chunk, blockYs);
	}
	
	protected void destroyLot(CityWorldGenerator generator, int y1, int y2) {
		int x1 = chunkX * SupportChunk.chunksBlockWidth;
		int z1 = chunkZ * SupportChunk.chunksBlockWidth;
		generator.decayBlocks.destroyWithin(x1, x1 + SupportChunk.chunksBlockWidth, y1, y2, z1, z1 + SupportChunk.chunksBlockWidth);
	}
	
	public void destroyBuilding(CityWorldGenerator generator, int y, int floors) {
		destroyLot(generator, y, y + DataContext.FloorHeight * (floors + 1));
	}
	
	private final static int lowestMineSegment = 16;
	
	public void generateMines(CityWorldGenerator generator, ShortChunk chunk) {
		
		// get shafted! (this builds down to keep the support poles happy)
		if (generator.settings.includeMines)
			for (int y = (blockYs.minHeight / 16 - 1) * 16; y >= lowestMineSegment; y -= 16) {
				if (isShaftableLevel(generator, y))
					generateHorizontalMineLevel(generator, chunk, y);
			}
	}
	
	protected int findHighestShaftableLevel(CityWorldGenerator generator, DataContext context, SupportChunk chunk) {

		// keep going down until we find what we are looking for
		for (int y = (blockYs.minHeight / 16 - 1) * 16; y >= lowestMineSegment; y -= 16) {
			if (isShaftableLevel(generator, y) && generator.shapeProvider.isHorizontalWEShaft(chunk.chunkX, y, chunk.chunkZ))
				return y + 7;
		}
		
		// nothing found
		return 0;
	}
	
	protected boolean isShaftableLevel(CityWorldGenerator generator, int blockY) {
		return blockY >= lowestMineSegment && blockY < blockYs.minHeight && blockYs.minHeight > generator.seaLevel;
	}

	private void generateHorizontalMineLevel(CityWorldGenerator generator, ShortChunk chunk, int y) {
		int y1 = y + 6;
		int y2 = y1 + 1;
		
		// draw the shafts/walkways
		boolean pathFound = false;
		if (generator.shapeProvider.isHorizontalNSShaft(chunk.chunkX, y, chunk.chunkZ)) {
			generateMineShaftSpace(generator, chunk, 6, 10, y1, y1 + 4, 0, 6);
			generateMineNSSupport(chunk, 6, y2, 1);
			generateMineNSSupport(chunk, 6, y2, 4);
			generateMineShaftSpace(generator, chunk, 6, 10, y1, y1 + 4, 10, 16);
			generateMineNSSupport(chunk, 6, y2, 11);
			generateMineNSSupport(chunk, 6, y2, 14);
			pathFound = true;
		}
		if (generator.shapeProvider.isHorizontalWEShaft(chunk.chunkX, y, chunk.chunkZ)) {
			generateMineShaftSpace(generator, chunk, 0, 6, y1, y1 + 4, 6, 10);
			generateMineWESupport(chunk, 1, y2, 6);
			generateMineWESupport(chunk, 4, y2, 6);
			generateMineShaftSpace(generator, chunk, 10, 16, y1, y1 + 4, 6, 10);
			generateMineWESupport(chunk, 11, y2, 6);
			generateMineWESupport(chunk, 14, y2, 6);
			pathFound = true;
		}
		
		// draw the center bit
		if (pathFound)
			generateMineShaftSpace(generator, chunk, 6, 10, y1, y1 + 4, 6, 10);
	}
	
	private final static short shaftBridgeId = ShortChunk.WOOD; 
	private final static short shaftSupportId = ShortChunk.FENCE;
	private final static short shaftBeamId = ShortChunk.WOOD;

	private void generateMineShaftSpace(CityWorldGenerator generator, ShortChunk chunk, int x1, int x2, int y1, int y2, int z1, int z2) {
		chunk.setEmptyBlocks(x1, x2, y1, z1, z2, shaftBridgeId);
		chunk.setBlocks(x1, x2, y1 + 1, y2, z1, z2, getAirMaterial(generator, y1 + 1));
	}
	
	private void generateMineNSSupport(ShortChunk chunk, int x, int y, int z) {
		
		// on a bridge
		if (chunk.isType(x, y - 1, z, shaftBridgeId) && 
			chunk.isType(x + 3, y - 1, z, shaftBridgeId)) {
			
			// place supports
			generateMineSupport(chunk, x, y - 1, z);
			generateMineSupport(chunk, x + 3, y - 1, z);
			
		// in a tunnel
		} else {
			chunk.setBlock(x, y, z, shaftSupportId);
			chunk.setBlock(x, y + 1, z, shaftSupportId);
			chunk.setBlock(x + 3, y, z, shaftSupportId);
			chunk.setBlock(x + 3, y + 1, z, shaftSupportId);
			chunk.setBlocks(x, x + 4, y + 2, z, z + 1, shaftBeamId);
		}
	}
	
	private void generateMineWESupport(ShortChunk chunk, int x, int y, int z) {
		// on a bridge
		if (chunk.isType(x, y - 1, z, shaftBridgeId) && 
			chunk.isType(x, y - 1, z + 3, shaftBridgeId)) {
			
			// place supports
			generateMineSupport(chunk, x, y - 1, z);
			generateMineSupport(chunk, x, y - 1, z + 3);
			
		// in a tunnel
		} else {
			chunk.setBlock(x, y, z, shaftSupportId);
			chunk.setBlock(x, y + 1, z, shaftSupportId);
			chunk.setBlock(x, y, z + 3, shaftSupportId);
			chunk.setBlock(x, y + 1, z + 3, shaftSupportId);
			chunk.setBlocks(x, x + 1, y + 2, z, z + 4, shaftBeamId);
		}
	}
	
	private void generateMineSupport(ShortChunk chunk, int x, int y, int z) {
		int aboveSupport = chunk.findLastEmptyAbove(x, y, z);
		if (aboveSupport < blockYs.maxHeight)
			chunk.setBlocks(x, y + 1, aboveSupport + 1, z, shaftSupportId);
	}
	
	public void generateMines(CityWorldGenerator generator, RealChunk chunk) {
		
		// get shafted!
		if (generator.settings.includeMines)
			for (int y = 0; y + 16 < blockYs.minHeight; y += 16) {
				if (isShaftableLevel(generator, y))
					generateVerticalMineLevel(generator, chunk, y);
			}
	}
	
	private void generateVerticalMineLevel(CityWorldGenerator generator, RealChunk chunk, int y) {
		int y1 = y + 6;
		boolean stairsFound = false;
		
		// going down?
		if (isShaftableLevel(generator, y - 16)) {
			if (generator.shapeProvider.isHorizontalNSShaft(chunk.chunkX, y, chunk.chunkZ) &&
				generator.shapeProvider.isHorizontalNSShaft(chunk.chunkX, y - 16, chunk.chunkZ)) {
				
				// draw the going down bit
				placeMineStairBase(chunk, 10, y1	, 15);
				placeMineStairStep(chunk, 10, y1    , 14, Stair.SOUTH, Stair.NORTHFLIP);
				placeMineStairStep(chunk, 10, y1 - 1, 13, Stair.SOUTH, Stair.NORTHFLIP);
				placeMineStairStep(chunk, 10, y1 - 2, 12, Stair.SOUTH, Stair.NORTHFLIP);
				placeMineStairStep(chunk, 10, y1 - 3, 11, Stair.SOUTH, Stair.NORTHFLIP);
				placeMineStairStep(chunk, 10, y1 - 4, 10, Stair.SOUTH, Stair.NORTHFLIP);
				placeMineStairStep(chunk, 10, y1 - 5,  9, Stair.SOUTH, Stair.NORTHFLIP);
				placeMineStairStep(chunk, 10, y1 - 6,  8, Stair.SOUTH, Stair.NORTHFLIP);
				stairsFound = true;
			}
			
			if (!stairsFound &&
				generator.shapeProvider.isHorizontalWEShaft(chunk.chunkX, y, chunk.chunkZ) &&
				generator.shapeProvider.isHorizontalWEShaft(chunk.chunkX, y - 16, chunk.chunkZ)) {
				
				// draw the going down bit
				placeMineStairBase(chunk, 15, y1	, 10);
				placeMineStairStep(chunk, 14, y1    , 10, Stair.EAST, Stair.WESTFLIP);
				placeMineStairStep(chunk, 13, y1 - 1, 10, Stair.EAST, Stair.WESTFLIP);
				placeMineStairStep(chunk, 12, y1 - 2, 10, Stair.EAST, Stair.WESTFLIP);
				placeMineStairStep(chunk, 11, y1 - 3, 10, Stair.EAST, Stair.WESTFLIP);
				placeMineStairStep(chunk, 10, y1 - 4, 10, Stair.EAST, Stair.WESTFLIP);
				placeMineStairStep(chunk,  9, y1 - 5, 10, Stair.EAST, Stair.WESTFLIP);
				placeMineStairStep(chunk,  8, y1 - 6, 10, Stair.EAST, Stair.WESTFLIP);
			}
		}
		
		// reset the stairs flag
		stairsFound = false;
		
		// going up?
		if (isShaftableLevel(generator, y + 32)) {
			if (generator.shapeProvider.isHorizontalNSShaft(chunk.chunkX, y, chunk.chunkZ) &&
				generator.shapeProvider.isHorizontalNSShaft(chunk.chunkX, y + 16, chunk.chunkZ)) {
					
				// draw the going up bit
				placeMineStairBase(chunk,  5, y1	, 15);
				placeMineStairStep(chunk,  5, y1 + 1, 14, Stair.NORTH, Stair.SOUTHFLIP);
				placeMineStairStep(chunk,  5, y1 + 2, 13, Stair.NORTH, Stair.SOUTHFLIP);
				placeMineStairStep(chunk,  5, y1 + 3, 12, Stair.NORTH, Stair.SOUTHFLIP);
				placeMineStairStep(chunk,  5, y1 + 4, 11, Stair.NORTH, Stair.SOUTHFLIP);
				placeMineStairStep(chunk,  5, y1 + 5, 10, Stair.NORTH, Stair.SOUTHFLIP);
				placeMineStairStep(chunk,  5, y1 + 6,  9, Stair.NORTH, Stair.SOUTHFLIP);
				placeMineStairStep(chunk,  5, y1 + 7,  8, Stair.NORTH, Stair.SOUTHFLIP);
				placeMineStairStep(chunk,  5, y1 + 8,  7, Stair.NORTH, Stair.SOUTHFLIP);
				placeMineStairBase(chunk,  5, y1 + 8,  6);
				placeMineStairBase(chunk,  6, y1 + 8,  6);
				placeMineStairBase(chunk,  7, y1 + 8,  6);
				placeMineStairBase(chunk,  8, y1 + 8,  6);
				placeMineStairBase(chunk,  9, y1 + 8,  6);
				placeMineStairBase(chunk, 10, y1 + 8,  6);
				placeMineStairStep(chunk, 10, y1 + 9,  7, Stair.SOUTH, Stair.NORTHFLIP);
				
				generateMineSupport(chunk, 6, y1 + 7, 7);
				generateMineSupport(chunk, 9, y1 + 7, 7);
				
				stairsFound = true;
			}
			
			if (!stairsFound &&
				generator.shapeProvider.isHorizontalWEShaft(chunk.chunkX, y, chunk.chunkZ) &&
				generator.shapeProvider.isHorizontalWEShaft(chunk.chunkX, y + 16, chunk.chunkZ)) {
				
				// draw the going up bit
				placeMineStairBase(chunk, 15, y1	,  5);
				placeMineStairStep(chunk, 14, y1 + 1,  5, Stair.WEST, Stair.EASTFLIP);
				placeMineStairStep(chunk, 13, y1 + 2,  5, Stair.WEST, Stair.EASTFLIP);
				placeMineStairStep(chunk, 12, y1 + 3,  5, Stair.WEST, Stair.EASTFLIP);
				placeMineStairStep(chunk, 11, y1 + 4,  5, Stair.WEST, Stair.EASTFLIP);
				placeMineStairStep(chunk, 10, y1 + 5,  5, Stair.WEST, Stair.EASTFLIP);
				placeMineStairStep(chunk,  9, y1 + 6,  5, Stair.WEST, Stair.EASTFLIP);
				placeMineStairStep(chunk,  8, y1 + 7,  5, Stair.WEST, Stair.EASTFLIP);
				placeMineStairStep(chunk,  7, y1 + 8,  5, Stair.WEST, Stair.EASTFLIP);
				placeMineStairBase(chunk,  6, y1 + 8,  5);
				placeMineStairBase(chunk,  6, y1 + 8,  6);
				placeMineStairBase(chunk,  6, y1 + 8,  7);
				placeMineStairBase(chunk,  6, y1 + 8,  8);
				placeMineStairBase(chunk,  6, y1 + 8,  9);
				placeMineStairBase(chunk,  6, y1 + 8, 10);
				placeMineStairStep(chunk,  7, y1 + 9, 10, Stair.EAST, Stair.WESTFLIP);
				
				generateMineSupport(chunk, 7, y1 + 7, 6);
				generateMineSupport(chunk, 7, y1 + 7, 9);
			}
		}
		
		// make the ceiling pretty
		boolean pathFound = false;
		if (generator.shapeProvider.isHorizontalNSShaft(chunk.chunkX, y, chunk.chunkZ)) {
			generateMineCeiling(chunk, 6, 10, y1 + 3, 0, 6);
			generateMineCeiling(chunk, 6, 10, y1 + 3, 10, 16);
			
			generateMineAlcove(generator, chunk, 4, y1, 2, 4, 2);
			generateMineAlcove(generator, chunk, 10, y1, 2, 11, 3);

			pathFound = true;
		}
		if (generator.shapeProvider.isHorizontalWEShaft(chunk.chunkX, y, chunk.chunkZ)) {
			generateMineCeiling(chunk, 0, 6, y1 + 3, 6, 10);
			generateMineCeiling(chunk, 10, 16, y1 + 3, 6, 10);

			generateMineAlcove(generator, chunk, 2, y1, 4, 2, 4);
			generateMineAlcove(generator, chunk, 2, y1, 10, 3, 11);
			
			pathFound = true;
		}
		
		// draw the center bit
		if (pathFound)
			generateMineCeiling(chunk, 6, 10, y1 + 3, 6, 10);
	}
	
	
	private void generateMineAlcove(CityWorldGenerator generator, RealChunk chunk, int x, int y, int z, int prizeX, int prizeZ) {
		if (chunkOdds.playOdds(generator.settings.oddsOfAlcoveInMines)) {
			if (!chunk.isEmpty(x, y, z) &&
				!chunk.isEmpty(x + 1, y, z) &&
				!chunk.isEmpty(x, y, z + 1) &&
				!chunk.isEmpty(x + 1, y, z + 1)) {
				chunk.setBlocks(x, x + 2, y + 1, y + 4, z, z + 2, Material.AIR);
				generateMineCeiling(chunk, x, x + 2, y + 3, z, z + 2);
				if (chunkOdds.playOdds(generator.settings.oddsOfSpawnerInMineAlcove))
					generateMineTrick(generator, chunk, prizeX, y + 1, prizeZ);
				else if (chunkOdds.playOdds(generator.settings.oddsOfTreasureInMineAlcove)) 
					generateMineTreat(generator, chunk, prizeX, y + 1, prizeZ);
			}
		}
	}
	
	private void generateMineCeiling(RealChunk chunk, int x1, int x2, int y, int z1, int z2) {
		for (int x = x1; x < x2; x++) {
			for (int z = z1; z < z2; z++) {
				if (chunkOdds.flipCoin())
					if (!chunk.isEmpty(x, y + 1, z) && chunk.isEmpty(x, y, z))
						chunk.setStoneSlab(x, y, z, Direction.StoneSlab.COBBLESTONEFLIP);
			}
		}
	}
	
	private void generateMineSupport(RealChunk chunk, int x, int y, int z) {
		int aboveSupport = chunk.findLastEmptyAbove(x, y, z);
		if (aboveSupport < blockYs.maxHeight)
			chunk.setBlocks(x, y + 1, aboveSupport + 1, z, Material.FENCE);
	}
	private void placeMineStairBase(RealChunk chunk, int x, int y, int z) {
		chunk.setBlocks(x, y + 1, y + 4, z, Material.AIR);
		chunk.setEmptyBlock(x, y, z, Material.WOOD);
	}
	
	private void placeMineStairStep(RealChunk chunk, int x, int y, int z, Stair direction, Stair flipDirection) {
		chunk.setBlocks(x, y + 1, y + 4, z, Material.AIR);
		chunk.setStair(x, y, z, Material.WOOD_STAIRS, direction);
		if (chunk.isEmpty(x, y - 1, z))
			chunk.setStair(x, y - 1, z, Material.WOOD_STAIRS, flipDirection);
	}
	
	private void generateMineTreat(CityWorldGenerator generator, RealChunk chunk, int x, int y, int z) {

		// cool stuff?
		if (generator.settings.treasuresInMines && chunkOdds.playOdds(generator.settings.oddsOfTreasureInMines)) {
			 chunk.setChest(x, y, z, Direction.General.SOUTH, chunkOdds, generator.lootProvider, LootLocation.MINE);
		}
	}

	private void generateMineTrick(CityWorldGenerator generator, RealChunk chunk, int x, int y, int z) {
		// not so cool stuff?
		if (generator.settings.spawnersInMines && chunkOdds.playOdds(generator.settings.oddsOfSpawnerInMines)) {
			chunk.setSpawner(x, y, z, generator.spawnProvider.getEntity(generator, chunkOdds, SpawnerLocation.MINE));
		}
	}

	public boolean isValidStrataY(CityWorldGenerator generator, int blockX, int blockY, int blockZ) {
		return true;
	}
	
	public void generateOres(CityWorldGenerator generator, RealChunk chunk) {
		
		// shape the world
		if (generator.settings.includeOres || generator.settings.includeUndergroundFluids)
			generator.oreProvider.sprinkleOres(generator, this, chunk, blockYs, chunkOdds, OreLocation.CRUST);
	}

	//TODO move this logic to SurroundingLots, add to it the ability to produce SurroundingHeights and SurroundingDepths
	public PlatLot[][] getNeighborPlatLots(PlatMap platmap, int platX, int platZ, boolean onlyConnectedNeighbors) {
		PlatLot[][] miniPlatMap = new PlatLot[3][3];
		
		// populate the results
		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				
				// which platchunk are we looking at?
				int atX = platX + x - 1;
				int atZ = platZ + z - 1;

				// is it in bounds?
				if (!(atX < 0 || atX > PlatMap.Width - 1 || atZ < 0 || atZ > PlatMap.Width - 1)) {
					PlatLot relative = platmap.getLot(atX, atZ);
					
					if (!onlyConnectedNeighbors || isConnected(relative)) {
						miniPlatMap[x][z] = relative;
					}
				}
			}
		}
		
		return miniPlatMap;
	}
	
	public void generateSurface(CityWorldGenerator generator, SupportChunk chunk, boolean includeTrees) {
		
		// plant grass or snow
		generator.surfaceProvider.generateSurface(generator, this, chunk, blockYs, includeTrees);
	}
	
	protected Material getAirMaterial(CityWorldGenerator generator, int y) {
		if (getTopY(generator) <= y)
			return Material.AIR;
		else
			return generator.shapeProvider.findAtmosphereMaterialAt(generator, y);
	}
}
