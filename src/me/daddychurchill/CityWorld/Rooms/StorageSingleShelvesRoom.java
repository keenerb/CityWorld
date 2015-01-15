package me.daddychurchill.CityWorld.Rooms;

import me.daddychurchill.CityWorld.WorldGenerator;
import me.daddychurchill.CityWorld.Support.Direction.Facing;
import me.daddychurchill.CityWorld.Support.Odds;
import me.daddychurchill.CityWorld.Support.RealChunk;

import org.bukkit.Material;

public class StorageSingleShelvesRoom extends StorageRoom {

	public StorageSingleShelvesRoom() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawFixture(WorldGenerator generator, RealChunk chunk, Odds odds, int floor, int x,
			int y, int z, int width, int height, int depth,
			Facing sideWithWall, Material materialWall, Material materialGlass) {
		int offset;
		switch (sideWithWall) {
		case NORTH:
		case SOUTH:
			offset = odds.getRandomInt(width);
			drawNSEmptyShelve(chunk, x + offset, y, z, height - 1, depth);
			break;
		case WEST:
		case EAST:
			offset = odds.getRandomInt(depth);
			drawWEEmptyShelve(chunk, x, y, z + offset, height - 1, width);
			break;
		}
	}

}
