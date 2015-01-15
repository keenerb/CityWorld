package me.daddychurchill.CityWorld.Plugins;

import me.daddychurchill.CityWorld.WorldGenerator;
import me.daddychurchill.CityWorld.Support.Odds;
import me.daddychurchill.CityWorld.Support.BlackMagic;
import me.daddychurchill.CityWorld.Support.SupportChunk;

import org.bukkit.CropState;
import org.bukkit.GrassSpecies;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.TreeSpecies;
import org.bukkit.TreeType;
import org.bukkit.material.Crops;
import org.bukkit.material.LongGrass;
import org.bukkit.material.NetherWarts;
import org.bukkit.material.Tree;

public abstract class CoverProvider extends Provider {
	
	public enum CoverageType {
		GRASS, FERN, DEAD_GRASS, DANDELION, DEAD_BUSH, 
		
		POPPY, BLUE_ORCHID, ALLIUM, AZURE_BLUET, OXEYE_DAISY,
		RED_TULIP, ORANGE_TULIP, WHITE_TULIP, PINK_TULIP,

		SUNFLOWER, LILAC, TALL_GRASS, TALL_FERN, ROSE_BUSH, PEONY,
		
		CACTUS, REED, EMERALD_GREEN, 
		
		OAK_SAPLING, PINE_SAPLING, BIRCH_SAPLING, 
		JUNGLE_SAPLING, ACACIA_SAPLING,
		
		MINI_OAK_TREE, SHORT_OAK_TREE, OAK_TREE, TALL_OAK_TREE, 
		MINI_PINE_TREE, SHORT_PINE_TREE, PINE_TREE, TALL_PINE_TREE, 
		MINI_BIRCH_TREE, SHORT_BIRCH_TREE, BIRCH_TREE, TALL_BIRCH_TREE, 
		MINI_JUNGLE_TREE, SHORT_JUNGLE_TREE, JUNGLE_TREE, TALL_JUNGLE_TREE,
		MINI_SWAMP_TREE, SWAMP_TREE,
		MINI_ACACIA_TREE, ACACIA_TREE,
//		TALL_BROWN_MUSHROOM, TALL_RED_MUSHROOM,
		  
		MINI_OAK_TRUNK, OAK_TRUNK, 
		MINI_PINE_TRUNK, PINE_TRUNK, 
		MINI_BIRCH_TRUNK, BIRCH_TRUNK, 
		MINI_JUNGLE_TRUNK, JUNGLE_TRUNK,
		MINI_SWAMP_TRUNK, SWAMP_TRUNK,
		MINI_ACACIA_TRUNK, ACACIA_TRUNK,
		
		WHEAT, CARROTS, POTATO, MELON, PUMPKIN, 

		BROWN_MUSHROOM, RED_MUSHROOM, NETHERWART,
		FIRE};
	
	public enum CoverageSets {SHORT_FLOWERS, TALL_FLOWERS, ALL_FLOWERS,
		SHORT_PLANTS, TALL_PLANTS, ALL_PLANTS,
		GENERAL_SAPLINGS, ALL_SAPLINGS, 
		OAK_TREES, PINE_TREES, BIRCH_TREES, 
		JUNGLE_TREES, ACACIA_TREES, SWAMP_TREES, 
		EDIBLE_PLANTS, SHORT_MUSHROOMS, 
		NETHER_PLANTS, DECAY_PLANTS};
								
	private final static CoverageType[] ShortFlowers = {
		CoverageType.DANDELION, CoverageType.POPPY, 
		CoverageType.BLUE_ORCHID, CoverageType.ALLIUM, 
		CoverageType.AZURE_BLUET, CoverageType.OXEYE_DAISY,
		CoverageType.RED_TULIP, CoverageType.ORANGE_TULIP, 
		CoverageType.WHITE_TULIP, CoverageType.PINK_TULIP};
	
	private final static CoverageType[] TallFlowers = {
		CoverageType.SUNFLOWER, CoverageType.LILAC,
		CoverageType.ROSE_BUSH, CoverageType.PEONY};
	
	private final static CoverageType[] AllFlowers = {
		CoverageType.DANDELION, CoverageType.POPPY, 
		CoverageType.BLUE_ORCHID, CoverageType.ALLIUM, 
		CoverageType.AZURE_BLUET, CoverageType.OXEYE_DAISY,
		CoverageType.RED_TULIP, CoverageType.ORANGE_TULIP, 
		CoverageType.WHITE_TULIP, CoverageType.PINK_TULIP,
		CoverageType.SUNFLOWER, CoverageType.LILAC,
		CoverageType.ROSE_BUSH, CoverageType.PEONY};
	
