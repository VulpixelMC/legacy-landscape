package gay.sylv.legacy_landscape.item;

import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.networking.client_bound.LegacyChunkPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class JappasWandItem extends Item {
	public JappasWandItem(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull InteractionResult useOn(UseOnContext context) {
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());
		if (state.isAir()) return InteractionResult.PASS;


		LevelChunk chunk = context.getLevel().getChunkAt(context.getClickedPos());

		if (chunk.getData(LegacyAttachments.LEGACY_CHUNK)) {
			context.getLevel().playSound(context.getPlayer(), Objects.requireNonNull(context.getPlayer()), SoundEvents.END_PORTAL_SPAWN, SoundSource.PLAYERS, 0.25F, 3.0F);

			if (!context.getLevel().isClientSide()) {
				LegacyAttachments.removeChunkData(
					(ServerLevel) context.getLevel(),
					chunk,
					LegacyAttachments.LEGACY_CHUNK,
					() -> new LegacyChunkPayload(chunk.getPos(), false)
				);
				EquipmentSlot slot = context.getPlayer().getEquipmentSlotForItem(context.getItemInHand());

				context.getItemInHand().hurtAndBreak(3, context.getPlayer(), slot);
			}

			return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
		}

		return InteractionResult.PASS;
	}
}
