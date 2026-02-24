package com.yigit.advancedhud.util;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.resources.ResourceLocation;
import com.yigit.advancedhud.mixin.client.ClientPlayerInteractionManagerAccessor;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

public class TargetInfo {
    private String name = "";
    private String extraInfo = "";
    private float health = -1;
    private float maxHealth = -1;
    private int armor = 0;
    private int toughness = 0;
    private String owner = "";
    private String modName = "";
    private String effectiveTool = "";
    private int growthProgress = -1;
    private boolean isCrops = false;
    private boolean isEntity = false;
    private String entityId = "";
    private boolean isWaterlogged = false;
    private boolean canHarvest = false;
    private float breakingProgress = 0;
    private float horseJump = -1;
    private float horseSpeed = -1;
    private String villagerProfession = "";
    private int villagerLevel = -1;
    private int beeCount = -1;
    private int itemCount = -1;
    private int inventorySize = -1;
    private String harvestLevel = "";
    private net.minecraft.world.item.ItemStack stack = net.minecraft.world.item.ItemStack.EMPTY;
    private LivingEntity targetedEntity = null;
    private static final Map<String, String> MOD_NAME_CACHE = new HashMap<>();

    public void update(Minecraft client) {
        HitResult hit = client.hitResult;
        if (hit == null || hit.getType() == HitResult.Type.MISS) {
            reset();
            return;
        }

        if (hit.getType() == HitResult.Type.BLOCK && hit instanceof BlockHitResult blockHit) {
            BlockPos pos = blockHit.getBlockPos();
            BlockState state = client.level.getBlockState(pos);
            this.name = state.getBlock().getName().getString();
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
            this.extraInfo = id.toString();
            this.modName = getModName(id.getNamespace());
            this.isEntity = false;
            this.stack = state.getBlock().asItem().getDefaultInstance();
            if (this.stack.isEmpty()) {
                this.stack = new net.minecraft.world.item.ItemStack(state.getBlock());
            }
            this.health = -1;
            
            // Tool Info
            if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) this.effectiveTool = "Pickaxe";
            else if (state.is(BlockTags.MINEABLE_WITH_AXE)) this.effectiveTool = "Axe";
            else if (state.is(BlockTags.MINEABLE_WITH_SHOVEL)) this.effectiveTool = "Shovel";
            else if (state.is(BlockTags.MINEABLE_WITH_HOE)) this.effectiveTool = "Hoe";
            
            this.harvestLevel = "";
            if (state.is(BlockTags.NEEDS_DIAMOND_TOOL)) this.harvestLevel = "Diamond";
            else if (state.is(BlockTags.NEEDS_IRON_TOOL)) this.harvestLevel = "Iron";
            else if (state.is(BlockTags.NEEDS_STONE_TOOL)) this.harvestLevel = "Stone";
            
            // Generic Crop Growth
            this.isCrops = false;
            this.growthProgress = -1;
            for (Property<?> property : state.getProperties()) {
                if (property instanceof IntegerProperty intProperty && (property.getName().equals("age") || property.getName().endsWith("_age"))) {
                    int age = state.getValue(intProperty);
                    int maxAge = intProperty.getPossibleValues().stream().mapToInt(v -> (Integer) v).max().orElse(0);
                    if (maxAge > 0) {
                        this.growthProgress = (age * 100) / maxAge;
                        this.isCrops = true;
                        break;
                    }
                }
            }

            // Waterlogged
            this.isWaterlogged = state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED);

            // Harvestability
            if (client.player != null) {
                this.canHarvest = client.player.hasCorrectToolForDrops(state);
            }

            // Breaking Progress
            if (client.gameMode != null) {
                this.breakingProgress = ((ClientPlayerInteractionManagerAccessor) client.gameMode).getBreakingProgress();
            }

            // Block Entity Data
            BlockEntity blockEntity = client.level.getBlockEntity(pos);
            
            if (blockEntity instanceof net.minecraft.world.Nameable nameable && nameable.hasCustomName()) {
                this.name = nameable.getDisplayName().getString();
            }

            if (blockEntity instanceof BeehiveBlockEntity beehive) {
                this.beeCount = beehive.getOccupantCount();
            } else {
                this.beeCount = -1;
            }

