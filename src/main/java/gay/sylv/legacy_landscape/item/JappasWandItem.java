package gay.sylv.legacy_landscape.item;

import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.data_attachment.LegacyChunkType;
import gay.sylv.legacy_landscape.data_components.Broken;
import gay.sylv.legacy_landscape.data_components.LegacyComponents;
import gay.sylv.legacy_landscape.networking.client_bound.LegacyChunkPayload;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

		if (chunk.hasData(LegacyAttachments.LEGACY_CHUNK) && chunk.getData(LegacyAttachments.LEGACY_CHUNK) == LegacyChunkType.DECAYED && !player.isCreative()) {
			return InteractionResult.PASS;
		}

		boolean broken = context.getItemInHand().getOrDefault(LegacyComponents.BROKEN, Broken.UNBROKEN).level() == 1;

		if (chunk.hasData(LegacyAttachments.LEGACY_CHUNK) || broken) {
			context.getLevel().playSound(context.getPlayer(), Objects.requireNonNull(context.getPlayer()), SoundEvents.END_PORTAL_SPAWN, SoundSource.PLAYERS, 0.25F, 3.0F);

			if (!context.getLevel().isClientSide()) {
				if (broken) {
					LegacyAttachments.setChunkData(
						(ServerLevel) context.getLevel(),
						chunk,
						LegacyAttachments.LEGACY_CHUNK,
						LegacyChunkType.DECAYED,
						data -> new LegacyChunkPayload(chunk.getPos(), Optional.of(data))
					);
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
					stack.set(LegacyComponents.BROKEN, new Broken(1));
					stack.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
					stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(1));
					context.getPlayer().onEquippedItemBroken(item, slot);
				});
			}

			return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
		}

		return InteractionResult.PASS;
	}
}
