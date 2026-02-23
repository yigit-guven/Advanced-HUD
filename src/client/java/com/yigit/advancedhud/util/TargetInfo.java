package com.yigit.advancedhud.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.registry.Registries;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.state.property.Properties;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.Identifier;
import com.yigit.advancedhud.mixin.client.ClientPlayerInteractionManagerAccessor;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerData;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;

public class TargetInfo {
    private String name = "";
    private String extraInfo = "";
    private float health = -1;
    private float maxHealth = -1;
    private int armor = 0;
    private String owner = "";
    private String version = "";
    private String modName = "";
    private String effectiveTool = "";
    private int growthProgress = -1;
    private boolean isCrops = false;
    private boolean isEntity = false;
    private String entityId = "";
    private boolean isWaterlogged = false;
    private float breakingProgress = 0;
    private float horseJump = -1;
    private float horseSpeed = -1;
    private String villagerProfession = "";
    private int villagerLevel = -1;
    private int beeCount = -1;
    private int itemCount = -1;
    private int inventorySize = -1;
    private net.minecraft.item.ItemStack stack = net.minecraft.item.ItemStack.EMPTY;

    public void update(MinecraftClient client) {
        HitResult hit = client.crosshairTarget;
        if (hit == null || hit.getType() == HitResult.Type.MISS) {
            reset();
            return;
        }

        if (hit.getType() == HitResult.Type.BLOCK && hit instanceof BlockHitResult blockHit) {
            BlockState state = client.world.getBlockState(blockHit.getBlockPos());
            this.name = state.getBlock().getName().getString();
            Identifier id = Registries.BLOCK.getId(state.getBlock());
            this.extraInfo = id.toString();
            this.modName = getModName(id.getNamespace());
            this.isEntity = false;
            this.stack = new net.minecraft.item.ItemStack(state.getBlock());
            this.health = -1;
            
            // Tool Info
            if (state.isIn(BlockTags.PICKAXE_MINEABLE)) this.effectiveTool = "Pickaxe";
            else if (state.isIn(BlockTags.AXE_MINEABLE)) this.effectiveTool = "Axe";
            else if (state.isIn(BlockTags.SHOVEL_MINEABLE)) this.effectiveTool = "Shovel";
            else if (state.isIn(BlockTags.HOE_MINEABLE)) this.effectiveTool = "Hoe";
            
            // Crop Growth
            if (state.contains(Properties.AGE_7)) {
                this.growthProgress = (state.get(Properties.AGE_7) * 100) / 7;
                this.isCrops = true;
            } else if (state.contains(Properties.AGE_3)) {
                this.growthProgress = (state.get(Properties.AGE_3) * 100) / 3;
                this.isCrops = true;
            } else if (state.contains(Properties.AGE_15)) {
                this.growthProgress = (state.get(Properties.AGE_15) * 100) / 15;
                this.isCrops = true;
            } else if (state.contains(Properties.AGE_25)) {
                this.growthProgress = (state.get(Properties.AGE_25) * 100) / 25;
                this.isCrops = true;
            } else if (state.contains(Properties.AGE_5)) {
                this.growthProgress = (state.get(Properties.AGE_5) * 100) / 5;
                this.isCrops = true;
            } else if (state.contains(Properties.AGE_4)) {
                this.growthProgress = (state.get(Properties.AGE_4) * 100) / 4;
                this.isCrops = true;
            } else if (state.contains(Properties.AGE_2)) {
                this.growthProgress = (state.get(Properties.AGE_2) * 100) / 2;
                this.isCrops = true;
            } else if (state.contains(Properties.AGE_1)) {
                this.growthProgress = (state.get(Properties.AGE_1) * 100) / 1;
                this.isCrops = true;
            }

            // Waterlogged
            if (state.contains(Properties.WATERLOGGED)) {
                this.isWaterlogged = state.get(Properties.WATERLOGGED);
            }

            // Breaking Progress
            if (client.interactionManager != null) {
                this.breakingProgress = ((ClientPlayerInteractionManagerAccessor) client.interactionManager).getBreakingProgress();
            }

            // Block Entity Data (Beehive, Containers)
            BlockPos pos = blockHit.getBlockPos();
            BlockEntity blockEntity = client.world.getBlockEntity(pos);
            
            if (blockEntity instanceof BeehiveBlockEntity beehive) {
                this.beeCount = beehive.getBeeCount();
            } else {
                this.beeCount = -1;
            }

            if (blockEntity instanceof Inventory inventory) {
                int count = 0;
                boolean hasData = false;
                for (int i = 0; i < inventory.size(); i++) {
                    if (!inventory.getStack(i).isEmpty()) {
                        count++;
                        hasData = true;
                    }
                }
                // Only show if we found items OR if we are in singleplayer (where data is more likely synced)
                // In multiplayer, standard chests don't sync inventory to the client.
                if (hasData || client.isInSingleplayer()) {
                    this.itemCount = count;
                    this.inventorySize = inventory.size();
                } else {
                    // Don't show misleading 0/27 in multiplayer
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
            Identifier id = Registries.ENTITY_TYPE.getId(entity.getType());
            this.entityId = id.toString();
            this.modName = getModName(id.getNamespace());
            
            if (entity instanceof LivingEntity living) {
                this.health = living.getHealth();
                this.maxHealth = living.getMaxHealth();
                this.extraInfo = String.format("%.1f / %.1f HP", health, maxHealth);
                this.armor = (int) living.getAttributeValue(EntityAttributes.GENERIC_ARMOR);
                if (living instanceof TameableEntity tameable && tameable.isTamed() && tameable.getOwner() != null) {
                    this.owner = tameable.getOwner().getDisplayName().getString();
                }
                
                // Horse Stats
                if (living instanceof AbstractHorseEntity horse) {
                    this.horseJump = (float) horse.getAttributeValue(EntityAttributes.HORSE_JUMP_STRENGTH);
                    this.horseSpeed = (float) horse.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                }
                
                // Villager Info
                if (living instanceof VillagerEntity villager) {
                    VillagerData data = villager.getVillagerData();
                    this.villagerProfession = Registries.VILLAGER_PROFESSION.getId(data.getProfession()).getPath();
                    this.villagerLevel = data.getLevel();
                }
            } else {
                this.health = -1;
                this.extraInfo = "Entity";
            }
            this.stack = net.minecraft.item.ItemStack.EMPTY;
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
        this.owner = "";
        this.modName = "";
        this.effectiveTool = "";
        this.growthProgress = -1;
        this.isCrops = false;
        this.isEntity = false;
        this.entityId = "";
        this.breakingProgress = 0;
        this.horseJump = -1;
        this.horseSpeed = -1;
        this.villagerProfession = "";
        this.villagerLevel = -1;
        this.beeCount = -1;
        this.itemCount = -1;
        this.inventorySize = -1;
        this.stack = net.minecraft.item.ItemStack.EMPTY;
    }

    private String getModName(String namespace) {
        return FabricLoader.getInstance().getModContainer(namespace)
                .map(container -> container.getMetadata().getName())
                .orElse(namespace.substring(0, 1).toUpperCase() + namespace.substring(1));
    }

    public String getName() { return name; }
    public String getExtraInfo() { return extraInfo; }
    public float getHealth() { return health; }
    public float getMaxHealth() { return maxHealth; }
    public int getArmor() { return armor; }
    public String getOwner() { return owner; }
    public String getModName() { return modName; }
    public String getEffectiveTool() { return effectiveTool; }
    public int getGrowthProgress() { return growthProgress; }
    public boolean isCrops() { return isCrops; }
    public String getEntityId() { return entityId; }
    public boolean isEntity() { return isEntity; }
    public boolean isWaterlogged() { return isWaterlogged; }
    public float getBreakingProgress() { return breakingProgress; }
    public float getHorseJump() { return horseJump; }
    public float getHorseSpeed() { return horseSpeed; }
    public String getVillagerProfession() { return villagerProfession; }
    public int getVillagerLevel() { return villagerLevel; }
    public int getBeeCount() { return beeCount; }
    public int getItemCount() { return itemCount; }
    public int getInventorySize() { return inventorySize; }
    public net.minecraft.item.ItemStack getStack() { return stack; }
    public boolean hasTarget() { return !name.isEmpty(); }
}
