package me.daddychurchill.CityWorld.Rooms;

import me.daddychurchill.CityWorld.WorldGenerator;
import me.daddychurchill.CityWorld.Support.Direction.Facing;
import me.daddychurchill.CityWorld.Support.Odds;
import me.daddychurchill.CityWorld.Support.RealChunk;

import org.bukkit.Material;

public class StorageFilledChestsRoom extends StorageRoom {

	public StorageFilledChestsRoom() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawFixture(WorldGenerator generator, RealChunk chunk, Odds odds, int floor, int x,
			int y, int z, int width, int height, int depth,
			Facing sideWithWall, Material materialWall, Material materialGlass) {
//		switch (sideWithWall) {
//		case NORTH:
//		case SOUTH:
//			
//			offset = odds.getRandomInt(width);
//			drawNSEmptyShelve(chunk, x + offset, y, z, 1, depth);
//			for (int run = 0; run < depth; run++)
//				chunk.setBlocks(x + offset, y + 1, y + 1 + Math.max(minheight, odds.getRandomInt(height - 1)), z + run, Material.BOOKSHELF);
//			break;
//		case WEST:
//		case EAST:
//			offset = odds.getRandomInt(depth);
//			drawWEEmptyShelve(chunk, x, y, z + offset, 1, width);
//			for (int run = 0; run < depth; run++)
//				chunk.setBlocks(x + run, y + 1, y + 1 + Math.max(minheight, odds.getRandomInt(height - 1)), z + offset, Material.BOOKSHELF);
//			break;
//		}
		

	}
	
	protected void drawChest(RealChunk chunk, int x, int y, int z) {
//		
//		// cool stuff?
//		if (generator.settings.treasuresInSewers && chunkOdds.playOdds(generator.settings.oddsOfTreasureInSewers)) {
//			 chunk.setChest(x, y, z, Direction.General.NORTH, chunkOdds, generator.lootProvider, LootLocation.SEWER);
//		}
	}

}