	private final static CoverageType[] ShortPlants = {
		CoverageType.GRASS, CoverageType.FERN};
	
	private final static CoverageType[] TallPlants = {
		CoverageType.CACTUS, CoverageType.REED,
		CoverageType.TALL_GRASS, CoverageType.TALL_FERN,
		CoverageType.EMERALD_GREEN};
	
	private final static CoverageType[] AllPlants = {
		CoverageType.GRASS, CoverageType.FERN,
		CoverageType.CACTUS, CoverageType.REED,
		CoverageType.TALL_GRASS, CoverageType.TALL_FERN,
		CoverageType.EMERALD_GREEN};

	private final static CoverageType[] EdiblePlants = {
		CoverageType.WHEAT, CoverageType.CARROTS,
		CoverageType.POTATO, CoverageType.MELON,
		CoverageType.PUMPKIN};

	private final static CoverageType[] GeneralSaplings = {
		CoverageType.OAK_SAPLING, CoverageType.PINE_SAPLING,
		CoverageType.BIRCH_SAPLING};
	
	private final static CoverageType[] AllSaplings = {
		CoverageType.OAK_SAPLING, CoverageType.PINE_SAPLING,
		CoverageType.BIRCH_SAPLING, CoverageType.JUNGLE_SAPLING, 
		CoverageType.ACACIA_SAPLING};
	
	private final static CoverageType[] OakTrees = {
		CoverageType.OAK_SAPLING, CoverageType.SHORT_OAK_TREE, 
		CoverageType.OAK_TREE, CoverageType.TALL_OAK_TREE};
	
	private final static CoverageType[] PineTrees = {
		CoverageType.PINE_SAPLING, CoverageType.SHORT_PINE_TREE, 
		CoverageType.PINE_TREE, CoverageType.TALL_PINE_TREE};
	
	private final static CoverageType[] BirchTrees = {
		CoverageType.BIRCH_SAPLING, CoverageType.SHORT_BIRCH_TREE, 
		CoverageType.BIRCH_TREE, CoverageType.TALL_BIRCH_TREE};
	
	private final static CoverageType[] JungleTrees = {
		CoverageType.JUNGLE_SAPLING, CoverageType.SHORT_JUNGLE_TREE, 
		CoverageType.JUNGLE_TREE, CoverageType.TALL_JUNGLE_TREE};
	
	private final static CoverageType[] AcaciaTrees = {
		CoverageType.ACACIA_SAPLING, CoverageType.ACACIA_TREE};
	
	private final static CoverageType[] SwampTrees = {
		CoverageType.SWAMP_TREE};
	
	private final static CoverageType[] ShortMushrooms = {
		CoverageType.BROWN_MUSHROOM, CoverageType.RED_MUSHROOM};

	private final static CoverageType[] NetherPlants = {
		CoverageType.BROWN_MUSHROOM, CoverageType.RED_MUSHROOM,
		CoverageType.NETHERWART, CoverageType.DEAD_BUSH,
		CoverageType.FIRE};
	
	private final static CoverageType[] DecayPlants = {
		CoverageType.BROWN_MUSHROOM, CoverageType.RED_MUSHROOM,
		CoverageType.DEAD_BUSH};
	
	protected final static double oddsOfDarkCover = Odds.oddsLikely;
	protected Odds odds;
	
	public CoverProvider(Odds odds) {
		super();
		this.odds = odds;
	}
	
	public abstract boolean generateCoverage(WorldGenerator generator, SupportChunk chunk, int x, int y, int z, CoverageType coverageType);
	
	private CoverageType getRandomCoverage(CoverageType ... types) {
		return types[odds.getRandomInt(types.length)];
	}
	
	public void generateRandomCoverage(WorldGenerator generator, SupportChunk chunk, int x, int y, int z, CoverageType ... types) {
		setCoverage(generator, chunk, x, y, z, getRandomCoverage(types));
	}
	
