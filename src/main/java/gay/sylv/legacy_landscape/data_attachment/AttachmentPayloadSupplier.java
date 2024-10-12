package gay.sylv.legacy_landscape.data_attachment;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

@FunctionalInterface
public interface AttachmentPayloadSupplier<P extends CustomPacketPayload, H extends IAttachmentHolder, T> {
	P of(H holder, T data);
}
