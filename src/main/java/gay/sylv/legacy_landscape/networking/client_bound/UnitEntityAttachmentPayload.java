package gay.sylv.legacy_landscape.networking.client_bound;

import gay.sylv.legacy_landscape.codec.LegacyCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.jetbrains.annotations.NotNull;

import static gay.sylv.legacy_landscape.LegacyLandscape.id;

public record UnitEntityAttachmentPayload(ResourceKey<AttachmentType<?>> attachmentType, int entityId, boolean remove) implements CustomPacketPayload {
	public static final Type<UnitEntityAttachmentPayload> TYPE = new Type<>(id("unit_entity_attachment"));

	public static final StreamCodec<FriendlyByteBuf, UnitEntityAttachmentPayload> STREAM_CODEC = StreamCodec.composite(
		LegacyCodecs.Stream.ATTACHMENT_TYPE,
		UnitEntityAttachmentPayload::attachmentType,
		ByteBufCodecs.VAR_INT,
		UnitEntityAttachmentPayload::entityId,
		LegacyCodecs.Stream.BOOL,
		UnitEntityAttachmentPayload::remove,
		UnitEntityAttachmentPayload::new
	);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