	public void generateCoverage(WorldGenerator generator, SupportChunk chunk, int x, int y, int z, CoverageSets coverageSet) {
		switch (coverageSet) {
		case ALL_FLOWERS:
			generateRandomCoverage(generator, chunk, x, y, z, AllFlowers);
			break;
		case ALL_PLANTS:
			generateRandomCoverage(generator, chunk, x, y, z, AllPlants);
			break;
		case ALL_SAPLINGS:
			generateRandomCoverage(generator, chunk, x, y, z, AllSaplings);
			break;
		case EDIBLE_PLANTS:
			generateRandomCoverage(generator, chunk, x, y, z, EdiblePlants);
			break;
		case GENERAL_SAPLINGS:
			generateRandomCoverage(generator, chunk, x, y, z, GeneralSaplings);
			break;
		case OAK_TREES:
			generateRandomCoverage(generator, chunk, x, y, z, OakTrees);
			break;
		case PINE_TREES:
			generateRandomCoverage(generator, chunk, x, y, z, PineTrees);
			break;
		case BIRCH_TREES:
			generateRandomCoverage(generator, chunk, x, y, z, BirchTrees);
			break;
		case JUNGLE_TREES:
			generateRandomCoverage(generator, chunk, x, y, z, JungleTrees);
			break;
		case ACACIA_TREES:
			generateRandomCoverage(generator, chunk, x, y, z, AcaciaTrees);
			break;
		case SWAMP_TREES:
			generateRandomCoverage(generator, chunk, x, y, z, SwampTrees);
			break;
		case NETHER_PLANTS:
			generateRandomCoverage(generator, chunk, x, y, z, NetherPlants);
			break;
		case DECAY_PLANTS:
			generateRandomCoverage(generator, chunk, x, y, z, DecayPlants);
			break;
		case SHORT_FLOWERS:
			generateRandomCoverage(generator, chunk, x, y, z, ShortFlowers);
			break;
		case SHORT_PLANTS:
			generateRandomCoverage(generator, chunk, x, y, z, ShortPlants);
			break;
		case SHORT_MUSHROOMS:
			generateRandomCoverage(generator, chunk, x, y, z, ShortMushrooms);
			break;
		case TALL_FLOWERS:
			generateRandomCoverage(generator, chunk, x, y, z, TallFlowers);
			break;
		case TALL_PLANTS:
			generateRandomCoverage(generator, chunk, x, y, z, TallPlants);
			break;
		}
	}
	
	protected void setCoverage(WorldGenerator generator, SupportChunk chunk, int x, int y, int z, CoverageType coverageType) {
		switch (coverageType) {
		case GRASS:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			chunk.setBlock(x, y, z, Material.LONG_GRASS, new LongGrass(GrassSpecies.NORMAL)); //TODO: Bukkit type mismatch/missing
			break;
		case FERN:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			chunk.setBlock(x, y, z, Material.LONG_GRASS, new LongGrass(GrassSpecies.FERN_LIKE)); 
			break;
		case DEAD_GRASS:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			chunk.setBlock(x, y, z, Material.LONG_GRASS, new LongGrass(GrassSpecies.DEAD));
			break;
		case CACTUS:
			chunk.setBlockIfNot(x, y - 1, z, Material.SAND);
			chunk.setBlocks(x, y, y + odds.getRandomInt(3) + 2, z, Material.CACTUS);
			break;
		case REED:
			chunk.setBlockIfNot(x, y - 1, z, Material.SAND, Material.GRASS, Material.DIRT, Material.SOIL);
			chunk.setBlocks(x, y, y + odds.getRandomInt(2) + 2, z, Material.SUGAR_CANE_BLOCK);
			break;
		case DANDELION:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			chunk.setBlock(x, y, z, Material.YELLOW_FLOWER);
			break;
		case POPPY:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.RED_ROSE, 0);
			break;
		case BLUE_ORCHID:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.RED_ROSE, 1);
			break;
		case ALLIUM:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.RED_ROSE, 2);
			break;
		case AZURE_BLUET:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.RED_ROSE, 3);
			break;
		case OXEYE_DAISY:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.RED_ROSE, 8);
			break;
		case RED_TULIP:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.RED_ROSE, 4);
			break;
		case ORANGE_TULIP:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.RED_ROSE, 5);
			break;
		case WHITE_TULIP:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.RED_ROSE, 6);
			break;
		case PINK_TULIP:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.RED_ROSE, 7);
			break;
		case SUNFLOWER:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.DOUBLE_PLANT, 0);
			BlackMagic.setBlock(chunk, x, y + 1, z, Material.DOUBLE_PLANT, 8);
			break;
		case LILAC:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.DOUBLE_PLANT, 1);
			BlackMagic.setBlock(chunk, x, y + 1, z, Material.DOUBLE_PLANT, 8);
			break;
		case TALL_GRASS:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.DOUBLE_PLANT, 2);
			BlackMagic.setBlock(chunk, x, y + 1, z, Material.DOUBLE_PLANT, 8);
			break;
		case TALL_FERN:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.DOUBLE_PLANT, 3);
			BlackMagic.setBlock(chunk, x, y + 1, z, Material.DOUBLE_PLANT, 8);
			break;
		case ROSE_BUSH:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.DOUBLE_PLANT, 4);
			BlackMagic.setBlock(chunk, x, y + 1, z, Material.DOUBLE_PLANT, 8);
			break;
		case PEONY:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.DOUBLE_PLANT, 5);
			BlackMagic.setBlock(chunk, x, y + 1, z, Material.DOUBLE_PLANT, 8);
			break;
		case EMERALD_GREEN:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT, Material.SOIL);
			chunk.setBlock(x, y, z, Material.LOG, new Tree(TreeSpecies.JUNGLE));
//			chunk.setBlocks(x, y + 1, y + odds.getRandomInt(2, 4), z, Material.LEAVES, new Leaves()); //TODO: Jungle + NoDecay
			BlackMagic.setBlocks(chunk, x, y + 1, y + odds.getRandomInt(2, 4), z, Material.LEAVES, 3 + 4); //TODO: Jungle + NoDecay
			break;
		case OAK_SAPLING:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT);
			chunk.setBlock(x, y, z, Material.SAPLING, new Tree(TreeSpecies.GENERIC)); //TODO: Bukkit type mismatch/missing
			break;
		case BIRCH_SAPLING:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT);
			chunk.setBlock(x, y, z, Material.SAPLING, new Tree(TreeSpecies.BIRCH));
			break;
		case PINE_SAPLING:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT);
			chunk.setBlock(x, y, z, Material.SAPLING, new Tree(TreeSpecies.REDWOOD)); //TODO: Bukkit type mismatch/missing
			break;
		case JUNGLE_SAPLING:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT);
			chunk.setBlock(x, y, z, Material.SAPLING, new Tree(TreeSpecies.JUNGLE));
			break;
		case ACACIA_SAPLING:
			chunk.setBlockIfNot(x, y - 1, z, Material.GRASS, Material.DIRT);
			chunk.setBlock(x, y, z, Material.SAPLING, new Tree(TreeSpecies.ACACIA));
			break;
			
		case MINI_OAK_TRUNK:
			generator.treeProvider.generateMiniTrunk(generator, chunk, x, y, z, TreeType.TREE);
			break;
		case OAK_TRUNK:
			generator.treeProvider.generateNormalTrunk(generator, chunk, x, y, z, TreeType.TREE);
			break;
		case MINI_PINE_TRUNK:
			generator.treeProvider.generateMiniTrunk(generator, chunk, x, y, z, TreeType.REDWOOD);
			break;
		case PINE_TRUNK:
			generator.treeProvider.generateNormalTrunk(generator, chunk, x, y, z, TreeType.REDWOOD);
			break;
		case MINI_BIRCH_TRUNK:
			generator.treeProvider.generateMiniTrunk(generator, chunk, x, y, z, TreeType.BIRCH);
			break;
		case BIRCH_TRUNK:
			generator.treeProvider.generateNormalTrunk(generator, chunk, x, y, z, TreeType.BIRCH);
			break;
		case MINI_JUNGLE_TRUNK:
			generator.treeProvider.generateNormalTrunk(generator, chunk, x, y, z, TreeType.JUNGLE);
			break;
		case JUNGLE_TRUNK:
			generator.treeProvider.generateNormalTrunk(generator, chunk, x, y, z, TreeType.JUNGLE);
			break;
		case MINI_SWAMP_TRUNK:
			generator.treeProvider.generateMiniTrunk(generator, chunk, x, y, z, TreeType.SWAMP);
			break;
		case SWAMP_TRUNK:
			generator.treeProvider.generateNormalTrunk(generator, chunk, x, y, z, TreeType.SWAMP);
			break;
		case MINI_ACACIA_TRUNK:
			generator.treeProvider.generateMiniTrunk(generator, chunk, x, y, z, TreeType.ACACIA);
			break;
		case ACACIA_TRUNK:
			generator.treeProvider.generateNormalTrunk(generator, chunk, x, y, z, TreeType.ACACIA);
			break;
			
		case MINI_OAK_TREE:
			generator.treeProvider.generateMiniTree(generator, chunk, x, y, z, TreeType.TREE);
			break;
		case SHORT_OAK_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.TREE);
			break;
		case OAK_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.BIG_TREE);
			break;
		case TALL_OAK_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.DARK_OAK);
			break;
		case MINI_PINE_TREE:
			generator.treeProvider.generateMiniTree(generator, chunk, x, y, z, TreeType.REDWOOD);
			break;
		case SHORT_PINE_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.REDWOOD);
			break;
		case PINE_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.TALL_REDWOOD);
			break;
		case TALL_PINE_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.MEGA_REDWOOD);
			break;
		case MINI_BIRCH_TREE:
			generator.treeProvider.generateMiniTree(generator, chunk, x, y, z, TreeType.BIRCH);
			break;
		case SHORT_BIRCH_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.BIRCH);
			break;
		case BIRCH_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.BIRCH);
			break;
		case TALL_BIRCH_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.TALL_BIRCH);
			break;
		case MINI_JUNGLE_TREE:
			generator.treeProvider.generateMiniTree(generator, chunk, x, y, z, TreeType.JUNGLE);
			break;
		case SHORT_JUNGLE_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.JUNGLE_BUSH);
			break;
		case JUNGLE_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.SMALL_JUNGLE);
			break;
		case TALL_JUNGLE_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.JUNGLE);
			break;
		case MINI_SWAMP_TREE:
			generator.treeProvider.generateMiniTree(generator, chunk, x, y, z, TreeType.SWAMP);
			break;
		case SWAMP_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.SWAMP);
			break;
		case MINI_ACACIA_TREE:
			generator.treeProvider.generateMiniTree(generator, chunk, x, y, z, TreeType.ACACIA);
			break;
		case ACACIA_TREE:
			generator.treeProvider.generateNormalTree(generator, chunk, x, y, z, TreeType.ACACIA);
			break;
			
		case WHEAT:
			chunk.setBlockIfNot(x, y - 1, z, Material.SOIL);
			chunk.setBlock(x, y, z, Material.CROPS, new Crops(getRandomWheatGrowth())); //TODO: Bukkit type mismatch/missing
			break;
		case CARROTS:
			chunk.setBlockIfNot(x, y - 1, z, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.CARROT, getRandomCarrotGrowth()); //TODO: Bukkit missing proper MaterialData
			break;
		case POTATO:
			chunk.setBlockIfNot(x, y - 1, z, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.POTATO, getRandomPotatoGrowth()); //TODO: Bukkit missing proper MaterialData
			break;
		case MELON:
			chunk.setBlockIfNot(x, y - 1, z, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.MELON_STEM, getRandomMelonGrowth()); //TODO: Bukkit missing proper MaterialData
			break;
		case PUMPKIN:
			chunk.setBlockIfNot(x, y - 1, z, Material.SOIL);
			BlackMagic.setBlock(chunk, x, y, z, Material.PUMPKIN_STEM, getRandomPumpkinGrowth()); //TODO: Bukkit missing proper MaterialData
			break;
		case DEAD_BUSH:
			chunk.setBlockIfNot(x, y - 1, z, Material.SAND, Material.DIRT, Material.HARD_CLAY);
			chunk.setBlock(x, y, z, Material.DEAD_BUSH);
			break;
		case BROWN_MUSHROOM:
			if (chunk.getActualBlock(x, y - 1, z).isLiquid())
				chunk.setBlock(x, y - 1, z, Material.MYCEL);
			chunk.setBlock(x, y, z, Material.BROWN_MUSHROOM); 
			break;
		case RED_MUSHROOM:
			if (chunk.getActualBlock(x, y - 1, z).isLiquid())
				chunk.setBlock(x, y - 1, z, Material.MYCEL);
			chunk.setBlock(x, y, z, Material.RED_MUSHROOM); 
			break;
		case NETHERWART:
			chunk.setBlockIfNot(x, y - 1, z, Material.SOUL_SAND);
			chunk.setBlock(x, y, z, Material.NETHER_WARTS, new NetherWarts(getRandomNetherWartGrowth())); //TODO: Bukkit type mismatch/missing
			break;
		case FIRE:
			chunk.setBlockIfNot(x, y - 1, z, Material.NETHERRACK);
			chunk.setBlock(x, y, z, Material.FIRE);
			break;
		}
	}
	
	private CropState getRandomWheatGrowth() {
		return CropState.values()[odds.getRandomInt(CropState.values().length)];
	}
	
	private int getRandomCarrotGrowth() {
		return odds.getRandomInt(8);
	}
	
	private int getRandomPotatoGrowth() {
		return odds.getRandomInt(8);
	}
	
	private int getRandomMelonGrowth() {
		return odds.getRandomInt(8);
	}
	
	private int getRandomPumpkinGrowth() {
		return odds.getRandomInt(8);
	}
	
	private NetherWartsState getRandomNetherWartGrowth() {
		return NetherWartsState.values()[odds.getRandomInt(NetherWartsState.values().length)];
	}
	
	protected boolean likelyCover(WorldGenerator generator) {
		return !generator.settings.darkEnvironment || odds.playOdds(oddsOfDarkCover);
	}
	
	// Based on work contributed by drew-bahrue (https://github.com/echurchill/CityWorld/pull/2)
	public static CoverProvider loadProvider(WorldGenerator generator, Odds odds) {

		CoverProvider provider = null;
		
//		// need something like PhatLoot but for coverage
//		provider = FoliageProvider_PhatFoliage.loadPhatFoliage();
		if (provider == null) {
			
			switch (generator.worldStyle) {
			case FLOODED:
				provider = new CoverProvider_Flooded(odds);
				break;
			default:
				switch (generator.worldEnvironment) {
				case NETHER:
					provider = new CoverProvider_Nether(odds);
					break;
				case THE_END:
					provider = new CoverProvider_TheEnd(odds);
					break;
				default:
					if (generator.settings.includeDecayedNature)
						provider = new CoverProvider_Decayed(odds);
					else
						provider = new CoverProvider_Normal(odds);
					break;
				}
				break;
			}
		}
	
		return provider;
	}
	
	public boolean isPlantable(WorldGenerator generator, SupportChunk chunk, int x, int y, int z) {
		
		// only if the spot above is empty
		if (!chunk.isEmpty(x, y + 1, z))
			return false;
		
		// depends on the block's type and what the world is like
		if (!generator.settings.includeAbovegroundFluids && y <= generator.seaLevel)
			return chunk.isType(x, y, z, Material.SAND);
		else
			return chunk.isPlantable(x, y, z);
	}
	
	protected boolean isATree(CoverageType coverageType) {
		switch (coverageType) {
		case MINI_OAK_TREE:
		case SHORT_OAK_TREE:
		case OAK_TREE:
		case TALL_OAK_TREE:
			
		case MINI_PINE_TREE:
		case SHORT_PINE_TREE:
		case PINE_TREE:
		case TALL_PINE_TREE:
		
		case MINI_BIRCH_TREE:
		case SHORT_BIRCH_TREE:
		case BIRCH_TREE:
		case TALL_BIRCH_TREE:
		
		case MINI_JUNGLE_TREE:
		case SHORT_JUNGLE_TREE:
		case JUNGLE_TREE:
		case TALL_JUNGLE_TREE:
		
		case MINI_SWAMP_TREE:
		case SWAMP_TREE:
		
		case MINI_ACACIA_TREE:
		case ACACIA_TREE:
			return true;
		
		default:
			return false;
		}
	}
	
	protected CoverageType convertToTrunk(CoverageType coverageType) {
		switch (coverageType) {
		case MINI_OAK_TREE:
			return CoverageType.MINI_OAK_TRUNK;
		case SHORT_OAK_TREE:
		case OAK_TREE:
		case TALL_OAK_TREE:
			return CoverageType.OAK_TRUNK;
			
		case MINI_PINE_TREE:
			return CoverageType.MINI_PINE_TRUNK;
		case SHORT_PINE_TREE:
		case PINE_TREE:
		case TALL_PINE_TREE:
			return CoverageType.PINE_TRUNK;
		
		case MINI_BIRCH_TREE:
			return CoverageType.MINI_BIRCH_TRUNK;
		case SHORT_BIRCH_TREE:
		case BIRCH_TREE:
		case TALL_BIRCH_TREE:
			return CoverageType.BIRCH_TRUNK;
		
		case MINI_JUNGLE_TREE:
			return CoverageType.MINI_JUNGLE_TRUNK;
		case SHORT_JUNGLE_TREE:
		case JUNGLE_TREE:
		case TALL_JUNGLE_TREE:
			return CoverageType.JUNGLE_TRUNK;
		
		case MINI_SWAMP_TREE:
			return CoverageType.MINI_SWAMP_TRUNK;
		case SWAMP_TREE:
			return CoverageType.SWAMP_TRUNK;
		
		case MINI_ACACIA_TREE:
			return CoverageType.MINI_ACACIA_TRUNK;
		case ACACIA_TREE:
			return CoverageType.ACACIA_TRUNK;
		
		default:
			return coverageType;
		}
	}
}