            if (blockEntity instanceof Container inv) {
                int totalItems = 0;
                for (int i = 0; i < inv.getContainerSize(); i++) {
                    if (!inv.getItem(i).isEmpty()) totalItems++;
                }
                
                boolean isLikelySynced = blockEntity instanceof AbstractFurnaceBlockEntity || 
                                       blockEntity instanceof BrewingStandBlockEntity ||
                                       blockEntity instanceof CampfireBlockEntity ||
                                       blockEntity instanceof ChiseledBookShelfBlockEntity;

                if (totalItems > 0 || isLikelySynced) {
                    this.itemCount = totalItems;
                    this.inventorySize = inv.getContainerSize();
                } else {
                    this.itemCount = -1;
                    this.inventorySize = -1;
                }
            } else {
                this.itemCount = -1;
                this.inventorySize = -1;
            }

        } else if (hit.getType() == HitResult.Type.ENTITY && hit instanceof EntityHitResult entityHit) {
            Entity entity = entityHit.getEntity();
            this.name = entity.getDisplayName().getString();
            this.isEntity = true;
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
            this.entityId = id.toString();
            this.modName = getModName(id.getNamespace());
            
            if (entity instanceof LivingEntity living) {
                this.targetedEntity = living;
                this.health = living.getHealth();
                this.maxHealth = living.getMaxHealth();
                this.extraInfo = String.format("%.1f / %.1f HP", health, maxHealth);
                this.armor = (int) living.getAttributeValue(Attributes.ARMOR);
                this.toughness = (int) living.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
                if (living instanceof TamableAnimal tameable && tameable.isTame() && tameable.getOwner() != null) {
                    this.owner = tameable.getOwner().getDisplayName().getString();
                }
                
                // Horse Stats
                if (living instanceof AbstractHorse horse) {
                    this.horseJump = (float) horse.getAttributeValue(Attributes.JUMP_STRENGTH);
                    this.horseSpeed = (float) horse.getAttributeValue(Attributes.MOVEMENT_SPEED);
                }
                
                // Villager Info
                if (living instanceof Villager villager) {
                    VillagerData data = villager.getVillagerData();
                    this.villagerProfession = BuiltInRegistries.VILLAGER_PROFESSION.getKey(data.getProfession()).getPath();
                    this.villagerLevel = data.getLevel();
                }
            } else {
                this.health = -1;
                this.extraInfo = "Entity";
                this.targetedEntity = null;
            }
            this.stack = net.minecraft.world.item.ItemStack.EMPTY;
        } else {
            reset();
        }
    }

    private void reset() {
        this.name = "";
        this.extraInfo = "";
        this.health = -1;
        this.maxHealth = -1;
        this.armor = 0;
        this.toughness = 0;
        this.owner = "";
        this.modName = "";
        this.effectiveTool = "";
        this.growthProgress = -1;
        this.isCrops = false;
        this.isEntity = false;
        this.entityId = "";
        this.isWaterlogged = false;
        this.canHarvest = false;
        this.breakingProgress = 0;
        this.horseJump = -1;
        this.horseSpeed = -1;
        this.villagerProfession = "";
        this.villagerLevel = -1;
        this.beeCount = -1;
        this.itemCount = -1;
        this.inventorySize = -1;
        this.harvestLevel = "";
        this.stack = net.minecraft.world.item.ItemStack.EMPTY;
        this.targetedEntity = null;
    }

    private String getModName(String namespace) {
        if (MOD_NAME_CACHE.containsKey(namespace)) {
            return MOD_NAME_CACHE.get(namespace);
        }
        
        String name = ModList.get().getModContainerById(namespace)
                .map(container -> container.getModInfo().getDisplayName())
                .orElse(namespace.substring(0, 1).toUpperCase() + namespace.substring(1));
        
        MOD_NAME_CACHE.put(namespace, name);
        return name;
    }

    public String getName() { return name; }
    public String getExtraInfo() { return extraInfo; }
    public float getHealth() { return health; }
    public float getMaxHealth() { return maxHealth; }
    public int getArmor() { return armor; }
    public int getToughness() { return toughness; }
    public String getOwner() { return owner; }
    public String getModName() { return modName; }
    public String getEffectiveTool() { return effectiveTool; }
    public int getGrowthProgress() { return growthProgress; }
    public boolean isCrops() { return isCrops; }
    public String getEntityId() { return entityId; }
    public boolean isEntity() { return isEntity; }
    public boolean isWaterlogged() { return isWaterlogged; }
    public boolean canHarvest() { return canHarvest; }
    public float getBreakingProgress() { return breakingProgress; }
    public float getHorseJump() { return horseJump; }
    public float getHorseSpeed() { return horseSpeed; }
    public String getVillagerProfession() { return villagerProfession; }
    public int getVillagerLevel() { return villagerLevel; }
    public int getBeeCount() { return beeCount; }
    public int getItemCount() { return itemCount; }
    public int getInventorySize() { return inventorySize; }
    public String getHarvestLevel() { return harvestLevel; }
    public net.minecraft.world.item.ItemStack getStack() { return stack; }
    public LivingEntity getTargetedEntity() { return targetedEntity; }
    public boolean hasTarget() { return !name.isEmpty(); }
}
