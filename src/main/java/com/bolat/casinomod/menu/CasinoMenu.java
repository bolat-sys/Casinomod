package com.bolat.casinomod.menu;

import com.bolat.casinomod.registry.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Rarity;
import net.minecraft.util.RandomSource;

public class CasinoMenu extends AbstractContainerMenu {
    private final SimpleContainer casino = new SimpleContainer(2);
    private final Player owner;
    private boolean rolling = false;

    public CasinoMenu(int id, Inventory inventory, FriendlyByteBuf data) { this(id, inventory); }
    public CasinoMenu(int id, Inventory inventory) {
        super(ModMenus.CASINO_MENU.get(), id);
        this.owner = inventory.player;
        addSlot(new Slot(casino, 0, 44, 35) {
            @Override public void setChanged() { super.setChanged(); tryRoll(); }
            @Override public boolean mayPlace(ItemStack stack) { return !stack.isEmpty(); }
        });
        addSlot(new Slot(casino, 1, 116, 35) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
        });
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++)
            addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col, 8 + col * 18, 142));
    }

    private void tryRoll() {
        if (rolling || casino.getItem(0).isEmpty() || !(owner instanceof ServerPlayer)) return;
        rolling = true;
        ItemStack stake = casino.getItem(0).copy();
        if (!casino.getItem(1).isEmpty()) {
            casino.setItem(0, ItemStack.EMPTY);
            if (!((ServerPlayer) owner).getInventory().add(stake)) ((ServerPlayer) owner).drop(stake, false);
            ((ServerPlayer) owner).sendSystemMessage(Component.literal("§eCollect your prize before placing another bet!"));
            rolling = false;
            return;
        }
        int chance = winChance(stake);
        casino.setItem(0, ItemStack.EMPTY);
        if (RandomSource.create().nextInt(100) < chance) {
            ItemStack reward = makeReward(stake);
            casino.setItem(1, reward);
            ((ServerPlayer) owner).sendSystemMessage(Component.literal("§aYOU WON! §fChance: " + chance + "%"));
        } else {
            ((ServerPlayer) owner).sendSystemMessage(Component.literal("§cYou lost! §7Chance was " + chance + "%"));
        }
        casino.setChanged();
        rolling = false;
    }

    private int winChance(ItemStack stack) {
        int base;
        if (stack.is(Items.NETHERITE_INGOT) || stack.is(Items.NETHERITE_BLOCK)) base = 80;
        else if (stack.is(Items.DIAMOND) || stack.is(Items.DIAMOND_BLOCK)) base = 60;
        else if (stack.is(Items.GOLD_INGOT) || stack.is(Items.GOLD_BLOCK)) base = 40;
        else if (stack.is(Items.IRON_INGOT) || stack.is(Items.IRON_BLOCK)) base = 25;
        else if (stack.is(ItemTags.LOGS) || stack.is(ItemTags.PLANKS)) base = 5;
        else if (stack.is(ItemTags.STONE_TOOL_MATERIALS) || stack.is(Items.COBBLESTONE)) base = 10;
        else if (stack.getRarity() == Rarity.EPIC) base = 70;
        else if (stack.getRarity() == Rarity.RARE) base = 45;
        else if (stack.getRarity() == Rarity.UNCOMMON) base = 25;
        else base = 8;
        return Math.min(90, Math.max(1, (int)Math.round(base * Math.sqrt(stack.getCount() / 64.0))));
    }

    private ItemStack makeReward(ItemStack stake) {
        if (stake.is(Items.NETHERITE_INGOT) || stake.is(Items.NETHERITE_BLOCK)) return new ItemStack(Items.NETHERITE_INGOT, 1 + RandomSource.create().nextInt(2));
        if (stake.is(Items.DIAMOND) || stake.is(Items.DIAMOND_BLOCK)) return new ItemStack(Items.DIAMOND, 2 + RandomSource.create().nextInt(5));
        if (stake.is(Items.GOLD_INGOT) || stake.is(Items.GOLD_BLOCK)) return new ItemStack(Items.GOLD_INGOT, 4 + RandomSource.create().nextInt(9));
        if (stake.is(Items.IRON_INGOT) || stake.is(Items.IRON_BLOCK)) return new ItemStack(Items.IRON_INGOT, 4 + RandomSource.create().nextInt(9));
        if (stake.is(ItemTags.LOGS) || stake.is(ItemTags.PLANKS)) return new ItemStack(Items.EMERALD, 1 + RandomSource.create().nextInt(2));
        return new ItemStack(Items.EMERALD, 1 + RandomSource.create().nextInt(4));
    }

    @Override public boolean stillValid(Player player) { return true; }
    @Override public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack source = slot.getItem(); result = source.copy();
            if (index < 2) { if (!moveItemStackTo(source, 2, slots.size(), true)) return ItemStack.EMPTY; }
            else { if (!moveItemStackTo(source, 0, 1, false)) return ItemStack.EMPTY; }
            if (source.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        }
        return result;
    }
}
