package gay.sylv.legacy_landscape.mixin.client;

import gay.sylv.legacy_landscape.entity.SilentLivingEntity;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class Mixin_LivingEntity extends Entity implements SilentLivingEntity {
	private Mixin_LivingEntity(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(
		method = "onEquipItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/Equipable;get(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/Equipable;"
		)
	)
	private void playEquipItemSoundLocally(EquipmentSlot slot, ItemStack oldItem, ItemStack newItem, CallbackInfo ci) {
		Equipable equipable = Equipable.get(newItem);
		if (((LivingEntity) (Object) this) instanceof LocalPlayer && this.legacy_landscape$isSilent()) {
			if (!this.isSpectator() && equipable != null && equipable.getEquipmentSlot() == slot) {
				this.level()
					.playLocalSound(
						this.getX(),
						this.getY(),
						this.getZ(),
						equipable.getEquipSound().value(),
						this.getSoundSource(),
						1.0F,
						1.0F,
						false
					);
			}
		}
	}
}
