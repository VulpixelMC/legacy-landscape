package gay.sylv.legacy_landscape.item;

import gay.sylv.legacy_landscape.CommonConfig;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.data_attachment.LegacyChunkType;
import gay.sylv.legacy_landscape.data_components.Broken;
import gay.sylv.legacy_landscape.data_components.LegacyComponents;
import gay.sylv.legacy_landscape.networking.client_bound.LegacyChunkPayload;
import gay.sylv.legacy_landscape.util.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class JappasWandItem extends TooltipItem {

	public JappasWandItem(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
		if (stack.has(LegacyComponents.BROKEN)) {
			return "broken." + Objects.requireNonNull(stack.get(LegacyComponents.BROKEN)).level() + ".item.legacy_landscape.jappas_wand";
		}

		return super.getDescriptionId(stack);
	}

	@Override
	protected boolean showTooltip(
		@NotNull ItemStack stack,
		@NotNull TooltipContext context,
		@NotNull Component tooltip,
		@NotNull TooltipFlag tooltipFlag
	) {
		Broken brokenLevel = ((ConditionalText) tooltip).legacy_landscape$getBroken();
		return brokenLevel.equals(stack.getOrDefault(LegacyComponents.BROKEN, Broken.UNBROKEN));
	}

	@Override
	public @NotNull InteractionResult useOn(UseOnContext context) {
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());
		if (state.isAir()) return InteractionResult.PASS;


		LevelChunk chunk = context.getLevel().getChunkAt(context.getClickedPos());
		Player player = Objects.requireNonNull(context.getPlayer());

		boolean playerCanUndecay = player.getAbilities().instabuild || CommonConfig.isDemoMode();

		// Prevent non-Creative players from recovering decayed chunks.
		if (
			chunk.hasData(LegacyAttachments.LEGACY_CHUNK) &&
			chunk.getData(LegacyAttachments.LEGACY_CHUNK) == LegacyChunkType.DECAYED &&
			!playerCanUndecay
		) {
			return InteractionResult.PASS;
		}

		// Prevent Adventure players from interacting with chunks.
		if (!player.mayBuild() && !chunk.hasData(LegacyAttachments.ALLOW_ADVENTURE_MODE)) {
			if (!context.getLevel().isClientSide()) {
				player.sendSystemMessage(Constants.ALLOW_ADVENTURE_MODE_OFF);
			}
			return InteractionResult.PASS;
		}

		boolean broken = context.getItemInHand().getOrDefault(LegacyComponents.BROKEN, Broken.UNBROKEN).equals(Broken.of(1));

		if (chunk.hasData(LegacyAttachments.LEGACY_CHUNK) || broken) {
			if (broken) {
				context.getLevel().playSound(context.getPlayer(), Objects.requireNonNull(context.getPlayer()), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.PLAYERS, 0.25F, 0.25F);
			} else {
				context.getLevel().playSound(context.getPlayer(), Objects.requireNonNull(context.getPlayer()), SoundEvents.END_PORTAL_SPAWN, SoundSource.PLAYERS, 0.25F, 3.0F);
			}

			if (!context.getLevel().isClientSide()) {
				boolean isCreative = player.isCreative();
				if (broken && !isCreative) {
					LegacyAttachments.setChunkData(
						(ServerLevel) context.getLevel(),
						chunk,
						LegacyAttachments.LEGACY_CHUNK,
						LegacyChunkType.DECAYED,
						data -> new LegacyChunkPayload(chunk.getPos(), Optional.of(data))
					);
				} else if (broken && !player.isCrouching()) {
					if (!chunk.hasData(LegacyAttachments.ALLOW_ADVENTURE_MODE)) {
						LegacyAttachments.setChunkData(
							(ServerLevel) context.getLevel(),
							chunk,
							LegacyAttachments.ALLOW_ADVENTURE_MODE
						);
						player.sendSystemMessage(Component.translatable("legacy_landscape.allow_adventure_mode.on"));
					} else {
						LegacyAttachments.removeChunkData(
							(ServerLevel) context.getLevel(),
							chunk,
							LegacyAttachments.ALLOW_ADVENTURE_MODE
						);
						player.sendSystemMessage(Component.translatable("legacy_landscape.allow_adventure_mode.off"));
					}
				} else if (broken && player.isCrouching()) {
					if (chunk.hasData(LegacyAttachments.ALLOW_ADVENTURE_MODE)) {
						player.sendSystemMessage(Component.translatable("legacy_landscape.allow_adventure_mode.on").withStyle(ChatFormatting.ITALIC));
					} else {
						player.sendSystemMessage(Component.translatable("legacy_landscape.allow_adventure_mode.off").withStyle(ChatFormatting.ITALIC));
					}
				} else {
					LegacyAttachments.removeChunkData(
						(ServerLevel) context.getLevel(),
						chunk,
						LegacyAttachments.LEGACY_CHUNK,
						() -> new LegacyChunkPayload(chunk.getPos(), Optional.empty())
					);
				}

				EquipmentSlot slot = context.getPlayer().getEquipmentSlotForItem(context.getItemInHand());

				context.getItemInHand().hurtAndBreak(3, (ServerLevel) context.getLevel(), context.getPlayer(), item -> {
					ItemStack stack = context.getPlayer().getItemBySlot(slot);
					stack.grow(1);
					dustOfDecay(stack);
					context.getPlayer().onEquippedItemBroken(item, slot);
				});
			}

			return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
		}

		return InteractionResult.PASS;
	}

	/**
	 * @implNote Do not change this method! These modifications do not apply retroactively.
	 */
	private static void dustOfDecay(ItemStack stack) {
		stack.set(LegacyComponents.BROKEN, new Broken(1));
		stack.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
		stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(1));
		stack.set(DataComponents.RARITY, Rarity.COMMON);
	}

	public static ItemStack dustOfDecay() {
		ItemStack stack = new ItemStack(LegacyItems.JAPPAS_WAND.get());
		dustOfDecay(stack);
		return stack;
	}
}
