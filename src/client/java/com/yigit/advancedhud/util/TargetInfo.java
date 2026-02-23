package com.yigit.advancedhud.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.registry.Registries;

public class TargetInfo {
    private String name = "";
    private String extraInfo = "";
    private float health = -1;
    private float maxHealth = -1;
    private boolean isEntity = false;
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
            this.extraInfo = Registries.BLOCK.getId(state.getBlock()).toString();
            this.isEntity = false;
            this.stack = new net.minecraft.item.ItemStack(state.getBlock());
            this.health = -1;
        } else if (hit.getType() == HitResult.Type.ENTITY && hit instanceof EntityHitResult entityHit) {
            Entity entity = entityHit.getEntity();
            this.name = entity.getDisplayName().getString();
            this.isEntity = true;
            if (entity instanceof LivingEntity living) {
                this.health = living.getHealth();
                this.maxHealth = living.getMaxHealth();
                this.extraInfo = String.format("%.1f / %.1f HP", health, maxHealth);
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
        this.isEntity = false;
        this.stack = net.minecraft.item.ItemStack.EMPTY;
    }

    public String getName() { return name; }
    public String getExtraInfo() { return extraInfo; }
    public float getHealth() { return health; }
    public float getMaxHealth() { return maxHealth; }
    public boolean isEntity() { return isEntity; }
    public net.minecraft.item.ItemStack getStack() { return stack; }
    public boolean hasTarget() { return !name.isEmpty(); }
}
