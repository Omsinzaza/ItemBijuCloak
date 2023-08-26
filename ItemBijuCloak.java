/*      */ package net.narutomod.item;
/*      */ 
/*      */ import com.google.common.collect.Multimap;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.model.ModelBase;
/*      */ import net.minecraft.client.model.ModelBiped;
/*      */ import net.minecraft.client.model.ModelBox;
/*      */ import net.minecraft.client.model.ModelRenderer;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.OpenGlHelper;
/*      */ import net.minecraft.client.renderer.block.model.ModelResourceLocation;
/*      */ import net.minecraft.client.util.ITooltipFlag;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.SharedMonsterAttributes;
/*      */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.MobEffects;
/*      */ import net.minecraft.inventory.EntityEquipmentSlot;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemArmor;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTBase;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.potion.PotionEffect;
/*      */ import net.minecraft.util.math.MathHelper;
/*      */ import net.minecraft.util.math.RayTraceResult;
/*      */ import net.minecraft.util.text.TextFormatting;
/*      */ import net.minecraft.util.text.translation.I18n;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraftforge.client.event.ModelRegistryEvent;
/*      */ import net.minecraftforge.client.model.ModelLoader;
/*      */ import net.minecraftforge.common.util.EnumHelper;
/*      */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*      */ import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
/*      */ import net.minecraftforge.fml.relauncher.Side;
/*      */ import net.minecraftforge.fml.relauncher.SideOnly;
/*      */ import net.narutomod.Chakra;
/*      */ import net.narutomod.ElementsNarutomodMod;
/*      */ import net.narutomod.ElementsNarutomodMod.ModElement.Tag;
/*      */ import net.narutomod.Particles;
/*      */ import net.narutomod.entity.EntityBijuManager;
/*      */ import net.narutomod.entity.EntityJinchurikiClone;
/*      */ import net.narutomod.potion.PotionChakraEnhancedStrength;
/*      */ import net.narutomod.potion.PotionReach;
/*      */ import net.narutomod.procedure.ProcedureSync;
/*      */ import net.narutomod.procedure.ProcedureUtils;
/*      */ 
/*      */ @Tag
/*      */ public class ItemBijuCloak
/*      */   extends ElementsNarutomodMod.ModElement {
/*      */   @ObjectHolder("narutomod:biju_cloakhelmet")
/*   57 */   public static final Item helmet = null;
/*      */   @ObjectHolder("narutomod:biju_cloakbody")
/*   59 */   public static final Item body = null;
/*      */   @ObjectHolder("narutomod:biju_cloaklegs")
/*   61 */   public static final Item legs = null;
/*      */   
/*   63 */   private final AttributeModifier CLOAK_MODIFIER = new AttributeModifier(UUID.fromString("e884e4a0-7f08-422d-9aac-119972cd764d"), "bijucloak.maxhealth", 180.0D, 0);
/*      */   @SideOnly(Side.CLIENT)
/*      */   private ModelBijuCloak[] bijuModel;
/*      */   
/*      */   public ItemBijuCloak(ElementsNarutomodMod instance) {
/*   68 */     super(instance, 577);
/*      */   }
/*      */ 
/*      */   
/*      */   @SideOnly(Side.CLIENT)
/*      */   public void init(FMLInitializationEvent event) {
/*   74 */     this.bijuModel = new ModelBijuCloak[10];
/*   75 */     for (int i = 0; i < 10; i++) {
/*   76 */       this.bijuModel[i] = new ModelBijuCloak(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void initElements() {
/*   82 */     ItemArmor.ArmorMaterial enuma = EnumHelper.addArmorMaterial("BIJU_CLOAK", "narutomod:sasuke_", 1024, new int[] { 1024, 1024, 1024, 1024 }, 0, null, 5.0F);
/*      */ 
/*      */     
/*   85 */     this.elements.items.add(() -> ((Item)(new ItemArmor(enuma, 0, EntityEquipmentSlot.HEAD)
/*      */         {
/*      */           @SideOnly(Side.CLIENT)
/*      */           public ModelBiped getArmorModel(EntityLivingBase living, ItemStack stack, EntityEquipmentSlot slot, ModelBiped defaultModel) {
/*   89 */             ItemBijuCloak.ModelBijuCloak armorModel = ItemBijuCloak.this.bijuModel[stack.getMetadata()];
/*   90 */             armorModel.isSneak = living.isSneaking();
/*   91 */             armorModel.isRiding = living.isRiding();
/*   92 */             armorModel.isChild = living.isChild();
/*   93 */             int tails = ItemBijuCloak.getTails(stack);
/*   94 */             (armorModel.earRight[0]).showModel = (tails != 1);
/*   95 */             armorModel.bodyShine = (tails == 9 && ItemBijuCloak.getCloakLevel(stack) == 2 && ItemBijuCloak.getCloakXp(stack) >= 800);
/*   96 */             armorModel.layerShine = true;
/*   97 */             return armorModel;
/*      */           }
/*      */ 
/*      */           
/*      */           public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
/*  102 */             super.onUpdate(itemstack, world, entity, par4, par5);
/*  103 */             if (!world.isRemote && entity instanceof EntityPlayer) {
/*  104 */               int cloakLevel = EntityBijuManager.cloakLevel((EntityPlayer)entity);
/*  105 */               if (cloakLevel <= 0) {
/*  106 */                 itemstack.shrink(1);
/*      */               }
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public int getMaxDamage() {
/*  113 */             return 0;
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean isDamageable() {
/*  118 */             return false;
/*      */           }
/*      */ 
/*      */           
/*      */           public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
/*  123 */             return ItemBijuCloak.getTexture(stack);
/*      */           }
/*      */         }).setTranslationKey("biju_cloakhelmet").setRegistryName("biju_cloakhelmet")).setCreativeTab(null));
/*      */     
/*  127 */     this.elements.items.add(() -> ((Item)(new ItemArmor(enuma, 0, EntityEquipmentSlot.CHEST)
/*      */         {
/*      */           @SideOnly(Side.CLIENT)
/*      */           public ModelBiped getArmorModel(EntityLivingBase living, ItemStack stack, EntityEquipmentSlot slot, ModelBiped defaultModel) {
/*  131 */             ItemBijuCloak.ModelBijuCloak armorModel = ItemBijuCloak.this.bijuModel[stack.getMetadata()];
/*  132 */             armorModel.isSneak = living.isSneaking();
/*  133 */             armorModel.isRiding = living.isRiding();
/*  134 */             armorModel.isChild = living.isChild();
/*  135 */             armorModel.bodyShine = (ItemBijuCloak.getTails(stack) == 9 && ItemBijuCloak.getCloakLevel(stack) == 2 && ItemBijuCloak.getCloakXp(stack) >= 800);
/*  136 */             armorModel.allTails.showModel = !armorModel.bodyShine;
/*  137 */             armorModel.layerShine = true;
/*  138 */             return armorModel;
/*      */           }
/*      */ 
/*      */           
/*      */           public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
/*  143 */             super.onUpdate(itemstack, world, entity, par4, par5);
/*      */             
/*  145 */             if (entity instanceof EntityPlayer) {
/*  146 */               EntityPlayer livingEntity = (EntityPlayer)entity;
/*  147 */               int cloakLevel = EntityBijuManager.cloakLevel(livingEntity);
/*  148 */               if (cloakLevel > 0) {
/*  149 */                 ItemStack helmetStack = livingEntity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
/*  150 */                 ItemStack legStack = livingEntity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
/*  151 */                 if (helmetStack.getItem() == ItemBijuCloak.helmet && itemstack.getItem() == ItemBijuCloak.body && legStack.getItem() == ItemBijuCloak.legs) {
/*  152 */                   ItemBijuCloak.this.setWearingFullSet(itemstack, true);
/*      */                   
/*  154 */                   if (!world.isRemote) {
/*  155 */                     ItemBijuCloak.setCloakLevel(helmetStack, cloakLevel);
/*  156 */                     ItemBijuCloak.setCloakLevel(itemstack, cloakLevel);
/*  157 */                     ItemBijuCloak.setCloakLevel(legStack, cloakLevel);
/*  158 */                     int wearingTicks = ItemBijuCloak.getWearingTicks((Entity)livingEntity);
/*  159 */                     int cloakXp = EntityBijuManager.getCloakXp(livingEntity);
/*  160 */                     wearingTicks = (wearingTicks > 0) ? ++wearingTicks : 1;
/*      */                     
/*  162 */                     if (wearingTicks <= cloakXp * 5 + 200 && Chakra.pathway(livingEntity).getAmount() > 0.0D) {
/*  163 */                       cloakXp += wearingTicks / 20;
/*  164 */                       ItemBijuCloak.setCloakXp(helmetStack, cloakXp);
/*  165 */                       ItemBijuCloak.setCloakXp(itemstack, cloakXp);
/*  166 */                       ItemBijuCloak.setCloakXp(legStack, cloakXp);
/*  167 */                       ItemBijuCloak.setWearingTicks((Entity)livingEntity, wearingTicks);
/*  168 */                       if (cloakXp >= 800 || (cloakLevel == 1 && cloakXp >= 400)) {
/*  169 */                         ItemBijuCloak.revertOriginal(livingEntity, itemstack);
/*  170 */                         ItemBijuCloak.applyEffects((EntityLivingBase)livingEntity, cloakLevel, (ItemBijuCloak.getTails(itemstack) != 1 && cloakLevel == 1));
/*      */                       } else {
/*  172 */                         ItemBijuCloak.this.spawnClone(livingEntity, itemstack);
/*      */                       } 
/*      */                     } else {
/*  175 */                       if (cloakXp < 400 || (cloakLevel == 2 && cloakXp < 800)) {
/*  176 */                         ItemBijuCloak.revertOriginal(livingEntity, itemstack);
/*      */                       }
/*  178 */                       EntityBijuManager.toggleBijuCloak(livingEntity);
/*  179 */                       itemstack.shrink(1);
/*      */                     } 
/*      */                   } 
/*      */                 } else {
/*  183 */                   ItemBijuCloak.this.setWearingFullSet(itemstack, false);
/*      */                 } 
/*  185 */               } else if (!world.isRemote) {
/*  186 */                 itemstack.shrink(1);
/*      */               } 
/*  188 */             } else if (entity instanceof EntityJinchurikiClone.EntityCustom && !world.isRemote && entity.isEntityAlive()) {
/*  189 */               ItemBijuCloak.setWearingTicks(entity, ItemBijuCloak.getWearingTicks(entity) + 1);
/*  190 */               int i = ItemBijuCloak.getCloakLevel(itemstack);
/*  191 */               ItemBijuCloak.applyEffects((EntityLivingBase)entity, i, (ItemBijuCloak.getTails(itemstack) != 1 && i == 1));
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
/*  197 */             Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
/*  198 */             if (slot == EntityEquipmentSlot.CHEST && ItemBijuCloak.this.isWearingFullSet(stack)) {
/*  199 */               multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), ItemBijuCloak.this.CLOAK_MODIFIER);
/*      */             }
/*  201 */             return multimap;
/*      */           }
/*      */ 
/*      */           
/*      */           @SideOnly(Side.CLIENT)
/*      */           public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
/*  207 */             super.addInformation(stack, worldIn, tooltip, flagIn);
/*  208 */             tooltip.add(I18n.translateToLocal("key.mcreator.specialjutsu2") + ": " + I18n.translateToLocal("entity.jinchuriki_clone.name"));
/*  209 */             int i = ItemBijuCloak.getCloakLevel(stack);
/*  210 */             if (i == 2) {
/*  211 */               tooltip.add(I18n.translateToLocal("key.mcreator.specialjutsu3") + ": " + I18n.translateToLocal("entity.tailbeastball.name"));
/*      */             }
/*  213 */             tooltip.add(TextFormatting.GRAY + I18n.translateToLocal("tooltip.bijucloak.level" + i));
/*  214 */             tooltip.add("JXP: " + TextFormatting.GREEN + ItemBijuCloak.getCloakXp(stack) + TextFormatting.RESET);
/*      */           }
/*      */ 
/*      */           
/*      */           public int getMaxDamage() {
/*  219 */             return 0;
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean isDamageable() {
/*  224 */             return false;
/*      */           }
/*      */ 
/*      */           
/*      */           public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
/*  229 */             return ItemBijuCloak.getTexture(stack);
/*      */           }
/*      */         }).setTranslationKey("biju_cloakbody").setRegistryName("biju_cloakbody")).setCreativeTab(null));
/*      */     
/*  233 */     this.elements.items.add(() -> ((Item)(new ItemArmor(enuma, 0, EntityEquipmentSlot.LEGS)
/*      */         {
/*      */           @SideOnly(Side.CLIENT)
/*      */           public ModelBiped getArmorModel(EntityLivingBase living, ItemStack stack, EntityEquipmentSlot slot, ModelBiped defaultModel) {
/*  237 */             ItemBijuCloak.ModelBijuCloak armorModel = ItemBijuCloak.this.bijuModel[stack.getMetadata()];
/*  238 */             armorModel.isSneak = living.isSneaking();
/*  239 */             armorModel.isRiding = living.isRiding();
/*  240 */             armorModel.isChild = living.isChild();
/*  241 */             armorModel.bodyShine = (ItemBijuCloak.getTails(stack) == 9 && ItemBijuCloak.getCloakLevel(stack) == 2 && ItemBijuCloak.getCloakXp(stack) >= 800);
/*  242 */             armorModel.layerShine = true;
/*  243 */             return armorModel;
/*      */           }
/*      */ 
/*      */           
/*      */           public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
/*  248 */             super.onUpdate(itemstack, world, entity, par4, par5);
/*  249 */             if (!world.isRemote && entity instanceof EntityPlayer) {
/*  250 */               int cloakLevel = EntityBijuManager.cloakLevel((EntityPlayer)entity);
/*  251 */               if (cloakLevel <= 0) {
/*  252 */                 itemstack.shrink(1);
/*      */               }
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public int getMaxDamage() {
/*  259 */             return 0;
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean isDamageable() {
/*  264 */             return false;
/*      */           }
/*      */ 
/*      */           
/*      */           public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
/*  269 */             return ItemBijuCloak.getTexture(stack);
/*      */           }
/*      */         }).setTranslationKey("biju_cloaklegs").setRegistryName("biju_cloaklegs")).setCreativeTab(null));
/*      */   }
/*      */   
/*      */   private static String getTexture(ItemStack stack) {
/*  275 */     int i = getTails(stack);
/*  276 */     int j = getCloakLevel(stack);
/*  277 */     int k = getCloakXp(stack);
/*  278 */     return (i == 1 && j == 1) ? "narutomod:textures/bijucloak_sand.png" : ((j == 2) ? ((i == 9 && k >= 800) ? ((k < 4800) ? "narutomod:textures/bijucloak_kurama.png" : "narutomod:textures/bijucloak_kcm2.png") : "narutomod:textures/bijucloakl2.png") : "narutomod:textures/bijucloakl1.png");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void clearCloakItems(EntityPlayer player) {
/*  290 */     player.inventory.clearMatchingItems(helmet, -1, -1, null);
/*  291 */     player.inventory.clearMatchingItems(body, -1, -1, null);
/*  292 */     player.inventory.clearMatchingItems(legs, -1, -1, null);
/*  293 */     player.getEntityData().removeTag("lungeAttackData");
/*      */   }
/*      */   
/*      */   public static void applyEffects(EntityLivingBase entity, int level) {
/*  297 */     applyEffects(entity, level, true);
/*      */   }
/*      */   
/*      */   public static void applyEffects(EntityLivingBase entity, int level, boolean smoke) {
/*  301 */     if (smoke) {
/*  302 */       Particles.spawnParticle(entity.world, Particles.Types.SMOKE, entity.posX, entity.posY + 0.8D, entity.posZ, 40, 0.2D, 0.4D, 0.2D, 0.0D, 0.0D, 0.0D, new int[] { 545783835, 20, 
/*      */             
/*  304 */             (int)(4.0D / (entity.getRNG().nextDouble() * 0.8D + 0.2D)), 0, entity.getEntityId() });
/*      */     }
/*  306 */     if (!entity.world.isRemote && entity.ticksExisted % 10 == 4) {
/*      */       
/*  308 */       entity.addPotionEffect(new PotionEffect(PotionChakraEnhancedStrength.potion, 12, level * 32, false, false));
/*  309 */       entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 12, level * 24, false, false));
/*  310 */       entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 12, 5, false, false));
/*  311 */       entity.addPotionEffect(new PotionEffect(PotionReach.potion, 12, level - 1, false, false));
/*  312 */       if (entity.getHealth() < entity.getMaxHealth() && entity.getHealth() > 0.0F) {
/*  313 */         entity.heal(level);
/*      */       }
/*  315 */       if (level == 2) {
/*  316 */         entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 12, 2, false, false));
/*      */       }
/*      */     } 
/*  319 */     if (!entity.world.isRemote && entity instanceof EntityPlayer) {
/*  320 */       NBTTagCompound compound = entity.getEntityData().hasKey("lungeAttackData") ? entity.getEntityData().getCompoundTag("lungeAttackData") : new NBTTagCompound();
/*  321 */       int attackTime = compound.getInteger("attackTime");
/*  322 */       Entity target = compound.hasKey("targetId") ? entity.world.getEntityByID(compound.getInteger("targetId")) : null;
/*  323 */       if (entity.swingProgressInt == 1) {
/*  324 */         RayTraceResult res = ProcedureUtils.objectEntityLookingAt((Entity)entity, 15.0D, 3.0D);
/*  325 */         if (res != null && res.entityHit instanceof EntityLivingBase && res.entityHit.isEntityAlive()) {
/*  326 */           target = res.entityHit;
/*  327 */           compound.setInteger("targetId", target.getEntityId());
/*  328 */           attackTime = 0;
/*  329 */           entity.rotationYaw = ProcedureUtils.getYawFromVec(target.getPositionVector()
/*  330 */               .subtract(entity.getPositionVector()));
/*  331 */           double d0 = target.posX - entity.posX;
/*  332 */           double d1 = target.posY - entity.posY;
/*  333 */           double d2 = target.posZ - entity.posZ;
/*  334 */           double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
/*  335 */           ProcedureUtils.setVelocity((Entity)entity, d0 * 0.5D, d1 * 0.5D + d3 * 0.025D, d2 * 0.5D);
/*      */         } 
/*      */       } 
/*  338 */       if (attackTime < 12 && target != null && target.getDistanceSq((Entity)entity) < 25.0D) {
/*  339 */         ((EntityPlayer)entity).attackTargetEntityWithCurrentItem(target);
/*  340 */         compound.removeTag("targetId");
/*      */       } 
/*  342 */       compound.setInteger("attackTime", ++attackTime);
/*  343 */       entity.getEntityData().setTag("lungeAttackData", (NBTBase)compound);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static int getTails(ItemStack stack) {
/*  348 */     return stack.getTagCompound().getInteger("Tails");
/*      */   }
/*      */   
/*      */   private static void setCloakLevel(ItemStack itemstack, int level) {
/*  352 */     if (!itemstack.hasTagCompound()) {
/*  353 */       itemstack.setTagCompound(new NBTTagCompound());
/*      */     }
/*  355 */     itemstack.getTagCompound().setInteger("BijuCloakLevel", level);
/*      */   }
/*      */   
/*      */   private static int getCloakLevel(ItemStack itemstack) {
/*  359 */     return itemstack.hasTagCompound() ? itemstack.getTagCompound().getInteger("BijuCloakLevel") : 0;
/*      */   }
/*      */   
/*      */   private static void setCloakXp(ItemStack itemstack, int xp) {
/*  363 */     if (!itemstack.hasTagCompound()) {
/*  364 */       itemstack.setTagCompound(new NBTTagCompound());
/*      */     }
/*  366 */     itemstack.getTagCompound().setInteger("BijuCloakXp", xp);
/*      */   }
/*      */   
/*      */   private static int getCloakXp(ItemStack itemstack) {
/*  370 */     return itemstack.hasTagCompound() ? itemstack.getTagCompound().getInteger("BijuCloakXp") : 0;
/*      */   }
/*      */   
/*      */   public static void setWearingTicks(Entity entity, int ticks) {
/*  374 */     ProcedureSync.EntityNBTTag.setAndSync(entity, "WearingBijuCloakTicks", ticks);
/*      */   }
/*      */   
/*      */   public static int getWearingTicks(Entity entity) {
/*  378 */     return entity.getEntityData().getInteger("WearingBijuCloakTicks");
/*      */   }
/*      */   
/*      */   private void setWearingFullSet(ItemStack itemstack, boolean b) {
/*  382 */     if (!itemstack.hasTagCompound()) {
/*  383 */       itemstack.setTagCompound(new NBTTagCompound());
/*      */     }
/*  385 */     itemstack.getTagCompound().setBoolean("WearingFullSetBijuCloak", b);
/*      */   }
/*      */   
/*      */   private boolean isWearingFullSet(ItemStack itemstack) {
/*  389 */     return (itemstack.hasTagCompound() && itemstack.getTagCompound().getBoolean("WearingFullSetBijuCloak"));
/*      */   }
/*      */   
/*      */   private void setClone(ItemStack itemstack, EntityJinchurikiClone.EntityCustom clone) {
/*  393 */     if (!itemstack.hasTagCompound()) {
/*  394 */       itemstack.setTagCompound(new NBTTagCompound());
/*      */     }
/*  396 */     itemstack.getTagCompound().setInteger("CloneID", clone.getEntityId());
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private static EntityJinchurikiClone.EntityCustom getClone(World world, ItemStack itemstack) {
/*  401 */     if (hasClone(itemstack)) {
/*  402 */       Entity entity = world.getEntityByID(itemstack.getTagCompound().getInteger("CloneID"));
/*  403 */       return (entity instanceof EntityJinchurikiClone.EntityCustom) ? (EntityJinchurikiClone.EntityCustom)entity : null;
/*      */     } 
/*  405 */     return null;
/*      */   }
/*      */   
/*      */   private static int getCloneId(ItemStack stack) {
/*  409 */     return (stack.hasTagCompound() && stack.getTagCompound().hasKey("CloneID")) ? stack.getTagCompound().getInteger("CloneID") : -1;
/*      */   }
/*      */   
/*      */   private static boolean hasClone(ItemStack stack) {
/*  413 */     return (getCloneId(stack) > 0);
/*      */   }
/*      */   
/*      */   private void spawnClone(EntityPlayer original, ItemStack stack) {
/*  417 */     if (!original.world.isRemote && !hasClone(stack)) {
/*  418 */       EntityJinchurikiClone.EntityCustom entity = new EntityJinchurikiClone.EntityCustom((EntityLivingBase)original);
/*  419 */       entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(this.CLOAK_MODIFIER);
/*  420 */       entity.setHealth(original.getHealth());
/*  421 */       original.world.spawnEntity((Entity)entity);
/*  422 */       setClone(stack, entity);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void revertOriginal(EntityPlayer player, ItemStack stack) {
/*  427 */     EntityJinchurikiClone.EntityCustom clone = getClone(player.world, stack);
/*  428 */     if (clone != null) {
/*  429 */       clone.setDead();
/*  430 */       stack.getTagCompound().removeTag("CloneID");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @SideOnly(Side.CLIENT)
/*      */   public void registerModels(ModelRegistryEvent event) {
/*  437 */     ModelLoader.setCustomModelResourceLocation(helmet, 0, new ModelResourceLocation("narutomod:biju_cloakhelmet", "inventory"));
/*  438 */     ModelLoader.setCustomModelResourceLocation(body, 0, new ModelResourceLocation("narutomod:biju_cloakbody", "inventory"));
/*  439 */     ModelLoader.setCustomModelResourceLocation(body, 1, new ModelResourceLocation("narutomod:biju_cloakbody", "inventory"));
/*  440 */     ModelLoader.setCustomModelResourceLocation(body, 2, new ModelResourceLocation("narutomod:biju_cloakbody", "inventory"));
/*  441 */     ModelLoader.setCustomModelResourceLocation(body, 3, new ModelResourceLocation("narutomod:biju_cloakbody", "inventory"));
/*  442 */     ModelLoader.setCustomModelResourceLocation(body, 4, new ModelResourceLocation("narutomod:biju_cloakbody", "inventory"));
/*  443 */     ModelLoader.setCustomModelResourceLocation(body, 5, new ModelResourceLocation("narutomod:biju_cloakbody", "inventory"));
/*  444 */     ModelLoader.setCustomModelResourceLocation(body, 6, new ModelResourceLocation("narutomod:biju_cloakbody", "inventory"));
/*  445 */     ModelLoader.setCustomModelResourceLocation(body, 7, new ModelResourceLocation("narutomod:biju_cloakbody", "inventory"));
/*  446 */     ModelLoader.setCustomModelResourceLocation(body, 8, new ModelResourceLocation("narutomod:biju_cloakbody", "inventory"));
/*  447 */     ModelLoader.setCustomModelResourceLocation(body, 9, new ModelResourceLocation("narutomod:biju_cloakbody", "inventory"));
/*  448 */     ModelLoader.setCustomModelResourceLocation(legs, 0, new ModelResourceLocation("narutomod:biju_cloaklegs", "inventory"));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @SideOnly(Side.CLIENT)
/*      */   public class ModelBijuCloak
/*      */     extends ModelBiped
/*      */   {
/*  457 */     private final ModelRenderer[] earLeft = new ModelRenderer[6];
/*  458 */     private final ModelRenderer[] earRight = new ModelRenderer[6];
/*      */     
/*      */     private final ModelRenderer sandEar;
/*      */     private final ModelRenderer cube_r1;
/*      */     private final ModelRenderer allTails;
/*  463 */     private final ModelRenderer[][] tail = new ModelRenderer[9][8];
/*      */ 
/*      */     
/*      */     private final ModelRenderer bipedBodyWear;
/*      */     
/*      */     private final ModelRenderer tailWears;
/*      */     
/*  470 */     private final ModelRenderer[][] tailWear = new ModelRenderer[1][8];
/*      */     private final ModelRenderer bipedRightArmWear;
/*      */     private final ModelRenderer sandArm;
/*      */     private final ModelRenderer bipedLeftArmWear;
/*      */     private final ModelRenderer bipedRightLegWear;
/*      */     private final ModelRenderer bipedLeftLegWear;
/*  476 */     private final float[][] tailSwayX = new float[9][8];
/*  477 */     private final float[][] tailSwayZ = new float[9][8];
/*  478 */     private final float[] leftEarSwayX = new float[6];
/*  479 */     private final float[] leftEarSwayZ = new float[6];
/*  480 */     private final float[] rightEarSwayX = new float[6];
/*  481 */     private final float[] rightEarSwayZ = new float[6];
/*  482 */     private int[] tailShowMap = new int[] { 0, 1, 6, 25, 30, 31, 504, 127, 510, 511 };
/*      */     private boolean bodyShine;
/*      */     private boolean layerShine;
/*      */     private boolean narutoRunPose;
/*  486 */     private final Random rand = new Random();
/*      */     
/*      */     public ModelBijuCloak(int tails) {
/*  489 */       this.textureWidth = 128;
/*  490 */       this.textureHeight = 64;
/*  491 */       this.bipedHead = new ModelRenderer((ModelBase)this);
/*  492 */       this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  493 */       this.bipedHead.cubeList.add(new ModelBox(this.bipedHead, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.6F, false));
/*  494 */       this.earLeft[0] = new ModelRenderer((ModelBase)this);
/*  495 */       this.earLeft[0].setRotationPoint(3.5F, -8.25F, -0.5F);
/*  496 */       this.bipedHead.addChild(this.earLeft[0]);
/*  497 */       setRotationAngle(this.earLeft[0], -0.5236F, 0.0F, 0.7854F);
/*  498 */       (this.earLeft[0]).cubeList.add(new ModelBox(this.earLeft[0], 32, 0, -0.5F, -1.5F, -0.5F, 1, 2, 1, 0.8F, false));
/*  499 */       this.earLeft[1] = new ModelRenderer((ModelBase)this);
/*  500 */       this.earLeft[1].setRotationPoint(0.0F, -1.0F, 0.0F);
/*  501 */       this.earLeft[0].addChild(this.earLeft[1]);
/*  502 */       setRotationAngle(this.earLeft[1], 0.0F, 0.0F, -0.1745F);
/*  503 */       (this.earLeft[1]).cubeList.add(new ModelBox(this.earLeft[1], 32, 0, -0.5F, -1.5F, -0.5F, 1, 2, 1, 0.7F, false));
/*  504 */       this.earLeft[2] = new ModelRenderer((ModelBase)this);
/*  505 */       this.earLeft[2].setRotationPoint(0.0F, -1.0F, 0.0F);
/*  506 */       this.earLeft[1].addChild(this.earLeft[2]);
/*  507 */       setRotationAngle(this.earLeft[2], 0.0F, 0.0F, -0.1745F);
/*  508 */       (this.earLeft[2]).cubeList.add(new ModelBox(this.earLeft[2], 32, 0, -0.5F, -1.5F, -0.5F, 1, 2, 1, 0.6F, false));
/*  509 */       this.earLeft[3] = new ModelRenderer((ModelBase)this);
/*  510 */       this.earLeft[3].setRotationPoint(0.0F, -1.0F, 0.0F);
/*  511 */       this.earLeft[2].addChild(this.earLeft[3]);
/*  512 */       setRotationAngle(this.earLeft[3], 0.0F, 0.0F, -0.1745F);
/*  513 */       (this.earLeft[3]).cubeList.add(new ModelBox(this.earLeft[3], 32, 0, -0.5F, -1.5F, -0.5F, 1, 2, 1, 0.4F, false));
/*  514 */       this.earLeft[4] = new ModelRenderer((ModelBase)this);
/*  515 */       this.earLeft[4].setRotationPoint(0.0F, -1.0F, 0.0F);
/*  516 */       this.earLeft[3].addChild(this.earLeft[4]);
/*  517 */       setRotationAngle(this.earLeft[4], 0.0F, 0.0F, -0.1745F);
/*  518 */       (this.earLeft[4]).cubeList.add(new ModelBox(this.earLeft[4], 32, 0, -0.5F, -1.5F, -0.5F, 1, 2, 1, 0.2F, false));
/*  519 */       this.earLeft[5] = new ModelRenderer((ModelBase)this);
/*  520 */       this.earLeft[5].setRotationPoint(0.0F, -1.0F, 0.0F);
/*  521 */       this.earLeft[4].addChild(this.earLeft[5]);
/*  522 */       setRotationAngle(this.earLeft[5], 0.0F, 0.0F, -0.1745F);
/*  523 */       (this.earLeft[5]).cubeList.add(new ModelBox(this.earLeft[5], 32, 0, -0.5F, -1.5F, -0.5F, 1, 2, 1, -0.1F, false));
/*  524 */       this.earRight[0] = new ModelRenderer((ModelBase)this);
/*  525 */       this.earRight[0].setRotationPoint(-3.5F, -8.25F, -0.5F);
/*  526 */       this.bipedHead.addChild(this.earRight[0]);
/*  527 */       setRotationAngle(this.earRight[0], -0.5236F, 0.0F, -0.7854F);
/*  528 */       (this.earRight[0]).cubeList.add(new ModelBox(this.earRight[0], 32, 0, -0.5F, -1.5F, -0.5F, 1, 2, 1, 0.8F, false));
/*  529 */       this.earRight[1] = new ModelRenderer((ModelBase)this);
/*  530 */       this.earRight[1].setRotationPoint(0.0F, -1.0F, 0.0F);
/*  531 */       this.earRight[0].addChild(this.earRight[1]);
/*  532 */       setRotationAngle(this.earRight[1], 0.0F, 0.0F, 0.1745F);
/*  533 */       (this.earRight[1]).cubeList.add(new ModelBox(this.earRight[1], 32, 0, -0.5F, -1.5F, -0.5F, 1, 2, 1, 0.7F, false));
/*  534 */       this.earRight[2] = new ModelRenderer((ModelBase)this);
/*  535 */       this.earRight[2].setRotationPoint(0.0F, -1.0F, 0.0F);
/*  536 */       this.earRight[1].addChild(this.earRight[2]);
/*  537 */       setRotationAngle(this.earRight[2], 0.0F, 0.0F, 0.1745F);
/*  538 */       (this.earRight[2]).cubeList.add(new ModelBox(this.earRight[2], 32, 0, -0.5F, -1.5F, -0.5F, 1, 2, 1, 0.6F, false));
/*  539 */       this.earRight[3] = new ModelRenderer((ModelBase)this);
/*  540 */       this.earRight[3].setRotationPoint(0.0F, -1.0F, 0.0F);
/*  541 */       this.earRight[2].addChild(this.earRight[3]);
/*  542 */       setRotationAngle(this.earRight[3], 0.0F, 0.0F, 0.1745F);
/*  543 */       (this.earRight[3]).cubeList.add(new ModelBox(this.earRight[3], 32, 0, -0.5F, -1.5F, -0.5F, 1, 2, 1, 0.4F, false));
/*  544 */       this.earRight[4] = new ModelRenderer((ModelBase)this);
/*  545 */       this.earRight[4].setRotationPoint(0.0F, -1.0F, 0.0F);
/*  546 */       this.earRight[3].addChild(this.earRight[4]);
/*  547 */       setRotationAngle(this.earRight[4], 0.0F, 0.0F, 0.1745F);
/*  548 */       (this.earRight[4]).cubeList.add(new ModelBox(this.earRight[4], 32, 0, -0.5F, -1.5F, -0.5F, 1, 2, 1, 0.2F, false));
/*  549 */       this.earRight[5] = new ModelRenderer((ModelBase)this);
/*  550 */       this.earRight[5].setRotationPoint(0.0F, -1.0F, 0.0F);
/*  551 */       this.earRight[4].addChild(this.earRight[5]);
/*  552 */       setRotationAngle(this.earRight[5], 0.0F, 0.0F, 0.1745F);
/*  553 */       (this.earRight[5]).cubeList.add(new ModelBox(this.earRight[5], 32, 0, -0.5F, -1.5F, -0.5F, 1, 2, 1, -0.1F, false));
/*  554 */       this.bipedHeadwear = new ModelRenderer((ModelBase)this);
/*  555 */       this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  556 */       this.bipedHeadwear.cubeList.add(new ModelBox(this.bipedHeadwear, 64, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.6F, false));
/*  557 */       this.sandEar = new ModelRenderer((ModelBase)this);
/*  558 */       this.sandEar.setRotationPoint(-4.425F, -8.0F, 0.0F);
/*  559 */       this.bipedHeadwear.addChild(this.sandEar);
/*  560 */       setRotationAngle(this.sandEar, 0.0F, 0.0F, -0.2618F);
/*  561 */       this.cube_r1 = new ModelRenderer((ModelBase)this);
/*  562 */       this.cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  563 */       this.sandEar.addChild(this.cube_r1);
/*  564 */       setRotationAngle(this.cube_r1, -0.7782F, -0.0998F, -0.1434F);
/*  565 */       this.cube_r1.cubeList.add(new ModelBox(this.cube_r1, 118, 0, -1.0F, -2.8F, -2.0F, 2, 6, 3, 0.0F, false));
/*  566 */       this.bipedBody = new ModelRenderer((ModelBase)this);
/*  567 */       this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  568 */       this.bipedBody.cubeList.add(new ModelBox(this.bipedBody, 16, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.6F, false));
/*  569 */       this.allTails = new ModelRenderer((ModelBase)this);
/*  570 */       this.bipedBody.addChild(this.allTails);
/*  571 */       this.tail[0][0] = new ModelRenderer((ModelBase)this);
/*  572 */       this.tail[0][0].setRotationPoint(0.0F, 10.5F, 2.0F);
/*  573 */       this.allTails.addChild(this.tail[0][0]);
/*  574 */       setRotationAngle(this.tail[0][0], -1.0472F, 0.0F, 0.0F);
/*  575 */       (this.tail[0][0]).cubeList.add(new ModelBox(this.tail[0][0], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  576 */       this.tail[0][1] = new ModelRenderer((ModelBase)this);
/*  577 */       this.tail[0][1].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  578 */       this.tail[0][0].addChild(this.tail[0][1]);
/*  579 */       setRotationAngle(this.tail[0][1], 0.2618F, 0.0F, 0.0F);
/*  580 */       (this.tail[0][1]).cubeList.add(new ModelBox(this.tail[0][1], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  581 */       this.tail[0][2] = new ModelRenderer((ModelBase)this);
/*  582 */       this.tail[0][2].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  583 */       this.tail[0][1].addChild(this.tail[0][2]);
/*  584 */       setRotationAngle(this.tail[0][2], 0.2618F, 0.0F, 0.0F);
/*  585 */       (this.tail[0][2]).cubeList.add(new ModelBox(this.tail[0][2], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.6F, false));
/*  586 */       this.tail[0][3] = new ModelRenderer((ModelBase)this);
/*  587 */       this.tail[0][3].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  588 */       this.tail[0][2].addChild(this.tail[0][3]);
/*  589 */       setRotationAngle(this.tail[0][3], 0.2618F, 0.0F, 0.0F);
/*  590 */       (this.tail[0][3]).cubeList.add(new ModelBox(this.tail[0][3], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  591 */       this.tail[0][4] = new ModelRenderer((ModelBase)this);
/*  592 */       this.tail[0][4].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  593 */       this.tail[0][3].addChild(this.tail[0][4]);
/*  594 */       setRotationAngle(this.tail[0][4], 0.2618F, 0.0F, 0.0F);
/*  595 */       (this.tail[0][4]).cubeList.add(new ModelBox(this.tail[0][4], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  596 */       this.tail[0][5] = new ModelRenderer((ModelBase)this);
/*  597 */       this.tail[0][5].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  598 */       this.tail[0][4].addChild(this.tail[0][5]);
/*  599 */       setRotationAngle(this.tail[0][5], 0.2618F, 0.0F, 0.0F);
/*  600 */       (this.tail[0][5]).cubeList.add(new ModelBox(this.tail[0][5], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.3F, false));
/*  601 */       this.tail[0][6] = new ModelRenderer((ModelBase)this);
/*  602 */       this.tail[0][6].setRotationPoint(0.0F, -4.0F, 0.0F);
/*  603 */       this.tail[0][5].addChild(this.tail[0][6]);
/*  604 */       setRotationAngle(this.tail[0][6], 0.2618F, 0.0F, 0.0F);
/*  605 */       (this.tail[0][6]).cubeList.add(new ModelBox(this.tail[0][6], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.6F, false));
/*  606 */       this.tail[0][7] = new ModelRenderer((ModelBase)this);
/*  607 */       this.tail[0][7].setRotationPoint(0.0F, -3.75F, 0.0F);
/*  608 */       this.tail[0][6].addChild(this.tail[0][7]);
/*  609 */       setRotationAngle(this.tail[0][7], 0.2618F, 0.0F, 0.0F);
/*  610 */       (this.tail[0][7]).cubeList.add(new ModelBox(this.tail[0][7], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -1.0F, false));
/*  611 */       this.tail[1][0] = new ModelRenderer((ModelBase)this);
/*  612 */       this.tail[1][0].setRotationPoint(0.0F, 10.5F, 2.0F);
/*  613 */       this.allTails.addChild(this.tail[1][0]);
/*  614 */       setRotationAngle(this.tail[1][0], -1.0472F, -0.5236F, -0.2618F);
/*  615 */       (this.tail[1][0]).cubeList.add(new ModelBox(this.tail[1][0], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  616 */       this.tail[1][1] = new ModelRenderer((ModelBase)this);
/*  617 */       this.tail[1][1].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  618 */       this.tail[1][0].addChild(this.tail[1][1]);
/*  619 */       setRotationAngle(this.tail[1][1], 0.2618F, 0.0F, 0.0F);
/*  620 */       (this.tail[1][1]).cubeList.add(new ModelBox(this.tail[1][1], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  621 */       this.tail[1][2] = new ModelRenderer((ModelBase)this);
/*  622 */       this.tail[1][2].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  623 */       this.tail[1][1].addChild(this.tail[1][2]);
/*  624 */       setRotationAngle(this.tail[1][2], 0.2618F, 0.0F, 0.0F);
/*  625 */       (this.tail[1][2]).cubeList.add(new ModelBox(this.tail[1][2], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.6F, false));
/*  626 */       this.tail[1][3] = new ModelRenderer((ModelBase)this);
/*  627 */       this.tail[1][3].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  628 */       this.tail[1][2].addChild(this.tail[1][3]);
/*  629 */       setRotationAngle(this.tail[1][3], 0.2618F, 0.0F, 0.0F);
/*  630 */       (this.tail[1][3]).cubeList.add(new ModelBox(this.tail[1][3], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  631 */       this.tail[1][4] = new ModelRenderer((ModelBase)this);
/*  632 */       this.tail[1][4].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  633 */       this.tail[1][3].addChild(this.tail[1][4]);
/*  634 */       setRotationAngle(this.tail[1][4], 0.2618F, 0.0F, 0.0F);
/*  635 */       (this.tail[1][4]).cubeList.add(new ModelBox(this.tail[1][4], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  636 */       this.tail[1][5] = new ModelRenderer((ModelBase)this);
/*  637 */       this.tail[1][5].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  638 */       this.tail[1][4].addChild(this.tail[1][5]);
/*  639 */       setRotationAngle(this.tail[1][5], 0.2618F, 0.0F, 0.0F);
/*  640 */       (this.tail[1][5]).cubeList.add(new ModelBox(this.tail[1][5], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.3F, false));
/*  641 */       this.tail[1][6] = new ModelRenderer((ModelBase)this);
/*  642 */       this.tail[1][6].setRotationPoint(0.0F, -4.0F, 0.0F);
/*  643 */       this.tail[1][5].addChild(this.tail[1][6]);
/*  644 */       setRotationAngle(this.tail[1][6], 0.2618F, 0.0F, 0.0F);
/*  645 */       (this.tail[1][6]).cubeList.add(new ModelBox(this.tail[1][6], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.6F, false));
/*  646 */       this.tail[1][7] = new ModelRenderer((ModelBase)this);
/*  647 */       this.tail[1][7].setRotationPoint(0.0F, -3.75F, 0.0F);
/*  648 */       this.tail[1][6].addChild(this.tail[1][7]);
/*  649 */       setRotationAngle(this.tail[1][7], 0.2618F, 0.0F, 0.0F);
/*  650 */       (this.tail[1][7]).cubeList.add(new ModelBox(this.tail[1][7], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -1.0F, false));
/*  651 */       this.tail[2][0] = new ModelRenderer((ModelBase)this);
/*  652 */       this.tail[2][0].setRotationPoint(0.0F, 10.5F, 2.0F);
/*  653 */       this.allTails.addChild(this.tail[2][0]);
/*  654 */       setRotationAngle(this.tail[2][0], -1.0472F, 0.5236F, 0.2618F);
/*  655 */       (this.tail[2][0]).cubeList.add(new ModelBox(this.tail[2][0], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  656 */       this.tail[2][1] = new ModelRenderer((ModelBase)this);
/*  657 */       this.tail[2][1].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  658 */       this.tail[2][0].addChild(this.tail[2][1]);
/*  659 */       setRotationAngle(this.tail[2][1], 0.2618F, 0.0F, 0.0F);
/*  660 */       (this.tail[2][1]).cubeList.add(new ModelBox(this.tail[2][1], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  661 */       this.tail[2][2] = new ModelRenderer((ModelBase)this);
/*  662 */       this.tail[2][2].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  663 */       this.tail[2][1].addChild(this.tail[2][2]);
/*  664 */       setRotationAngle(this.tail[2][2], 0.2618F, 0.0F, 0.0F);
/*  665 */       (this.tail[2][2]).cubeList.add(new ModelBox(this.tail[2][2], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.6F, false));
/*  666 */       this.tail[2][3] = new ModelRenderer((ModelBase)this);
/*  667 */       this.tail[2][3].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  668 */       this.tail[2][2].addChild(this.tail[2][3]);
/*  669 */       setRotationAngle(this.tail[2][3], 0.2618F, 0.0F, 0.0F);
/*  670 */       (this.tail[2][3]).cubeList.add(new ModelBox(this.tail[2][3], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  671 */       this.tail[2][4] = new ModelRenderer((ModelBase)this);
/*  672 */       this.tail[2][4].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  673 */       this.tail[2][3].addChild(this.tail[2][4]);
/*  674 */       setRotationAngle(this.tail[2][4], 0.2618F, 0.0F, 0.0F);
/*  675 */       (this.tail[2][4]).cubeList.add(new ModelBox(this.tail[2][4], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  676 */       this.tail[2][5] = new ModelRenderer((ModelBase)this);
/*  677 */       this.tail[2][5].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  678 */       this.tail[2][4].addChild(this.tail[2][5]);
/*  679 */       setRotationAngle(this.tail[2][5], 0.2618F, 0.0F, 0.0F);
/*  680 */       (this.tail[2][5]).cubeList.add(new ModelBox(this.tail[2][5], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.3F, false));
/*  681 */       this.tail[2][6] = new ModelRenderer((ModelBase)this);
/*  682 */       this.tail[2][6].setRotationPoint(0.0F, -4.0F, 0.0F);
/*  683 */       this.tail[2][5].addChild(this.tail[2][6]);
/*  684 */       setRotationAngle(this.tail[2][6], 0.2618F, 0.0F, 0.0F);
/*  685 */       (this.tail[2][6]).cubeList.add(new ModelBox(this.tail[2][6], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.6F, false));
/*  686 */       this.tail[2][7] = new ModelRenderer((ModelBase)this);
/*  687 */       this.tail[2][7].setRotationPoint(0.0F, -3.75F, 0.0F);
/*  688 */       this.tail[2][6].addChild(this.tail[2][7]);
/*  689 */       setRotationAngle(this.tail[2][7], 0.2618F, 0.0F, 0.0F);
/*  690 */       (this.tail[2][7]).cubeList.add(new ModelBox(this.tail[2][7], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -1.0F, false));
/*  691 */       this.tail[3][0] = new ModelRenderer((ModelBase)this);
/*  692 */       this.tail[3][0].setRotationPoint(0.0F, 10.5F, 2.0F);
/*  693 */       this.allTails.addChild(this.tail[3][0]);
/*  694 */       setRotationAngle(this.tail[3][0], -1.0472F, -1.0472F, -0.5236F);
/*  695 */       (this.tail[3][0]).cubeList.add(new ModelBox(this.tail[3][0], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  696 */       this.tail[3][1] = new ModelRenderer((ModelBase)this);
/*  697 */       this.tail[3][1].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  698 */       this.tail[3][0].addChild(this.tail[3][1]);
/*  699 */       setRotationAngle(this.tail[3][1], 0.2618F, 0.0F, 0.0F);
/*  700 */       (this.tail[3][1]).cubeList.add(new ModelBox(this.tail[3][1], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  701 */       this.tail[3][2] = new ModelRenderer((ModelBase)this);
/*  702 */       this.tail[3][2].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  703 */       this.tail[3][1].addChild(this.tail[3][2]);
/*  704 */       setRotationAngle(this.tail[3][2], 0.2618F, 0.0F, 0.0F);
/*  705 */       (this.tail[3][2]).cubeList.add(new ModelBox(this.tail[3][2], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.6F, false));
/*  706 */       this.tail[3][3] = new ModelRenderer((ModelBase)this);
/*  707 */       this.tail[3][3].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  708 */       this.tail[3][2].addChild(this.tail[3][3]);
/*  709 */       setRotationAngle(this.tail[3][3], 0.2618F, 0.0F, 0.0F);
/*  710 */       (this.tail[3][3]).cubeList.add(new ModelBox(this.tail[3][3], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  711 */       this.tail[3][4] = new ModelRenderer((ModelBase)this);
/*  712 */       this.tail[3][4].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  713 */       this.tail[3][3].addChild(this.tail[3][4]);
/*  714 */       setRotationAngle(this.tail[3][4], 0.2618F, 0.0F, 0.0F);
/*  715 */       (this.tail[3][4]).cubeList.add(new ModelBox(this.tail[3][4], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  716 */       this.tail[3][5] = new ModelRenderer((ModelBase)this);
/*  717 */       this.tail[3][5].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  718 */       this.tail[3][4].addChild(this.tail[3][5]);
/*  719 */       setRotationAngle(this.tail[3][5], 0.2618F, 0.0F, 0.0F);
/*  720 */       (this.tail[3][5]).cubeList.add(new ModelBox(this.tail[3][5], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.3F, false));
/*  721 */       this.tail[3][6] = new ModelRenderer((ModelBase)this);
/*  722 */       this.tail[3][6].setRotationPoint(0.0F, -4.0F, 0.0F);
/*  723 */       this.tail[3][5].addChild(this.tail[3][6]);
/*  724 */       setRotationAngle(this.tail[3][6], 0.2618F, 0.0F, 0.0F);
/*  725 */       (this.tail[3][6]).cubeList.add(new ModelBox(this.tail[3][6], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.6F, false));
/*  726 */       this.tail[3][7] = new ModelRenderer((ModelBase)this);
/*  727 */       this.tail[3][7].setRotationPoint(0.0F, -3.75F, 0.0F);
/*  728 */       this.tail[3][6].addChild(this.tail[3][7]);
/*  729 */       setRotationAngle(this.tail[3][7], 0.2618F, 0.0F, 0.0F);
/*  730 */       (this.tail[3][7]).cubeList.add(new ModelBox(this.tail[3][7], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -1.0F, false));
/*  731 */       this.tail[4][0] = new ModelRenderer((ModelBase)this);
/*  732 */       this.tail[4][0].setRotationPoint(0.0F, 10.5F, 2.0F);
/*  733 */       this.allTails.addChild(this.tail[4][0]);
/*  734 */       setRotationAngle(this.tail[4][0], -1.0472F, 1.0472F, 0.5236F);
/*  735 */       (this.tail[4][0]).cubeList.add(new ModelBox(this.tail[4][0], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  736 */       this.tail[4][1] = new ModelRenderer((ModelBase)this);
/*  737 */       this.tail[4][1].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  738 */       this.tail[4][0].addChild(this.tail[4][1]);
/*  739 */       setRotationAngle(this.tail[4][1], 0.2618F, 0.0F, 0.0F);
/*  740 */       (this.tail[4][1]).cubeList.add(new ModelBox(this.tail[4][1], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  741 */       this.tail[4][2] = new ModelRenderer((ModelBase)this);
/*  742 */       this.tail[4][2].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  743 */       this.tail[4][1].addChild(this.tail[4][2]);
/*  744 */       setRotationAngle(this.tail[4][2], 0.2618F, 0.0F, 0.0F);
/*  745 */       (this.tail[4][2]).cubeList.add(new ModelBox(this.tail[4][2], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.6F, false));
/*  746 */       this.tail[4][3] = new ModelRenderer((ModelBase)this);
/*  747 */       this.tail[4][3].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  748 */       this.tail[4][2].addChild(this.tail[4][3]);
/*  749 */       setRotationAngle(this.tail[4][3], 0.2618F, 0.0F, 0.0F);
/*  750 */       (this.tail[4][3]).cubeList.add(new ModelBox(this.tail[4][3], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  751 */       this.tail[4][4] = new ModelRenderer((ModelBase)this);
/*  752 */       this.tail[4][4].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  753 */       this.tail[4][3].addChild(this.tail[4][4]);
/*  754 */       setRotationAngle(this.tail[4][4], 0.2618F, 0.0F, 0.0F);
/*  755 */       (this.tail[4][4]).cubeList.add(new ModelBox(this.tail[4][4], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  756 */       this.tail[4][5] = new ModelRenderer((ModelBase)this);
/*  757 */       this.tail[4][5].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  758 */       this.tail[4][4].addChild(this.tail[4][5]);
/*  759 */       setRotationAngle(this.tail[4][5], 0.2618F, 0.0F, 0.0F);
/*  760 */       (this.tail[4][5]).cubeList.add(new ModelBox(this.tail[4][5], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.3F, false));
/*  761 */       this.tail[4][6] = new ModelRenderer((ModelBase)this);
/*  762 */       this.tail[4][6].setRotationPoint(0.0F, -4.0F, 0.0F);
/*  763 */       this.tail[4][5].addChild(this.tail[4][6]);
/*  764 */       setRotationAngle(this.tail[4][6], 0.2618F, 0.0F, 0.0F);
/*  765 */       (this.tail[4][6]).cubeList.add(new ModelBox(this.tail[4][6], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.6F, false));
/*  766 */       this.tail[4][7] = new ModelRenderer((ModelBase)this);
/*  767 */       this.tail[4][7].setRotationPoint(0.0F, -3.75F, 0.0F);
/*  768 */       this.tail[4][6].addChild(this.tail[4][7]);
/*  769 */       setRotationAngle(this.tail[4][7], 0.2618F, 0.0F, 0.0F);
/*  770 */       (this.tail[4][7]).cubeList.add(new ModelBox(this.tail[4][7], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -1.0F, false));
/*  771 */       this.tail[5][0] = new ModelRenderer((ModelBase)this);
/*  772 */       this.tail[5][0].setRotationPoint(0.0F, 10.5F, 2.0F);
/*  773 */       this.allTails.addChild(this.tail[5][0]);
/*  774 */       setRotationAngle(this.tail[5][0], -1.5718F, -0.2618F, 0.0F);
/*  775 */       (this.tail[5][0]).cubeList.add(new ModelBox(this.tail[5][0], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  776 */       this.tail[5][1] = new ModelRenderer((ModelBase)this);
/*  777 */       this.tail[5][1].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  778 */       this.tail[5][0].addChild(this.tail[5][1]);
/*  779 */       setRotationAngle(this.tail[5][1], 0.2618F, 0.0F, 0.0F);
/*  780 */       (this.tail[5][1]).cubeList.add(new ModelBox(this.tail[5][1], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  781 */       this.tail[5][2] = new ModelRenderer((ModelBase)this);
/*  782 */       this.tail[5][2].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  783 */       this.tail[5][1].addChild(this.tail[5][2]);
/*  784 */       setRotationAngle(this.tail[5][2], 0.2618F, 0.0F, 0.0F);
/*  785 */       (this.tail[5][2]).cubeList.add(new ModelBox(this.tail[5][2], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.6F, false));
/*  786 */       this.tail[5][3] = new ModelRenderer((ModelBase)this);
/*  787 */       this.tail[5][3].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  788 */       this.tail[5][2].addChild(this.tail[5][3]);
/*  789 */       setRotationAngle(this.tail[5][3], 0.2618F, 0.0F, 0.0F);
/*  790 */       (this.tail[5][3]).cubeList.add(new ModelBox(this.tail[5][3], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  791 */       this.tail[5][4] = new ModelRenderer((ModelBase)this);
/*  792 */       this.tail[5][4].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  793 */       this.tail[5][3].addChild(this.tail[5][4]);
/*  794 */       setRotationAngle(this.tail[5][4], 0.2618F, 0.0F, 0.0F);
/*  795 */       (this.tail[5][4]).cubeList.add(new ModelBox(this.tail[5][4], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  796 */       this.tail[5][5] = new ModelRenderer((ModelBase)this);
/*  797 */       this.tail[5][5].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  798 */       this.tail[5][4].addChild(this.tail[5][5]);
/*  799 */       setRotationAngle(this.tail[5][5], 0.2618F, 0.0F, 0.0F);
/*  800 */       (this.tail[5][5]).cubeList.add(new ModelBox(this.tail[5][5], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.3F, false));
/*  801 */       this.tail[5][6] = new ModelRenderer((ModelBase)this);
/*  802 */       this.tail[5][6].setRotationPoint(0.0F, -4.0F, 0.0F);
/*  803 */       this.tail[5][5].addChild(this.tail[5][6]);
/*  804 */       setRotationAngle(this.tail[5][6], 0.2618F, 0.0F, 0.0F);
/*  805 */       (this.tail[5][6]).cubeList.add(new ModelBox(this.tail[5][6], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.6F, false));
/*  806 */       this.tail[5][7] = new ModelRenderer((ModelBase)this);
/*  807 */       this.tail[5][7].setRotationPoint(0.0F, -3.75F, 0.0F);
/*  808 */       this.tail[5][6].addChild(this.tail[5][7]);
/*  809 */       setRotationAngle(this.tail[5][7], 0.2618F, 0.0F, 0.0F);
/*  810 */       (this.tail[5][7]).cubeList.add(new ModelBox(this.tail[5][7], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -1.0F, false));
/*  811 */       this.tail[6][0] = new ModelRenderer((ModelBase)this);
/*  812 */       this.tail[6][0].setRotationPoint(0.0F, 10.5F, 2.0F);
/*  813 */       this.allTails.addChild(this.tail[6][0]);
/*  814 */       setRotationAngle(this.tail[6][0], -1.5718F, 0.2618F, 0.0F);
/*  815 */       (this.tail[6][0]).cubeList.add(new ModelBox(this.tail[6][0], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  816 */       this.tail[6][1] = new ModelRenderer((ModelBase)this);
/*  817 */       this.tail[6][1].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  818 */       this.tail[6][0].addChild(this.tail[6][1]);
/*  819 */       setRotationAngle(this.tail[6][1], 0.2618F, 0.0F, 0.0F);
/*  820 */       (this.tail[6][1]).cubeList.add(new ModelBox(this.tail[6][1], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  821 */       this.tail[6][2] = new ModelRenderer((ModelBase)this);
/*  822 */       this.tail[6][2].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  823 */       this.tail[6][1].addChild(this.tail[6][2]);
/*  824 */       setRotationAngle(this.tail[6][2], 0.2618F, 0.0F, 0.0F);
/*  825 */       (this.tail[6][2]).cubeList.add(new ModelBox(this.tail[6][2], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.6F, false));
/*  826 */       this.tail[6][3] = new ModelRenderer((ModelBase)this);
/*  827 */       this.tail[6][3].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  828 */       this.tail[6][2].addChild(this.tail[6][3]);
/*  829 */       setRotationAngle(this.tail[6][3], 0.2618F, 0.0F, 0.0F);
/*  830 */       (this.tail[6][3]).cubeList.add(new ModelBox(this.tail[6][3], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  831 */       this.tail[6][4] = new ModelRenderer((ModelBase)this);
/*  832 */       this.tail[6][4].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  833 */       this.tail[6][3].addChild(this.tail[6][4]);
/*  834 */       setRotationAngle(this.tail[6][4], 0.2618F, 0.0F, 0.0F);
/*  835 */       (this.tail[6][4]).cubeList.add(new ModelBox(this.tail[6][4], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  836 */       this.tail[6][5] = new ModelRenderer((ModelBase)this);
/*  837 */       this.tail[6][5].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  838 */       this.tail[6][4].addChild(this.tail[6][5]);
/*  839 */       setRotationAngle(this.tail[6][5], 0.2618F, 0.0F, 0.0F);
/*  840 */       (this.tail[6][5]).cubeList.add(new ModelBox(this.tail[6][5], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.3F, false));
/*  841 */       this.tail[6][6] = new ModelRenderer((ModelBase)this);
/*  842 */       this.tail[6][6].setRotationPoint(0.0F, -4.0F, 0.0F);
/*  843 */       this.tail[6][5].addChild(this.tail[6][6]);
/*  844 */       setRotationAngle(this.tail[6][6], 0.2618F, 0.0F, 0.0F);
/*  845 */       (this.tail[6][6]).cubeList.add(new ModelBox(this.tail[6][6], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.6F, false));
/*  846 */       this.tail[6][7] = new ModelRenderer((ModelBase)this);
/*  847 */       this.tail[6][7].setRotationPoint(0.0F, -3.75F, 0.0F);
/*  848 */       this.tail[6][6].addChild(this.tail[6][7]);
/*  849 */       setRotationAngle(this.tail[6][7], 0.2618F, 0.0F, 0.0F);
/*  850 */       (this.tail[6][7]).cubeList.add(new ModelBox(this.tail[6][7], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -1.0F, false));
/*  851 */       this.tail[7][0] = new ModelRenderer((ModelBase)this);
/*  852 */       this.tail[7][0].setRotationPoint(0.0F, 10.5F, 2.0F);
/*  853 */       this.allTails.addChild(this.tail[7][0]);
/*  854 */       setRotationAngle(this.tail[7][0], -1.5718F, 0.7854F, 0.0F);
/*  855 */       (this.tail[7][0]).cubeList.add(new ModelBox(this.tail[7][0], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  856 */       this.tail[7][1] = new ModelRenderer((ModelBase)this);
/*  857 */       this.tail[7][1].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  858 */       this.tail[7][0].addChild(this.tail[7][1]);
/*  859 */       setRotationAngle(this.tail[7][1], 0.2618F, 0.0F, 0.0F);
/*  860 */       (this.tail[7][1]).cubeList.add(new ModelBox(this.tail[7][1], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  861 */       this.tail[7][2] = new ModelRenderer((ModelBase)this);
/*  862 */       this.tail[7][2].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  863 */       this.tail[7][1].addChild(this.tail[7][2]);
/*  864 */       setRotationAngle(this.tail[7][2], 0.2618F, 0.0F, 0.0F);
/*  865 */       (this.tail[7][2]).cubeList.add(new ModelBox(this.tail[7][2], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.6F, false));
/*  866 */       this.tail[7][3] = new ModelRenderer((ModelBase)this);
/*  867 */       this.tail[7][3].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  868 */       this.tail[7][2].addChild(this.tail[7][3]);
/*  869 */       setRotationAngle(this.tail[7][3], 0.2618F, 0.0F, 0.0F);
/*  870 */       (this.tail[7][3]).cubeList.add(new ModelBox(this.tail[7][3], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  871 */       this.tail[7][4] = new ModelRenderer((ModelBase)this);
/*  872 */       this.tail[7][4].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  873 */       this.tail[7][3].addChild(this.tail[7][4]);
/*  874 */       setRotationAngle(this.tail[7][4], 0.2618F, 0.0F, 0.0F);
/*  875 */       (this.tail[7][4]).cubeList.add(new ModelBox(this.tail[7][4], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  876 */       this.tail[7][5] = new ModelRenderer((ModelBase)this);
/*  877 */       this.tail[7][5].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  878 */       this.tail[7][4].addChild(this.tail[7][5]);
/*  879 */       setRotationAngle(this.tail[7][5], 0.2618F, 0.0F, 0.0F);
/*  880 */       (this.tail[7][5]).cubeList.add(new ModelBox(this.tail[7][5], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.3F, false));
/*  881 */       this.tail[7][6] = new ModelRenderer((ModelBase)this);
/*  882 */       this.tail[7][6].setRotationPoint(0.0F, -4.0F, 0.0F);
/*  883 */       this.tail[7][5].addChild(this.tail[7][6]);
/*  884 */       setRotationAngle(this.tail[7][6], 0.2618F, 0.0F, 0.0F);
/*  885 */       (this.tail[7][6]).cubeList.add(new ModelBox(this.tail[7][6], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.6F, false));
/*  886 */       this.tail[7][7] = new ModelRenderer((ModelBase)this);
/*  887 */       this.tail[7][7].setRotationPoint(0.0F, -3.75F, 0.0F);
/*  888 */       this.tail[7][6].addChild(this.tail[7][7]);
/*  889 */       setRotationAngle(this.tail[7][7], 0.2618F, 0.0F, 0.0F);
/*  890 */       (this.tail[7][7]).cubeList.add(new ModelBox(this.tail[7][7], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -1.0F, false));
/*  891 */       this.tail[8][0] = new ModelRenderer((ModelBase)this);
/*  892 */       this.tail[8][0].setRotationPoint(0.0F, 10.5F, 2.0F);
/*  893 */       this.allTails.addChild(this.tail[8][0]);
/*  894 */       setRotationAngle(this.tail[8][0], -1.5718F, -0.7854F, 0.0F);
/*  895 */       (this.tail[8][0]).cubeList.add(new ModelBox(this.tail[8][0], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  896 */       this.tail[8][1] = new ModelRenderer((ModelBase)this);
/*  897 */       this.tail[8][1].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  898 */       this.tail[8][0].addChild(this.tail[8][1]);
/*  899 */       setRotationAngle(this.tail[8][1], 0.2618F, 0.0F, 0.0F);
/*  900 */       (this.tail[8][1]).cubeList.add(new ModelBox(this.tail[8][1], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  901 */       this.tail[8][2] = new ModelRenderer((ModelBase)this);
/*  902 */       this.tail[8][2].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  903 */       this.tail[8][1].addChild(this.tail[8][2]);
/*  904 */       setRotationAngle(this.tail[8][2], 0.2618F, 0.0F, 0.0F);
/*  905 */       (this.tail[8][2]).cubeList.add(new ModelBox(this.tail[8][2], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.6F, false));
/*  906 */       this.tail[8][3] = new ModelRenderer((ModelBase)this);
/*  907 */       this.tail[8][3].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  908 */       this.tail[8][2].addChild(this.tail[8][3]);
/*  909 */       setRotationAngle(this.tail[8][3], 0.2618F, 0.0F, 0.0F);
/*  910 */       (this.tail[8][3]).cubeList.add(new ModelBox(this.tail[8][3], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.3F, false));
/*  911 */       this.tail[8][4] = new ModelRenderer((ModelBase)this);
/*  912 */       this.tail[8][4].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  913 */       this.tail[8][3].addChild(this.tail[8][4]);
/*  914 */       setRotationAngle(this.tail[8][4], 0.2618F, 0.0F, 0.0F);
/*  915 */       (this.tail[8][4]).cubeList.add(new ModelBox(this.tail[8][4], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.0F, false));
/*  916 */       this.tail[8][5] = new ModelRenderer((ModelBase)this);
/*  917 */       this.tail[8][5].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  918 */       this.tail[8][4].addChild(this.tail[8][5]);
/*  919 */       setRotationAngle(this.tail[8][5], 0.2618F, 0.0F, 0.0F);
/*  920 */       (this.tail[8][5]).cubeList.add(new ModelBox(this.tail[8][5], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.3F, false));
/*  921 */       this.tail[8][6] = new ModelRenderer((ModelBase)this);
/*  922 */       this.tail[8][6].setRotationPoint(0.0F, -4.0F, 0.0F);
/*  923 */       this.tail[8][5].addChild(this.tail[8][6]);
/*  924 */       setRotationAngle(this.tail[8][6], 0.2618F, 0.0F, 0.0F);
/*  925 */       (this.tail[8][6]).cubeList.add(new ModelBox(this.tail[8][6], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.6F, false));
/*  926 */       this.tail[8][7] = new ModelRenderer((ModelBase)this);
/*  927 */       this.tail[8][7].setRotationPoint(0.0F, -3.75F, 0.0F);
/*  928 */       this.tail[8][6].addChild(this.tail[8][7]);
/*  929 */       setRotationAngle(this.tail[8][7], 0.2618F, 0.0F, 0.0F);
/*  930 */       (this.tail[8][7]).cubeList.add(new ModelBox(this.tail[8][7], 16, 32, -2.0F, -5.5F, -2.0F, 4, 6, 4, -1.0F, false));
/*  931 */       this.bipedBodyWear = new ModelRenderer((ModelBase)this);
/*  932 */       this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  933 */       this.bipedBodyWear.cubeList.add(new ModelBox(this.bipedBodyWear, 80, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.65F, false));
/*  934 */       this.bipedBodyWear.cubeList.add(new ModelBox(this.bipedBodyWear, 80, 32, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.7F, false));
/*  935 */       this.tailWears = new ModelRenderer((ModelBase)this);
/*  936 */       this.tailWears.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  937 */       this.bipedBodyWear.addChild(this.tailWears);
/*  938 */       this.tailWear[0][0] = new ModelRenderer((ModelBase)this);
/*  939 */       this.tailWear[0][0].setRotationPoint(0.0F, 10.5F, 2.0F);
/*  940 */       this.tailWears.addChild(this.tailWear[0][0]);
/*  941 */       setRotationAngle(this.tailWear[0][0], -1.0472F, 0.0F, 0.0F);
/*  942 */       (this.tailWear[0][0]).cubeList.add(new ModelBox(this.tailWear[0][0], 102, 4, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.01F, false));
/*  943 */       this.tailWear[0][1] = new ModelRenderer((ModelBase)this);
/*  944 */       this.tailWear[0][1].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  945 */       this.tailWear[0][0].addChild(this.tailWear[0][1]);
/*  946 */       setRotationAngle(this.tailWear[0][1], 0.2618F, 0.0F, 0.0F);
/*  947 */       (this.tailWear[0][1]).cubeList.add(new ModelBox(this.tailWear[0][1], 102, 4, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.31F, false));
/*  948 */       this.tailWear[0][2] = new ModelRenderer((ModelBase)this);
/*  949 */       this.tailWear[0][2].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  950 */       this.tailWear[0][1].addChild(this.tailWear[0][2]);
/*  951 */       setRotationAngle(this.tailWear[0][2], 0.2618F, 0.0F, 0.0F);
/*  952 */       (this.tailWear[0][2]).cubeList.add(new ModelBox(this.tailWear[0][2], 102, 4, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.61F, false));
/*  953 */       this.tailWear[0][3] = new ModelRenderer((ModelBase)this);
/*  954 */       this.tailWear[0][3].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  955 */       this.tailWear[0][2].addChild(this.tailWear[0][3]);
/*  956 */       setRotationAngle(this.tailWear[0][3], 0.2618F, 0.0F, 0.0F);
/*  957 */       (this.tailWear[0][3]).cubeList.add(new ModelBox(this.tailWear[0][3], 102, 4, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.31F, false));
/*  958 */       this.tailWear[0][4] = new ModelRenderer((ModelBase)this);
/*  959 */       this.tailWear[0][4].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  960 */       this.tailWear[0][3].addChild(this.tailWear[0][4]);
/*  961 */       setRotationAngle(this.tailWear[0][4], 0.2618F, 0.0F, 0.0F);
/*  962 */       (this.tailWear[0][4]).cubeList.add(new ModelBox(this.tailWear[0][4], 102, 4, -2.0F, -5.5F, -2.0F, 4, 6, 4, 0.01F, false));
/*  963 */       this.tailWear[0][5] = new ModelRenderer((ModelBase)this);
/*  964 */       this.tailWear[0][5].setRotationPoint(0.0F, -5.0F, 0.0F);
/*  965 */       this.tailWear[0][4].addChild(this.tailWear[0][5]);
/*  966 */       setRotationAngle(this.tailWear[0][5], 0.2618F, 0.0F, 0.0F);
/*  967 */       (this.tailWear[0][5]).cubeList.add(new ModelBox(this.tailWear[0][5], 102, 4, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.29F, false));
/*  968 */       this.tailWear[0][6] = new ModelRenderer((ModelBase)this);
/*  969 */       this.tailWear[0][6].setRotationPoint(0.0F, -4.0F, 0.0F);
/*  970 */       this.tailWear[0][5].addChild(this.tailWear[0][6]);
/*  971 */       setRotationAngle(this.tailWear[0][6], 0.2618F, 0.0F, 0.0F);
/*  972 */       (this.tailWear[0][6]).cubeList.add(new ModelBox(this.tailWear[0][6], 102, 4, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.59F, false));
/*  973 */       this.tailWear[0][7] = new ModelRenderer((ModelBase)this);
/*  974 */       this.tailWear[0][7].setRotationPoint(0.0F, -3.75F, 0.0F);
/*  975 */       this.tailWear[0][6].addChild(this.tailWear[0][7]);
/*  976 */       setRotationAngle(this.tailWear[0][7], 0.2618F, 0.0F, 0.0F);
/*  977 */       (this.tailWear[0][7]).cubeList.add(new ModelBox(this.tailWear[0][7], 102, 4, -2.0F, -5.5F, -2.0F, 4, 6, 4, -0.99F, false));
/*  978 */       this.bipedRightArm = new ModelRenderer((ModelBase)this);
/*  979 */       this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
/*  980 */       this.bipedRightArm.cubeList.add(new ModelBox(this.bipedRightArm, 40, 16, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.6F, false));
/*  981 */       this.bipedRightArmWear = new ModelRenderer((ModelBase)this);
/*  982 */       this.bipedRightArmWear.setRotationPoint(-5.0F, 2.0F, 0.0F);
/*  983 */       this.bipedRightArmWear.cubeList.add(new ModelBox(this.bipedRightArmWear, 104, 16, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.65F, false));
/*  984 */       this.bipedRightArmWear.cubeList.add(new ModelBox(this.bipedRightArmWear, 104, 32, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.7F, false));
/*  985 */       this.sandArm = new ModelRenderer((ModelBase)this);
/*  986 */       if (tails == 1) {
/*  987 */         this.sandArm.setRotationPoint(-1.6421F, 7.959F, -3.46F);
/*  988 */         this.bipedRightArmWear.addChild(this.sandArm);
/*  989 */         ModelRenderer sandHand = new ModelRenderer((ModelBase)this);
/*  990 */         sandHand.setRotationPoint(-2.3579F, 3.041F, 1.46F);
/*  991 */         this.sandArm.addChild(sandHand);
/*  992 */         ModelRenderer finger = new ModelRenderer((ModelBase)this);
/*  993 */         finger.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  994 */         sandHand.addChild(finger);
/*  995 */         setRotationAngle(finger, 0.0F, 0.0F, 0.1309F);
/*  996 */         ModelRenderer cube_r3 = new ModelRenderer((ModelBase)this);
/*  997 */         cube_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  998 */         finger.addChild(cube_r3);
/*  999 */         setRotationAngle(cube_r3, -0.0999F, -0.5148F, 0.2009F);
/* 1000 */         cube_r3.cubeList.add(new ModelBox(cube_r3, 120, 35, -0.075F, -3.0F, -1.0F, 2, 4, 2, 0.0F, false));
/* 1001 */         ModelRenderer cube_r4 = new ModelRenderer((ModelBase)this);
/* 1002 */         cube_r4.setRotationPoint(1.1324F, 2.5785F, 0.6538F);
/* 1003 */         finger.addChild(cube_r4);
/* 1004 */         setRotationAngle(cube_r4, 0.1719F, -0.4971F, -0.3492F);
/* 1005 */         cube_r4.cubeList.add(new ModelBox(cube_r4, 120, 42, -1.0F, -2.3F, -1.0F, 2, 4, 2, -0.25F, false));
/* 1006 */         ModelRenderer finger3 = new ModelRenderer((ModelBase)this);
/* 1007 */         finger3.setRotationPoint(4.9282F, 0.7341F, 1.2245F);
/* 1008 */         sandHand.addChild(finger3);
/* 1009 */         setRotationAngle(finger3, 0.0F, 0.5672F, 0.0F);
/* 1010 */         ModelRenderer cube_r5 = new ModelRenderer((ModelBase)this);
/* 1011 */         cube_r5.setRotationPoint(0.9968F, -0.7341F, 0.5755F);
/* 1012 */         finger3.addChild(cube_r5);
/* 1013 */         setRotationAngle(cube_r5, 0.0999F, -0.5148F, -0.2009F);
/* 1014 */         cube_r5.cubeList.add(new ModelBox(cube_r5, 120, 35, -1.925F, -3.0F, -1.0F, 2, 4, 2, 0.0F, true));
/* 1015 */         ModelRenderer cube_r6 = new ModelRenderer((ModelBase)this);
/* 1016 */         cube_r6.setRotationPoint(-0.1356F, 1.8444F, -0.0783F);
/* 1017 */         finger3.addChild(cube_r6);
/* 1018 */         setRotationAngle(cube_r6, -0.1719F, -0.4971F, 0.3492F);
/* 1019 */         cube_r6.cubeList.add(new ModelBox(cube_r6, 120, 42, -1.0F, -2.3F, -1.0F, 2, 4, 2, -0.25F, true));
/* 1020 */         ModelRenderer finger2 = new ModelRenderer((ModelBase)this);
/* 1021 */         finger2.setRotationPoint(0.0F, 0.0F, 4.0F);
/* 1022 */         sandHand.addChild(finger2);
/* 1023 */         setRotationAngle(finger2, 0.0F, 0.0F, 0.1309F);
/* 1024 */         ModelRenderer cube_r7 = new ModelRenderer((ModelBase)this);
/* 1025 */         cube_r7.setRotationPoint(0.0F, 0.0F, 0.0F);
/* 1026 */         finger2.addChild(cube_r7);
/* 1027 */         setRotationAngle(cube_r7, 0.0999F, 0.5148F, 0.2009F);
/* 1028 */         cube_r7.cubeList.add(new ModelBox(cube_r7, 120, 35, -0.075F, -3.0F, -1.0F, 2, 4, 2, 0.0F, false));
/* 1029 */         ModelRenderer cube_r8 = new ModelRenderer((ModelBase)this);
/* 1030 */         cube_r8.setRotationPoint(1.1324F, 2.5785F, -0.6538F);
/* 1031 */         finger2.addChild(cube_r8);
/* 1032 */         setRotationAngle(cube_r8, -0.1719F, 0.4971F, -0.3492F);
/* 1033 */         cube_r8.cubeList.add(new ModelBox(cube_r8, 120, 42, -1.0F, -2.3F, -1.0F, 2, 4, 2, -0.25F, false));
/* 1034 */         ModelRenderer bump2 = new ModelRenderer((ModelBase)this);
/* 1035 */         bump2.setRotationPoint(0.4421F, -2.584F, 3.21F);
/* 1036 */         this.sandArm.addChild(bump2);
/* 1037 */         ModelRenderer cube_r9 = new ModelRenderer((ModelBase)this);
/* 1038 */         cube_r9.setRotationPoint(0.0F, 0.0F, 0.2F);
/* 1039 */         bump2.addChild(cube_r9);
/* 1040 */         setRotationAngle(cube_r9, -0.4102F, -0.4102F, -0.7854F);
/* 1041 */         cube_r9.cubeList.add(new ModelBox(cube_r9, 95, 0, -2.0F, -2.0F, -2.0F, 4, 4, 4, 0.6F, false));
/* 1042 */         ModelRenderer bump = new ModelRenderer((ModelBase)this);
/* 1043 */         bump.setRotationPoint(0.4421F, -8.584F, 3.21F);
/* 1044 */         this.sandArm.addChild(bump);
/* 1045 */         setRotationAngle(bump, 0.0F, 0.0F, 0.48F);
/* 1046 */         ModelRenderer cube_r10 = new ModelRenderer((ModelBase)this);
/* 1047 */         cube_r10.setRotationPoint(0.0F, 0.0F, 0.0F);
/* 1048 */         bump.addChild(cube_r10);
/* 1049 */         setRotationAngle(cube_r10, -0.4102F, -0.4102F, -0.7854F);
/* 1050 */         cube_r10.cubeList.add(new ModelBox(cube_r10, 95, 0, -1.9203F, -2.0731F, -1.8318F, 4, 4, 4, 0.6F, false));
/* 1051 */         ModelRenderer sparefingers = new ModelRenderer((ModelBase)this);
/* 1052 */         sparefingers.setRotationPoint(0.0F, 0.0F, 0.0F);
/* 1053 */         this.sandArm.addChild(sparefingers);
/* 1054 */         ModelRenderer cube_r11 = new ModelRenderer((ModelBase)this);
/* 1055 */         cube_r11.setRotationPoint(0.0F, 0.0F, 0.0F);
/* 1056 */         sparefingers.addChild(cube_r11);
/* 1057 */         setRotationAngle(cube_r11, -1.6095F, -1.0268F, 1.3404F);
/* 1058 */         cube_r11.cubeList.add(new ModelBox(cube_r11, 120, 42, -0.25F, -2.0F, -1.0F, 2, 4, 2, -0.25F, false));
/* 1059 */         ModelRenderer cube_r12 = new ModelRenderer((ModelBase)this);
/* 1060 */         cube_r12.setRotationPoint(-1.3089F, -4.5636F, 3.7447F);
/* 1061 */         sparefingers.addChild(cube_r12);
/* 1062 */         setRotationAngle(cube_r12, 0.1512F, 0.1609F, 0.7314F);
/* 1063 */         cube_r12.cubeList.add(new ModelBox(cube_r12, 120, 42, -2.475F, -2.0F, -1.0F, 2, 4, 2, -0.25F, false));
/* 1064 */         ModelRenderer cube_r13 = new ModelRenderer((ModelBase)this);
/* 1065 */         cube_r13.setRotationPoint(0.5218F, 0.2381F, 6.7784F);
/* 1066 */         sparefingers.addChild(cube_r13);
/* 1067 */         setRotationAngle(cube_r13, 1.6292F, 0.9065F, 1.702F);
/* 1068 */         cube_r13.cubeList.add(new ModelBox(cube_r13, 120, 42, -1.0F, -3.0F, -2.175F, 2, 4, 2, -0.25F, false));
/* 1069 */         ModelRenderer cube_r14 = new ModelRenderer((ModelBase)this);
/* 1070 */         cube_r14.setRotationPoint(2.5926F, -1.7054F, 6.8658F);
/* 1071 */         sparefingers.addChild(cube_r14);
/* 1072 */         setRotationAngle(cube_r14, 1.1536F, 0.8762F, 0.7801F);
/* 1073 */         cube_r14.cubeList.add(new ModelBox(cube_r14, 120, 42, -2.15F, -5.25F, -0.625F, 2, 4, 2, -0.25F, false));
/*      */       } 
/* 1075 */       this.bipedLeftArm = new ModelRenderer((ModelBase)this);
/* 1076 */       this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
/* 1077 */       this.bipedLeftArm.cubeList.add(new ModelBox(this.bipedLeftArm, 32, 48, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.6F, false));
/* 1078 */       this.bipedLeftArmWear = new ModelRenderer((ModelBase)this);
/* 1079 */       this.bipedLeftArmWear.setRotationPoint(5.0F, 2.0F, 0.0F);
/* 1080 */       this.bipedLeftArmWear.cubeList.add(new ModelBox(this.bipedLeftArmWear, 96, 48, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.65F, false));
/* 1081 */       this.bipedLeftArmWear.cubeList.add(new ModelBox(this.bipedLeftArmWear, 112, 48, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.7F, false));
/* 1082 */       this.bipedRightLeg = new ModelRenderer((ModelBase)this);
/* 1083 */       this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
/* 1084 */       this.bipedRightLeg.cubeList.add(new ModelBox(this.bipedRightLeg, 0, 16, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.6F, false));
/* 1085 */       this.bipedRightLegWear = new ModelRenderer((ModelBase)this);
/* 1086 */       this.bipedRightLegWear.setRotationPoint(-1.9F, 12.0F, 0.0F);
/* 1087 */       this.bipedRightLegWear.cubeList.add(new ModelBox(this.bipedRightLegWear, 64, 16, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.65F, false));
/* 1088 */       this.bipedRightLegWear.cubeList.add(new ModelBox(this.bipedRightLegWear, 64, 32, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.7F, false));
/* 1089 */       this.bipedLeftLeg = new ModelRenderer((ModelBase)this);
/* 1090 */       this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
/* 1091 */       this.bipedLeftLeg.cubeList.add(new ModelBox(this.bipedLeftLeg, 16, 48, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.6F, false));
/* 1092 */       this.bipedLeftLegWear = new ModelRenderer((ModelBase)this);
/* 1093 */       this.bipedLeftLegWear.setRotationPoint(1.9F, 12.0F, 0.0F);
/* 1094 */       this.bipedLeftLegWear.cubeList.add(new ModelBox(this.bipedLeftLegWear, 80, 48, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.65F, false));
/* 1095 */       this.bipedLeftLegWear.cubeList.add(new ModelBox(this.bipedLeftLegWear, 64, 48, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.7F, false));
/*      */       int i;
/* 1097 */       for (i = 1; i < 6; i++) {
/* 1098 */         this.leftEarSwayX[i] = (this.rand.nextFloat() * 0.2618F + 0.0873F) * (this.rand.nextBoolean() ? -1.0F : 1.0F);
/* 1099 */         this.leftEarSwayZ[i] = (this.rand.nextFloat() * 0.2618F + 0.0873F) * (this.rand.nextBoolean() ? -1.0F : 1.0F);
/* 1100 */         this.rightEarSwayX[i] = (this.rand.nextFloat() * 0.2618F + 0.0873F) * (this.rand.nextBoolean() ? -1.0F : 1.0F);
/* 1101 */         this.rightEarSwayZ[i] = (this.rand.nextFloat() * 0.2618F + 0.0873F) * (this.rand.nextBoolean() ? -1.0F : 1.0F);
/*      */       } 
/* 1103 */       for (i = 0; i < 9; i++) {
/* 1104 */         for (int j = 1; j < 8; j++) {
/* 1105 */           this.tailSwayX[i][j] = (this.rand.nextFloat() * 0.1745F + 0.1745F) * (this.rand.nextBoolean() ? -1.0F : 1.0F);
/* 1106 */           this.tailSwayZ[i][j] = (this.rand.nextFloat() * 0.2618F + 0.2618F) * (this.rand.nextBoolean() ? -1.0F : 1.0F);
/*      */         } 
/*      */       } 
/* 1109 */       setModelVisibilities(tails);
/*      */     }
/*      */     
/*      */     private void setModelVisibilities(int numberoftails) {
/* 1113 */       int j = this.tailShowMap[numberoftails];
/* 1114 */       for (int i = 0; i < 9; i++) {
/* 1115 */         (this.tail[i][0]).showModel = ((j & 1 << i) != 0);
/*      */       }
/* 1117 */       if (numberoftails != 1) {
/* 1118 */         this.sandArm.showModel = false;
/*      */       } else {
/* 1120 */         (this.earLeft[0]).showModel = false;
/* 1121 */         (this.earRight[0]).showModel = false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void render(Entity entity, float f0, float f1, float f2, float f3, float f4, float f5) {
/* 1127 */       this.bipedHeadwear.showModel = false;
/* 1128 */       this.bipedBody.showModel = (this.bipedBody.showModel && !this.bipedRightLeg.showModel && !this.bipedLeftLeg.showModel);
/* 1129 */       GlStateManager.pushMatrix();
/* 1130 */       GlStateManager.depthMask(true);
/* 1131 */       GlStateManager.matrixMode(5890);
/* 1132 */       GlStateManager.loadIdentity();
/* 1133 */       GlStateManager.translate(0.0F, f2 * 0.01F, 0.0F);
/* 1134 */       GlStateManager.matrixMode(5888);
/* 1135 */       GlStateManager.enableBlend();
/* 1136 */       GlStateManager.color(1.0F, 1.0F, 1.0F, MathHelper.clamp(ItemBijuCloak.getWearingTicks(entity) / 80.0F, 0.0F, 1.0F));
/* 1137 */       GlStateManager.disableLighting();
/* 1138 */       GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
/* 1139 */       int k = entity.getBrightnessForRender();
/* 1140 */       if (this.bodyShine) {
/* 1141 */         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
/*      */       } else {
/* 1143 */         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (k % 65536), (k / 65536));
/*      */       } 
/* 1145 */       (Minecraft.getMinecraft()).entityRenderer.setupFogColor(true);
/* 1146 */       super.render(entity, f0, f1, f2, f3, f4, f5);
/* 1147 */       (Minecraft.getMinecraft()).entityRenderer.setupFogColor(false);
/* 1148 */       GlStateManager.matrixMode(5890);
/* 1149 */       GlStateManager.loadIdentity();
/* 1150 */       GlStateManager.matrixMode(5888);
/* 1151 */       if (this.layerShine) {
/* 1152 */         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
/*      */       } else {
/* 1154 */         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (k % 65536), (k / 65536));
/*      */       } 
/* 1156 */       if (entity.isSneaking()) {
/* 1157 */         GlStateManager.translate(0.0F, 0.2F, 0.0F);
/*      */       }
/* 1159 */       this; copyModelAngles(this.bipedBody, this.bipedBodyWear);
/* 1160 */       this; copyModelAngles(this.bipedRightArm, this.bipedRightArmWear);
/* 1161 */       this; copyModelAngles(this.bipedLeftArm, this.bipedLeftArmWear);
/* 1162 */       this; copyModelAngles(this.bipedRightLeg, this.bipedRightLegWear);
/* 1163 */       this; copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegWear);
/* 1164 */       this.bipedHeadwear.showModel = this.bipedHead.showModel;
/* 1165 */       this.bipedBodyWear.showModel = this.bipedBody.showModel;
/* 1166 */       this.bipedRightArmWear.showModel = this.bipedRightArm.showModel;
/* 1167 */       this.bipedLeftArmWear.showModel = this.bipedLeftArm.showModel;
/* 1168 */       this.bipedRightLegWear.showModel = this.bipedRightLeg.showModel;
/* 1169 */       this.bipedLeftLegWear.showModel = this.bipedLeftLeg.showModel;
/* 1170 */       this.bipedHeadwear.render(f5);
/* 1171 */       this.bipedBodyWear.render(f5);
/* 1172 */       this.bipedRightArmWear.render(f5);
/* 1173 */       this.bipedLeftArmWear.render(f5);
/* 1174 */       this.bipedRightLegWear.render(f5);
/* 1175 */       this.bipedLeftLegWear.render(f5);
/* 1176 */       GlStateManager.enableLighting();
/* 1177 */       GlStateManager.disableBlend();
/* 1178 */       GlStateManager.depthMask(false);
/* 1179 */       GlStateManager.popMatrix();
/*      */     }
/*      */     
/*      */     public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 1183 */       modelRenderer.rotateAngleX = x;
/* 1184 */       modelRenderer.rotateAngleY = y;
/* 1185 */       modelRenderer.rotateAngleZ = z;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
/* 1190 */       if (this.narutoRunPose) {
/* 1191 */         this.isSneak = true;
/*      */       }
/* 1193 */       super.setRotationAngles(f, f1, f2, f3, f4, f5, e); int i;
/* 1194 */       for (i = 1; i < 6; i++) {
/* 1195 */         (this.earLeft[i]).rotateAngleX = -0.1745F + MathHelper.sin(f2 * 0.15F) * this.leftEarSwayX[i];
/* 1196 */         (this.earLeft[i]).rotateAngleZ = MathHelper.cos(f2 * 0.15F) * this.leftEarSwayZ[i];
/* 1197 */         (this.earRight[i]).rotateAngleX = 0.1745F + MathHelper.sin(f2 * 0.15F) * this.rightEarSwayX[i];
/* 1198 */         (this.earRight[i]).rotateAngleZ = MathHelper.cos(f2 * 0.15F) * this.rightEarSwayZ[i];
/*      */       } 
/* 1200 */       for (i = 0; i < 9; i++) {
/* 1201 */         for (int j = 2; j < 8; j++) {
/* 1202 */           (this.tail[i][j]).rotateAngleX = 0.2618F + MathHelper.sin(f2 * 0.15F) * this.tailSwayX[i][j];
/* 1203 */           (this.tail[i][j]).rotateAngleZ = MathHelper.cos(f2 * 0.15F) * this.tailSwayZ[i][j];
/* 1204 */           if (i == 0) {
/* 1205 */             (this.tailWear[i][j]).rotateAngleX = (this.tail[i][j]).rotateAngleX;
/* 1206 */             (this.tailWear[i][j]).rotateAngleZ = (this.tail[i][j]).rotateAngleZ;
/*      */           } 
/*      */         } 
/*      */       } 
/* 1210 */       if (this.narutoRunPose) {
/* 1211 */         this.bipedRightArm.rotateAngleX = 1.4835F;
/* 1212 */         this.bipedRightArm.rotateAngleY = -0.3927F;
/* 1213 */         this.bipedLeftArm.rotateAngleX = 1.4835F;
/* 1214 */         this.bipedLeftArm.rotateAngleY = 0.3927F;
/*      */       } 
/*      */     }
/*      */     
/*      */     public void setNarutoRunPose(boolean b) {
/* 1219 */       this.narutoRunPose = b;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Omsin\Downloads\narutomod-0.3.0-B-deobf.jar!\net\narutomod\item\ItemBijuCloak.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */