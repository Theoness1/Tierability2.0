package tierability.worldgen.feature;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import tierability.TierabilityMod;
import tierability.block.TierabilityBlocks;

public class TierabilityConfiguredFeature {

    private static ConfiguredFeature<?, ?> ORE_T1_OVERWORLD = Feature.ORE
            .configure(new OreFeatureConfig(
                    OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
                    TierabilityBlocks.T1_ORE.getDefaultState(),
                    6)) // Vein size
            .range(new RangeDecoratorConfig(
                    // You can also use one of the other height providers if you don't want a uniform distribution
                    UniformHeightProvider.create(YOffset.aboveBottom(5), YOffset.fixed(52)))) // Inclusive min and max height
            .spreadHorizontally()
            .repeat(7); // Number of veins per chunk

    private static ConfiguredFeature<?, ?> ORE_T2_OVERWORLD = Feature.ORE
            .configure(new OreFeatureConfig(
                    OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
                    TierabilityBlocks.T2_ORE.getDefaultState(),
                    4)) // Vein size
            .range(new RangeDecoratorConfig(
                    // You can also use one of the other height providers if you don't want a uniform distribution
                    UniformHeightProvider.create(YOffset.aboveBottom(5), YOffset.fixed(32)))) // Inclusive min and max height
            .spreadHorizontally()
            .repeat(5); // Number of veins per chunk

    public static final Feature<DefaultFeatureConfig> CRYSTAL_FEATURE = new CrystalFeature();
    public static final ConfiguredFeature<?,?> CRYSTAL_FEATURE_CONFIG = CRYSTAL_FEATURE.configure(new DefaultFeatureConfig()).decorate(Decorator.NOPE.configure(DecoratorConfig.DEFAULT));

    static public void register() {
        RegistryKey<ConfiguredFeature<?, ?>> oreT1Overworld = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
                new Identifier(TierabilityMod.MODID, "ore_t1_overworld"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreT1Overworld.getValue(), ORE_T1_OVERWORLD);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, oreT1Overworld);

        RegistryKey<ConfiguredFeature<?, ?>> oreT2Overworld = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
                new Identifier(TierabilityMod.MODID, "ore_t2_overworld"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreT2Overworld.getValue(), ORE_T2_OVERWORLD);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, oreT2Overworld);

        Registry.register(Registry.FEATURE, new Identifier(TierabilityMod.MODID, "crystal_feature"), CRYSTAL_FEATURE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(TierabilityMod.MODID, "crystal_feature"), CRYSTAL_FEATURE_CONFIG);
        if(BuiltinRegistries.CONFIGURED_FEATURE.getKey(CRYSTAL_FEATURE_CONFIG).isPresent()){
            BiomeModifications.addFeature(ctx -> ctx.getBiome().getCategory() != Biome.Category.THEEND, GenerationStep.Feature.UNDERGROUND_DECORATION, BuiltinRegistries.CONFIGURED_FEATURE.getKey(CRYSTAL_FEATURE_CONFIG).get());
        }
    }


}
