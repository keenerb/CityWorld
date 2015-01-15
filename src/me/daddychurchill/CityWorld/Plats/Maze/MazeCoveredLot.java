package me.daddychurchill.CityWorld.Plats.Maze;

import me.daddychurchill.CityWorld.WorldGenerator;
import me.daddychurchill.CityWorld.Context.DataContext;
import me.daddychurchill.CityWorld.Support.PlatMap;
import me.daddychurchill.CityWorld.Support.RealChunk;

public class MazeCoveredLot extends MazeNatureLot {

	public MazeCoveredLot(PlatMap platmap, int chunkX, int chunkZ) {
		super(platmap, chunkX, chunkZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generateActualBlocks(WorldGenerator generator, PlatMap platmap, RealChunk chunk, DataContext context, int platX, int platZ) {
		super.generateActualBlocks(generator, platmap, chunk, context, platX, platZ);
		
		// top it off
		int y = generator.streetLevel + mazeHeight - mazeDepth - 1;
		chunk.setBlocks(0, 16, y, 0, 16, wallMaterial);
		chunk.setWalls(0, 16, generator.streetLevel + 3, y - 1, 0, 16, wallMaterial);
	}
}
