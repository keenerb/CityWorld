package me.daddychurchill.CityWorld.Rooms;

import me.daddychurchill.CityWorld.WorldGenerator;
import me.daddychurchill.CityWorld.Support.Direction.Facing;
import me.daddychurchill.CityWorld.Support.Odds;
import me.daddychurchill.CityWorld.Support.RealChunk;

import org.bukkit.Material;

public class StorageDoubleShelvesRoom extends StorageRoom {

	public StorageDoubleShelvesRoom() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawFixture(WorldGenerator generator, RealChunk chunk, Odds odds, int floor, int x,
			int y, int z, int width, int height, int depth,
			Facing sideWithWall, Material materialWall, Material materialGlass) {
		switch (sideWithWall) {
		case SOUTH:
			drawNSEmptyShelves(chunk, x, y, z, width, height - 1, depth, depth - 1);
			break;
		case NORTH:
			drawNSEmptyShelves(chunk, x, y, z, width, height - 1, depth, 0);
			break;
		case EAST:
			drawWEEmptyShelves(chunk, x, y, z, width, height - 1, depth, width - 1);
			break;
		case WEST:
			drawWEEmptyShelves(chunk, x, y, z, width, height - 1, depth, 0);
			break;
		}
	}
	
	public void drawNSEmptyShelves(RealChunk chunk, int x, int y, int z, int width, int height, int depth, int i) {
		for (int offset = 0; offset < width; offset += 2) {
			drawNSEmptyShelve(chunk, x + offset, y, z, height, depth);
		}
	}
	
	public void drawWEEmptyShelves(RealChunk chunk, int x, int y, int z, int width, int height, int depth, int i) {
		for (int offset = 0; offset < depth; offset += 2) {
			drawWEEmptyShelve(chunk, x, y, z + offset, height, width);
		}
	}
}
