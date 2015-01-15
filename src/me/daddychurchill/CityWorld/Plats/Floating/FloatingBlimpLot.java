package me.daddychurchill.CityWorld.Plats.Floating;

import me.daddychurchill.CityWorld.WorldGenerator;
import me.daddychurchill.CityWorld.Context.DataContext;
import me.daddychurchill.CityWorld.Plats.IsolatedLot;
import me.daddychurchill.CityWorld.Plats.PlatLot;
import me.daddychurchill.CityWorld.Plugins.BalloonProvider;
import me.daddychurchill.CityWorld.Support.ByteChunk;
import me.daddychurchill.CityWorld.Support.PlatMap;
import me.daddychurchill.CityWorld.Support.RealChunk;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;

public class FloatingBlimpLot extends IsolatedLot {

	protected final static Material base = Material.SMOOTH_BRICK;
	protected final static Material underlayment = Material.STONE;
	protected final static Material pedestal = Material.STONE;
	
	boolean manyBalloons;
		
	public FloatingBlimpLot(PlatMap platmap, int chunkX, int chunkZ) {
		super(platmap, chunkX, chunkZ);
		
		manyBalloons = chunkOdds.flipCoin();
		this.style = LotStyle.NATURE;
	}
	
	@Override
	public PlatLot newLike(PlatMap platmap, int chunkX, int chunkZ) {
		return new FloatingBlimpLot(platmap, chunkX, chunkZ);
	}

	@Override
	public int getBottomY(WorldGenerator generator) {
		return 0;
	}

	@Override
	public int getTopY(WorldGenerator generator) {
		return generator.streetLevel;
	}

	@Override
	protected void generateActualChunk(WorldGenerator generator,
			PlatMap platmap, ByteChunk chunk, BiomeGrid biomes,
			DataContext context, int platX, int platZ) {
		
		boolean toNorth = platmap.isStructureLot(platX, platZ - 1);
		boolean toSouth = platmap.isStructureLot(platX, platZ + 1);
		boolean toWest = platmap.isStructureLot(platX - 1, platZ);
		boolean toEast = platmap.isStructureLot(platX + 1, platZ);
		
		chunk.setCircle(8, 8, 6, generator.streetLevel, base, true);
		if (toNorth) {
			chunk.setBlocks(0, 16, generator.streetLevel, 0, 7, base);
			chunk.setBlocks(7, 9, generator.streetLevel - 2, generator.streetLevel, 0, 13, underlayment);
		}
		if (toSouth) {
			chunk.setBlocks(0, 16, generator.streetLevel, 8, 16, base);
			chunk.setBlocks(7, 9, generator.streetLevel - 2, generator.streetLevel, 3, 16, underlayment);
		}
		if (toWest) {
			chunk.setBlocks(0, 7, generator.streetLevel, 0, 16, base);
			chunk.setBlocks(0, 13, generator.streetLevel - 2, generator.streetLevel, 7, 9, underlayment);
		}
		if (toEast) {
			chunk.setBlocks(8, 16, generator.streetLevel, 0, 16, base);
			chunk.setBlocks(3, 16, generator.streetLevel - 2, generator.streetLevel, 7, 9, underlayment);
		}
		
		// what types of balloon?
		if (manyBalloons) {
			chunk.setBlocks(7, 9, generator.streetLevel + 1, generator.streetLevel + 5, 3, 13, pedestal);
			chunk.setBlocks(3, 13, generator.streetLevel + 1, generator.streetLevel + 5, 7, 9, pedestal);
		} else {
			chunk.setBlocks(7, 9, generator.streetLevel + 1, generator.streetLevel + 5, 1, 3, pedestal);
			chunk.setBlocks(7, 9, generator.streetLevel + 1, generator.streetLevel + 5, 13, 15, pedestal);
			chunk.setBlocks(1, 3, generator.streetLevel + 1, generator.streetLevel + 5, 7, 9, pedestal);
			chunk.setBlocks(13, 15, generator.streetLevel + 1, generator.streetLevel + 5, 7, 9, pedestal);
		}
	}

	@Override
	protected void generateActualBlocks(WorldGenerator generator,
			PlatMap platmap, RealChunk chunk, DataContext context, int platX,
			int platZ) {
		if (generator.settings.includeDecayedBuildings)
			destroyLot(generator, generator.streetLevel - 1, generator.streetLevel + 3);
		
		// what type of balloon?
		BalloonProvider balloons = generator.balloonProvider;
		if (manyBalloons) {
			balloons.generateBalloon(generator, chunk, context, 7 + chunkOdds.getRandomInt(2), generator.streetLevel + 5, 3, chunkOdds);
			balloons.generateBalloon(generator, chunk, context, 7 + chunkOdds.getRandomInt(2), generator.streetLevel + 5, 12, chunkOdds);
			balloons.generateBalloon(generator, chunk, context, 3, generator.streetLevel + 5, 7 + chunkOdds.getRandomInt(2), chunkOdds);
			balloons.generateBalloon(generator, chunk, context, 12, generator.streetLevel + 5, 7 + chunkOdds.getRandomInt(2), chunkOdds);
		} else {
			balloons.generateBlimp(generator, chunk, context, generator.streetLevel + 5, chunkOdds);
		}
	}

}
