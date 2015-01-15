package me.daddychurchill.CityWorld.Plats;

import me.daddychurchill.CityWorld.WorldGenerator;
import me.daddychurchill.CityWorld.Support.PlatMap;

public abstract class ConstructLot extends IsolatedLot {

	public ConstructLot(PlatMap platmap, int chunkX, int chunkZ) {
		super(platmap, chunkX, chunkZ);
	}

	@Override
	public boolean isPlaceableAt(WorldGenerator generator, int chunkX, int chunkZ) {
		return generator.settings.inConstructRange(chunkX, chunkZ);
	}
	
	@Override
	public PlatLot validateLot(PlatMap platmap, int platX, int platZ) {
		return null;
	}
}
